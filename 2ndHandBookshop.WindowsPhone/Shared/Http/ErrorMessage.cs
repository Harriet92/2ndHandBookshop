using Windows.Web.Http;

namespace SecondHandBookshop.Shared.Http
{
    public class ErrorMessage
    {
        public HttpStatusCode StatusCode { get; set; }
        public string Message { get; set; }
    }
}
