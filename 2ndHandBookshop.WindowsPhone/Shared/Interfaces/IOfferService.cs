using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace SecondHandBookshop.Shared.Interfaces
{
    public interface IOfferService<TOffer>
    {
        Task<TOffer> AddOffer(TOffer newOffer);
        Task<List<TOffer>> GetOffers();
        Task<List<TOffer>> GetLatestOffers(int count);
    }
}
