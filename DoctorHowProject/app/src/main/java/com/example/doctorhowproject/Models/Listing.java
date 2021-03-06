package com.example.doctorhowproject.Models;

import java.util.Objects;

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
    private String address;
    private RealmList<String> imagesPaths;

    public Listing() {
        imagesPaths = new RealmList<>();
    }

    public Listing(String title, RealmList<String> imagesPaths) {
        this.imagesPaths = imagesPaths;
        this.title = title;
    }

    public Listing(String title, RealmList<String> imagesPaths, String phone, User owner, String details) {
        this.imagesPaths = imagesPaths;
        this.title = title;
        this.phone = phone;
        this.owner = owner;
        this.details = details;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<String> getImagesPaths() {
        return imagesPaths;
    }

    public void setImagesPaths(RealmList<String> imagesPaths) {
        this.imagesPaths = imagesPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return Objects.equals(id, listing.id) &&
                Objects.equals(owner, listing.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }
}
