using SecondHandBookshop.WindowsPhone.ViewModels;
using Windows.UI.Xaml.Controls;

namespace SecondHandBookshop.WindowsPhone.Views
{
    public sealed partial class SearchView : UserControl
    {
        private SearchViewModel viewModel;
        public SearchView()
        {
            this.InitializeComponent();
            viewModel = new SearchViewModel();
            DataContext = viewModel;
        }
    }
}
