package com.szlif.bookshop;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.szlif.bookshop.models.User;
import com.szlif.bookshop.network.UpdateUserRequest;


public class BuyCurrency extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_currency);

        findViewById(R.id.buy_100).setOnClickListener(new BuyClickListener(100));
        findViewById(R.id.buy_200).setOnClickListener(new BuyClickListener(200));
        findViewById(R.id.buy_500).setOnClickListener(new BuyClickListener(500));
        findViewById(R.id.buy_1000).setOnClickListener(new BuyClickListener(1000));

    }

    public void BuyCurrency(int amount) {

        UpdateUserRequest request = new UpdateUserRequest(AppData.token, AppData.user.id, AppData.user.money + amount);
        spiceManager.execute(request, new RequestListener<User>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(User user) {
                AppData.user.money = user.money;
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy_currency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class BuyClickListener implements View.OnClickListener {

        private int amount;

        public BuyClickListener(int amount) {

            this.amount = amount;
        }

        @Override
        public void onClick(View view) {
            BuyCurrency(amount);
        }
    }
}
