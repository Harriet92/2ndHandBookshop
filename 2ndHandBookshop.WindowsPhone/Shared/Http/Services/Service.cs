using System;
using System.Threading;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Web.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Windows.Storage.Streams;
using SecondHandBookshop.Shared.Http.Params;

namespace SecondHandBookshop.Shared.Http.Services
{
    public abstract class Service
    {
        protected HttpClient httpClient;
        protected CancellationTokenSource cts;
        protected readonly string serviceUri;
        private const string BaseUri = @"https://sheltered-forest-8633.herokuapp.com";
        protected Service(string serviceEndpoint)
        {
            Helpers.CreateHttpClient(ref httpClient);
            cts = new CancellationTokenSource();
            serviceUri = BaseUri + serviceEndpoint;
        }

        protected async Task<T> GetRequest<T>(string resourceEndpoint)
            where T: RequestResponse
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(serviceUri + resourceEndpoint, out resourceAddress))
            {
                return default(T);
            }
            HttpResponseMessage response = await httpClient.GetAsync(resourceAddress).AsTask(cts.Token); 
            return await GetResponse<T>(response);
        }

        protected async Task<T> PostRequest<T>(string resourceEndpoint, JObject content)
            where T: RequestResponse
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(serviceUri + resourceEndpoint, out resourceAddress))
            {
                return default(T);
            }
            var stringContent = new HttpStringContent(content.ToString(), UnicodeEncoding.Utf8, "application/json");
            HttpResponseMessage response = await httpClient.PostAsync(resourceAddress, stringContent);
            return await GetResponse<T>(response);
        }
        private async Task<T> GetResponse<T>(HttpResponseMessage response) where T : RequestResponse
        {
            string message = await response.Content.ReadAsStringAsync();
            if (response.StatusCode != HttpStatusCode.Ok && response.StatusCode != HttpStatusCode.Created)
            {
                var error = ((T)Activator.CreateInstance(typeof(T)));
                JObject m = JsonConvert.DeserializeObject<JObject>(message);
                error.Error(response.StatusCode, JsonConvert.DeserializeObject<string>(m["array"].ToString()));
                return error;
            }
            return JsonConvert.DeserializeObject<T>(message);
        }
    }
}
