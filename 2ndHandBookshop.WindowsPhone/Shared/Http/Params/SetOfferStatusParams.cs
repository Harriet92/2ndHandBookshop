namespace SecondHandBookshop.Shared.Http.Params
{
    public class SetOfferRequestParams
    {
        public int offerid { get; set; }
        public int status { get; set; }
    }

    public class SetOfferStatusResponseParams : RequestResponse
    {
        public bool Result { get; set; }
    }

}
