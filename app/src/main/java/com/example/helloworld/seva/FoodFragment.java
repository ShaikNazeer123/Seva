package com.example.helloworld.seva;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {

    static Context context;
    RecyclerView rc;
    private ArrayList<ListModel> customListViewValues = new ArrayList<ListModel>();
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    ListModel dl;

    private FragmentActivity myContext;
    private HomeFragment.OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mStorage;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseFood;

    private String name;
    private String phonenumber;
    private String title;
    private String description;
    private String location;
    private String expirydate;
    private String image;

    private String uId;
    private String postUID;
    private String postDate;

    public FoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        // Inflate the layout for this fragment
        rc = (RecyclerView) view.findViewById(R.id.recycler_view_food);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseFood = FirebaseDatabase.getInstance().getReference().child("Food");

        uId = mAuth.getCurrentUser().getUid();

        getData();

        rc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rc.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(customListViewValues,context);
        rc.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void getData() {
        customListViewValues.clear();

        mDatabaseFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                //iterate through each Post

                if(currMap != null) {
                    for (Map.Entry<String, Map<String, String>> entry : currMap.entrySet()) {

                        //Get user map
                        Map<String, String> singlePost = (Map<String, String>) entry.getValue();

                        ListModel temp = new ListModel();

                        description = singlePost.get("description");
                        title = singlePost.get("title");
                        location = singlePost.get("address");
                        expirydate = singlePost.get("date");
                        image = singlePost.get("image");
                        postUID = singlePost.get("uid");
                        name = singlePost.get("name");
                        phonenumber = singlePost.get("contact");
                        postDate = singlePost.get("postdate");

                        temp.setItemName(name);
                        temp.setItemDescription(description);
                        temp.setItemPhoneNumber(phonenumber);
                        temp.setItemLocation(location);
                        temp.setItemTitle(title);
                        temp.setItemExpiryDate(expirydate);
                        temp.setImage(image);
                        temp.setItemPostDate(postDate);

                        if(singlePost.get("imageUri")!=null) temp.setmImageUri(Uri.parse(singlePost.get("imageUri")));
                        customListViewValues.add(temp);
                    }

                    //ArrayList<Blog> values = (ArrayList<Blog>) dataSnapshot.getValue();

                    rc.setAdapter(new CustomAdapter(customListViewValues, context));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}