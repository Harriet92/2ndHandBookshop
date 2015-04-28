using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Http.Services
{
    public class UserService : Service, IUserService<User>
    {
        public UserService()
            :base("/users")
        {
        }

        public async Task<User> LogIn(string login, string password)
        {
            throw new NotImplementedException();
        }

        public async Task<User> Register(string login, string password)
        {
            throw new NotImplementedException();
        }

        public async Task<List<User>> GetUsers()
        {
            return await GetRequest<List<User>>(base.serviceUri);
        }
    }
}
