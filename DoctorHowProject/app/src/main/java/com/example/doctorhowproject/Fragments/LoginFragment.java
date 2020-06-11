package com.example.doctorhowproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Activities.MainActivity;
import com.example.doctorhowproject.Database.GenericConstants;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginFragment extends Fragment {
    private EditText mPasswordTxt;
    private EditText mEmailTxt;
    private Realm realm;
    private FragmentActivity mActivity;

    //INFLATE THE LAYOUT XML AND SAVE THE CURRENT ACTIVITY IN A VARIABLE
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        realm = Realm.getDefaultInstance();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // IN THIS METHOD PUT ON CLICK LISTENERS AND ALL OTHER STUFF
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //GET THE PASSWORD AND EMAIL TEXT VIEWS
        mPasswordTxt = (EditText) mActivity.findViewById(R.id.password_txt);
        mEmailTxt = (EditText) mActivity.findViewById(R.id.email_txt);

        //GET THE BUTTON FOR LOGIN AND REGISTER
        Button loginBtn = mActivity.findViewById(R.id.login_btn);
        Button registerBtn = mActivity.findViewById(R.id.register_btn);

        //CHECK FIELDS, IF LOGIN IS SUCCESSFULLY, GO TO HOME PAGE WITH THIS USER
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    User user = new User(mEmailTxt.getText().toString(), mPasswordTxt.getText().toString());
                    goToHomePage(user);
                }
            }
        });

        //OPEN REGISTER FRAGMENT
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();//CLOSE THE INSTANCE WHEN THIS FRAGMENT IS DESTROYED
    }

    private boolean validateFields() {
        String email = mEmailTxt.getText().toString();
        String password = mPasswordTxt.getText().toString();

        if (email.trim().length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.trim().length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this.getContext(), GenericConstants.INCORRECT_EMAIL, Toast.LENGTH_LONG).show();
            return false;
        }
        //MAKE A NEW USER OBJECT AND TRY TO FIND IT IN THE DATABASE
        User user = new User(email, password);
        if (findUser(user) == null) {
            Toast.makeText(this.getContext(), GenericConstants.USER_NOT_EXIST, Toast.LENGTH_LONG).show();
            return false;
        }
        //IF ALL CONDITIONS ARE NULL, LOGIN IS SUCCESSFULLY
        return true;

    }

    private User findUser(final User user) {
        RealmResults<User> query = realm.where(User.class)
                .equalTo("email", user.getEmail())
                .equalTo("password", user.getPassword())
                .findAllAsync();

        if (query.isLoaded()) {
            if (query.size() == 0)
                return null;
            return query.get(0);
        }

        return null;
    }

    private void goToHomePage(User user) {
        Intent intent = new Intent(mActivity, HomePageActivity.class);
        intent.putExtra("user", user);
        mActivity.startActivity(intent);
    }

    private void goToRegister(){
        RegisterFragment fragment = new RegisterFragment();
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment,"register_fragment")
                .commit();
    }
}
