using Caliburn.Micro;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _2ndHandBookshop.WindowsPhone.ViewModels
{
    public class MainPageViewModel : PropertyChangedBase
    {
        public string Name
        {
            get
            {
                return "2nd Hand Bookshop";
            }            
        }

        public string[] Items
        {
            get
            {
                return new[] { "lala", "po" };
            }
        }
    }
}
