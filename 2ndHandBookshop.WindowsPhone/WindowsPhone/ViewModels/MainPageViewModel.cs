using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using Windows.Devices.Geolocation;
using Windows.UI.Xaml.Navigation;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class MainPageViewModel : Screen
    {
        public string HubHeader
        {
            get { return "Second Hand Bookshop"; }
        }
        public SearchViewModel SearchViewModel { get; set; }
        public NewestOffersViewModel NewestOffersViewModel { get; set; }
        public AddOfferViewModel AddOfferViewModel { get; set; }
        public AccountViewModel AccountViewModel { get; set; }
        public MainPageViewModel(IOfferService<Offer> offerService, IAccountManager<User> accountManager , INavigationService navigationService )
        {
            SearchViewModel = new SearchViewModel(offerService, navigationService);
            NewestOffersViewModel = new NewestOffersViewModel(offerService, navigationService);
            AddOfferViewModel = new AddOfferViewModel(offerService);
            AccountViewModel = new AccountViewModel(accountManager, navigationService);
        }
        public string SearchSectionHeader
        {
            get { return "Search"; }
        }
        public string AddOfferHeader
        {
            get { return "Add offer"; }
        }
        public string AccountHeader
        {
            get { return "Account"; }
        }

        public string NewestOffersHeader
        {
            get { return "Newest offers"; }
        }

        protected override void OnActivate()
        {
            base.OnActivate();
            AccountViewModel.Refresh();
        }
    }
}
