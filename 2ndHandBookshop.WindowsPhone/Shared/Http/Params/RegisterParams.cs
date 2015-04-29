using System;
using System.Collections.Generic;
using System.Text;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class RegisterRequestParams
    {
        public string name { get; set; }
        public string email { get; set; }
        public string password { get; set; }
    }

    public class RegisterResponseParams : RequestResponse
    {
        public string name { get; set; }
        public string email { get; set; }
        public string url { get; set; }
    }

}
