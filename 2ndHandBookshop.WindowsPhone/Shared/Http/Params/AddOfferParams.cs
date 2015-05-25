using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class AddOfferResponseParams : RequestResponse
    {
        public OfferDTO offer { get; set; }
    }

}
