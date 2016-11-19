package com.dangnhat.mycontact.Model;

import java.io.Serializable;

/**
 * Created by dangn on 11/19/2016.
 */

public class Contact implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private byte[] avatar;
    private int favorite;

    public Contact(int id, String firstName, String lastName, byte[] avatar, int favorite) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.favorite = favorite;
    }

    public Contact(){
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
        this.avatar = null ;
        this.favorite = 0;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
