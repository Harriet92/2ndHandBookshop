using System.Collections.Generic;
using System.Collections.ObjectModel;
using SecondHandBookshop.Shared.Http;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class NewestOffersViewModel : ISectionViewModel
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
        }

        private async void RefreshOffers()
        {
            List<Offer> users = await ServerRequest.GetLatestOffers();
            foreach (Offer o in users)
                Offers.Add(o);
        }
    }
}
