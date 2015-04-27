using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Linq;

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
            return Enum.GetNames(typeof (Gender)).ToList();
        }

        public static List<Gender> ConvertFromString(List<string> tagList)
        {
            return tagList.Select(tag => (Gender) Enum.Parse(typeof (Gender), tag)).ToList();
        }
    }
}
