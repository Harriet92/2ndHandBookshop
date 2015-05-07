using System;
using Windows.Devices.Enumeration;
using Windows.Media.Capture;
using Windows.Media.Devices;
using Windows.Media.MediaProperties;
using Windows.Storage;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;
using Caliburn.Micro;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class AddOfferViewModel : PropertyChangedBase
    {
        private MediaCapture _mediaCapture;
        private CaptureElement _captureElement;
        public AddOfferViewModel()
        {
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

        public void Post()
        {
            ClearFormula();
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
