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

        private void TextBox_OnTextChanged(object sender, TextChangedEventArgs e)
        {
            viewModel.TextBox_OnTextChanged(sender, e);
        }
    }
}
