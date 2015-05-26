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
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.GetUserRequest;


public class OfferActivity extends BaseActivity {

    private int bookId;
    private int ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Intent in = getIntent();
        ((TextView)findViewById(R.id.book_name)).setText(in.getStringExtra("name"));
        ((TextView)findViewById(R.id.book_author)).setText(in.getStringExtra("author"));
        ((TextView)findViewById(R.id.offer_price)).setText(Integer.toString(in.getIntExtra("price", 0)));
        ((TextView)findViewById(R.id.book_description)).setText(in.getStringExtra("description"));
        String photobase64 = in.getStringExtra("photo");
        if(photobase64 != null && !photobase64.isEmpty()) {
            byte[] decodedString = Base64.decode(photobase64, Base64.DEFAULT);
            ((ImageView)findViewById(R.id.book_cover)).setImageBitmap(getResizedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), 150));
        }

        bookId = in.getIntExtra("id", -1);
        ownerId = in.getIntExtra("owner_id", -1);
        String tags = in.getStringExtra("tags");
        if(tags != null && !tags.isEmpty()) {
            TextView tagsView = (TextView) findViewById(R.id.book_tags);
            tagsView.setText(tags.replace(";", ", "));
            tagsView.setVisibility(View.VISIBLE);
        }

        getOwner();

    }

    private void getOwner(){

        GetUserRequest request = new GetUserRequest(AppData.token, ownerId);
        spiceManager.execute(request, new GetOwnerListener());

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

    private class GetOwnerListener extends BookshopRequestListener<User> {

        @Override
        public void onRequestCompleted(User user) {

            TextView ownerField = (TextView) findViewById(R.id.book_owner);
            ownerField.setText(user.name + " (show)");
            ownerField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OfferActivity.this, ProfileActivity.class);
                    intent.putExtra("user_id", ownerId);
                    startActivity(intent);
                }
            });

        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Unable to get owner", Toast.LENGTH_SHORT);
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT);
        }
    }
}
