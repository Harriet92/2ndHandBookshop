using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class LoginParams
    {
        public string login { get; set; }
        public string password { get; set; }
    }
    public class LoginResponseParams : RequestResponse
    {
        public string expiration_date { get; set; }
        public UserDTO user { get; set; }
        public string token { get; set; }
    }

}
