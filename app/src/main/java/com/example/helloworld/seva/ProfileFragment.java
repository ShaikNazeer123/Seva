package com.example.helloworld.seva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;

    private ImageButton mProfileImage;
    private ImageView mProfileEditButton;

    private TextView mNameField;
    private TextView mContactField;
    private TextView mOrganizationField;
    private TextView mGenderField;
    private TextView mAddressField;
    private TextView mDobField;

    int btn;

    View mView;

    private Button mPostBtn;

    private Uri mImageUri = null;

    private ProgressDialog mProgress;

    private String uId;

    private static final int GALLERY_REQUEST = 1;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mView = view;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        uId = mAuth.getCurrentUser().getUid();

        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(getContext());

        mProfileImage = (ImageButton) mView.findViewById(R.id.profileImage);
        mProfileEditButton = (ImageView) mView.findViewById(R.id.profile_edit_button);

        mNameField = (TextView) mView.findViewById(R.id.name_field);
        mContactField = (TextView) mView.findViewById(R.id.contact_field);
        mOrganizationField = (TextView) mView.findViewById(R.id.organization_field);
        mAddressField = (TextView) mView.findViewById(R.id.address_field);
        mGenderField = (TextView) mView.findViewById(R.id.gender_field);
        mDobField = (TextView) mView.findViewById(R.id.dob_field);

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Map<String,String> > currMap = (Map<String,Map<String,String> >) dataSnapshot.getValue();

                final Map<String,String> userMap;
                userMap = currMap.get(uId);

                mNameField.setText(userMap.get("name"));
                mContactField.setText(userMap.get("contact"));
                mOrganizationField.setText(userMap.get("organization"));
                mAddressField.setText(userMap.get("address"));
                mGenderField.setText(userMap.get("gender"));
                mDobField.setText(userMap.get("dob"));

                Picasso.with(getActivity()).load(userMap.get("image")).networkPolicy(NetworkPolicy.OFFLINE).into(mProfileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(getActivity()).load(userMap.get("image")).into(mProfileImage);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(getActivity()).load(userMap.get("image")).into(mProfileImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileEditButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startEditing();
            }
        });


        return view;
    }

    private void startEditing() {
        Intent editIntent = new Intent(getActivity(), EditProfileActivity.class);
        editIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(editIntent);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String st) {
        if (mListener != null) {
            mListener.onFragmentInteraction(st);
        }
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
