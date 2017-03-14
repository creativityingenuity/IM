package com.gxs.im.model;


public class ContactUpdateEvent {
    public String contact;
    public boolean isAdded;

    public ContactUpdateEvent(String contact, boolean isAdded) {
        this.contact = contact;
        this.isAdded = isAdded;
    }
}
