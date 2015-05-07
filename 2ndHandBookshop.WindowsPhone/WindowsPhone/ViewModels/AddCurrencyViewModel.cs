using System;
using System.Collections.ObjectModel;
using Windows.UI.Popups;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class AddCurrencyViewModel : PropertyChangedBase
    {
        private readonly IAccountManager<User> accountManager;
        private readonly IUserService userService;
        public AddCurrencyViewModel(IAccountManager<User> _accountManager, IUserService _userService)
        {
            userService = _userService;
            accountManager = _accountManager;
        }

        public async void Buy(int amount)
        {
            var result = await userService.AddCurrencyToUser(accountManager.LoggedUser.Id, amount);
            MessageDialog msgbox = new MessageDialog(result ? "Purchase succeeded!" : "Error, try again later.");
            await msgbox.ShowAsync();  
        }
    }
}
