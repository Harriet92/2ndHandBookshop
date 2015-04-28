using System;
using System.Collections.Generic;
using System.Text;

namespace SecondHandBookshop.Shared.Extensions
{
    public static class SettingsHelper
    {
        public static T SafeValue<T>(this IDictionary<string, object> settings, string key)
        {
            object outValue;
            if (settings.TryGetValue(key, out outValue))
            {
                if (outValue is T) return (T)outValue;
            }
            return default(T);
        }
    }
}
