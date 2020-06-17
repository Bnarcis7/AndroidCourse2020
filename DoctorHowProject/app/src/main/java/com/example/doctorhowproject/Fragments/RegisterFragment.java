package com.example.doctorhowproject.Fragments;

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
import androidx.fragment.app.FragmentActivity;

import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.Utils.GenericConstants;

import io.realm.Realm;

public class RegisterFragment extends Fragment {
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private FragmentActivity mActivity;
    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mRealm = Realm.getDefaultInstance();
        mRealm.refresh();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //GET TEXT VIEWS
        mFirstName = mActivity.findViewById(R.id.register_first_txt);
        mLastName = mActivity.findViewById(R.id.register_last_txt);
        mEmail = mActivity.findViewById(R.id.register_email_txt);
        mPhone = mActivity.findViewById(R.id.register_phone);
        mPassword = mActivity.findViewById(R.id.register_password_txt);

        //GET BUTTONS
        Button registerBtn = mActivity.findViewById(R.id.register_btn);
        Button backBtn = mActivity.findViewById(R.id.register_back_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    User user = new User();
                    user.setEmail(mEmail.getText().toString().trim());
                    user.setPassword(mPassword.getText().toString().trim());
                    user.setFirstName(mFirstName.getText().toString().trim());
                    user.setLastName(mLastName.getText().toString().trim());
                    user.setPhone(mPhone.getText().toString().trim());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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

    private void addUser(final User user) {
        // Search for max id in the table, if there is none, set the id to 1
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(User.class).max("id");
                int nextId;

                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }

                user.setId(nextId);
                realm.insertOrUpdate(user);
            }
        });

        Toast.makeText(this.getContext(), GenericConstants.USER_ADDED, Toast.LENGTH_LONG).show();
    }

    private void goToLogin() {
        LoginFragment fragment = new LoginFragment();
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "login_fragment")
                .commit();
    }
}
