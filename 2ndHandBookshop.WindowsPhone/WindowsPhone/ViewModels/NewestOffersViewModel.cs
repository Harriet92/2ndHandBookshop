using System.Collections.Generic;
using System.Collections.ObjectModel;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using SecondHandBookshop.Shared.Models;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class NewestOffersViewModel : Screen
    {
        private readonly IOfferService<OfferDTO> offersService;
        private readonly INavigationService navigationService;
        public string Header
        {
            get { return "Newest offers"; }
        }

        public ObservableCollection<Offer> Offers { get; set; }

        public NewestOffersViewModel(IOfferService<OfferDTO> _offerService, INavigationService _navigationService)
        {
            offersService = _offerService;
            navigationService = _navigationService;
            Offers = new ObservableCollection<Offer>();
            RefreshOffers();
        }

        public void OfferClick(ItemClickEventArgs e)
        {
            navigationService.Navigated += NavigationServiceOnNavigated;
            navigationService.NavigateToViewModel<OfferDetailsViewModel>(e.ClickedItem as Offer);
            navigationService.Navigated -= NavigationServiceOnNavigated;
        }
        private static void NavigationServiceOnNavigated(object sender, NavigationEventArgs args)
        {
            FrameworkElement view;
            OfferDetailsViewModel viewModel;
            if ((view = args.Content as FrameworkElement) == null ||
                (viewModel = view.DataContext as OfferDetailsViewModel) == null) return;

            viewModel.CurrentOffer = args.Parameter as Offer;
        }
        private async void RefreshOffers()
        {
            List<OfferDTO> offersDtos = await offersService.GetLatestOffers(5);
            foreach (OfferDTO o in offersDtos)
                Offers.Add(AutoMapper.Mapper.Map<Offer>(o));
            NotifyOfPropertyChange(() => Offers);
        }
    }
}
