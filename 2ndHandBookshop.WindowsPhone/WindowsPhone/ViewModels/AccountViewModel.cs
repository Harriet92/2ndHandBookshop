using System.Collections.ObjectModel;
using Caliburn.Micro;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using Message = SecondHandBookshop.Shared.Models.Message;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class AccountViewModel : PropertyChangedBase
    {
        private readonly IAccountManager<User> accountManager;
        public AccountViewModel(IAccountManager<User> _accountManager)
        {
            accountManager = _accountManager;
            Messages = new BindableCollection<Message>()
            {
                new Message()
                {
                    IsNotRead = true,
                    MessageText = "Propozycja kupna: Stephen King, Misery, 200$",
                    SenderNickname = "Kubi"
                }
            };
        }

        public string Nickname
        {
            get { return accountManager.LoggedUser.Name; }
        }
        public string Currency
        {
            get { return accountManager.LoggedUser.CurrencyCount.ToString(); }
        }
        public string Bought
        {
            get { return accountManager.LoggedUser.Bought.ToString(); }
        }
        public string Sold
        {
            get { return accountManager.LoggedUser.Sold.ToString(); }
        }
        public ObservableCollection<Shared.Models.Message> Messages { get; set; }
    }
}
