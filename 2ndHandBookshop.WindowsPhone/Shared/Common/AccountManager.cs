using System;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Common
{
    public class AccountManager : IAccountManager<User>
    {
        public AccountManager()
        {
            LoggedUser = new User()
            {
                Name = "Emilka",
                Sold = 10,
                Bought = 46,
                CurrencyCount = 200
            };
        }
        public User LoggedUser { get; set; }
    }
}
