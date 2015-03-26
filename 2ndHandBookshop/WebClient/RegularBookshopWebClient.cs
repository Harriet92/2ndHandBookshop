using System;
using RestSharp;

namespace WebClient
{
    public class RegularBookshopWebClient : IBookshopWebClient
    {
        private RestClient client;

        public RegularBookshopWebClient(string baseEndpoint)
        {
            client = new RestClient(baseEndpoint);
        }

        public object GetUsers()
        {
            var request = new RestRequest("users", Method.GET);
            var response = client.Execute(request);
            Console.WriteLine(response.Content); // raw content as string
            return response.Content;
        }
    }
}
