using SecondHandBookshop.WindowsPhone.ViewModels;
using Windows.UI.Xaml.Controls;

namespace SecondHandBookshop.WindowsPhone.Views
{
    public sealed partial class AccountView : UserControl
    {
        private AccountViewModel viewModel;
        public AccountView()
        {
            this.InitializeComponent();
            viewModel = new AccountViewModel();
            DataContext = viewModel;
        }
    }
}
