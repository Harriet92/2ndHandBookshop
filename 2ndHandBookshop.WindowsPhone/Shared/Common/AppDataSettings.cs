using Windows.Storage;
using SecondHandBookshop.Shared.Extensions;
using SecondHandBookshop.Shared.Interfaces;

namespace SecondHandBookshop.Shared.Common
{
    public class AppDataSettings : IUserSettings
    {
        private const string RememberPasswordKey = "rememberpass";
        private const string PasswordKey = "password";
        private const string LastLocationKey = "lastlocation";
        public bool RememberPassword
        {
            get { return ApplicationData.Current.LocalSettings.Values.SafeValue<bool>(RememberPasswordKey); }
            set { ApplicationData.Current.LocalSettings.Values[RememberPasswordKey] = value; }
        }

        public string SavedPassword
        {
            get { return ApplicationData.Current.LocalSettings.Values.SafeValue<string>(PasswordKey); }
            set { ApplicationData.Current.LocalSettings.Values[RememberPasswordKey] = value; }
        }

        public string LastLocation
        {
            get { return ApplicationData.Current.LocalSettings.Values.SafeValue<string>(LastLocationKey); }
            set { ApplicationData.Current.LocalSettings.Values[RememberPasswordKey] = value; }
        }
    }
}
