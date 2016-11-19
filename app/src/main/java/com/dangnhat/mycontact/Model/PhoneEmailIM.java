package com.dangnhat.mycontact.Model;

import java.io.Serializable;

/**
 * Created by dangn on 11/19/2016.
 */

public class PhoneEmailIM implements Serializable {
    private int id;
    private int contactID;
    private int name;
    private String value;


    public PhoneEmailIM(int id, int contactID, int name, String value) {
        this.id = id;
        this.contactID = contactID;
        this.name = name;
        this.value = value;
    }

    public PhoneEmailIM(){
        this.id = 0;
        this.contactID = 0;
        this.name = 0;
        this.value = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
