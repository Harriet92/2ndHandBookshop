using System;
using System.IO;
using System.Threading.Tasks;
using Windows.UI.Xaml.Media.Imaging;

namespace SecondHandBookshop.Shared.Helpers
{
    public class ImageConverter
    {
        public static BitmapImage GetFromBase64String(string base64)
        {
            if (String.IsNullOrEmpty(base64))
                return null;
            byte[] fileBytes = Convert.FromBase64String(base64);

            using (MemoryStream ms = new MemoryStream(fileBytes, 0, fileBytes.Length))
            {
                ms.Write(fileBytes, 0, fileBytes.Length);
                BitmapImage bitmapImage = new BitmapImage();
                bitmapImage.SetSource(ms.AsRandomAccessStream());
                return bitmapImage;
            }
        }
    }
}
