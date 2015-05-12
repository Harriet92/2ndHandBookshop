package com.szlif.bookshop;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.szlif.bookshop.models.Session;
import com.szlif.bookshop.models.User;
import com.szlif.bookshop.network.LoginRequest;


public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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

    public void MakeRequest(View view)
    {
        performRequest();
    }

    private void performRequest() {

        LoginRequest request = new LoginRequest("Emilka1", "trudnehaslo");
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new LoginRequestListener());
    }

    private class LoginRequestListener implements RequestListener<Session> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
        }

        @Override
        public void onRequestSuccess(Session session) {
            EditText text = (EditText) findViewById(R.id.text_field);
            text.setText(session.token);
        }
    }
}
