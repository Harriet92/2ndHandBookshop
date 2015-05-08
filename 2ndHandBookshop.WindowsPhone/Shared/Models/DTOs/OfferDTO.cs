using System;
using Windows.Devices.Geolocation;
using SecondHandBookshop.Shared.Enums;

namespace SecondHandBookshop.Shared.Models.DTOs
{
    public class OfferDTO
    {
        public int id { get; set; }
        public string booktitle { get; set; }
        public string bookauthor { get; set; }
        public string description { get; set; }
        public int ownerid { get; set; }
        public int purchaserid { get; set; }
        public DateTime startedat { get; set; }
        public DateTime expiresat { get; set; }
        public int price { get; set; }
        public OfferStatus Status { get; set; }
        public string photoBase64 { get; set; }
        public string tags { get; set; }
        public string url { get; set; }
        public BasicGeoposition location { get; set; }
    }
}
