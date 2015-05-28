package com.szlif.bookshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.CreateOfferRequest;

import java.io.ByteArrayOutputStream;


public class AddOfferActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText mTitleField;
    private EditText mAuthorField;
    private EditText mDescriptionField;
    private EditText mPriceField;
    private Spinner mTagsSpinner;
    private ImageView mCoverField;

    private boolean photoTaken = false;
    private LocationManager locationManager;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        mTitleField = (EditText) findViewById(R.id.title_field);
        mAuthorField = (EditText) findViewById(R.id.author_field);
        mDescriptionField = (EditText) findViewById(R.id.description_field);
        mPriceField = (EditText) findViewById(R.id.price_field);
        ((Button)findViewById(R.id.create_offer_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToMakeOffer();
            }
        });
        mTagsSpinner = (Spinner) findViewById(R.id.tags_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTagsSpinner.setAdapter(adapter);
        mCoverField = (ImageView) findViewById(R.id.cover_field);
        mCoverField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 50, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocation = location;
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) { }
            @Override
            public void onProviderDisabled(String s) {}
        }, null);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mCoverField.setImageBitmap(imageBitmap);
            mCoverField.setAlpha(1.0f);
            photoTaken = true;
        }
    }

    private void tryToMakeOffer() {
        final String title = mTitleField.getText().toString();
        final String author = mAuthorField.getText().toString();
        final String description = mDescriptionField.getText().toString();
        String priceString = mPriceField.getText().toString();
        final int price;

        if(title.length() < 6) {
            mTitleField.setError("Title too short");
            mTitleField.requestFocus();
            return;
        }

        if(author.length() < 6) {
            mAuthorField.setError("Author too short");
            mAuthorField.requestFocus();
            return;
        }

        try{
            price = Integer.parseInt(priceString);
        }
        catch(NumberFormatException e) {
            mPriceField.setError("Invalid number!");
            mPriceField.requestFocus();
            return;
        }

        if(price <= 0) {
            mPriceField.setError("Price too low!");
            mPriceField.requestFocus();
            return;
        }

        String tags = mTagsSpinner.getSelectedItem().toString();
        if(tags.contains("Undefined")) tags = null;

        String photobase64 = null;
        if(photoTaken) {
            ImageEncoder encoder = new ImageEncoder(300);
            photobase64 = encoder.EncodeToBas64String(((BitmapDrawable) mCoverField.getDrawable()).getBitmap());

            if(photobase64 == null) {
                Toast.makeText(getApplicationContext(), "Unable to decode image", Toast.LENGTH_SHORT).show();
                photoTaken = false;
                mCoverField.setImageResource(R.drawable.book);
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Processing...", Toast.LENGTH_SHORT).show();
        CreateOfferRequest request;
        if(lastKnownLocation != null && lastKnownLocation.getAccuracy() < 150) {
            Float latitude = (float)lastKnownLocation.getLatitude();
            Float longitude = (float)lastKnownLocation.getLongitude();
            request = new CreateOfferRequest(AppData.token, title, price, author,
                    description, photobase64, tags, 1, latitude, longitude);
        } else {
            request = new CreateOfferRequest(AppData.token, title, price, author,
                    description, photobase64, tags, 1, null, null);
        }

        spiceManager.execute(request, new CreateOfferRequestListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_offer, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


    private class CreateOfferRequestListener extends BookshopRequestListener<OfferDetail> {
        @Override
        public void onRequestCompleted(OfferDetail offerDetail) {
            Toast.makeText(getApplicationContext(), "Offfer created!", Toast.LENGTH_LONG).show();
            Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
            in.putExtra("user_id", AppData.user.id);
            startActivity(in);
            finish();
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Error: " + error.error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}
