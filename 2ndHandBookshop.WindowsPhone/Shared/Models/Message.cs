using System;
using System.Collections.Generic;
using Windows.UI.Xaml.Media.Imaging;
using SecondHandBookshop.Shared.Enums;

namespace SecondHandBookshop.Shared.Models
{
    public class Message
    {
        public string SenderNickname { get; set; }
        public string MessageText { get; set; }
        public bool IsNotRead { get; set; }
    }
}
