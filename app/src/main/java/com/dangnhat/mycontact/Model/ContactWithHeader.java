package com.dangnhat.mycontact.Model;

/**
 * Created by dangn on 11/19/2016.
 */

public class ContactWithHeader {
    public int sectionFirstPosition;
    public Contact contact;

    public ContactWithHeader(int sectionFirstPosition, Contact contact) {
        this.sectionFirstPosition = sectionFirstPosition;
        this.contact = contact;
    }

    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

    public void setSectionFirstPosition(int sectionFirstPosition) {
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
