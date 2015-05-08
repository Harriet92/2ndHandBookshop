using System;
using Windows.Devices.Geolocation;
using Windows.Media.Capture;
using Windows.Media.MediaProperties;
using Windows.Storage;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media.Imaging;
using AutoMapper;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Common;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Helpers;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class AddOfferViewModel : ViewModel
    {
        private MediaCapture _mediaCapture;
        private CaptureElement _captureElement;
        private readonly IOfferService<OfferDTO> offerService;
        private readonly LocationManager locationManager;
        public AddOfferViewModel(IOfferService<OfferDTO> _offerService, LocationManager _locationManager)
        {
            locationManager = _locationManager;
            offerService = _offerService;
            SetPlaceholderImage();
            ConfigureMedia();
        }

        public string Author { get; set; }
        public string Title { get; set; }
        public int Price { get; set; }
        public string Description { get; set; }
        public BitmapImage Photo { get; set; }
        public bool AddLocalizationInfo { get; set; }
        public bool ShowCaptureFrame { get; set; }
        public MediaCapture MediaCapture
        {
            get
            {
                if (_mediaCapture == null) _mediaCapture = new MediaCapture();
                return _mediaCapture;
            }
            set
            {
                _mediaCapture = value;
                NotifyOfPropertyChange(() => MediaCapture);
            }
        }
        public CaptureElement CaptureElement
        {
            get
            {
                if (_captureElement == null) _captureElement = new CaptureElement();
                return _captureElement;
            }
            set
            {
                _captureElement = value;
                NotifyOfPropertyChange(() => CaptureElement);
            }
        }

        public async void Post()
        {
            ShowLoadingIndicator();
            BasicGeoposition location = new BasicGeoposition();
            if (AddLocalizationInfo)
                location = await locationManager.GetCurrentLocation();
            var newOffer = new Offer()
            {
                BookAuthor = Author,
                BookTitle = Title,
                Price = Price,
                Description = Description,
                StartedAt = DateTime.UtcNow,
                Status = OfferStatus.Added,
                Location = location
            };
            Photo = new BitmapImage(new Uri("ms-appx:///Assets/pies.jpg", UriKind.RelativeOrAbsolute));
            //newOffer.PhotoBase64 = await Photo.ConvertToBase64();
            var result = await offerService.AddOffer(Mapper.Map<OfferDTO>(newOffer));
            if (result != null)
                ClearFormula();
            HideLoadingIndicator();
        }

        public async void AddImage()
        {
            ShowCaptureFrame = true;
            NotifyOfPropertyChange(() => ShowCaptureFrame);
            await MediaCapture.StartPreviewAsync();
        }

        public async void TakeAPhoto()
        {
            ImageEncodingProperties imgFormat = ImageEncodingProperties.CreateJpeg();
            StorageFile file = await ApplicationData.Current.LocalFolder.CreateFileAsync("TestPhoto.jpg",
                CreationCollisionOption.GenerateUniqueName);
            await _mediaCapture.CapturePhotoToStorageFileAsync(imgFormat, file);
            Photo = new BitmapImage(new Uri(file.Path));
            await MediaCapture.StopPreviewAsync();
            NotifyOfPropertyChange(() => Photo);
            ShowCaptureFrame = false;
            NotifyOfPropertyChange(() => ShowCaptureFrame);
        }
        private void ClearFormula()
        {
            Author = Title = Description = String.Empty;
            Price = 0;
            Photo = null;
            AddLocalizationInfo = false;
            RefreshBindings();
        }

        private void SetPlaceholderImage()
        {
            Photo = new BitmapImage(new Uri("ms-appx:///Assets/add.png", UriKind.RelativeOrAbsolute));
            NotifyOfPropertyChange(() => Photo);
        }

        private async void ConfigureMedia()
        {
            await MediaCapture.InitializeAsync();
            CaptureElement.Source = MediaCapture;
        }

        private void RefreshBindings()
        {
            NotifyOfPropertyChange(() => Author);
            NotifyOfPropertyChange(() => Title);
            NotifyOfPropertyChange(() => Price);
            NotifyOfPropertyChange(() => Description);
            NotifyOfPropertyChange(() => AddLocalizationInfo);
            NotifyOfPropertyChange(() => Photo);
        }
    }
}
