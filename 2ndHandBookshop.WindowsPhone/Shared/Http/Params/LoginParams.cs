namespace SecondHandBookshop.Shared.Http.Params
{
    public class LoginParams
    {
        public string login { get; set; }
        public string password { get; set; }
    }
    public class LoginResponseParams : RequestResponse
    {
        public string token { get; set; }
    }

}
