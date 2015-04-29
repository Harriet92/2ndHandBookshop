using System;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Common
{
    public class AccountManager : IAccountManager<User>
    {
        public User LoggedUser { get; set; }
    }
}
