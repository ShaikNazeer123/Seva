package com.example.helloworld.seva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditProfileActivity extends AppCompatActivity {


    private EditText mEditName;
    private EditText mEditContact;
    private EditText mEditOrganization;
    private EditText mEditAddress;
    private EditText mEditGender;

    private Button mSaveBtn;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorage;

    private FirebaseAuth mAuth;

    private Uri mImageUri = null;

    private ProgressDialog mProgress;

    private String uId;

    private static final int GALLERY_REQUEST = 1;

    ImageButton imageView;
    EditText dobbtn;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorage = FirebaseStorage.getInstance().getReference();

        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditContact = (EditText) findViewById(R.id.edit_contact);
        mEditOrganization = (EditText) findViewById(R.id.edit_organization);
        mEditAddress = (EditText) findViewById(R.id.edit_address);
        mEditGender = (EditText) findViewById(R.id.edit_gender);

        imageView = (ImageButton) findViewById(R.id.profileImage);
        dobbtn = (EditText) findViewById(R.id.pickdob);
        datePicker = (DatePicker) findViewById(R.id.datepicker);


        mSaveBtn = (Button)findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        dobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.setVisibility(View.VISIBLE);
                datePicker.bringToFront();
            }
        });
    }

    @Override
    protected void onActivityResult(int RequestCode,int resultCode,Intent data){
        super.onActivityResult(RequestCode,resultCode,data);

        if(RequestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }


        if (RequestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageUri = resultUri;
                imageView.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void saveProfile(){

        mProgress.setMessage("Saving Profile...");
        mProgress.show();

        uId = mAuth.getCurrentUser().getUid();

        final String name = mEditName.getText().toString().trim();
        final String contact = mEditContact.getText().toString().trim();
        final String organization = mEditOrganization.getText().toString().trim();
        final String address = mEditAddress.getText().toString().trim();
        final String gender = mEditGender.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(organization) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(gender) && mImageUri!=null){

            StorageReference filepath = mStorage.child("Profile_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabaseUsers.child(uId);
                    newPost.child("name").setValue(name);
                    newPost.child("contact").setValue(contact);
                    newPost.child("organization").setValue(organization);
                    newPost.child("address").setValue(address);
                    newPost.child("gender").setValue(gender);
                    newPost.child("image").setValue(downloadUri.toString());
                    //newPost.child("uid").setValue(FirebaseAuth.getCurrUser().getUid());

                    mProgress.dismiss();

                    Intent mainIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });

        }
    }
}
