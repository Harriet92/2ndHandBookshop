using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class MainPageViewModel : PropertyChangedBase
    {
        public string HubHeader
        {
            get { return "Second Hand Bookshop"; }
        }
        public SearchViewModel SearchViewModel { get; set; }
        public NewestOffersViewModel NewestOffersViewModel { get; set; }
        public AddOfferViewModel AddOfferViewModel { get; set; }
        public AccountViewModel AccountViewModel { get; set; }
        public MainPageViewModel(IOfferService<Offer> offerService, IAccountManager<User> accountManager  )
        {
            SearchViewModel = new SearchViewModel(offerService);
            NewestOffersViewModel = new NewestOffersViewModel(offerService);
            AddOfferViewModel = new AddOfferViewModel();
            AccountViewModel = new AccountViewModel(accountManager);
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
    }
}
