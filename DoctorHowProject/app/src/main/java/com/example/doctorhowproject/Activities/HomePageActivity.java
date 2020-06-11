package com.example.doctorhowproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.doctorhowproject.Fragments.FavoritesFragment;
import com.example.doctorhowproject.Fragments.ListingsFragment;
import com.example.doctorhowproject.Fragments.UserProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //GET THE USER I PASSED IN INTENT, SET TEXT VIEWS TEXT TO USER INFO
        user= (User) getIntent().getSerializableExtra("user");
        TextView nav_email= (TextView) findViewById(R.id.nav_txt_email);
        TextView nav_name= (TextView) findViewById(R.id.nav_txt_name);
        nav_email.setText(user.getEmail());
        nav_email.setText(user.getName());

        //OPEN LISTINGS FRAGMENT AS DEFAULT FRAGMENT ON HOME PAGE
        openListingsFragment();

        //CHANGE THE TOOLBAR WITH A CUSTOM ONE
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SET THE NAVIGATION MENU IN THE HOME ACTIVITY LAYOUT
        drawer = findViewById(R.id.activity_home_page);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        //IDK I SAW ON TUTORIAL, SOMETHING ABOUT BLIND PEOPLE
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void openListingsFragment() {
        ListingsFragment fragment = new ListingsFragment();
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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}