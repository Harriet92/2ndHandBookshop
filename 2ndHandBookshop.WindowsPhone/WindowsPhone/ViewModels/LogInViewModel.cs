using Caliburn.Micro;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class LogInViewModel : PropertyChangedBase
    {
        private readonly INavigationService navigationService;
        private readonly IUserService userService;
        private readonly IAccountManager<User> accountManager;
        public string Header
        {
            get { return "Log in"; }
        }

        public string LoginTextBox { get; set; }
        public string PasswordTextBox { get; set; }

        public LogInViewModel(INavigationService _navigationService, IUserService _userService, IAccountManager<User> _accountManager)
        {
            navigationService = _navigationService;
            userService = _userService;
            accountManager = _accountManager;
        }
        public void Register()
        {
            navigationService.NavigateToViewModel<RegistrationViewModel>();
        }
        public async void LogIn()
        {
            LoginResponseParams user = await userService.LogIn(LoginTextBox, PasswordTextBox);
            if (user.ErrorMessage == null)
            {//TODO: Map from response
                accountManager.LoggedUser = new User()
                {
                    Name = LoginTextBox,
                    CurrencyCount = 200,
                    Bought = 15,
                    Sold = 10
                };
                navigationService.NavigateToViewModel<MainPageViewModel>();
            }
        }
    }
}
