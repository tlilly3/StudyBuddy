package com.thomas.studybuddy;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class AddTutorListingAcitivity extends AppCompatActivity {
    private EditText building;
    private EditText roomNumber;
    private EditText description;
    private Spinner spinner;
    private String course;
    private EditText date;
    private EditText time;
    private Button addbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tutor_listing_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Get Tutoring");
        building = (EditText) findViewById(R.id.building);
        roomNumber = (EditText) findViewById(R.id.room_number);
        description = (EditText) findViewById(R.id.description);
        spinner = (Spinner) findViewById(R.id.class_name);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        addbutton = (Button) findViewById(R.id.add_button);
        date.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddTutorListingAcitivity.this);
                dialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(AddTutorListingAcitivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(String.format(Locale.US,"%d:%d", hourOfDay, minute));
                    }
                }, 12, 0, false);
                dialog.show();
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String buildingName = building.getText().toString();
                    String room = roomNumber.getText().toString();
                    String topic = description.getText().toString();
                    String timestamp = date.getText().toString();
                    timestamp += time.getText().toString();
                    ClassModel cm = new ClassModel(course, buildingName, Long.valueOf(room), topic, 1L, 1L);
                    cm.setType("T");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    cm.setHostUID(user.getUid());
                    // Todo add real display name
                    cm.setHost("Thomas Lilly");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tutor_list");
                    ref.push().child("info").setValue(cm);
                    finish();
                }
            }
        });
    }
    private boolean validate() {
        // Reset errors.
        building.setError(null);
        roomNumber.setError(null);
        description.setError(null);
        date.setError(null);
        time.setError(null);
        // Store values at the time of the login attempt.
        String buildingName = building.getText().toString();
        String room = roomNumber.getText().toString();
        String topic = description.getText().toString();

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
        } else if (date.getText().toString().isEmpty()) {
            date.setError("Date needed");
            focusView = date;
            cancel = false;
        } else if (time.getText().toString().isEmpty()) {
            time.setError("Time needed");
            focusView = time;
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


}
