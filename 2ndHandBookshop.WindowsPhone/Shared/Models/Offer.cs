using System;
using System.Collections.Generic;
using System.Text;
using Windows.Devices.Geolocation;
using Windows.UI.Xaml.Media.Imaging;
using Newtonsoft.Json;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Helpers;

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
        public int Price { get; set; }
        public OfferStatus Status { get; set; }
        public BasicGeoposition Location { get; set; }
        [JsonIgnore]
        public BitmapImage Photo { get; set; }

        public string Tags
        {
            get { return SerializeTags(); }
            set { GenderTags = DeserializeTags(value); }
        }
        [JsonIgnore]
        public List<Gender> GenderTags { get; set; }
        public Offer()
        {
            Status = OfferStatus.Added;
            GenderTags = new List<Gender>();
        }

        private string SerializeTags()
        {
            if (GenderTags.Count < 1)
                return String.Empty;
            StringBuilder builder = new StringBuilder(GenderTags[0].ToString());
            for (int i = 1; i < GenderTags.Count; i++)
            {
                builder.Append(String.Format(".{0}", GenderTags[i]));
            }
            return builder.ToString();
        }

        private List<Gender> DeserializeTags(string list)
        {
            if(String.IsNullOrEmpty(list))
                return new List<Gender>();
            string[] tags = list.Split(',');
            var result = new List<Gender>();
            if (tags.Length < 1)
                return result;

            for (int i = 1; i < tags.Length; i++)
            {
                result.Add((Gender)Enum.Parse(typeof(Gender), tags[i]));
            }
            return result;
        }
    }
}
