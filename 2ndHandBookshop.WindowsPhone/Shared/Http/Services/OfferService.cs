using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;

namespace SecondHandBookshop.Shared.Http.Services
{
    public class OfferService : Service, IOfferService<Offer>
    {
        public OfferService()
            : base("/offers")
        {
        }


        public async Task<Offer> AddOffer(Offer newOffer)
        {
            throw new NotImplementedException();
        }

        public async Task<List<Offer>> GetOffers()
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
                    BookAuthor = "Super kucharz",
                    BookTitle = "Kucharkowanie",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes,
                    GenderTags = new List<Gender> {Gender.Cooking}
                },
                new Offer()
                {
                    BookAuthor = "Wołoszański",
                    BookTitle = "XXI wiek",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes,
                    GenderTags = new List<Gender> {Gender.Historical }
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

        public async Task<List<Offer>> GetLatestOffers(int count)
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
                    BookAuthor = "Super kucharz",
                    BookTitle = "Kucharkowanie",
                    CurrencyWorth = 10,
                    DiamondsWorth = 100,
                    Description = "Blah blah, lorem ipsum, super ksionszka",
                    PurchaserId = 0,
                    SellerId = 1,
                    State = BookState.ReadSeveralTimes,
                    GenderTags = new List<Gender> {Gender.Cooking}
                }
            };
        }
    }
}
