package com.szlif.bookshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.GetOffersRequest;

import java.util.ArrayList;


public class SearchActivity extends BaseActivity {

    private PagingListView mListView;
    private EditText mSearchView;
    private CheckBox mCloseView;
    private Spinner mTagView;

    private OffersArrayAdapter adapter;

    private int pager = 0;
    private int offersPerPage = 15;

    private Integer searchOnlyClose;
    private String lastSearchQuery;
    private String lastSearchTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_search);

        mSearchView = getAndSetupSearchView(R.id.search_query_view);
        mListView = getAndSetupListView(R.id.search_list_view);
        mTagView = getAndSetupSpinnerView(R.id.tags_spinner);
        mCloseView = (CheckBox) findViewById(R.id.close_checkbox_field);
    }

    private Spinner getAndSetupSpinnerView(int id) {
        Spinner spinner = (Spinner) findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return spinner;
    }

    private EditText getAndSetupSearchView(int id){
        final EditText view = (EditText) findViewById(id);
        Button submitButton = (Button) findViewById(R.id.submit_search);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchOnlyClose = mCloseView.isChecked() ? 1 : null;
                lastSearchQuery = mSearchView.getText().toString();
                lastSearchTags = mTagView.getSelectedItem().toString();
                if (lastSearchTags.contains("Undefined")) lastSearchTags = null;
                if (lastSearchQuery.isEmpty()) lastSearchQuery = null;

                clearData();
            }
        });

        return view;
    }

    private PagingListView getAndSetupListView(int id) {
        PagingListView view = (PagingListView) findViewById(id);
        adapter = new OffersArrayAdapter();
        view.setAdapter(adapter);
        view.setHasMoreItems(false);
        view.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                performSearch();
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getApplicationContext(), OfferActivity.class);
                OfferDetail selectedItem = (OfferDetail)adapterView.getItemAtPosition(i);
                in.putExtra("name", selectedItem.booktitle);
                in.putExtra("author", selectedItem.bookauthor);
                in.putExtra("price", selectedItem.price);
                in.putExtra("id", selectedItem.id);
                in.putExtra("owner_id", selectedItem.ownerid);
                in.putExtra("description", selectedItem.description);
                in.putExtra("photo", selectedItem.photobase64);
                in.putExtra("status", selectedItem.status);
                in.putExtra("tags", selectedItem.tags);
                startActivity(in);
            }
        });
        return view;
    }

    private void performSearch() {
        GetOffersRequest request = new GetOffersRequest(AppData.token, offersPerPage, pager,
                lastSearchQuery, lastSearchQuery, searchOnlyClose, null, null, lastSearchTags, 1);
        spiceManager.execute(request, new GetOffersRequestListener());
        pager++;
    }


    private void clearData() {
        pager = 0;
        adapter.removeAllItems();
        mListView.setHasMoreItems(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }

    private class GetOffersRequestListener extends BookshopRequestListener<OfferDetail.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT);
            mListView.onFinishLoading(false, null);

        }

        @Override
        public void onRequestCompleted(OfferDetail.List offerDetails) {
            mListView.onFinishLoading(offerDetails.array.size() == offersPerPage, offerDetails.array);
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            Toast.makeText(getApplicationContext(), "Error: " + error.error, Toast.LENGTH_SHORT);
            mListView.onFinishLoading(false, null);
        }
    }

    private class OffersArrayAdapter extends PagingBaseAdapter<OfferDetail> {

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

            if(offer.photobase64 != null && !offer.photobase64.isEmpty()) {
                byte[] decodedString = Base64.decode(offer.photobase64, Base64.DEFAULT);
                coverView.setImageBitmap(getResizedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), 100));
            } else {
                coverView.setImageResource(R.drawable.book);
            }

            return convertView;
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
    }
}
