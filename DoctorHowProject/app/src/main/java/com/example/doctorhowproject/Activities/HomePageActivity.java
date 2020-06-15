package com.example.doctorhowproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.doctorhowproject.Fragments.HomeDefaultFragment;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.doctorhowproject.Fragments.FavoritesFragment;
import com.example.doctorhowproject.Fragments.UserProfileFragment;
import com.google.android.material.navigation.NavigationView;



import io.realm.Realm;
import io.realm.RealmResults;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public  User user;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        realm = Realm.getDefaultInstance();


        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(permissions, 1);


        //GET THE USER ID I PASSED IN THE INTENT
        int id = (int) getIntent().getSerializableExtra("userId");
        user = findUserById(id);

        //OPEN LISTINGS FRAGMENT AS DEFAULT FRAGMENT ON HOME PAGE
        openListingsFragment();

        //CHANGE THE TOOLBAR WITH A CUSTOM ONE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //GET THE NAV VIEW AND SET ITS HEADER TEXT VIEWS
        drawer = findViewById(R.id.activity_home_page);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        TextView nav_email = headerView.findViewById(R.id.nav_txt_email);
        TextView nav_name = headerView.findViewById(R.id.nav_txt_name);
        nav_email.setText(user.getEmail());
        nav_name.setText(user.getName());

        //IDK I SAW ON TUTORIAL, SOMETHING ABOUT BLIND PEOPLE
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void openListingsFragment() {
        HomeDefaultFragment fragment = new HomeDefaultFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, "listings_fragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        //When back pressed, I want to close the navigation menu first
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Navigation menu menu items clicks
        switch (menuItem.getItemId()) {
            case R.id.nav_profile: {
                UserProfileFragment fragment = new UserProfileFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "user_profile_fragment")
                        .commit();
                break;
            }

            case R.id.nav_favorites: {
                FavoritesFragment fragment = new FavoritesFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "favorites_fragment")
                        .commit();
                break;
            }

            case R.id.nav_home: {
                HomeDefaultFragment fragment = new HomeDefaultFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "home_fragment")
                        .commit();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private User findUserById(int id) {
        RealmResults<User> query = realm.where(User.class)
                .equalTo("id", id)
                .findAll();

        return query.get(0);
    }
}