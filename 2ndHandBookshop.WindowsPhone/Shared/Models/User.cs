using System.Collections.Generic;
using SecondHandBookshop.Shared.Enums;

namespace SecondHandBookshop.Shared.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public List<Gender> GenderTags { get; set; }

        public User()
        {
            GenderTags = new List<Gender>();
        }
    }
}
