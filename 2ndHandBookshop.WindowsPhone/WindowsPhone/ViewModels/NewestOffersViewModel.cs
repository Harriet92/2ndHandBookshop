using System.Collections.Generic;
using System.Collections.ObjectModel;
using SecondHandBookshop.Shared.Models;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class NewestOffersViewModel : PropertyChangedBase
    {
        private readonly IOfferService<Offer> offersService; 
        public string Header
        {
            get { return "Newest offers"; }
        }

        public ObservableCollection<Offer> Offers { get; set; }

        public NewestOffersViewModel(IOfferService<Offer> _offerService)
        {
            offersService = _offerService;
            Offers = new ObservableCollection<Offer>();
            RefreshOffers();
            NotifyOfPropertyChange(() => Offers);
        }

        private async void RefreshOffers()
        {
            List<Offer> users = await offersService.GetLatestOffers(5);
            foreach (Offer o in users)
                Offers.Add(o);
        }
    }
}
