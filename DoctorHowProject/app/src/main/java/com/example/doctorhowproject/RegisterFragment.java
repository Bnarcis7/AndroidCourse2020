package com.example.doctorhowproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    //@InjectView(R.id.sign_up_btn)
    //@InjectView(R.id.email_txt)
    //@InjectView(R.id.password_txt)
    //@InjectView(R.id.name_txt)
    Button mSignUpBtn;
    EditText mEmailTxt;
    EditText mPasswordTxt;
    EditText mNameTxt;
    private FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        clickListener();
        return view;
    }

    private void clickListener() {
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validationResponse = validateFields();
                if (validationResponse.equals(GenericConstants.SUCCESS)) {
                    boolean isCreated = createUser();
                    if (isCreated) {
                        Intent intent = new Intent(mActivity, HomePageActivity.class);
                        intent.putExtra(DbConstants.EMAIL_KEY, mEmailTxt.getText().toString());
                        startActivity(intent);
                        mActivity.finish();
                    } else {
                        Toast.makeText(mActivity, GenericConstants.OPERATION_FAILED, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, validationResponse, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean createUser() {
        User user = new User();
        user.setEmail(mEmailTxt.getText().toString());
        BaseUtility baseUtility = new BaseUtility();
        String encryptedPassword = baseUtility.encrypt(mPasswordTxt.getText().toString());
        user.setPassword(encryptedPassword);
        user.setName(mNameTxt.getText().toString());
        DbUtility dbUtility = new DbUtility(mActivity);
        return dbUtility.createUser(user);
    }

    private String validateFields() {
        Editable email = mEmailTxt.getText();
        Editable password = mPasswordTxt.getText();
        Editable name = mNameTxt.getText();
        DbUtility dbUtility = new DbUtility(mActivity);

        if (email == null || email.toString().trim().length() == 0) {
            return GenericConstants.NULL_FIELDS;
        }else if (password == null || password.toString().trim().length() == 0) {
            return GenericConstants.NULL_FIELDS;
        } else if (name == null || name.toString().trim().length() == 0) {
            return GenericConstants.NULL_FIELDS;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
            return GenericConstants.INCORRECT_EMAIL;
        } else if (dbUtility.isUserExist(email.toString())) {
            return GenericConstants.USER_ALREADY_EXIST;
        } else {
            return GenericConstants.SUCCESS;
        }

    }
}
