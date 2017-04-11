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
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Studys"));
        tabLayout.addTab(tabLayout.newTab().setText("Homeworks"));
        ClassPager classPager =
                new ClassPager(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(classPager);
        Button b = (Button) findViewById(R.id.test_button);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("sessions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Try to update the mapview
//                Log.d("Firebase Callback", dataSnapshot.child("lat").toString());
                LatLng latLng = new LatLng((Double)dataSnapshot.child("lat").getValue(), (Double)dataSnapshot.child("lng").getValue());
                mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("Firebase", "Child was changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "Child was removed successfully");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("Firebase", "I'm still not really sure what this does");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "There was an error");
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ref.child("sessions").push().getKey();
                ref.child("sessions").child(key).child("lat").setValue(33.774918);
                ref.child("sessions").child(key).child("lng").setValue(-84.396453);
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.774259, -84.398170);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Campanile"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
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
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


}
