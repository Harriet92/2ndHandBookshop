using System;
using System.Threading.Tasks;
using Windows.UI.Popups;
using Caliburn.Micro;

namespace SecondHandBookshop.WindowsPhone.ViewModels
{
    public class ViewModel: PropertyChangedBase
    {
        private bool showLoadingIndicator = false;
        public bool ProgressRing
        {
            get
            {
                return showLoadingIndicator;
            }
            set
            {
                showLoadingIndicator = value;
                NotifyOfPropertyChange(() => ProgressRing);
            }
        }

        public void ShowLoadingIndicator()
        {
            ProgressRing = true;
        }

        public void HideLoadingIndicator()
        {
            ProgressRing = false;
        }

        public async Task<bool> ShowMessage(string message)
        {
            MessageDialog dialog = new MessageDialog(message);
            await dialog.ShowAsync();
            return true;
        }
    }
}
