package com.example.doctorhowproject.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Application.MyApplication;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.Utils.GenericConstants;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;


public class NewListingFragment extends Fragment {
    private EditText mTitle;
    private EditText mPhone;
    private EditText mDetails;
    private Listing mNewListing;
    private File mDestinationFolder;
    private ArrayList<Bitmap> mImages;
    private HomePageActivity mActivity;
    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        mRealm = Realm.getDefaultInstance();
        mRealm.refresh();
        return inflater.inflate(R.layout.fragment_new_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = mActivity.findViewById(R.id.new_listing_title);
        mPhone = mActivity.findViewById(R.id.new_listing_phone);
        mDetails = mActivity.findViewById(R.id.new_listing_details);
        mImages = new ArrayList<>();

        mNewListing = new Listing();
        setListingId();

        mDestinationFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DoctorHowImagesFolder");

        Button imagePickBtn = mActivity.findViewById(R.id.new_listing_images_btn);
        FloatingActionButton finishBtn = mActivity.findViewById(R.id.floating_button_finish);

        imagePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    mNewListing.setOwner(mActivity.getUser());
                    mNewListing.setTitle(mTitle.getText().toString().trim());
                    mNewListing.setPhone(mPhone.getText().toString().trim());
                    mNewListing.setDetails(mDetails.getText().toString().trim());
                    mNewListing.setOwner(mActivity.getUser());

                    for (Bitmap i : mImages) {
                        saveImage(i);
                    }

                    addListing(mNewListing);
                    goToHome();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);
                        mImages.add(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private boolean validateFields() {
        String title = mTitle.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String details = mDetails.getText().toString().trim();

        if (title.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
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

        if (details.length() == 0) {
            Toast.makeText(this.getContext(),
                    GenericConstants.NULL_FIELDS,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void pickFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    private void setListingId() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int nextId;
                // Find max id in listing table
                Number currentIdNum = realm.where(Listing.class)
                        .max("id");
                // If there is none, the id is 1, else its max id +1
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                mNewListing.setId(nextId);
            }
        });
    }

    private void saveImage(Bitmap image) {
        // Check if there is write permission and create destination folder
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (!mDestinationFolder.exists()) {
                if (!mDestinationFolder.mkdirs()) {
                    Toast.makeText(getContext(),
                            "Failed to mkdir " + mDestinationFolder.getPath(),
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }
        } else {
            requestStoragePermission();
        }

        // Create a media file in which will copy the selected image by user
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mDestinationFolder.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        // Copy image to media file
        try {
            FileOutputStream out = new FileOutputStream(mediaFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(),
                    mDestinationFolder.getPath() + "/" + mediaFile.getName(),
                    Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    e.toString(),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Add a new image path in the new listing made by user
        String imgPath = mDestinationFolder.getPath() + "/" + mediaFile.getName();
        mNewListing.getImagesPaths().add(imgPath);
    }

    private void addListing(final Listing listing) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(listing);
            }
        });
    }

    private void goToHome(){
        HomeDefaultFragment fragment=new HomeDefaultFragment();
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    private void requestStoragePermission() {
        // Code for permission check and show dialog in case there is none
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("Permission needed")
                    .setMessage("We need this permission so we can save this image in the server side")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    GenericConstants.WRITE_STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }
}