package com.thomas.studybuddy;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thomas.studybuddy.dummy.DummyContent;

import java.util.Iterator;

import biz.laenger.android.vpbs.BottomSheetUtils;

public class HomeActivity extends MainActivity implements ClassFragment.OnListFragmentInteractionListener, OnMapReadyCallback {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Studys"));
        tabLayout.addTab(tabLayout.newTab().setText("Homeworks"));
        ClassPager classPager =
                new ClassPager(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(classPager);
        BottomSheetUtils.setupViewPager(mViewPager);
        Button b = (Button) findViewById(R.id.test_button);
        final ClassModel cm = new ClassModel("CS 4001", "Howey", (double)101, "Assignment 12 more physics ", (double)10, (double)45);
        cm.setLat(33.775179);
        cm.setLng(-84.396361);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("sessions").push().setValue(cm);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.774259, -84.398170);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Campanile"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16 ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onListFragmentInteraction(ClassModel item) {
        Log.d("MAIN", "Interacted with The fragment");
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
