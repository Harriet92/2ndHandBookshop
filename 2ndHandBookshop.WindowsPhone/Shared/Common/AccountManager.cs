using System;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Common
{
    public class AccountManager
    {
        private static User loggedUser;

        public static User LoggedUser
        {
            get
            {
                if (loggedUser != null)
                    return loggedUser;
                loggedUser = LogIn();
                return loggedUser;
            }
            private set
            {
            }
        }

        private static User LogIn()
        {
            throw new NotImplementedException();
        }
    }
}
