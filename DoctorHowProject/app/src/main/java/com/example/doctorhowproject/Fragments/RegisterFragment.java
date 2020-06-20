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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doctorhowproject.Models.DoctorCodes;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.Models.UserType;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.Utils.GenericConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class RegisterFragment extends Fragment {
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mDoctorCode;
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

        //addFromHttps("https://randomname.de/?format=json&count=10&phone=a");

        mFirstName = mActivity.findViewById(R.id.register_first_txt);
        mLastName = mActivity.findViewById(R.id.register_last_txt);
        mEmail = mActivity.findViewById(R.id.register_email_txt);
        mPhone = mActivity.findViewById(R.id.register_phone);
        mPassword = mActivity.findViewById(R.id.register_password_txt);
        mDoctorCode = mActivity.findViewById(R.id.register_doctor_code);

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

                    switch (checkDoctorCode()) {
                        case 2: {
                            UserType doctorType = mRealm.where(UserType.class)
                                    .equalTo("type", "Doctor")
                                    .findFirst();
                            user.setType(doctorType);
                            addUser(user);
                            goToLogin();
                            break;
                        }


                        case 1: {
                            UserType userType = mRealm.where(UserType.class)
                                    .equalTo("type", "User")
                                    .findFirst();
                            user.setType(userType);
                            addUser(user);
                            goToLogin();
                            break;
                        }

                        case 3: {
                            Toast.makeText(getContext(),
                                    "Invalid code, please leave blank field" +
                                            " or a correct code", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
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
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (password.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (firstName.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (lastName.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (phone.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this.getContext(),
                    GenericConstants.INCORRECT_EMAIL,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this.getContext(),
                    GenericConstants.INCORRECT_PHONE,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (mRealm.where(User.class).equalTo("email", email).findFirst() == null) {
            Toast.makeText(this.getContext(),
                    GenericConstants.USER_ALREADY_EXIST, Toast.LENGTH_SHORT);
        }

        return true;
    }

    private int checkDoctorCode() {
        String code = mDoctorCode.getText().toString().trim();
        if (code.length() == 0) {
            return 1; // In case the field is empty => register as user
        }
        DoctorCodes doctorCode = mRealm.where(DoctorCodes.class)
                .equalTo("code", code)
                .findFirst();
        if (doctorCode != null) {
            return 2; // In case code is ok => register as doctor
        }
        return 3; // Else code is not ok => ask for empty field or ok code
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
        Toast.makeText(this.getContext(), GenericConstants.USER_ADDED, Toast.LENGTH_SHORT).show();
    }

    private void goToLogin() {
        LoginFragment fragment = new LoginFragment();
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "login_fragment")
                .commit();
    }

    private void addFromHttps(String url) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handleResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "That didnt work :(", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void handleResponse(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentObject = jsonArray.getJSONObject(i);
            String firstName = currentObject.getString("firstname");
            String lastName = currentObject.getString("lastname");
            String email = currentObject.getString("email");
            String password = currentObject.getString("password");
            String phone = currentObject.getString("phone");

            JSONObject phoneObject = new JSONObject(phone);
            String mobilePhone = phoneObject.getString("mobile");

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(mobilePhone);
            user.setType(new UserType("user"));

            addUser(user);
        }
    }
}
