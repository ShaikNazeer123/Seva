package com.example.helloworld.seva;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabasePost;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseFood;
    private DatabaseReference mDatabaseClothes;
    private DatabaseReference mDatabaseBooks;
    private DatabaseReference mDatabaseMisc;

    ImageButton addImage;
    EditText addTitle;
    EditText addDescription;
    EditText addAddress;
    EditText addDate;
    ImageView datePicker;
    RadioGroup radioGroup;
    Button updateBtn;
    Calendar myCalendar;

    private String title;
    private String description;
    private String address;
    private String date;
    private String imageUrl;

    private Uri mImageUri = null;

    private ProgressDialog mProgress;

    private String uId;
    private String postId;
    private String categoryType;

    private static final int GALLERY_REQUEST = 1;

    int btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        categoryType = intent.getStringExtra("categoryType");

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//        mDatabaseFood = FirebaseDatabase.getInstance().getReference().child("Food");
//        mDatabaseClothes = FirebaseDatabase.getInstance().getReference().child("Clothes");
//        mDatabaseBooks = FirebaseDatabase.getInstance().getReference().child("Books");
//        mDatabaseMisc = FirebaseDatabase.getInstance().getReference().child("Misc");

        mStorage = FirebaseStorage.getInstance().getReference();

        addImage = (ImageButton)findViewById(R.id.addimage);
        addTitle = (EditText) findViewById(R.id.addTitle);
        addDescription = (EditText) findViewById(R.id.addDescription);
        addAddress = (EditText) findViewById(R.id.addAddress);
        addDate = (EditText) findViewById(R.id.addDate);
        datePicker = (ImageView) findViewById(R.id.pickexpdate);
        updateBtn = (Button) findViewById(R.id.addUpdateBtn);

        mDatabasePost = mDatabase.child(categoryType).child(postId);

        mDatabasePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> postMap = (Map<String,String>) dataSnapshot.getValue();

                title = postMap.get("title");
                description = postMap.get("description");
                address = postMap.get("address");
                date = postMap.get("date");
                imageUrl = postMap.get("image");
                mImageUri = Uri.parse(postMap.get("imageUri"));

                addTitle.setText(title);
                addDescription.setText(description);
                addAddress.setText(address);
                addDate.setText(date);

                Picasso.with(EditActivity.this).load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(addImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(EditActivity.this).load(imageUrl).into(addImage);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(EditActivity.this).load(imageUrl).into(addImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdating();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener(){

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

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatePickerDialog dpd = new DatePickerDialog(EditActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                    dpd.show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        addDate.setText(sdf.format(myCalendar.getTime()));
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
                addImage.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void startUpdating(){
        mProgress.setMessage("Updating Post...");
        mProgress.show();

        final String newTitle = addTitle.getText().toString().trim();
        final String newDescription = addDescription.getText().toString().trim();
        final String newAddress = addAddress.getText().toString().trim();
        final String newDate = addDate.getText().toString().trim();

        //TODO pick date from date picker

        if(!TextUtils.isEmpty(newTitle) && !TextUtils.isEmpty(newDescription) && !TextUtils.isEmpty(newAddress) && !TextUtils.isEmpty(newDate) && mImageUri != null){

            StorageReference filepath = mStorage.child("Post_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    String formattedDate = df.format(c);

                    mDatabasePost.child("title").setValue(newTitle);
                    mDatabasePost.child("description").setValue(newDescription);
                    mDatabasePost.child("address").setValue(newAddress);
                    mDatabasePost.child("date").setValue(newDate);
                    mDatabasePost.child("image").setValue(downloadUri.toString());
                    mDatabasePost.child("imageUri").setValue(mImageUri.toString());
                    mDatabasePost.child("postdate").setValue(formattedDate);

                    //newPost.child("uid").setValue(FirebaseAuth.getCurrUser().getUid());

                    mProgress.dismiss();

                    Intent mainIntent = new Intent(EditActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });

        }
        else{
            Toast.makeText(this,"Any field should not be empty",Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }
    }

}
