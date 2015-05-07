using System;
using Windows.ApplicationModel.Activation;
using Windows.Security.Authentication.Web;
using Windows.UI.Xaml;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Common;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Helpers;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class LogInViewModel : PropertyChangedBase, IWebAuthenticationBrokerContinuable
    {
        private readonly INavigationService navigationService;
        private readonly IUserService userService;
        private readonly IAccountManager<User> accountManager;
        private readonly FacebookManager fbManager;
        public string Header
        {
            get { return "Log in"; }
        }

        public string LoginTextBox { get; set; }
        public string PasswordTextBox { get; set; }

        public LogInViewModel(INavigationService _navigationService, IUserService _userService, IAccountManager<User> _accountManager, FacebookManager _fbManager)
        {
            navigationService = _navigationService; 
            userService = _userService;
            accountManager = _accountManager;
            fbManager = _fbManager;
            Uri _callbackUri = WebAuthenticationBroker.GetCurrentApplicationCallbackUri();
        }
        public void Register()
        {
            navigationService.NavigateToViewModel<RegistrationViewModel>();
        }

        public void FbLogin()
        {
            fbManager.LoginAndContinue();
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
        public async void ContinueWithWebAuthenticationBroker(WebAuthenticationBrokerContinuationEventArgs args)
        {
            fbManager.ContinueAuthentication(args);
            if (fbManager.AccessToken != null)
            {
                var result = await fbManager.GetFacebookProfileInfo();
                accountManager.LoggedUser = result;
                // TODO: Add fetching user profile from server!
                navigationService.NavigateToViewModel<MainPageViewModel>();
            }
        }  
    }
}
