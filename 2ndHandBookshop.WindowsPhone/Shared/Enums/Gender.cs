using System.Collections.Generic;
using System.Dynamic;

namespace SecondHandBookshop.Shared.Enums
{
    public enum Gender
    {
        Drama,
        ScienceFiction,
        Historical,
        Education,
        Cooking,
        Fantasy,
        Romance,
        Criminal,
        Children
    }

    public static class GenderList
    {
        public static List<string> Get()
        {
            return new List<string>
            {
                "Drama",
                "ScienceFiction",
                "Historical",
                "Education",
                "Cooking",
                "Fantasy",
                "Romance",
                "Criminal",
                "Children"
            };
        }
    }
}
