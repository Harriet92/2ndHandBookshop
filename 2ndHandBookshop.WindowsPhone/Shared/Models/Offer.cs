using System;
using System.Collections.Generic;
using System.IO;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media.Imaging;
using SecondHandBookshop.Shared.Enums;

namespace SecondHandBookshop.Shared.Models
{
    public class Offer
    {
        public int Id { get; set; }
        public string BookTitle { get; set; }
        public string BookAuthor { get; set; }
        public string Description { get; set; }
        public int SellerId { get; set; }
        public int PurchaserId { get; set; }
        public DateTime StartedAt { get; set; }
        public DateTime ExpiresAt { get; set; }
        public int CurrencyWorth { get; set; }
        public int DiamondsWorth { get; set; }
        public BookState State { get; set; }
        public TransactionStep TransactionStep { get; set; }
        public BitmapImage Photo { get; set; }
        public List<Gender> GenderTags { get; set; } 

        public Offer()
        {
            StartedAt = DateTime.Now;
            ExpiresAt = StartedAt + TimeSpan.FromDays(7);
            TransactionStep = TransactionStep.Added;
            GenderTags = new List<Gender>();
            //TODO: Remove placeholder!
            Photo = new BitmapImage(new Uri("ms-appx:///Assets/mediumtile-sdk.png", UriKind.RelativeOrAbsolute));
        }
    }
}
