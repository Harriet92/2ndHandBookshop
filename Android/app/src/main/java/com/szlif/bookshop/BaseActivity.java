package com.szlif.bookshop;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.octo.android.robospice.SpiceManager;
import com.szlif.bookshop.network.BookshopService;

public class BaseActivity extends Activity {

    protected SpiceManager spiceManager = new SpiceManager(BookshopService.class);

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_bar_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (id == R.id.action_bar_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("user_id", AppData.user.id);
            startActivity(intent);
        }
        if (id == R.id.action_bar_add_offer) {
            Intent intent = new Intent(this, AddOfferActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_bar_logout) {
            AppData.user = null;
            AppData.token = null;
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            LoginManager.getInstance().logOut();
            startActivity(intent);
        }
        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
