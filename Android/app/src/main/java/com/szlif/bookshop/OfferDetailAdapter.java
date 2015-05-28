package com.szlif.bookshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.OfferStatus;

class OffersArrayAdapter extends PagingBaseAdapter<OfferDetail> {

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public OfferDetail getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.search_row, parent, false);
        }
        TextView titleView = (TextView) convertView.findViewById(R.id.booktitle);
        TextView authorView = (TextView) convertView.findViewById(R.id.book_author);
        TextView priceView = (TextView) convertView.findViewById(R.id.price);
        ImageView coverView = (ImageView) convertView.findViewById(R.id.book_cover);
        TextView statusView = (TextView) convertView.findViewById(R.id.status_info);

        OfferDetail offer = this.getItem(position);
        titleView.setText(offer.booktitle);
        authorView.setText(offer.bookauthor);
        priceView.setText(Integer.toString(offer.price));

        ImageEncoder encoder = new ImageEncoder(100);
        Bitmap image = encoder.DecodeFromBase64String(offer.photobase64);
        if(image != null) {
            coverView.setImageBitmap(image);
        } else {
            coverView.setImageResource(R.drawable.book);
        }

        if(offer.ownerid == AppData.user.id) {
            statusView.setVisibility(View.VISIBLE);

            if(offer.status == OfferStatus.Added.getValue()) {
                statusView.setText("You are owner");
            }
            if(offer.status == OfferStatus.PurchaseRequested.getValue()) {
                statusView.setText("Purchase requested!");
            }
            if(offer.status == OfferStatus.PurchaseAccepted.getValue()) {
                statusView.setText("You accepted purchase");
            }
            if(offer.status == OfferStatus.Cancelled.getValue()) {
                statusView.setText("This offer is cancelled");
            }
            if(offer.status == OfferStatus.Finalized.getValue()) {
                statusView.setText("This offer is finalized");
            }
        }else if(offer.purchaserid == AppData.user.id) {
            statusView.setVisibility(View.VISIBLE);

            if(offer.status == OfferStatus.PurchaseRequested.getValue()) {
                statusView.setText("You are waiting for acceptance");
            }
            if(offer.status == OfferStatus.PurchaseAccepted.getValue()) {
                statusView.setText("Your request is accepted");
            }
            if(offer.status == OfferStatus.Finalized.getValue()) {
                statusView.setText("You bought this offer");
            }
        } else {
            statusView.setVisibility(View.GONE);
        }

        return convertView;
    }
}