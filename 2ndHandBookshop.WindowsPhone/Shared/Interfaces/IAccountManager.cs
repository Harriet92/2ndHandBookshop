using System;
using System.Collections.Generic;
using System.Text;

namespace SecondHandBookshop.Shared.Interfaces
{
    public interface IAccountManager<TUser>
    {
        TUser LoggedUser { get; }
        TUser LogIn { get; set; }
    }
}
