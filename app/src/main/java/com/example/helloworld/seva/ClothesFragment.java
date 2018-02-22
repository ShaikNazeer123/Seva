package com.example.helloworld.seva;


import android.content.Context;
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

import java.util.ArrayList;


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

        dl = new ListModel();
        getData();

        rc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rc.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(customListViewValues,context);
        rc.setAdapter(mAdapter);

    }

    public void getData() {
        customListViewValues.clear();
        ListModel l = new ListModel();
        l.setItemName("Nazeer");
        l.setItemDescription("Testing");
        l.setItemPhoneNumber("984890662");
        l.setItemLocation("Chennai");
        l.setItemTitle("Clash Test");
        l.setItemExpiryDate("09-12-18");
        customListViewValues.add(l);
        ListModel l1 = new ListModel();
        l1.setItemName("Nazeer");
        l1.setItemDescription("Testing");
        l1.setItemPhoneNumber("984890662");
        l1.setItemLocation("Chennai");
        l1.setItemTitle("Itemhfghf Test");
        l1.setItemExpiryDate("09-12-18");
        customListViewValues.add(l1);
        l.setItemName("Nazeer");
        l.setItemDescription("Testing");
        l.setItemPhoneNumber("984890662");
        l.setItemLocation("Chennai");
        l.setItemTitle("Clash Test");
        l.setItemExpiryDate("09-12-18");
        customListViewValues.add(l);


    }
}
