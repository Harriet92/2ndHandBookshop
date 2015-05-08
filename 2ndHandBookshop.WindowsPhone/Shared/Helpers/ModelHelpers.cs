using System;
using System.Collections.Generic;
using System.Text;

namespace SecondHandBookshop.Shared.Helpers
{
    public static class ModelHelpers
    {
        public static int UrlToId(string url)
        {
            if (String.IsNullOrEmpty(url))
                return -1;
            var split = url.Split('/');
            return split.Length > 0 ? Int32.Parse(split[split.Length - 1]) : -1;
        }
    }
}
