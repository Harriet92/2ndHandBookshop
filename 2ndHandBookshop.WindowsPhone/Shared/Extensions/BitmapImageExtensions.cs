using System;
using System.IO;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Windows.Storage.Streams;
using Windows.UI.Xaml.Media.Imaging;

namespace SecondHandBookshop.Shared.Helpers
{
    public static class BitmapImageExtensions
    {
        public static async Task<string> ConvertToBase64(this BitmapImage bitmapImage)
        {
            try
            {
                var fixedUri = bitmapImage.UriSource;
                if (!fixedUri.ToString().Contains("///"))
                    fixedUri = new Uri(bitmapImage.UriSource.Scheme + "://" + bitmapImage.UriSource.AbsolutePath);

                RandomAccessStreamReference rasr = RandomAccessStreamReference.CreateFromUri(fixedUri);
                var streamWithContent = await rasr.OpenReadAsync();
                byte[] buffer = new byte[streamWithContent.Size];
                var result =
                    await
                        streamWithContent.ReadAsync(buffer.AsBuffer(), (uint)streamWithContent.Size,
                            InputStreamOptions.None);
                using (MemoryStream ms = new MemoryStream(result.ToArray()))
                {
                    return Convert.ToBase64String(ms.ToArray());
                }
            }
            catch (Exception)
            {
                return String.Empty;
            }
        }
    }
}
