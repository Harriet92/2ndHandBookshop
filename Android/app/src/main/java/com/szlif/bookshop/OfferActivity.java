package com.szlif.bookshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.models.Error;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.GetOfferRequest;
import com.szlif.bookshop.network.GetUserRequest;




public class OfferActivity extends BaseActivity {

    private TextView bookTitleView;
    private TextView bookAuthorView;
    private TextView priceView;
    private TextView descriptionView;
    private ImageView photoView;
    private TextView tagsView;
    private TextView ownerField;
    private TextView additionalInfoView;

    private int offerId;
    private int ownerId;
    private ProgressBar progressBar;
    private View offerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        progressBar = (ProgressBar) findViewById(R.id.load_progress);
        offerView = findViewById(R.id.offer_view);
        additionalInfoView = (TextView) findViewById(R.id.offer_additional_info);
        ownerField = (TextView) findViewById(R.id.book_owner);
        bookTitleView = (TextView)findViewById(R.id.book_name);
        bookAuthorView = (TextView)findViewById(R.id.book_author);
        priceView = (TextView)findViewById(R.id.offer_price);
        descriptionView = (TextView)findViewById(R.id.book_description);
        photoView = (ImageView)findViewById(R.id.book_cover);
        tagsView = (TextView) findViewById(R.id.book_tags);

        Intent in = getIntent();
        refresh(in.getIntExtra("offer_id", -1));
    }

    private void refresh(int offerId) {

        setLoading(true);

        this.offerId = offerId;
        getOffer(offerId);
    }

    private void setLoading(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
        offerView.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    private void getOffer(int offerId) {
        GetOfferRequest request = new GetOfferRequest(AppData.token, offerId);
        spiceManager.execute(request, new GetOfferListener());
    }

    private void getOwner(int ownerId){
        GetUserRequest request = new GetUserRequest(AppData.token, ownerId);
        spiceManager.execute(request, new GetOwnerListener());
    }


    private void setAdditionalOptions(int ownerId, int purchaserId, int status) {
        setButton(R.id.offer_button_1, null, null);
        setButton(R.id.offer_button_2, null, null);

        if(status == OfferStatus.Cancelled.getValue()) {
            additionalInfoView.setText("This offer has been canceled");
            return;
        }
        if(status == OfferStatus.Finalized.getValue()) {
            additionalInfoView.setText("Transaction finalized");
        }

        if (ownerId == AppData.user.id) {
            if(status == OfferStatus.Added.getValue()){
                additionalInfoView.setText("Nobody interested yet");
                setButton(R.id.offer_button_1, "Cancel offer", new SetOfferStatus(OfferStatus.Cancelled.getValue()));
            }
            if(status == OfferStatus.PurchaseRequested.getValue()) {
                additionalInfoView.setText("Purchase requested by: ");
                setButton(R.id.offer_button_1, "Accept offer", new SetOfferStatus(OfferStatus.PurchaseAccepted.getValue()));
                setButton(R.id.offer_button_2, "Cancel offer", new SetOfferStatus(OfferStatus.Added.getValue()));
            }
            if(status == OfferStatus.PurchaseAccepted.getValue()) {
                additionalInfoView.setText("Purchase accepted for: ");
                setButton(R.id.offer_button_1, "Start NFC", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO - start NFC activity
                    }
                });
                setButton(R.id.offer_button_2, "Cancel offer", new SetOfferStatus(OfferStatus.Added.getValue()));
            }
        } else {
            if(status == OfferStatus.Added.getValue()){
                additionalInfoView.setText("Do you want this book?");
                setButton(R.id.offer_button_1, "Buy offer", new SetOfferStatus(OfferStatus.PurchaseRequested.getValue()));
            }
            if (purchaserId == AppData.user.id) {
                if(status == OfferStatus.PurchaseRequested.getValue()) {
                    additionalInfoView.setText("You are waiting for acceptance.");
                    setButton(R.id.offer_button_2, "Cancel request", new SetOfferStatus(OfferStatus.Added.getValue()));
                }
                if(status == OfferStatus.PurchaseAccepted.getValue()) {
                    additionalInfoView.setText("You offer has been accepted");
                    setButton(R.id.offer_button_1, "Start NFC", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // TODO - start NFC activity
                        }
                    });
                    setButton(R.id.offer_button_2, "Cancel offer", new SetOfferStatus(OfferStatus.Added.getValue()));
                }
            } else {
                additionalInfoView.setText("This offer is reserved");
            }
        }
    }

    private void setButton(int id, String text, View.OnClickListener callback) {
        Button button = (Button) findViewById(id);
        if(text != null) {
            button.setVisibility(View.VISIBLE);
            button.setText(text);
            button.setOnClickListener(callback);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private class SetOfferStatus implements View.OnClickListener {

        private int status;

        public SetOfferStatus(int status) {
            this.status = status;
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class GetOfferListener extends BookshopRequestListener<OfferDetail> {

        @Override
        public void onRequestCompleted(OfferDetail offerDetail) {
            bookTitleView.setText(offerDetail.booktitle);
            bookAuthorView.setText(offerDetail.bookauthor);
            descriptionView.setText(offerDetail.description);
            if(offerDetail.tags != null && !offerDetail.tags.isEmpty()) {
                tagsView.setText(offerDetail.tags.replace(";", ", "));
                tagsView.setVisibility(View.VISIBLE);
            } else {
                tagsView.setVisibility(View.GONE);
            }
            ImageEncoder encoder = new ImageEncoder(300);
            Bitmap image = encoder.DecodeFromBase64String(offerDetail.photobase64);
            if(image != null) {
                photoView.setImageBitmap(image);
            } else {
                photoView.setImageResource(R.drawable.book);
            }

            setAdditionalOptions(offerDetail.ownerid, offerDetail.purchaserid, offerDetail.status);
            ownerId = offerDetail.ownerid;
            getOwner(offerDetail.ownerid);
        }

        @Override
        public void onRequestError(Error error) {
            Toast.makeText(getApplicationContext(), "Unable to get offer", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



    private class GetOwnerListener extends BookshopRequestListener<User> {

        @Override
        public void onRequestCompleted(User user) {

            ownerField.setText(user.name + " (show)");
            ownerField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OfferActivity.this, ProfileActivity.class);
                    intent.putExtra("user_id", ownerId);
                    startActivity(intent);
                }
            });

            setLoading(false);
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Unable to get owner", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
