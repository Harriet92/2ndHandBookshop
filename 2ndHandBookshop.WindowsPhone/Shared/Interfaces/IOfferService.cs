using System.Collections.Generic;
using System.Threading.Tasks;
using SecondHandBookshop.Shared.Http.Params;

namespace SecondHandBookshop.Shared.Interfaces
{
    public interface IOfferService<TOffer>
    {
        Task<TOffer> AddOffer(TOffer newOffer);
        Task<GetOffersResponseParams> GetOffers();
        Task<List<TOffer>> GetLatestOffers(int count);

        Task<SetOfferStatusResponseParams> SetOfferAsCancelled(int offerId);
        Task<SetOfferStatusResponseParams> SetOfferAsFinalized(int offerId);
        Task<SetOfferStatusResponseParams> PurchaseOffer(int offerId);
    }
}
