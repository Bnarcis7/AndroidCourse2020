package com.example.doctorhowproject.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.doctorhowproject.Fragments.FavoritesFragment;
import com.example.doctorhowproject.Fragments.HomeDefaultFragment;
import com.example.doctorhowproject.Fragments.UserProfileFragment;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private User mUser;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);
        mRealm = Realm.getDefaultInstance();

        int id = (int) getIntent().getSerializableExtra("userId");
        mUser = findUserById(id);
        mRealm.close();
        // Open listings fragment as default fragment
        openListingsFragment();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawer = findViewById(R.id.activity_home_page);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        TextView nav_email = headerView.findViewById(R.id.nav_txt_email);
        TextView nav_name = headerView.findViewById(R.id.nav_txt_name);
        nav_email.setText(mUser.getEmail());
        nav_name.setText(mUser.getFirstName());

        // Add action bar drawer to layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        // When back pressed, close the navigation menu first
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Navigation menu menu items clicks
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

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openListingsFragment() {
        HomeDefaultFragment fragment = new HomeDefaultFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "listings_fragment")
                .commit();
    }

    private User findUserById(int id) {
        User query = mRealm.where(User.class)
                .equalTo("id", id)
                .findFirst();

        return mRealm.copyFromRealm(query);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}