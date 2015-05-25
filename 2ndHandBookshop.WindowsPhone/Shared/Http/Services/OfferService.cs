using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json.Serialization;
using SecondHandBookshop.Shared.Enums;
using SecondHandBookshop.Shared.Http.Params;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;

namespace SecondHandBookshop.Shared.Http.Services
{
    public class OfferService : Service, IOfferService<OfferDTO>
    {
        public OfferService()
            : base("/offers")
        {
        }


        public async Task<AddOfferResponseParams> AddOffer(OfferDTO newOffer)
        {
            string path = "";
            JObject content = JObject.FromObject(newOffer, JsonSerializer.Create(new JsonSerializerSettings { ContractResolver = new CamelCasePropertyNamesContractResolver() }));
            var response = await PostRequest<AddOfferResponseParams>(path, content);
            return response;
        }

        public async Task<SetOfferStatusResponseParams> SetOfferAsCancelled(int offerId)
        {
            return await SetOfferStatus(offerId, OfferStatus.Cancelled);
        }
        public async Task<SetOfferStatusResponseParams> SetOfferAsFinalized(int offerId)
        {
            return await SetOfferStatus(offerId, OfferStatus.Finalized);
        }
        public async Task<SetOfferStatusResponseParams> PurchaseOffer(int offerId)
        {
            return await SetOfferStatus(offerId, OfferStatus.PurchasedOffer);
        }

        public async Task<SetOfferStatusResponseParams> SetOfferStatus(int _offerId, OfferStatus _status)
        {
            string path = "/" + _offerId;
            JObject content = JObject.FromObject(new SetOfferRequestParams(){offerid = _offerId, status = (int) _status});
            var response = await PatchRequest<SetOfferStatusResponseParams>(path, content);
            return response;
        }

        public async Task<GetOffersResponseParams> GetOffers()
        {
            return await GetRequest<GetOffersResponseParams>("");
        }

        public async Task<List<OfferDTO>> GetLatestOffers(int count)
        {
            var result = await GetRequest<GetOffersResponseParams>("");
            if (result.IsSuccess)
                return result.array.ToList().Take(count).ToList();//.Sort((x,y) => x.startedat < y.startedat);
            return new List<OfferDTO>();
        }
    }
}
