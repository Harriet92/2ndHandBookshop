using System;
using System.Collections.Generic;
using Windows.ApplicationModel;
using Windows.ApplicationModel.Activation;
using Windows.Devices.Geolocation;
using Windows.Globalization.DateTimeFormatting;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Caliburn.Micro;
using SDKTemplate.Common;
using SecondHandBookshop.Shared.Common;
using SecondHandBookshop.Shared.Helpers;
using SecondHandBookshop.Shared.Http.Services;
using SecondHandBookshop.Shared.Interfaces;
using SecondHandBookshop.Shared.Models;
using SecondHandBookshop.Shared.Models.DTOs;
using SecondHandBookshop.WindowsPhone.ViewModels;
using SecondHandBookshop.WindowsPhone.Views;

namespace SecondHandBookshop.WindowsPhone
{
    /// <summary>
    /// Provides application-specific behavior to supplement the default Application class.
    /// </summary>
    public sealed partial class App : CaliburnApplication
    {
        private WinRTContainer container;
        private readonly ContinuationManager _continuator = new ContinuationManager();
        public App()
        {
            InitializeComponent();
            ConfigureAutoMapper();
        }

        protected override void Configure()
        {
            container = new WinRTContainer();

            container.RegisterWinRTServices();
            container.RegisterInstance(typeof(IAccountManager<User>), "", new AccountManager());
            container.RegisterInstance(typeof(IOfferService<OfferDTO>), "", new OfferService());
            container.RegisterInstance(typeof(IUserService), "", new UserService(container.GetInstance<IAccountManager<User>>()));
            container.RegisterInstance(typeof(FacebookManager), "", new FacebookManager());
            container.RegisterInstance(typeof(LocationManager), "", new LocationManager());

            container.PerRequest<MainPageViewModel>();
            container.PerRequest<AccountViewModel>();
            container.PerRequest<AddOfferViewModel>();
            container.PerRequest<NewestOffersViewModel>();
            container.PerRequest<SearchViewModel>();
            container.PerRequest<LogInViewModel>();
            container.PerRequest<RegistrationViewModel>();
            container.PerRequest<OfferDetailsViewModel>();
            container.PerRequest<AddCurrencyViewModel>();
        }

        protected override void PrepareViewFirst(Frame rootFrame)
        {
            container.RegisterNavigationService(rootFrame);
        }

        protected override void OnLaunched(LaunchActivatedEventArgs args)
        {
            DisplayRootView<LogInView>();
        }
        protected async override void OnActivated(IActivatedEventArgs args)
        {
            CreateRootFrame();

            if (args.PreviousExecutionState == ApplicationExecutionState.Terminated)
            {
                try
                {
                    await SuspensionManager.RestoreAsync();
                }
                catch { }
            }

            if (args is IContinuationActivatedEventArgs)
                _continuator.ContinueWith(args, container.GetInstance<LogInViewModel>());

            Window.Current.Activate();
        }
        protected override async void OnSuspending(object sender, SuspendingEventArgs e)
        {
            var deferral = e.SuspendingOperation.GetDeferral();
            await SuspensionManager.SaveAsync();
            deferral.Complete();
        }
        protected override object GetInstance(Type service, string key)
        {
            return container.GetInstance(service, key);
        }

        protected override IEnumerable<object> GetAllInstances(Type service)
        {
            return container.GetAllInstances(service);
        }

        protected override void BuildUp(object instance)
        {
            container.BuildUp(instance);
        }

        private void CreateRootFrame()
        {
            Frame rootFrame = Window.Current.Content as Frame;
            if (rootFrame == null)
            {
                rootFrame = new Frame();
                SuspensionManager.RegisterFrame(rootFrame, "AppFrame");
                Window.Current.Content = rootFrame;
            }
        }

        private void ConfigureAutoMapper()
        {
            AutoMapper.Mapper.CreateMap<UserDTO, User>()
                .ForMember(x => x.CurrencyCount, opt => opt.ResolveUsing(y => y.money))
                .ForMember(x => x.Id, opt => opt.ResolveUsing(y => ModelHelpers.UrlToId(y.url)));

            AutoMapper.Mapper.CreateMap<Offer, OfferDTO>();
            AutoMapper.Mapper.CreateMap<OfferDTO, Offer>()
                .ForMember(x => x.SellerId, opt => opt.ResolveUsing(y => y.ownerid));
        }
    }
}
