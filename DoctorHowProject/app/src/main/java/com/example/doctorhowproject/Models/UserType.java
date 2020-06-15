package com.example.doctorhowproject.Models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserType extends RealmObject implements Serializable {
    @PrimaryKey
    private Integer id;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserType(String type){
        this.type=type;
    }

    public UserType(){

    }
}
