package com.szlif.bookshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.BookshopService;
import com.szlif.bookshop.network.GetOfferRequest;
import com.szlif.bookshop.network.GetOffersRequest;
import com.szlif.bookshop.network.LoginRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class SearchActivity extends BaseActivity {

    private PagingListView mListView;
    private SearchView mSearchView;

    private OffersArrayAdapter adapter;

    private ArrayList<OfferDetail> offers = new ArrayList<>();
    private int pager = 0;
    private int offersPerPage = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_search);

        mSearchView = getAndSetupSearchView(R.id.search_query_view);
        mListView = getAndSetupListView(R.id.search_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }

    private SearchView getAndSetupSearchView(int id){
        final SearchView view = (SearchView) findViewById(id);
        view.setIconifiedByDefault(false);
        view.setSubmitButtonEnabled(true);
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                performSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
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
            if(pager * offersPerPage < offers.size()) {
                Toast.makeText(SearchActivity.this, Integer.toString(Math.min(offers.size(), (pager +1) * offersPerPage)), Toast.LENGTH_LONG).show();
                boolean hasMoreItems = (pager + 1) * offersPerPage < offers.size();
                mListView.onFinishLoading(hasMoreItems, offers.subList(pager * offersPerPage, Math.min(offers.size(), (pager +1) * offersPerPage)));
                mListView.scrollBy(0, 1);
                pager++;
            }else {
                mListView.onFinishLoading(false, null);
            }
            }
        });
        return view;
    }

    private void performSearch(CharSequence query) {
        mListView.setHasMoreItems(true);
        GetOffersRequest request = new GetOffersRequest(AppData.token);
        spiceManager.execute(request, new GetOffersRequestListener());
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
        }

        @Override
        public void onRequestCompleted(OfferDetail.List offerDetails) {
            clearData();
            offers = offerDetails.array;
            mListView.setHasMoreItems(true);
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
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

            if(offer.photobase64 != null) {
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
