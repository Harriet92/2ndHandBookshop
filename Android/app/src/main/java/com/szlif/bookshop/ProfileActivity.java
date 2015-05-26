package com.szlif.bookshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.GetUserRequest;


public class ProfileActivity extends BaseActivity {

    private TextView usernameField;
    private TextView soldField;
    private TextView boughtField;
    private View currencyView;
    private TextView currentCurrencyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameField = (TextView) findViewById(R.id.username);
        soldField = (TextView) findViewById(R.id.sold_view);
        boughtField = (TextView) findViewById(R.id.bought_view);
        currencyView = findViewById(R.id.currency_view);
        currentCurrencyView = (TextView) findViewById(R.id.currency);
        Button buyMoreButton = (Button) findViewById(R.id.buy_more_currency);
        buyMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        Intent in = getIntent();
        getUser(in.getIntExtra("user_id", -1));
    }
    private void getUser(int id){

        GetUserRequest request = new GetUserRequest(AppData.token, id);
        spiceManager.execute(request, new GetUserListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private class GetUserListener extends BookshopRequestListener<User> {

        @Override
        public void onRequestCompleted(User user) {
            usernameField.setText(user.name);
            soldField.setText(Integer.toString(user.sold));
            boughtField.setText(Integer.toString(user.bought));

            if(AppData.user.id == user.id) {
                currencyView.setVisibility(View.VISIBLE);
                currentCurrencyView.setText(Integer.toString(user.money));
            }
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Error: " + error.error, Toast.LENGTH_SHORT);
            finish();
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT);
            finish();
        }
    }
}
