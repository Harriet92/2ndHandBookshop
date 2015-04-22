using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class MainPageViewModel
    {
        public string HubHeader
        {
            get { return "Second Hand Bookshop"; }
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
