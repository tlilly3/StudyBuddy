package com.thomas.studybuddy;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thomas.studybuddy.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import biz.laenger.android.vpbs.BottomSheetUtils;

public class HomeActivity extends MainActivity implements ClassFragment.OnListFragmentInteractionListener, OnMapReadyCallback {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    HashMap<LatLng, Marker> mapMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        mapMarkers = new HashMap<>();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("sessions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LatLng latLng = new LatLng((Double) dataSnapshot.child("lat").getValue(), (Double) dataSnapshot.child("lng").getValue());
                Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title(dataSnapshot.child("course").getValue() + " - " + dataSnapshot.child("description").getValue()));
                mapMarkers.put(latLng, m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                LatLng latLng = new LatLng((Double) dataSnapshot.child("lat").getValue(), (Double) dataSnapshot.child("lng").getValue());
                mapMarkers.get(latLng).remove();
                mapMarkers.remove(latLng);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Study"));
        tabLayout.addTab(tabLayout.newTab().setText("Homework"));
        ClassPager classPager =
                new ClassPager(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(classPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        BottomSheetUtils.setupViewPager(mViewPager);
        Button b = (Button) findViewById(R.id.test_button);
//        final ClassModel cm = new ClassModel("CS 4001", "Howey", 101, "Assignment 12 more physics ", (double)10, (double)45);
//        cm.setLat(33.775179);
//        cm.setLng(-84.396361);
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        cm.setType("H");
//        cm.setHostUID(user.getUid());
//        cm.setHost(user.getDisplayName());
//        cm.setParticipants(new ArrayList<String>(Arrays.asList("Nicolette Prevost", "Pratik Kunapali", "Hannah Lee")));
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ref.child("sessions").child(user.getUid()).setValue(cm);
//            }
//        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.774259, -84.398170);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Campanile"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16 ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onListFragmentInteraction(ClassModel item) {
        Log.d("MAIN", "Interacted with The fragment");
        Intent classDetail = new Intent(this, ClassJoinDetail.class);
        classDetail.putExtra("class", item);
        startActivity(classDetail);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_session) {
            Intent addSession = new Intent(this, ClassHostActivity.class);
            startActivity(addSession);
        }

        return super.onOptionsItemSelected(item);
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class ClassPager extends FragmentStatePagerAdapter {
        public ClassPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return ClassFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


}
