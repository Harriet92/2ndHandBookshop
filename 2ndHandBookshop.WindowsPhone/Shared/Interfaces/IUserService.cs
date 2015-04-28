using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace SecondHandBookshop.Shared.Interfaces
{
    public interface IUserService<TUser>
    {
        Task<TUser> LogIn(string login, string password);
        Task<TUser> Register(string login, string password);
        Task<List<TUser>> GetUsers();
    }
}
