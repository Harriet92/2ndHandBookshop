﻿using System.Collections.Generic;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class GetUsersResponseParams : RequestResponse
    {
        public List<UserDTO> array { get; set; }
    }

}
