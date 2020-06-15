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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Application.MyApplication;
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

    private Listing mNewListing;
    private HomePageActivity mActivity;
    private EditText mTitle;
    private EditText mPhone;
    private EditText mDetails;
    private Realm realm;
    private ArrayList<Bitmap> mImages;
    private File mDestinationFolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        realm = Realm.getDefaultInstance();
        return inflater.inflate(R.layout.fragment_new_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = mActivity.findViewById(R.id.new_listing_title);
        mPhone = mActivity.findViewById(R.id.new_listing_phone);
        mDetails = mActivity.findViewById(R.id.new_listing_details);
        mDestinationFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DoctorHowImagesFolder");

        mImages = new ArrayList<>();

        Button imagePickBtn = mActivity.findViewById(R.id.new_listing_images_btn);
        FloatingActionButton finishBtn = mActivity.findViewById(R.id.floating_button_finish);

        mNewListing = new Listing();
        setListingId();

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
//                    mNewListing.setOwner(mActivity.user);
//                    mNewListing.setTitle(mTitle.getText().toString().trim());
//                    mNewListing.setPhone(mPhone.getText().toString().trim());
//                    mNewListing.setDetails(mDetails.getText().toString().trim());
//                    addListing(mNewListing);
                    for(Bitmap i : mImages){
                        saveImage(i);
                    }
                }
            }
        });
    }

    private void pickFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
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

    private boolean validateFields() {
        String title = mTitle.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String details = mDetails.getText().toString().trim();

        if (title.length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (phone.length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }

        if (details.length() == 0) {
            Toast.makeText(this.getContext(), GenericConstants.NULL_FIELDS, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void addListing(final Listing listing) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //FIND MAX ID IN LISTING TABLE
                Number currentIdNum = realm.where(Listing.class).max("id");
                int nextId;

                //IF THERE IS NONE IN THE DATABASE THE ID IS 1, ELSE ITS THE NEXT NUMBER
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                //SET LISTING ID AND INSERT IT INTO DB
                listing.setId(nextId);
                realm.insertOrUpdate(listing);
            }
        });

        Toast.makeText(this.getContext(), GenericConstants.USER_ADDED, Toast.LENGTH_LONG).show();
    }

    private void setListingId() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //FIND MAX ID IN LISTING TABLE
                Number currentIdNum = realm.where(Listing.class).max("id");
                int nextId;

                //IF THERE IS NONE IN THE DATABASE THE ID IS 1, ELSE ITS THE NEXT NUMBER
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }

                mNewListing.setId(nextId);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    private File saveImage(Bitmap image){

        if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            if (! mDestinationFolder.exists()){
                if (! mDestinationFolder.mkdirs()){
                    Toast.makeText(getContext(), "Failed to mkdir "+mDestinationFolder.getPath(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }
        else{
            requestStoragePermission();
        }


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;


        mediaFile = new File(mDestinationFolder.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        try {
            FileOutputStream out = new FileOutputStream(mediaFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(),"Image "+image.toString()+" saved into "+mDestinationFolder.getPath()+" as "
            +mediaFile.getName(),Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getContext(),e.toString(), Toast.LENGTH_SHORT).show();
        }


        return mediaFile;
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(mActivity)
                    .setTitle("Permission needed")
                    .setMessage("We need this permission so we can save this image in another folder for later use")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},GenericConstants.WRITE_STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
}