using System;
using Windows.UI.Xaml.Media.Imaging;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class OfferDetailsViewModel : Screen
    {
        private Offer offer;
        private readonly INavigationService navigationService;
        public OfferDetailsViewModel(INavigationService _navigationService)
        {
            navigationService = _navigationService;
        }

        public string Author { get; set; }
        public string Title { get; set; }
        public int Price { get; set; }
        public string Description { get; set; }
        public BitmapImage Photo { get; set; }
        public string LocalizationInfo { get; set; }
        public Offer CurrentOffer
        {
            get { return offer; }
            set
            {
                offer = value;
                BindOffer();
                RefreshBindings();
            }
        }

        public void Buy()
        {
            throw new NotImplementedException();
        }
        public void Back()
        {
            navigationService.GoBack();
        }


        private void SetPlaceholderImage()
        {
            Photo = new BitmapImage(new Uri("ms-appx:///Assets/add.png", UriKind.RelativeOrAbsolute));
            NotifyOfPropertyChange(() => Photo);
        }


        private void RefreshBindings()
        {
            NotifyOfPropertyChange(() => Author);
            NotifyOfPropertyChange(() => Title);
            NotifyOfPropertyChange(() => Price);
            NotifyOfPropertyChange(() => Description);
            NotifyOfPropertyChange(() => LocalizationInfo);
            NotifyOfPropertyChange(() => Photo);
        }

        private void BindOffer()
        {
            if (offer == null)
                return;
            Author = offer.BookAuthor;
            Title = offer.BookTitle;
            Description = offer.Description;
            Photo = offer.Photo;
            Price = offer.CurrencyWorth;
            //TODO: Add localization and tags
        }
    }
}
