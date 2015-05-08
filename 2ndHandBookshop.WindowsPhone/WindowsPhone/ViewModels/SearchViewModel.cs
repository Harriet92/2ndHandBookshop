using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using AutoMapper;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Common;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;
using WinRTXamlToolkit.Tools;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class SearchViewModel : PropertyChangedBase
    {
        private readonly IOfferService<OfferDTO> offerService;
        private readonly INavigationService navigationService;
        private readonly LocationManager locationManager;
        private const double LocationDistance = 10;//km
        public SearchViewModel(IOfferService<OfferDTO> _offerService, INavigationService _navigationService, LocationManager _locationManager)
        {
            locationManager = _locationManager;
            offerService = _offerService;
            navigationService = _navigationService;
            RefreshOffers();
            Results = new ObservableCollection<Offer>();
        }

        private bool nearMyLocation;

        public bool NearMyLocation
        {
            get
            {
                return nearMyLocation;
            }
            set
            {
                nearMyLocation = value;
                NearMyLocationCB_Checked(value);
            }
        }

        public List<string> GenderTags
        {
            get { return GenderList.Get(); }

        }

        private List<string> selectedGenderTags;
        public List<string> SelectedGenderTag
        {
            get
            {
                return selectedGenderTags;
            }
            set
            {
                selectedGenderTags = value;
                NotifyOfPropertyChange(() => SelectedGenderTag);
            }
        }

        public ObservableCollection<Offer> Results { get; set; }

        private List<Offer> offersCache;

        private bool AuthorTitleFilter(Offer offer, string filterText)
        {
            return offer.BookTitle.ToLower().Contains(filterText) || offer.BookAuthor.ToLower().Contains(filterText);
        }
        private bool TagFilter(Offer offer, List<string> tags)
        {
            return GenderList.ConvertFromString(tags).Any(genTag => offer.GenderTags.Contains(genTag));
        }
        private bool LocationFilter(Offer offer, BasicGeoposition pos)
        {
            var dist = locationManager.Distance(offer.Location, pos);
            return dist <= LocationDistance;
        }

        public async void NearMyLocationCB_Checked(bool value)
        {
            var loc = await locationManager.GetCurrentLocation();
            if (value)
            {
                FilterResults((offer, l) => LocationFilter(offer, (BasicGeoposition)l), loc);
            }
            else
            {
                FilterResults((offer, tags) => true, null);
            }
        }

        public void SearchTextBox_TextChanged(TextBox textBox)
        {
            string lowerText = textBox.Text.ToLower();
            FilterResults((offer, text) => AuthorTitleFilter(offer, (string)text), lowerText);
        }

        public void TagGridView_SelectionChanged(GridView gridView)
        {
            if (gridView.SelectedItems.Count > 0)
            {
                FilterResults((offer, tags) => TagFilter(offer, (List<string>)tags), gridView.SelectedItems.OfType<string>().ToList());
            }
            else
            {
                FilterResults((offer, tags) => true, null);
            }
        }

        private void FilterResults(Func<Offer, object, bool> filterFunc, object args)
        {
            foreach (var offer in offersCache)
            {
                if (filterFunc(offer, args))
                {
                    if (!Results.Contains(offer))
                        Results.Add(offer);
                }
                else
                {
                    Results.Remove(offer);
                }
                NotifyOfPropertyChange(() => Results);
            }
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
            var result = await offerService.GetOffers();
            offersCache = new List<Offer>();
            result.array.ForEach(x => offersCache.Add(Mapper.Map<Offer>(x)));
            Results = new ObservableCollection<Offer>(offersCache);
            if (Results.Count > 0)
                Results[0].Location = await locationManager.GetCurrentLocation();
            NotifyOfPropertyChange(() => Results);
        }
    }
}
