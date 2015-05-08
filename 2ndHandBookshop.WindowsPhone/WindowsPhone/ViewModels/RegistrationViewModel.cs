using System;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Windows.Foundation.Diagnostics;
using Windows.UI.Popups;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class RegistrationViewModel : ViewModel
    {
        private readonly IUserService userService;
        private readonly INavigationService navigationService;
        public RegistrationViewModel(IUserService _userService, INavigationService _navigationService)
        {
            navigationService = _navigationService;
            userService = _userService;
        }
        public string Header
        {
            get { return "Register"; }
        }

        public string LoginTextBox { get; set; }
        public string PasswordTextBox { get; set; }
        public string RepeatPasswordTextBox { get; set; }
        public string EmailTextBox { get; set; }
        public async void Register()
        {
            if (!await ValidateInput())
            {
                HideLoadingIndicator();
                return;
            }
            ShowLoadingIndicator();
            RegisterResponseParams result = await userService.Register(LoginTextBox, EmailTextBox, PasswordTextBox);
            HideLoadingIndicator();
            if (result.IsSuccess)
            {
                var sucDialog = new MessageDialog("Registration completed, please log in using provided credentials");
                await sucDialog.ShowAsync();
                navigationService.NavigateToViewModel<LogInViewModel>();
            }
            else
            {
                var errorDialog = new MessageDialog(result.error);
                await errorDialog.ShowAsync();
            }
        }

        private async Task<bool> ValidateInput()
        {
            string message;
            Regex emailValidation = new Regex(@".+\@.+\..+");
            if (String.IsNullOrEmpty(LoginTextBox))
            {
                message = "Login field must not be empty";
            }
            else if (String.IsNullOrEmpty(PasswordTextBox) || String.IsNullOrEmpty(RepeatPasswordTextBox))
            {
                message = "Password fields must not be empty";
            }
            else if (String.IsNullOrEmpty(EmailTextBox))
            {
                message = "Email field must not be empty";
            }
            else if (PasswordTextBox != RepeatPasswordTextBox)
            {
                message = "Passwords don't match!";
            }
            else if (!emailValidation.IsMatch(EmailTextBox))
            {
                message = "Provide a correct email address!";
            }
            else
            {
                return true;
            }
            MessageDialog errorDialog = new MessageDialog(message);
            await errorDialog.ShowAsync();
            return false;
        }
    }
}
