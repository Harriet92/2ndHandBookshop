package com.szlif.bookshop;

import android.support.v7.app.AppCompatActivity;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

public class BaseActivity extends AppCompatActivity {
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);


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
}
