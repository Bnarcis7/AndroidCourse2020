package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE first_name = :firstName AND last_name =:lastName")
    User getUser(String firstName,String lastName);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
