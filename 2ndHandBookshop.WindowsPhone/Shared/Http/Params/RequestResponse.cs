using System;
using Windows.Web.Http;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class RequestResponse
    {
        public string error { get; set; }
        public HttpStatusCode StatusCode { get; set; }
        public void Error(HttpStatusCode statusCode, string message)
        {
            error = message;
            StatusCode = statusCode;
        }

        public bool IsSuccess { get { return String.IsNullOrEmpty(error); } }
    }
}
