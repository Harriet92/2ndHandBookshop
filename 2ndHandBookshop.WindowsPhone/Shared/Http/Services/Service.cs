using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Windows.Storage.Streams;
using Windows.Web.Http;
using Flurl;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Helpers;

namespace SecondHandBookshop.Shared.Http.Services
{
    public abstract class Service
    {
        protected HttpClient httpClient;
        protected CancellationTokenSource cts;
        protected readonly string serviceUri;
        protected static string CurrentUserToken;
        private const string BaseUri = @"https://sheltered-forest-8633.herokuapp.com";
        protected Service(string serviceEndpoint)
        {
            Helpers.CreateHttpClient(ref httpClient);
            cts = new CancellationTokenSource();
            serviceUri = BaseUri + serviceEndpoint;
        }

        protected async Task<T> GetRequest<T>(string resourceEndpoint, Dictionary<string, string> parameters = null, bool authorize = true)
            where T: RequestResponse
        {
            Uri resourceAddress;
            if(parameters == null) parameters = new Dictionary<string, string>();
            if(authorize) parameters.Add("token", CurrentUserToken);
            string query = (serviceUri + resourceEndpoint).SetQueryParams(parameters);
            if (!Helpers.TryGetUri(query, out resourceAddress))
            {
                return default(T);
            }
            HttpResponseMessage response = await httpClient.GetAsync(resourceAddress).AsTask(cts.Token); 
            return await GetResponse<T>(response);
        }

        protected async Task<T> PostRequest<T>(string resourceEndpoint, JObject content, bool authorize = true)
            where T: RequestResponse
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(serviceUri + resourceEndpoint, out resourceAddress))
            {
                return default(T);
            }
            var stringContent = new HttpStringContent(authorize ? AppendToken(content).ToString() : content.ToString(), UnicodeEncoding.Utf8, "application/json");
            HttpResponseMessage response = await httpClient.PostAsync(resourceAddress, stringContent);
            return await GetResponse<T>(response);
        }
        protected async Task<T> PatchRequest<T>(string resourceEndpoint, JObject content, bool authorize = true)
            where T : RequestResponse
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(serviceUri + resourceEndpoint, out resourceAddress))
            {
                return default(T);
            }
            var stringContent = new HttpStringContent(authorize ? AppendToken(content).ToString() : content.ToString(), UnicodeEncoding.Utf8, "application/json");
            HttpResponseMessage response = await httpClient.PatchAsync(resourceAddress, stringContent);
            return await GetResponse<T>(response);
        }

        private JObject AppendToken(JObject content)
        {
            content.Add("token", CurrentUserToken);
            return content;
        }

        private string AppendToken(string queryString)
        {
            return queryString + '%' + "token=" + CurrentUserToken;
        }

        private async Task<T> GetResponse<T>(HttpResponseMessage response) where T : RequestResponse
        {
            string message = await response.Content.ReadAsStringAsync();
            if (response.StatusCode != HttpStatusCode.Ok && response.StatusCode != HttpStatusCode.Created)
            {
                var error = ((T)Activator.CreateInstance(typeof(T)));
                error.Error(response.StatusCode, "Http status: " + response.StatusCode + "\nPlease check your internet connection or try again later.");
                return error;
            }
            return JsonConvert.DeserializeObject<T>(message);
        }
    }
}
