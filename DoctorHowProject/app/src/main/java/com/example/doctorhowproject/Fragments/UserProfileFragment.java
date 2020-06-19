package com.example.doctorhowproject.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Activities.MainActivity;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.Utils.GenericConstants;

import io.realm.Realm;

public class UserProfileFragment extends Fragment {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private HomePageActivity mActivity;
    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        mRealm = Realm.getDefaultInstance();
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button updateBtn = mActivity.findViewById(R.id.user_update_btn);
        Button logoutBtn = mActivity.findViewById(R.id.user_logout_btn);
        mFirstName = mActivity.findViewById(R.id.user_first_name);
        mLastName = mActivity.findViewById(R.id.user_last_name);
        mEmail = mActivity.findViewById(R.id.user_email);
        mPhone = mActivity.findViewById(R.id.user_phone);
        mPassword = mActivity.findViewById(R.id.user_password);

        mFirstName.setText(mActivity.getUser().getFirstName());
        mLastName.setText(mActivity.getUser().getLastName());
        mEmail.setText(mActivity.getUser().getEmail());
        mPhone.setText(mActivity.getUser().getPhone());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePassword()) {
                    if (validateFields()) {
                        mActivity.getUser().setEmail(mEmail.getText().toString().trim());
                        mActivity.getUser().setFirstName(mFirstName.getText().toString().trim());
                        mActivity.getUser().setLastName(mLastName.getText().toString().trim());
                        mActivity.getUser().setPhone(mPhone.getText().toString().trim());
                        updateUser(mActivity.getUser());
                        mActivity.updateUserView();
                        goToHomePage();
                    }
                } else
                    Toast.makeText(getContext(),
                            "Invalid password",
                            Toast.LENGTH_SHORT)
                            .show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setUser(null);
                goToLogin();
            }
        });
    }


    private boolean validateFields() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();

        if (email.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (password.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (firstName.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (lastName.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (phone.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this.getContext(),
                    GenericConstants.INCORRECT_EMAIL,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this.getContext(),
                    GenericConstants.INCORRECT_PHONE,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        return mPassword.getText().toString().trim().equals(mActivity.getUser().getPassword());
    }

    private void updateUser(final User user) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {

                realm.insertOrUpdate(user);
            }
        });

        Toast.makeText(this.getContext(), GenericConstants.USER_UPDATED, Toast.LENGTH_LONG).show();
    }

    private void goToHomePage() {
        HomeDefaultFragment fragment = new HomeDefaultFragment();
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "user_profile_fragment")
                .commit();
    }

    private void goToLogin() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }
}