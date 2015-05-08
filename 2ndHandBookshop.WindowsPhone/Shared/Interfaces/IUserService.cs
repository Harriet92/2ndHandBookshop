using System.Threading.Tasks;
using SecondHandBookshop.Shared.Http.Params;

namespace SecondHandBookshop.Shared.Interfaces
{
    public interface IUserService
    {
        Task<LoginResponseParams> LogIn(string login, string password);
        Task<RegisterResponseParams> Register(string name, string email, string password);
        Task<GetUsersResponseParams> GetUsers();
        Task<bool> AddCurrencyToUser(int amount);
    }
}
