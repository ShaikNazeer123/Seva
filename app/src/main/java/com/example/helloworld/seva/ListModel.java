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
    private String image;
    private String itemPostDate;
    private String postId;
    private String categoryType;

    private String uId;

    private Boolean isLiked = false;
    private Boolean isCompleted = false;

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

    public String getImage(){
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemPostDate(){
        return itemPostDate;
    }

    public void setItemPostDate(String itemPostDate) {
        this.itemPostDate = itemPostDate;
    }

    public String getPostId(){
        return postId;
    }

    public void setPostId (String postId) {
        this.postId = postId;
    }

    public String getuId(){
        return uId;
    }

    public void setuId (String uId) {
        this.uId = uId;
    }

    public String getCategoryType(){
        return categoryType;
    }

    public void setCategoryType (String categoryType) {
        this.categoryType = categoryType;
    }

    public Boolean getLikeStatus(){
        return isLiked;
    }

    public void setIsLiked(){
        isLiked = true;
    }

    public void resetisLiked(){
        isLiked = false;
    }


    public Boolean geCompleteStatus(){
        return isCompleted;
    }

    public void setIsCompleted(){
        isCompleted = true;
    }

    public void resetisCompleted(){
        isCompleted = false;
    }
}
