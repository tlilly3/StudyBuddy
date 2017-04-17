package com.thomas.studybuddy;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class ClassHostActivity extends MainActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button hostButton;
    private DatabaseReference mDatabase;
    private RadioGroup group;
    private EditText building;
    private EditText roomNumber;
    private EditText description;
    private EditText capacity;
    private Spinner spinner;
    private String course;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private Double lat;
    private Double lng;
    private boolean hasChild;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_host);
        setUp();
        building = (EditText) findViewById(R.id.building);
        roomNumber = (EditText) findViewById(R.id.room_number);
        description = (EditText) findViewById(R.id.description);
        capacity = (EditText) findViewById(R.id.capacity);
        hostButton = (Button) findViewById(R.id.host_button);
        group = (RadioGroup) findViewById(R.id.radio_group);
        spinner = (Spinner) findViewById(R.id.class_name);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                course = "This should never happen";
            }
        });
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sessions");
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        hasChild = snapshot.hasChild(user.getUid());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Firebase Child Check", "Operation Cancelled");
                    }
                });
                if (hasChild) {
                    Snackbar.make(findViewById(R.id.host_layout), "Please End Current Session to start new one", Snackbar.LENGTH_SHORT);
                } else if (validate()) {
                    String buildingName = building.getText().toString();
                    String room = roomNumber.getText().toString();
                    String topic = description.getText().toString();
                    String cap = capacity.getText().toString();
                    ClassModel cm = new ClassModel(course, buildingName, Double.valueOf(room), topic, Double.valueOf(1), Double.valueOf(cap));
                    cm.setLat(lat);
                    cm.setLng(lng);
                    if (group.getCheckedRadioButtonId() == R.id.study_radio) {
                        cm.setType("S");
                    } else {
                        cm.setType("H");
                    }
                    cm.setHostUID(user.getUid());
                    cm.setHost(user.getDisplayName());
                    ref.child(user.getUid()).setValue(cm);

                }
            }
        });

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.add_session);
        menuItem.setVisible(false);
        return true;
    }

    private boolean validate() {
        // Reset errors.
        building.setError(null);
        roomNumber.setError(null);
        description.setError(null);
        capacity.setError(null);
        // Store values at the time of the login attempt.
        String buildingName = building.getText().toString();
        String room = roomNumber.getText().toString();
        String topic = description.getText().toString();
        String cap = capacity.getText().toString();

        boolean cancel = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (buildingName.isEmpty()) {
            building.setError("Location not specified");
            focusView = building;
            cancel = false;
        } else if (room.isEmpty()) {
            roomNumber.setError("Room number needed");
            focusView = roomNumber;
            cancel = false;
        } else if (topic.isEmpty()) {
            description.setError("Topic needed");
            focusView = description;
            cancel = false;
        } else if (cap.isEmpty()) {
            capacity.setError("Room capcity needed");
            focusView = capacity;
            cancel = false;
        } else if (group.getCheckedRadioButtonId() == -1) {
            cancel = false;
            Snackbar.make(findViewById(R.id.host_layout), "You must select a radio button", Snackbar.LENGTH_SHORT);
        }
        try {
            Integer.parseInt(cap);
        } catch (NumberFormatException e) {
            capacity.setError("Invalid Input");
            focusView = capacity;
            cancel = false;
        }
        try {
            Integer.parseInt(room);
        } catch (NumberFormatException e) {
            roomNumber.setError("Invalid Input");
            focusView = roomNumber;
            cancel = false;
        }
        if (focusView != null) {
            focusView.requestFocus();
        }
        return cancel;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Snackbar.make(findViewById(R.id.host_layout), "Connection Suspended", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(findViewById(R.id.host_layout), "Could not connect to Google", Snackbar.LENGTH_SHORT).show();
    }
}
