package com.example.helloworld.seva;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        IntersetsFragment.OnFragmentInteractionListener,
        LikeSupportFragment.OnFragmentInteractionListener,
        MyAddsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        PostAddFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DrawerLayout drawer;
    ImageView profileBtn;
    TextView phoneNumber;
    TextView profileName;

    String userName;
    String userContact;
    String userImage;

    DatabaseReference mDatabase;

    HomeFragment homeFragment;
    MyAddsFragment myAddsFragment;
    IntersetsFragment intersetsFragment;
    AboutFragment aboutFragment;
    LikeSupportFragment likeSupportFragment;
    PostAddFragment postAddFragment;
    static String uId;

    static Context k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.e("Main Activity ","Oncreate");

        mAuth = FirebaseAuth.getInstance();

        uId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View ll = navigationView.getHeaderView(0);
        profileBtn = (ImageView) ll.findViewById(R.id.profilebtn);
        phoneNumber = (TextView) ll.findViewById(R.id.phonenumber);
        profileName = (TextView) ll.findViewById(R.id.profilename);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> singlePost = (Map<String,String>) dataSnapshot.getValue();

                userName = (String) singlePost.get("name");
                userImage = (String) singlePost.get("image");
                userContact = (String) singlePost.get("contact");

                profileName.setText(userName);
                phoneNumber.setText(userContact);
                Picasso.with(MainActivity.this).load(userImage).networkPolicy(NetworkPolicy.OFFLINE).into(profileBtn, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(MainActivity.this).load(userImage).into(profileBtn);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(MainActivity.this).load(userImage).into(profileBtn);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(profileBtn==null){
            Log.e("Profile Button","null");
        }

        //profile button action
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("btn ","Listener");
                DrawerLayout mDrawerLayout;
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
                android.support.v4.app.Fragment fragment =null;
                fragment = new ProfileFragment();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.mainFrame,fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        homeFragment = HomeFragment.newInstance(null,null);
        myAddsFragment = MyAddsFragment.newInstance(null,null);
        intersetsFragment = IntersetsFragment.newInstance(null,null);
        aboutFragment = AboutFragment.newInstance(null,null);
        likeSupportFragment = LikeSupportFragment.newInstance(null,null);

        /*
        Intent intentExtras = getIntent();
        Bundle extraData = intentExtras.getExtras();

        String value = (String)extraData.getString("first");

        Log.e("One","tell me1");
        if(value.compareTo("Yes") == 0 ){
            Log.e("Two","tell me2");
            android.support.v4.app.Fragment fragment;
            fragment = new ProfileFragment();
            Log.e("Three","tell me3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Log.e("Four","tell me4");
            ft.replace(R.id.mainFrame,fragment);
            Log.e("Five","tell me5");
            ft.commit();
            Log.e("Six","tell me6");
        }
        Log.e("Seven","tell me7");

        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///default fragment
        navigationView.setCheckedItem(R.id.home);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame,new HomeFragment());

        ft.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.e("Mainactivity","onbackpressed");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                //change, if error occur
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            mAuth.signOut();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if(id==R.id.addbutton){
            android.support.v4.app.Fragment fragment =null;
            fragment = new PostAddFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.Fragment fragment =null;
        if (id == R.id.home) {
            fragment = homeFragment;
        } else if (id == R.id.my_ads) {
            fragment = myAddsFragment;
        } else if (id == R.id.interests) {
            fragment = intersetsFragment;
        } else if (id == R.id.about) {
            fragment = aboutFragment;
        }else if(id == R.id.like_support){
            fragment = likeSupportFragment;
        }else if (id == R.id.logout) {
            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            mAuth.signOut();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }

        if(fragment!=null){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public Context getContext(){
        return this;
    }

    public static String getuId() {
        return uId;
    }

    @Override
    public void onFragmentInteraction(String st) {
        getSupportActionBar().setTitle(st);
        //title
    }
}
