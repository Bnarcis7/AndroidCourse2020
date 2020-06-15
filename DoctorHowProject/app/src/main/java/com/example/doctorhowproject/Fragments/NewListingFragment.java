package com.example.doctorhowproject.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Models.ListingImage;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.Utils.GenericConstants;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;

import static com.example.doctorhowproject.Utils.Utils.imageToArray;

public class NewListingFragment extends Fragment {

    private Listing mNewListing;
    private ArrayList<ListingImage> listingImages;
    private HomePageActivity mActivity;
    private EditText mTitle;
    private EditText mPhone;
    private EditText mDetails;
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        realm=Realm.getDefaultInstance();
        listingImages=new ArrayList<>();
        return inflater.inflate(R.layout.fragment_new_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle=mActivity.findViewById(R.id.new_listing_title);
        mPhone=mActivity.findViewById(R.id.new_listing_phone);
        mDetails=mActivity.findViewById(R.id.new_listing_details);
        Button imagePickBtn = mActivity.findViewById(R.id.new_listing_images_btn);
        FloatingActionButton finishBtn=mActivity.findViewById(R.id.floating_button_finish);
        mNewListing=new Listing();
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
                if(validateFields()){
                    mNewListing.setOwner(mActivity.user);
                    mNewListing.setTitle(mTitle.getText().toString().trim());
                    mNewListing.setPhone(mPhone.getText().toString().trim());
                    mNewListing.setDetails(mDetails.getText().toString().trim());
                    addListing(mNewListing);
                }

                for(ListingImage i : listingImages){
                    mNewListing.getListingImages().add(i);
                }
            }
        });
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1: {
                        if (data != null){
                            Uri imageUri = data.getData();
                            try {
                                Bitmap image = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUri);
                                ListingImage listingImage=new ListingImage(imageToArray(image));
                                listingImages.add(listingImage);
                                Toast.makeText(getContext(),"Image loaded",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                            break;
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

        Toast.makeText(this.getContext(), GenericConstants.SUCCESS, Toast.LENGTH_LONG).show();
        return true;
    }

    private void addListing(final Listing listing){
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

    private void addListingImage(final ListingImage listingImage){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //FIND MAX ID IN LISTING IMAGE TABLE
                Number currentIdNum = realm.where(ListingImage.class).max("id");
                int nextId;

                //IF THERE IS NONE IN THE DATABASE THE ID IS 1, ELSE ITS THE NEXT NUMBER
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                //SET LISTING IMAGE ID AND INSERT IT INTO DB
                listingImage.setId(nextId);
                realm.insertOrUpdate(listingImage);
            }
        });

        Toast.makeText(this.getContext(), GenericConstants.SUCCESS, Toast.LENGTH_SHORT).show();
    }

    private void setListingId(){
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
}