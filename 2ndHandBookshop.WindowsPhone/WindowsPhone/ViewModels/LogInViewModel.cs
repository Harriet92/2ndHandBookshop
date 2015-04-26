using Caliburn.Micro;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class LogInViewModel : PropertyChangedBase
    {
        private readonly INavigationService navigationService;
        public string Header
        {
            get { return "Log in"; }
        }

        public string LoginTextBox { get; set; }
        public string PasswordTextBox { get; set; }

        public LogInViewModel(INavigationService _navigationService)
        {
            navigationService = _navigationService;
        }
        public void Register()
        {
            SendRequest();
        }
        public void LogIn()
        {
            navigationService.NavigateToViewModel<MainPageViewModel>();
        }

        private void SendRequest()
        {
            
        }
    }
}
