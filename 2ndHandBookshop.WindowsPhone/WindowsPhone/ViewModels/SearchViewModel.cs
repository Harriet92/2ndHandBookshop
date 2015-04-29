using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using Windows.UI.Xaml.Controls;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class SearchViewModel : PropertyChangedBase, ISectionViewModel
    {
        private readonly IOfferService<Offer> offerService;
        public SearchViewModel(IOfferService<Offer> _offerService)
        {
            offerService = _offerService;
            RefreshOffers();
            Results = new ObservableCollection<Offer>(offersCache);
        }
        public string Header
        {
            get { return "Search"; }
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
        private async void RefreshOffers()
        {
            offersCache = await offerService.GetOffers(); 
        }
    }
}
