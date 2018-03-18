package com.example.helloworld.seva;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
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

    private Uri mImageUri = null;

    private ProgressDialog mProgress;

    private String uId;

    private static final int GALLERY_REQUEST = 1;

    int btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseFood = FirebaseDatabase.getInstance().getReference().child("Food");
        mDatabaseClothes = FirebaseDatabase.getInstance().getReference().child("Clothes");
        mDatabaseBooks = FirebaseDatabase.getInstance().getReference().child("Books");
        mDatabaseMisc = FirebaseDatabase.getInstance().getReference().child("Misc");

        mStorage = FirebaseStorage.getInstance().getReference();

        addImage = (ImageButton)findViewById(R.id.addimage);
        addTitle = (EditText) findViewById(R.id.addTitle);
        addDescription = (EditText) findViewById(R.id.addDescription);
        addAddress = (EditText) findViewById(R.id.addAddress);
        addDate = (EditText) findViewById(R.id.addDate);
        datePicker = (ImageView) findViewById(R.id.pickexpdate);
        updateBtn = (Button) findViewById(R.id.addUpdateBtn);



    }

}
