namespace SecondHandBookshop.Shared.Interfaces
{
    public interface IUserSettings
    {
        bool RememberPassword { get; set; }
        string SavedPassword { get; set; }
        string LastLocation { get; set; }
    }
}
