package com.example.doctorhowproject.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorhowproject.Database.GenericConstants;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;


import io.realm.Realm;

public class RegisterFragment extends Fragment {
    private TextView mEmail;
    private TextView mPassword;
    private TextView mName;
    private Realm realm;
    private FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        realm = Realm.getDefaultInstance(); // GET AN INSTANCE FOR THIS THREAD ONLY!
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //GET TEXT VIEWS
        mEmail = mActivity.findViewById(R.id.register_email_txt);
        mPassword = mActivity.findViewById(R.id.register_password_txt);
        mName = mActivity.findViewById(R.id.register_name_txt);

        //GET BUTTONS
        Button registerBtn = mActivity.findViewById(R.id.register_btn);
        Button backBtn = mActivity.findViewById(R.id.register_back_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    User user = new User();
                    user.setEmail(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    user.setName(mName.getText().toString());
                    addUser(user);
                    goToLogin();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

    }


    private boolean validateFields() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String name = mName.getText().toString().trim();

        if (email.length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (name.length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this.getContext(), GenericConstants.INCORRECT_EMAIL, Toast.LENGTH_LONG).show();
            return false;
        }

        Toast.makeText(this.getContext(), GenericConstants.SUCCESS, Toast.LENGTH_LONG).show();
        return true;
    }

    private void addUser(final User user) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //FIND MAX ID IN USER TABLE
                Number currentIdNum = realm.where(User.class).max("id");
                int nextId;

                //IF THERE IS NONE IN THE DATABASE THE ID IS 1, ELSE ITS THE NEXT NUMBER
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                //SET USERS ID AND INSERT IT INTO DB
                user.setId(nextId);
                realm.insertOrUpdate(user);
            }
        });

        Toast.makeText(this.getContext(), GenericConstants.USER_ADDED, Toast.LENGTH_LONG).show();
    }


    private void goToLogin() {
        LoginFragment fragment = new LoginFragment();
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, "login_fragment")
                .commit();
    }
}