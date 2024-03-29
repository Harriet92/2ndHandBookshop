package com.szlif.bookshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.paging.listview.PagingListView;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.GetOffersRequest;
import com.szlif.bookshop.network.GetUserRequest;


public class ProfileActivity extends BaseActivity {

    private TextView usernameField;
    private TextView soldField;
    private TextView boughtField;
    private View currencyView;
    private TextView currentCurrencyView;
    private ProgressBar progressBar;
    private View userView;

    private int user_id;

    private Integer offersPerPage = 15;
    private Integer pager = 0;
    private OffersArrayAdapter adapter;
    private PagingListView itemsView;

    private TextView emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameField = (TextView) findViewById(R.id.username);
        soldField = (TextView) findViewById(R.id.sold_view);
        boughtField = (TextView) findViewById(R.id.bought_view);
        currencyView = findViewById(R.id.currency_view);
        currentCurrencyView = (TextView) findViewById(R.id.currency);
        emailView = (TextView) findViewById(R.id.user_email);
        progressBar = (ProgressBar) findViewById(R.id.load_progress);
        userView = findViewById(R.id.user_view);
        itemsView = getAndSetupListView(R.id.items_list_view);
        Button buyMoreButton = (Button) findViewById(R.id.buy_more_currency);
        buyMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, BuyCurrency.class);
                startActivity(intent);
            }
        });

        Intent in = getIntent();
        refresh(in.getIntExtra("user_id", -1), in.getBooleanExtra("show_email", false));
    }

    private PagingListView getAndSetupListView(int id) {
        PagingListView view = (PagingListView) findViewById(id);
        adapter = new OffersArrayAdapter();
        view.setAdapter(adapter);
        view.setHasMoreItems(false);
        view.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                performSearch();
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getApplicationContext(), OfferActivity.class);
                OfferDetail selectedItem = (OfferDetail)adapterView.getItemAtPosition(i);
                in.putExtra("offer_id", selectedItem.id);
                startActivity(in);
            }
        });
        return view;
    }

    private void refresh(int user_id, boolean showEmail) {
        userView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        this.user_id = user_id;
        getUser(user_id, showEmail);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refresh(intent.getIntExtra("user_id", -1), intent.getBooleanExtra("show_email", false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent in = getIntent();
        refresh(in.getIntExtra("user_id", -1), in.getBooleanExtra("show_email", false));
    }

    private void getUser(int id, boolean showEmail){

        GetUserRequest request = new GetUserRequest(AppData.token, id);
        spiceManager.execute(request, new GetUserListener(showEmail));
    }


    private void performSearch() {
        boolean isMyProfile = AppData.user.id == user_id;
        GetOffersRequest request = new GetOffersRequest(AppData.token, offersPerPage, pager,
                null, null, null, user_id, isMyProfile ? user_id : null, null,
                AppData.user.id == user_id ? null : 1, null, null);
        spiceManager.execute(request, new GetOffersRequestListener());
        pager++;
    }


    private void clearData() {
        pager = 0;
        adapter.removeAllItems();
        itemsView.setHasMoreItems(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private class GetOffersRequestListener extends BookshopRequestListener<OfferDetail.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT);
            itemsView.onFinishLoading(false, null);

        }

        @Override
        public void onRequestCompleted(OfferDetail.List offerDetails) {
            itemsView.onFinishLoading(offerDetails.array.size() == offersPerPage, offerDetails.array);
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Error: " + error.error, Toast.LENGTH_SHORT);
            itemsView.onFinishLoading(false, null);
        }
    }

    private class GetUserListener extends BookshopRequestListener<User> {

        private boolean showEmail;

        public GetUserListener(boolean showEmail) {

            this.showEmail = showEmail;
        }

        @Override
        public void onRequestCompleted(User user) {
            usernameField.setText(user.name);
            soldField.setText("Sold: " + Integer.toString(user.sold));
            boughtField.setText("Bought:" + Integer.toString(user.bought));

            if(AppData.user.id == user.id) {
                currencyView.setVisibility(View.VISIBLE);
                currentCurrencyView.setText(Integer.toString(user.money));
                AppData.user = user;
            } else {
                currencyView.setVisibility(View.GONE);
            }

            if(AppData.user.id == user.id || this.showEmail) {
                emailView.setText(user.email);
                emailView.setVisibility(View.VISIBLE);
            } else {
                emailView.setVisibility(View.GONE);
            }

            clearData();

            userView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Error: " + error.error, Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
