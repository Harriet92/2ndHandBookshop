using System;
using System.Collections.Generic;
using System.Text;
using Windows.ApplicationModel.Activation;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace SecondHandBookshop.Shared.Helpers
{
    class ContinuationManager
    {
        public void ContinueWith(IActivatedEventArgs args, IWebAuthenticationBrokerContinuable continuator)
        {
            switch (args.Kind)
            {
                case ActivationKind.PickFileContinuation:
                    break;
                case ActivationKind.PickFolderContinuation:
                    break;
                case ActivationKind.PickSaveFileContinuation:
                    break;
                case ActivationKind.WebAuthenticationBrokerContinuation:
                    if (continuator != null)
                        continuator.ContinueWithWebAuthenticationBroker((WebAuthenticationBrokerContinuationEventArgs)args);
                    break;
                default:
                    break;
            }
        }
    }
    interface IWebAuthenticationBrokerContinuable
    {
        void ContinueWithWebAuthenticationBroker(WebAuthenticationBrokerContinuationEventArgs args);
    }  
}
