package com.example.doctorhowproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Utils.GenericConstants;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginFragment extends Fragment {
    private EditText mPasswordTxt;
    private EditText mEmailTxt;
    private Realm mRealm;
    private FragmentActivity mActivity;
    private CheckBox mShowPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mRealm = Realm.getDefaultInstance();
        mRealm.refresh();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPasswordTxt = (EditText) mActivity.findViewById(R.id.password_txt);
        mShowPassword= mActivity.findViewById(R.id.show_ckb);
        mEmailTxt = (EditText) mActivity.findViewById(R.id.email_txt);

        Button loginBtn = mActivity.findViewById(R.id.login_btn);
        Button registerBtn = mActivity.findViewById(R.id.register_btn);

        //CODE FOR SHOWPASSWORD CHECKBOX
        /*mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPasswordTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    mPasswordTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });*/


        // Check fields, if they are ok and user is found in the database, go to home page
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                User user=validateFields();
                if(user!=null){
                    // Replace the user with only email and password with the one with full info
                    // from database
                    user=findUserByEmailPassword(user);
                    if(user!=null) {
                        goToHomePage(user);
                        return;
                    }
                    Toast.makeText(getContext(),
                            GenericConstants.USER_NOT_EXIST,
                            Toast.LENGTH_LONG).show();
                }
            }
        });


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
        mRealm.close();
    }

    private User validateFields() {
        String email = mEmailTxt.getText().toString().trim();
        String password = mPasswordTxt.getText().toString().trim();

        if (email.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        if (password.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this.getContext(),
                    GenericConstants.INCORRECT_EMAIL,
                    Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        return new User(email, password);
    }

    private User findUserByEmailPassword(final User user) {
        RealmResults<User> query = mRealm.where(User.class)
                .equalTo("email", user.getEmail())
                .equalTo("password", user.getPassword())
                .findAll();

        if (query.isLoaded()) {
            if(query.size() == 0){
                return null;
            }
            return query.get(0);
        }

        return null;
    }

    private void goToHomePage(User user) {
        // Pass only user id to the next activity


        Intent intent = new Intent(mActivity, HomePageActivity.class);
        intent.putExtra("userId", user.getId());
        mActivity.startActivity(intent);
    }

    private void goToRegister(){
        RegisterFragment fragment = new RegisterFragment();
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment,"register_fragment")
                .commit();
    }
}
