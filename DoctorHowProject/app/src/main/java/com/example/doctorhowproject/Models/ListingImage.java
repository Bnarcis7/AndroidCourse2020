package com.example.doctorhowproject.Models;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListingImage extends RealmObject {
    @PrimaryKey
    private Integer id;

    private byte[] imgArray;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImgArray() {
        return imgArray;
    }

    public void setImgArray(byte[] imgArray) {
        this.imgArray = imgArray;
    }


    public ListingImage(){}

    public ListingImage(byte[] imgArray){
        this.imgArray=imgArray;
    }
}
