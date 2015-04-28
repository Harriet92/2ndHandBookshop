using System;
using System.Threading;
using System.Threading.Tasks;
using Windows.Web.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

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
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(serviceUri + resourceEndpoint, out resourceAddress))
            {
                return default(T);
            }
            HttpResponseMessage response = await httpClient.GetAsync(resourceAddress).AsTask(cts.Token);
            JObject m = JsonConvert.DeserializeObject<JObject>(response.Content.ToString());
            return JsonConvert.DeserializeObject<T>(m["array"].ToString());
        }

        protected async Task<T> PostRequest<T>(string resourceEndpoint, JObject content)
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(serviceUri + resourceEndpoint, out resourceAddress))
            {
                return default(T);
            }
            var stringContent = new HttpStringContent(content.ToString());
            HttpResponseMessage response = await httpClient.PostAsync(resourceAddress, stringContent);
            JObject m = JsonConvert.DeserializeObject<JObject>(response.Content.ToString());
            return JsonConvert.DeserializeObject<T>(m["array"].ToString());
        }
    }
}
