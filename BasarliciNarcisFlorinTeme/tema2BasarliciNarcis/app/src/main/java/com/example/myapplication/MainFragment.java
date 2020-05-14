package com.example.myapplication;


import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;


import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class MainFragment extends Fragment {

    private Button btnAddUser;
    private Button btnRemoveUser;
    private Button btnsyncServer;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    public MainFragment() {
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        firstNameEditText = view.findViewById(R.id.text_first_name);
        lastNameEditText = view.findViewById(R.id.text_last_name);
        AddUserBtn(view);
        RemoveUserBtn(view);
        SyncServer(view);

        return view;
    }


    private void SyncServer(View view) {
        btnsyncServer = view.findViewById(R.id.btn_sync);

        btnsyncServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SyncWithServerTask().execute();
            }
        });
    }


    private void AddUserBtn(View view)
    {
        btnAddUser = view.findViewById(R.id.btn_add_user);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                if(!firstName.equals("") && !lastName.equals(""))
                {
                    User user = new User();
                    user.firstName = firstName;
                    user.lastName = lastName;

                    new InsertUserTask().execute(user);

                    Toast.makeText(getActivity(),"User added",Toast.LENGTH_SHORT).show();
                }

                firstNameEditText.setText("");
                lastNameEditText.setText("");
            }
        });
    }

    private void RemoveUserBtn(View view)
    {
        btnRemoveUser = view.findViewById(R.id.btn_remove_user);

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                if(!firstName.equals("") && !lastName.equals(""))
                {
                    User user = new User();
                    user.firstName = firstName;
                    user.lastName = lastName;

                    Boolean result = false;
                    try {
                        result = new ExistUserTask().execute(user).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(result)
                    {
                        new RemoveUserTask().execute(user);
                        Toast.makeText(getActivity(),"User Deleted",Toast.LENGTH_SHORT).show();
                    }

                    else Toast.makeText(getActivity(),"User not existent",Toast.LENGTH_SHORT).show();

                    firstNameEditText.setText("");
                    lastNameEditText.setText("");
                }
            }
        });
    }

    public class InsertUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            AppDatabase.getInstance(getContext()).userDao().insert(users[0]);
            return null;
        }
    }

    public class RemoveUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User ... users) {
            AppDatabase.getInstance(getContext()).userDao().delete(users[0]);
            return null;
        }
    }

    public class ExistUserTask extends AsyncTask<User,Void,Boolean> {
        @Override
        protected Boolean doInBackground(User ... users) {
            User user = AppDatabase.getInstance(getContext()).userDao().getUser(users[0].firstName,users[0].lastName);
            if(user!=null)
                return true;
            return false;
        }
    }

}
