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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClothesFragment extends Fragment {

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
    private DatabaseReference mDatabaseClothes;

    private String name;
    private String phonenumber;
    private String title;
    private String description;
    private String location;
    private String expirydate;
    private String image;
    private String imageUri;

    private String uId;
    private String postUID;
    private String postDate;


    public ClothesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clothes, container, false);
        // Inflate the layout for this fragment
        rc = (RecyclerView) view.findViewById(R.id.recycler_view_clothes);
        return view;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseClothes = FirebaseDatabase.getInstance().getReference().child("Clothes");

        uId = mAuth.getCurrentUser().getUid();

        getData();

        rc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rc.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(customListViewValues,context);
        rc.setAdapter(mAdapter);

    }

    public void getData() {

        mDatabaseClothes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customListViewValues.clear();
                Map<String, Map<String, Object> > currMap = (Map<String, Map<String, Object> >) dataSnapshot.getValue();

                //iterate through each Post

                if(currMap != null) {
                    for (Map.Entry<String, Map<String, Object> > entry : currMap.entrySet()) {

                        //Get user map

                        ListModel temp = new ListModel();
                        Map<String, Object> singlePost = (Map<String, Object>) entry.getValue();
                        if(singlePost == null) continue;
                        Boolean isLiked = false;
                        Boolean isCompleted = false;
                        String postKey = entry.getKey();
                        for(Map.Entry<String, Object> entry11 : singlePost.entrySet()){

                            String fieldKey = entry11.getKey();

                            //Log.e("Yups",fieldKey);

                            if(fieldKey.equals("interested")){
//                                Log.e("Bro","interested");
                                Map<String,String> fieldValue = (Map<String,String>) entry11.getValue();

                                if(fieldValue.get(uId) != null){
                                    isLiked = true;
                                }
                            }
                            else{
//                                Log.e("done",fieldKey);

                                String fieldValue = (String)entry11.getValue();
                                if(fieldKey.equals("description")) {
                                    description = fieldValue;
                                }
                                if(fieldKey.equals("title")) {
                                    title = fieldValue;
                                }
                                if(fieldKey.equals("address")) {
                                    location = fieldValue;
                                }
                                if(fieldKey.equals("date")) {
                                    expirydate = fieldValue;
                                }
                                if(fieldKey.equals("image")) {
                                    image = fieldValue;
                                }
                                if(fieldKey.equals("uid")) {
                                    postUID = fieldValue;
                                }
                                if(fieldKey.equals("name")) {
                                    name = fieldValue;
                                }
                                if(fieldKey.equals("contact")) {
                                    phonenumber = fieldValue;
                                }
                                if(fieldKey.equals("postdate")) {
                                    postDate = fieldValue;
                                }
                                if(fieldKey.equals("imageUri")) {
                                    imageUri = fieldValue;
                                }

                                if(fieldKey.equals("iscompleted")) {
                                    if(fieldValue.equals("true")){
                                        temp.setIsCompleted();
                                    }
                                    else{
                                        temp.resetisCompleted();
                                    }
                                }
                            }
                        }

                        temp.setItemName(name);
                        temp.setItemDescription(description);
                        temp.setItemPhoneNumber(phonenumber);
                        temp.setItemLocation(location);
                        temp.setItemTitle(title);
                        temp.setItemExpiryDate(expirydate);
                        temp.setImage(image);
                        temp.setItemPostDate(postDate);
                        temp.setCategoryType("Clothes");
                        temp.setPostId(postKey);
                        temp.setuId(uId);

                        if(imageUri!=null) temp.setmImageUri(Uri.parse(imageUri));

                        if(isLiked == true){
                            temp.setIsLiked();
                        }
                        else{
                            temp.resetisLiked();
                        }
//                        Log.e("data: ",temp.getItemName()+" "+temp.getItemDescription()+" "+temp.getItemPhoneNumber()+" "+temp.getItemLocation()+" ");
                      /*  SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
                        //  SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
                        try {
                            Date date = dateFormat.parse(expirydate);
                            Log.e("Time", date+"");
                            if (System.currentTimeMillis() <= date.getTime()) {
                                customListViewValues.add(temp);
                            }
                        } catch (ParseException e) {
                        }*/
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
