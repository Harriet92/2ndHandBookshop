using System;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;

namespace SecondHandBookshop.Shared.Common
{
    public class LocationManager
    {
        public async Task<BasicGeoposition> GetCurrentLocation()
        {
            Geolocator geo = new Geolocator();
            Geoposition pos = await geo.GetGeopositionAsync(); 
            return pos.Coordinate.Point.Position;
        }

        public async Task<double> GetDistanceFromCurrentLocation(BasicGeoposition target)
        {
            var currentLoc = await GetCurrentLocation();
            return Distance(currentLoc, target);
        }

        public double Distance(BasicGeoposition pos1, BasicGeoposition pos2)
        {
            double R = 6371;
            double dLat = ToRadian(pos2.Latitude - pos1.Latitude);
            double dLon = ToRadian(pos2.Longitude - pos1.Longitude);
            double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                Math.Cos(ToRadian(pos1.Latitude)) * Math.Cos(ToRadian(pos2.Latitude)) *
                Math.Sin(dLon / 2) * Math.Sin(dLon / 2);
            double c = 2 * Math.Asin(Math.Min(1, Math.Sqrt(a)));
            double d = R * c;
            return d;
        }
        private double ToRadian(double val)
        {
            return (Math.PI / 180) * val;
        }
    }
}
