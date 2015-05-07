using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml.Media.Imaging;
using Newtonsoft.Json;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Helpers;

namespace SecondHandBookshop.Shared.Models
{
    public class Offer
    {
        private string photoBase64;
        private BitmapImage photo;
        public int Id { get; set; }
        public string BookTitle { get; set; }
        public string BookAuthor { get; set; }
        public string Description { get; set; }
        public int SellerId { get; set; }
        public int PurchaserId { get; set; }
        public DateTime StartedAt { get; set; }
        public DateTime ExpiresAt { get; set; }
        public int CurrencyWorth { get; set; }
        public OfferStatus Status { get; set; }
        public BitmapImage Photo { get { return photo;} set { photo = value; }}
        public string PhotoBase64
        {
            get
            {
                return photoBase64;
            }
            set
            {
                photoBase64 = value;
                Photo = ImageConverter.GetFromBase64String(value);
            }
        }

        public string Tags
        {
            get { return SerializeTags(); }
            set { GenderTags = DeserializeTags(value); }
        }
        public List<Gender> GenderTags { get; set; }
        public Offer()
        {
            StartedAt = DateTime.Now;
            ExpiresAt = StartedAt + TimeSpan.FromDays(7);
            Status = OfferStatus.Added;
            GenderTags = new List<Gender>();
            //TODO: Remove placeholder!
            Photo = new BitmapImage(new Uri("ms-appx:///Assets/pies.jpg", UriKind.RelativeOrAbsolute));
        }

        public async void SetPhotoString(BitmapImage bi)
        {
            photoBase64 = await bi.ConvertToBase64();
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
