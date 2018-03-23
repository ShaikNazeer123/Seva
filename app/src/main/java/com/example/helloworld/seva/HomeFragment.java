package com.example.helloworld.seva;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private FragmentActivity myContext;
    FragmentPagerAdapter adapterViewPager;
    TabLayout mainTabLayout;
    ViewPager mainViewPager;

    View mHomeFragmentView=null;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Home : ","onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("OnCreateView","Called");
        if(mHomeFragmentView==null)
         mHomeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        return mHomeFragmentView;
    }

    class MyOwnPagerAdapter extends FragmentPagerAdapter{
        String tabData[] = {"Food","Clothes","Books","Miscellaneous"};

        public MyOwnPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Came",String.valueOf(position));
            if(position == 0){
                return new FoodFragment();
            }

            else if(position == 1){
                return new ClothesFragment();
            }

            else if(position == 2){
                return new BooksFragment();
            }

            else if(position == 3){
                return new MiscellaneousFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabData.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return tabData[position];
        }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("OnViewCreated","Called");
        mainTabLayout = (TabLayout)view.findViewById(R.id.maintablayout);
        mainViewPager = (ViewPager)view.findViewById(R.id.mainviewpager);
        mainViewPager.setOffscreenPageLimit(4);

        mainViewPager.setAdapter(new MyOwnPagerAdapter(getChildFragmentManager()));
        mainTabLayout.setupWithViewPager(mainViewPager);

        if(mListener!=null){
            mListener.onFragmentInteraction("Home");
        }

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
