package com.example.doctorhowproject.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DoctorCodes extends RealmObject {
    @PrimaryKey
    Integer id;

    String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
