package com.szlif.bookshop.models;

public enum OfferStatus {
    Added(1),
    PurchaseRequested(2),
    Cancelled(3),
    Finalized(4),
    PurchaseAccepted(5);

    private int value;

    private OfferStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
