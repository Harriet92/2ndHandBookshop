using System;
using System.Threading.Tasks;
using Windows.ApplicationModel.Activation;
using Windows.Devices.Geolocation;
using Windows.Security.Authentication.Web;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.Web.Http;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Common;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Helpers;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class LogInViewModel : ViewModel, IWebAuthenticationBrokerContinuable
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
            if (!await ValidateInput())
            {
                HideLoadingIndicator();
                return;
            }
            ShowLoadingIndicator();
            LoginResponseParams user = await userService.LogIn(LoginTextBox, PasswordTextBox);
            HideLoadingIndicator();
            if (user.IsSuccess)
            {
                navigationService.NavigateToViewModel<MainPageViewModel>();
            }
            else
            {
                await ShowMessage(user.error);
            }
        }
        public async void ContinueWithWebAuthenticationBroker(WebAuthenticationBrokerContinuationEventArgs args)
        {
            fbManager.ContinueAuthentication(args);
            ShowLoadingIndicator();
            if (fbManager.AccessToken != null)
            {
                var result = await fbManager.GetFacebookProfileInfo();
                //if (result != null)
                //{
                //    var userFromDB = await userService.LogIn(LoginTextBox, PasswordTextBox);
                //    if (userFromDB.IsSuccess)
                //    {
                //        navigationService.NavigateToViewModel<MainPageViewModel>();
                //    }
                //    else if (!userFromDB.IsSuccess && userFromDB.StatusCode == HttpStatusCode.Ok)
                //    {
                        
                //    }
                //}
                accountManager.LoggedUser = result;
                // TODO: Add fetching user profile from server!
                navigationService.NavigateToViewModel<MainPageViewModel>();
            }
        }

        private async Task<bool> ValidateInput()
        {
            if (String.IsNullOrEmpty(LoginTextBox) || String.IsNullOrEmpty(PasswordTextBox))
            {
                await ShowMessage("Provide login and password!");
                return false;
            }
            return true;
        }
    }
}
