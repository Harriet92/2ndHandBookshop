using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Windows.Web.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Http;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Http
{
    public static class ServerRequest
    {
        private static HttpClient httpClient;
        private static CancellationTokenSource cts;
        private const string usersUri = @"https://sheltered-forest-8633.herokuapp.com/users";

        static ServerRequest()
        {
            Helpers.CreateHttpClient(ref httpClient);
            cts = new CancellationTokenSource();
        }
        public static async Task<List<User>> GetUsers()
        {
            Uri resourceAddress;
            if (!Helpers.TryGetUri(usersUri, out resourceAddress))
            {
                return null;
            }
            HttpResponseMessage response = await httpClient.GetAsync(resourceAddress).AsTask(cts.Token);
            JObject m = JsonConvert.DeserializeObject<JObject>(response.Content.ToString());
            return JsonConvert.DeserializeObject<List<User>>(m["array"].ToString());
        }

        public static async Task<List<Offer>> GetLatestOffers()
        {
            return new List<Offer>
            {
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                }
            };
        }

        public static async Task<List<Offer>> GetOffers()
        {
            return new List<Offer>
            {
                new Offer()
                {
                    BookAuthor = "J. K. Rownling",
                    BookTitle = "Harry Potter",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes,
                    GenderTags = new List<Gender> {Gender.Children, Gender.Fantasy}
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes,
                    GenderTags = new List<Gender> {Gender.Drama, Gender.Fantasy}
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                },
                new Offer()
                {
                    BookAuthor = "Stephen King",
                    BookTitle = "Misery",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes
                }
            };
        }
    }
}
