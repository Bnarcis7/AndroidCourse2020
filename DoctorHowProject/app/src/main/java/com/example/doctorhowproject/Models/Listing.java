package com.example.doctorhowproject.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Listing extends RealmObject {
    @PrimaryKey
    private Integer id;

    private String imgURL;

    private User owner;

    private String details;

    private String phone;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Listing(){}

    public Listing(String title){
        this.title=title;
    }
}
