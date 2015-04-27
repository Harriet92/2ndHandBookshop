using System.Collections.Generic;
using System.Collections.ObjectModel;
using Windows.UI.ViewManagement;
using SecondHandBookshop.Shared.Http;
using SecondHandBookshop.Shared.Models;
using Caliburn.Micro;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class NewestOffersViewModel : PropertyChangedBase
    {
        public string Header
        {
            get { return "Newest offers"; }
        }
        //just to test HTTP requests and list binding
        public ObservableCollection<Offer> Offers { get; set; }

        public NewestOffersViewModel()
        {
            Offers = new ObservableCollection<Offer>();
            RefreshOffers();
            NotifyOfPropertyChange(() => Offers);
        }

        private async void RefreshOffers()
        {
            List<Offer> users = await ServerRequest.GetLatestOffers();
            foreach (Offer o in users)
                Offers.Add(o);
        }

        public void Button()
        {
            RefreshOffers();
        }
    }
}
