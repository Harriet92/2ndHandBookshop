using System.Collections.Generic;
using System.Collections.ObjectModel;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using SecondHandBookshop.Shared.Models;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class NewestOffersViewModel : Screen
    {
        private readonly IOfferService<Offer> offersService;
        private readonly INavigationService navigationService;
        public string Header
        {
            get { return "Newest offers"; }
        }

        public ObservableCollection<Offer> Offers { get; set; }

        public NewestOffersViewModel(IOfferService<Offer> _offerService, INavigationService _navigationService)
        {
            offersService = _offerService;
            navigationService = _navigationService;
            Offers = new ObservableCollection<Offer>();
            RefreshOffers();
            NotifyOfPropertyChange(() => Offers);
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
            List<Offer> users = await offersService.GetLatestOffers(5);
            foreach (Offer o in users)
                Offers.Add(o);
        }
    }
}
