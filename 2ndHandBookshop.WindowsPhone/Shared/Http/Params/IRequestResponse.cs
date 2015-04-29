using Windows.Web.Http;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class RequestResponse
    {
        public ErrorMessage ErrorMessage { get; set; }
        public void Error(HttpStatusCode statusCode, string message)
        {
            ErrorMessage = new ErrorMessage()
            {
                StatusCode = statusCode,
                Message = message
            };
        }
    }
}
