using System.Collections.Generic;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.Shared.Http.Params
{
    public class GetOffersResponseParams : RequestResponse
    {
        public List<OfferDTO> array { get; set; }
    }

}
