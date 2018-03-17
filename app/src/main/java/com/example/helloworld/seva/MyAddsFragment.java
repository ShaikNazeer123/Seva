package com.example.helloworld.seva;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAddsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAddsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAddsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static Context context;
    RecyclerView rc;
    private ArrayList<ListModel> customListViewValues = new ArrayList<ListModel>();
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    ListModel dl;
    private FragmentActivity myContext;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mStorage;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseFood;
    private DatabaseReference mDatabaseClothes;
    private DatabaseReference mDatabaseBooks;
    private DatabaseReference mDatabaseMisc;

    private String name;
    private String phonenumber;
    private String title;
    private String description;
    private String location;
    private String expirydate;
    private String image;

    private String uId;

    public MyAddsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAddsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAddsFragment newInstance(String param1, String param2) {
        MyAddsFragment fragment = new MyAddsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(mListener!=null){
            mListener.onFragmentInteraction("My adds");
        }

        View view = inflater.inflate(R.layout.fragment_my_adds, container, false);
        rc = (RecyclerView) view.findViewById(R.id.recycler_view_my_adds);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String st) {
        if (mListener != null) {
            mListener.onFragmentInteraction(st);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseFood = FirebaseDatabase.getInstance().getReference().child("Food");
        mDatabaseClothes = FirebaseDatabase.getInstance().getReference().child("Clothes");
        mDatabaseBooks = FirebaseDatabase.getInstance().getReference().child("Books");
        mDatabaseMisc = FirebaseDatabase.getInstance().getReference().child("Misc");

        uId = mAuth.getCurrentUser().getUid();

        getData();

        rc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rc.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(customListViewValues,context);
        rc.setAdapter(mAdapter);
    }

    public void getData() {
        customListViewValues.clear();

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(uId)) {
                    Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                    final Map<String, String> userMap;
                    userMap = currMap.get(uId);

                    name = userMap.get("name");
                    phonenumber = userMap.get("contact");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseFood.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                //iterate through each Post

                if(currMap != null) {

                    for (Map.Entry<String, Map<String, String>> entry : currMap.entrySet()) {

                        //Get user map
                        Map<String, String> singlePost = (Map<String, String>) entry.getValue();
                        if(singlePost.get("uid").equals(uId)) {
                            ListModel temp = new ListModel();

                            description = singlePost.get("description");
                            title = singlePost.get("title");
                            location = singlePost.get("location");
                            expirydate = singlePost.get("date");
                            image = singlePost.get("image");
                            temp.setItemName(name);
                            temp.setItemDescription(description);
                            temp.setItemPhoneNumber(phonenumber);
                            temp.setItemLocation(location);
                            temp.setItemTitle(title);
                            temp.setItemExpiryDate(expirydate);
                            temp.setImage(image);
                            if(singlePost.get("imageUri")!=null) temp.setmImageUri(Uri.parse(singlePost.get("imageUri")));
                            customListViewValues.add(temp);
                        }
                    }

                    //ArrayList<Blog> values = (ArrayList<Blog>) dataSnapshot.getValue();

                    /*rc.setAdapter(new CustomAdapter(customListViewValues, context));*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseBooks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                //iterate through each Post
                if(currMap != null) {
                    for (Map.Entry<String, Map<String, String>> entry : currMap.entrySet()) {
                        //Get user map
                        Map<String, String> singlePost = (Map<String, String>) entry.getValue();
                        if (singlePost.get("uid").equals(uId)) {
                            ListModel temp = new ListModel();

                            description = singlePost.get("description");
                            title = singlePost.get("title");
                            location = singlePost.get("location");
                            expirydate = singlePost.get("date");
                            image = singlePost.get("image");
                            temp.setItemName(name);
                            temp.setItemDescription(description);
                            temp.setItemPhoneNumber(phonenumber);
                            temp.setItemLocation(location);
                            temp.setItemTitle(title);
                            temp.setItemExpiryDate(expirydate);
                            temp.setImage(image);
                            if(singlePost.get("imageUri")!=null) temp.setmImageUri(Uri.parse(singlePost.get("imageUri")));
                            customListViewValues.add(temp);
                        }
                    }

                    //ArrayList<Blog> values = (ArrayList<Blog>) dataSnapshot.getValue();

                    /*rc.setAdapter(new CustomAdapter(customListViewValues, context));*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseClothes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                //iterate through each Post
                if(currMap != null) {
                    for (Map.Entry<String, Map<String, String>> entry : currMap.entrySet()) {

                        //Get user map
                        Map<String, String> singlePost = (Map<String, String>) entry.getValue();

                        ListModel temp = new ListModel();
                        if(singlePost.get("uid").equals(uId)) {
                            description = singlePost.get("description");
                            title = singlePost.get("title");
                            location = singlePost.get("location");
                            expirydate = singlePost.get("date");
                            image = singlePost.get("image");
                            temp.setItemName(name);
                            temp.setItemDescription(description);
                            temp.setItemPhoneNumber(phonenumber);
                            temp.setItemLocation(location);
                            temp.setItemTitle(title);
                            temp.setItemExpiryDate(expirydate);
                            temp.setImage(image);
                            if(singlePost.get("imageUri")!=null) temp.setmImageUri(Uri.parse(singlePost.get("imageUri")));
                            customListViewValues.add(temp);
                        }
                    }

                    //ArrayList<Blog> values = (ArrayList<Blog>) dataSnapshot.getValue();

                    /*rc.setAdapter(new CustomAdapter(customListViewValues, context));*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseMisc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> currMap = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                //iterate through each Post
                if(currMap != null) {

                    for (Map.Entry<String, Map<String, String>> entry : currMap.entrySet()) {

                        //Get user map
                        Map<String, String> singlePost = (Map<String, String>) entry.getValue();
                        ListModel temp = new ListModel();
                        if(singlePost.get("uid").equals(uId)) {
                            description = singlePost.get("description");
                            title = singlePost.get("title");
                            location = singlePost.get("location");
                            expirydate = singlePost.get("date");
                            image = singlePost.get("image");
                            temp.setItemName(name);
                            temp.setItemDescription(description);
                            temp.setItemPhoneNumber(phonenumber);
                            temp.setItemLocation(location);
                            temp.setItemTitle(title);
                            temp.setItemExpiryDate(expirydate);
                            temp.setImage(image);
                            if(singlePost.get("imageUri")!=null) temp.setmImageUri(Uri.parse(singlePost.get("imageUri")));
                            customListViewValues.add(temp);
                        }
                        Log.e("size",customListViewValues.size()+"");
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String st);
    }
}
