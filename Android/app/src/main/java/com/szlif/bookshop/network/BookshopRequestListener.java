package com.szlif.bookshop.network;

import com.octo.android.robospice.request.listener.RequestListener;
import com.szlif.bookshop.models.Error;

public abstract class BookshopRequestListener<RESULT extends Error>
        implements RequestListener<RESULT> {

    @Override
    final public void onRequestSuccess(RESULT result) {
        if(result.error == null || result.error.isEmpty()) {
            this.onRequestCompleted(result);
        }
        else {
            this.onRequestError(result);
        }
    }

    abstract public void onRequestCompleted(RESULT result);

    abstract public void onRequestError(Error error);
}
