using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using WebClient;

namespace WebClientTests
{
    [TestClass]
    public class TestClass
    {
        const string API_URL = "https://sheltered-forest-8633.herokuapp.com";
        private IBookshopWebClient client = new RegularBookshopWebClient(API_URL);

        [TestMethod]
        public void TestGetUsers()
        {
            var users = client.GetUsers();
            Assert.IsNotNull(users);
        }
    }
}
