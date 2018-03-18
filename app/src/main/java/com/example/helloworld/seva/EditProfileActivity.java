package com.example.helloworld.seva;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {


    private EditText mEditName;
    private EditText mEditContact;
    private EditText mEditOrganization;
    private EditText mEditAddress;
    private Spinner mEditGender;
    private static final String[]paths = {"Male", "Female"};

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
    ImageView dobbtn;
    DatePicker datePicker;
    TextView dobField;
    Calendar myCalendar;
    String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorage = FirebaseStorage.getInstance().getReference();

        uId = mAuth.getCurrentUser().getUid();

        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditContact = (EditText) findViewById(R.id.edit_contact);
        mEditOrganization = (EditText) findViewById(R.id.edit_organization);
        mEditAddress = (EditText) findViewById(R.id.edit_address);
        mEditGender = (Spinner) findViewById(R.id.edit_gender);

        imageView = (ImageButton) findViewById(R.id.profileImage);
        dobbtn = (ImageView) findViewById(R.id.pickdob);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
        dobField = (TextView)findViewById(R.id.dob);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEditGender.setAdapter(adapter);
        mEditGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        gender="Male";
                        break;
                    case 1:
                        gender="Female";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    gender="Male";
            }
        });

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(uId)) {
                    Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                    final Map<String, String> userMap;
                    userMap = currMap.get(uId);

                    mEditName.setText(userMap.get("name"));
                    mEditContact.setText(userMap.get("contact"));
                    mEditOrganization.setText(userMap.get("organization"));
                    mEditAddress.setText(userMap.get("address"));

                    dobField.setText(userMap.get("dob"));
                    if(userMap.get("imageUri") != null) mImageUri = Uri.parse(userMap.get("imageUri"));

                    Picasso.with(EditProfileActivity.this).load(userMap.get("image")).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(EditProfileActivity.this).load(userMap.get("image")).into(imageView);
                        }

                        @Override
                        public void onError() {
                            Picasso.with(EditProfileActivity.this).load(userMap.get("image")).into(imageView);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(EditProfileActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dobField.setText(sdf.format(myCalendar.getTime()));
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

        final String name = mEditName.getText().toString().trim();
        final String contact = mEditContact.getText().toString().trim();
        final String organization = mEditOrganization.getText().toString().trim();
        final String address = mEditAddress.getText().toString().trim();
        final String dob = dobField.getText().toString().trim();
        //TODO pick date from date picker

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(organization) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(gender) && (contact.length()==10) && mImageUri != null){

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
                    newPost.child("dob").setValue(dob);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("imageUri").setValue(mImageUri.toString());
                    //newPost.child("uid").setValue(FirebaseAuth.getCurrUser().getUid());
                    mProgress.dismiss();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                    Log.e("back","frag");
                }
            });

        }
        else{
            //Toast.makeText(this,"Any field should not be empty",Toast.LENGTH_SHORT).show();
            if(TextUtils.isEmpty(name)){
                mEditName.setError("Name Can't be empty");
            }
            if(TextUtils.isEmpty(contact)){
                mEditContact.setError("Contact Can't be empty");
            }
            if(contact.length()!=10){
                mEditContact.setError("Enter exactly 10 digits");
            }
            if(TextUtils.isEmpty(organization)){
                mEditOrganization.setError("Organization Can't be empty");
            }
            if(TextUtils.isEmpty(address)){
                mEditAddress.setError("Address Can't be empty");
            }

            if(TextUtils.isEmpty(dob)){
                dobField.setError("DOB Can't be empty");
            }

            mProgress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
