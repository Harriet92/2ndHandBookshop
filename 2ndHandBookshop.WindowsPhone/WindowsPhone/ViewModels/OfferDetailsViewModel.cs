using System;
using Windows.UI.Xaml.Media.Imaging;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Common;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class OfferDetailsViewModel : ViewModel
    {
        private Offer offer;
        private readonly INavigationService navigationService;
        private readonly FacebookManager fbManager;
        private readonly IAccountManager<User> accountManager;
        private readonly IOfferService<OfferDTO> offerService;
        public OfferDetailsViewModel(INavigationService _navigationService, FacebookManager _fbManager, IAccountManager<User> _accountManager, IOfferService<OfferDTO> _offerService)
        {
            offerService = _offerService;
            accountManager = _accountManager;
            fbManager = _fbManager;
            navigationService = _navigationService;
            NotifyOfPropertyChange(() => ShowCancel);
        }

        public string Author { get; set; }
        public string Title { get; set; }
        public int Price { get; set; }
        public string Description { get; set; }
        public BitmapImage Photo { get; set; }
        public string LocalizationInfo { get; set; }
        public bool ShowCancel { get { return accountManager != null && accountManager.LoggedUser.Id == offer.SellerId; } }

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

        public async void Buy()
        {
            //if (Price > accountManager.LoggedUser.CurrencyCount)
            //{
            //    ShowMessage("You need more currency for that!");
            //    return;
            //}
            var result = await offerService.PurchaseOffer(offer.Id);
            if (result.IsSuccess)
            {
                await ShowMessage("Purchase succeeded!");
            }
            else
            {
                await ShowMessage(result.error);
            }
        }

        public async void Share()
        {
            await fbManager.ShareOffer(CurrentOffer);
        }

        public async void Cancel()
        {
            var result = await offerService.SetOfferAsCancelled(offer.Id);
            if (result.IsSuccess)
            {
                await ShowMessage("Offer has successfully been cancelled");
            }
            else
            {
                await ShowMessage(result.error);
            }
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
            NotifyOfPropertyChange(() => ShowCancel);
        }

        private void BindOffer()
        {
            if (offer == null)
                return;
            Author = offer.BookAuthor;
            Title = offer.BookTitle;
            Description = offer.Description;
            Photo = offer.Photo;
            Price = offer.Price;
            //TODO: Add localization and tags
        }
    }
}
