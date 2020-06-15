package com.example.doctorhowproject.Models;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Listing extends RealmObject {
    @PrimaryKey
    private Integer id;

    private User owner;

    private String details;

    private String phone;

    private String title;

    private RealmList<String> imagesPaths;

    public RealmList<String> getImagesPaths() {
        return imagesPaths;
    }

    public void setImagesPaths(RealmList<String> imagesPaths) {
        this.imagesPaths = imagesPaths;
    }

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

    public Listing(){
    }

    public Listing(String title,RealmList<String> imagesPaths){
        this.imagesPaths=imagesPaths;
        this.title=title;

    }

    public Listing(String title,RealmList<String> imagesPaths,String phone,User owner,String details){
        this.imagesPaths=imagesPaths;
        this.title=title;
        this.phone=phone;
        this.owner=owner;
        this.details=details;
    }

}
