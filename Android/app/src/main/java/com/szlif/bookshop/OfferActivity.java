package com.szlif.bookshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class OfferActivity extends Activity {

    private int bookId;
    private String tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Intent in = getIntent();
        ((TextView)findViewById(R.id.book_name)).setText(in.getStringExtra("name"));
        ((TextView)findViewById(R.id.book_author)).setText(in.getStringExtra("author"));
        ((TextView)findViewById(R.id.offer_price)).setText(Integer.toString(in.getIntExtra("price", 0)));
        ((TextView)findViewById(R.id.book_owner)).setText(in.getStringExtra("name"));
        ((TextView)findViewById(R.id.book_description)).setText(in.getStringExtra("description"));
        String photobase64 = in.getStringExtra("photo");
        if(photobase64 != null && !photobase64.isEmpty()) {
            byte[] decodedString = Base64.decode(photobase64, Base64.DEFAULT);
            ((ImageView)findViewById(R.id.book_cover)).setImageBitmap(getResizedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), 150));
        }

        bookId = in.getIntExtra("id", -1);
        tags = in.getStringExtra("tags");
        if(tags != null && !tags.isEmpty()) {
            TextView tagsView = (TextView) findViewById(R.id.book_tags);
            tagsView.setText(tags.replace(";", ", "));
            tagsView.setVisibility(View.VISIBLE);
        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int maxSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float biggerDim = width > height ? width : height;
        float scale = maxSize / biggerDim;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
