using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml.Controls;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Http;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class SearchViewModel : ISectionViewModel
    {
        public SearchViewModel()
        {
            Results = new ObservableCollection<Offer>();
            RefreshOffers();
        }
        public string Header
        {
            get { return "Search"; }
        }

        //NOT WORKING ;((((
        public string AuthorTitleSearchBox { get; set; }

        public List<string> TagList
        {
            get { return GenderList.Get(); }
        }

        public ObservableCollection<Offer> Results { get; set; }

        private List<Offer> FilterResults()
        {
            return null;
        }

        public void TextBox_OnTextChanged(object sender, TextChangedEventArgs e)
        {
            var offers = Results.Where(x =>!( x.BookTitle.Contains(e.ToString()) || x.BookAuthor.Contains(e.ToString()))).ToList();
            foreach (var offer in offers)
            {
                Results.Remove(offer);
            }
        }

        private List<Gender> genderTagsSelected;
        private async void RefreshOffers()
        {
            List<Offer> users = await ServerRequest.GetOffers();
            foreach (Offer o in users)
                Results.Add(o);
        }
    }
}
