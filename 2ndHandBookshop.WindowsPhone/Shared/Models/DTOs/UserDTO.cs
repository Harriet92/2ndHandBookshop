using System;
using System.Collections.Generic;
using System.Text;

namespace SecondHandBookshop.Shared.Models.DTOs
{
    public class UserDTO
    {
        public string name { get; set; }
        public string email { get; set; }
        public string url { get; set; }
        public int money { get; set; }
    }
}
