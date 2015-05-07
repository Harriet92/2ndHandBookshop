﻿using System.Collections.Generic;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Http.Params;

namespace SecondHandBookshop.Shared.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public int CurrencyCount { get; set; }
        public int Bought { get; set; }
        public int Sold { get; set; }

        //TODO: Move to settings?
        //public List<Gender> GenderTags { get; set; }


        public User()
        {
        }
    }
}
