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

        return convertView;
    }
}