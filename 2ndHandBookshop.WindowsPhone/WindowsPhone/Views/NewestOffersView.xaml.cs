using SecondHandBookshop.WindowsPhone.ViewModels;
using Windows.UI.Xaml.Controls;

namespace SecondHandBookshop.WindowsPhone.Views
{
    public sealed partial class NewestOffersView : UserControl
    {
        private NewestOffersViewModel viewModel;
        public NewestOffersView()
        {
            this.InitializeComponent();
            viewModel = new NewestOffersViewModel();
            DataContext = viewModel;
        }
    }
}
