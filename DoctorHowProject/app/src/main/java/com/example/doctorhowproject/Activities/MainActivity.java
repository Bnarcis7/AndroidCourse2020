package com.example.doctorhowproject.Activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorhowproject.Fragments.LoginFragment;
import com.example.doctorhowproject.Models.DoctorCodes;
import com.example.doctorhowproject.Models.UserType;
import com.example.doctorhowproject.R;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        insertDoctorCodesAndTypes();
        goToLogin();
    }

    private void insertDoctorCodesAndTypes() {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(DoctorCodes.class).max("id");
                int nextId;

                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }

                DoctorCodes code1 = new DoctorCodes();
                code1.setId(nextId);
                code1.setCode("343434");
                DoctorCodes code2 = new DoctorCodes();
                code2.setId(nextId + 1);
                code2.setCode("232232");
                DoctorCodes code3 = new DoctorCodes();
                code3.setId(nextId + 2);
                code3.setCode("121212");

                realm.insertOrUpdate(code1);
                realm.insertOrUpdate(code2);
                realm.insertOrUpdate(code3);

                currentIdNum = realm.where(UserType.class).max("id");

                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }

                UserType type1 = new UserType();
                type1.setId(nextId);
                type1.setType("User");
                UserType type2 = new UserType();
                type2.setId(nextId + 1);
                type2.setType("Doctor");

                realm.insertOrUpdate(type1);
                realm.insertOrUpdate(type2);
            }
        });

    }

    private void goToLogin() {
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, "login_fragment")
                .commit();

    }
}
