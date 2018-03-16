package com.example.helloworld.seva;

import android.net.Uri;

/**
 * Created by Shaik Nazeer on 20-02-2018.
 */

public class ListModel {
    private String name;
    private String phoneNumber;
    private String title;
    private String description;
    private String location;
    private String expiryDate;

    private Uri mImageUri;

    public String getItemName(){
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public String getItemPhoneNumber() {
        return phoneNumber;
    }

    public void setItemPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getItemTitle() {
        return title;
    }

    public void setItemTitle(String title) {
        this.title = title;
    }

    public String getItemDescription() {
        return description;
    }

    public void setItemDescription(String description) {
        this.description = description;
    }

    public String getItemLocation() {
        return location;
    }

    public void setItemLocation(String location) {
        this.location = location;
    }

    public String getItemExpiryDate() {
        return expiryDate;
    }

    public void setItemExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Uri getImageUri(){
        return mImageUri;
    }

    public void setmImageUri(Uri imageUri) {
        this.mImageUri = imageUri;
    }
}
