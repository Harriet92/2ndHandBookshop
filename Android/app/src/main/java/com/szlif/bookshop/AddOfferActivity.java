package com.szlif.bookshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
        String title = mTitleField.getText().toString();
        String author = mAuthorField.getText().toString();
        String description = mDescriptionField.getText().toString();
        String priceString = mPriceField.getText().toString();
        int price;

        if(title.length() < 3) {
            mTitleField.setError("Title too short");
            mTitleField.requestFocus();
            return;
        }

        if(author.length() < 3) {
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
        if(tags == "Undefined") tags = null;

        String photobase64 = null;
        if(photoTaken) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bmp = ((BitmapDrawable) mCoverField.getDrawable()).getBitmap();
                bmp = getResizedBitmap(bmp, 300);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                photobase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
            catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to decode image", Toast.LENGTH_SHORT).show();
            }
        }

        Toast.makeText(getApplicationContext(), "Processing...", Toast.LENGTH_SHORT).show();
        CreateOfferRequest request = new CreateOfferRequest(AppData.token, title, price, author, description, photobase64, tags, 1);
        spiceManager.execute(request, new CreateOfferRequestListener());
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
        getMenuInflater().inflate(R.menu.menu_add_offer, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


    private class CreateOfferRequestListener extends BookshopRequestListener<OfferDetail> {
        @Override
        public void onRequestCompleted(OfferDetail offerDetail) {
            Toast.makeText(getApplicationContext(), "Offfer created!", Toast.LENGTH_LONG).show();
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
