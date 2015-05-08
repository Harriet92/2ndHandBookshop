using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using SecondHandBookshop.Shared.Extensions;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Http.Services
{
    public class UserService : Service, IUserService
    {
        private IAccountManager<User> accountManager; 
        public UserService(IAccountManager<User> _accountManager )
            :base("/users")
        {
            accountManager = _accountManager;
        }

        public async Task<LoginResponseParams> LogIn(string login, string password)
        {
            string path = "/login";
            JObject content = JObject.FromObject(new LoginParams() { login = login, password = password.ToMD5Hash() });
            var response = await PostRequest<LoginResponseParams>(path, content);
            accountManager.LoggedUser = AutoMapper.Mapper.Map<User>(response.user);
            Service.CurrentUserToken = response.token;
            return response;
        }

        public async Task<RegisterResponseParams> Register(string name, string email, string password)
        {
            string path = "";
            JObject content = JObject.FromObject(new RegisterRequestParams() { name = name, email = email, password = password.ToMD5Hash() });
            var response = await PostRequest<RegisterResponseParams>(path, content, false).ConfigureAwait(false);
            return response;
        }

        public async Task<GetUsersResponseParams> GetUsers()
        {
            return await GetRequest<GetUsersResponseParams>(base.serviceUri);
        }

        public async Task<bool> AddCurrencyToUser(int amount)
        {
            accountManager.LoggedUser.CurrencyCount += amount;
            return true;
        }
    }
}
