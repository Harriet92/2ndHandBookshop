using Windows.Foundation.Diagnostics;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class RegistrationViewModel : PropertyChangedBase
    {
        private readonly IUserService userService; 
        public RegistrationViewModel(IUserService _userService)
        {
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
            RegisterResponseParams result = await userService.Register(LoginTextBox, EmailTextBox, PasswordTextBox);
            if (result.ErrorMessage == null)
                LoginTextBox = result.email;

        }
    }
}
