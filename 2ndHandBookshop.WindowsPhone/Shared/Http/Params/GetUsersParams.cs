using System.Collections.Generic;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class GetUsersResponseParams : RequestResponse
    {
        public List<User> Users { get; set; }
    }

}
