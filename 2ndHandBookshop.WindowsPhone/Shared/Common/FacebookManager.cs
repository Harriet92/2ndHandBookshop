using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading.Tasks;
using Windows.ApplicationModel.Activation;
using Windows.Security.Authentication.Web;
using Windows.UI.Popups;
using Windows.UI.Xaml.Media.Imaging;
using Facebook;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Common
{
    public class FacebookManager
    {
        private FacebookClient fbClient = new FacebookClient();
        private readonly Uri callbackUri = WebAuthenticationBroker.GetCurrentApplicationCallbackUri();
        private readonly Uri loginUrl;
        private const string FacebookAppId = "426817894146914";//Enter your FaceBook App ID here  
        private const string FacebookPermissions = "user_about_me,read_stream,publish_actions";
        public string AccessToken
        {
            get { return fbClient.AccessToken; }
        }

        public FacebookManager()
        {
            loginUrl = fbClient.GetLoginUrl(new
            {
                client_id = FacebookAppId,
                redirect_uri = callbackUri.AbsoluteUri,
                scope = FacebookPermissions,
                display = "popup",
                response_type = "token"
            });
            Debug.WriteLine(callbackUri);//This is useful for fill Windows Store ID in Facebook WebSite  
        }

        public void LoginAndContinue()
        {
            WebAuthenticationBroker.AuthenticateAndContinue(loginUrl);
        }
        public void ContinueAuthentication(WebAuthenticationBrokerContinuationEventArgs args)
        {
            ValidateAndProccessResult(args.WebAuthenticationResult);
        }

        public async Task<User> GetFacebookProfileInfo()
        {
            if (AccessToken != null)
            {
                dynamic result = await fbClient.GetTaskAsync("me");
                return new User
                {
                    FacebookId = result.id,
                    Email = result.email,
                    FacebookName = result.name,
                };
            }
            return null;
        }

        public BitmapImage GetUserProfilePicture(string userID)
        {
            string profilePictureUrl = string.Format("https://graph.facebook.com/{0}/picture?type={1}&access_token={2}", userID, "square", AccessToken);
            return new BitmapImage(new Uri(profilePictureUrl));
        }
        public async Task<bool> ShareOffer(Offer offer)
        {
            var postParams = new
            {
                name = "See the offer: " + offer.BookAuthor + " " + offer.BookTitle ,
                caption = "Second Hand Bookshop",
                link = "http://secondhandbookshop.azurewebsites.net/",
                description = offer.Description,
                //picture = "http://facebooksdk.net/assets/img/logo75x75.png"
            };
            try
            {
                dynamic fbPostTaskResult = await fbClient.PostTaskAsync("/me/feed", postParams);
                var responseresult = (IDictionary<string, object>)fbPostTaskResult;
                MessageDialog SuccessMsg = new MessageDialog("Message posted sucessfully on facebook wall");
                await SuccessMsg.ShowAsync();
                return true;
            }
            catch (Exception ex)
            {
                MessageDialog ErrMsg = new MessageDialog("Error Ocuured!");
                ErrMsg.ShowAsync();
            }
            return false;
        }  
        private void ValidateAndProccessResult(WebAuthenticationResult result)
        {
            if (result.ResponseStatus == WebAuthenticationStatus.Success)
            {
                var responseUri = new Uri(result.ResponseData.ToString());
                var facebookOAuthResult = fbClient.ParseOAuthCallbackUrl(responseUri);

                if (string.IsNullOrWhiteSpace(facebookOAuthResult.Error))
                    fbClient.AccessToken = facebookOAuthResult.AccessToken;
                else
                {
                    //error i.e. user cancelled login
                }
            }
            else if (result.ResponseStatus == WebAuthenticationStatus.ErrorHttp)
            {
                //http error
            }
            else
            {
                fbClient.AccessToken = null;//Keep null when user signout from facebook  
            }
        }
    }
}
