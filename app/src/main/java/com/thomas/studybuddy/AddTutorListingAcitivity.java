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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AddTutorListingAcitivity extends AppCompatActivity {
    private AutoCompleteTextView building;
    private EditText roomNumber;
    private EditText description;
    private Spinner spinner;
    private String course;
    private EditText date;
    private EditText time;
    private Button addbutton;
    private EditText cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tutor_listing_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Get Tutoring");
        building = (AutoCompleteTextView) findViewById(R.id.building);
        roomNumber = (EditText) findViewById(R.id.room_number);
        description = (EditText) findViewById(R.id.description);
        spinner = (Spinner) findViewById(R.id.class_name);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.asList("CS 3210", "CS 3312", "CX 4220", "CX 4365"));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
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
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        cost = (EditText) findViewById(R.id.cost);
        addbutton = (Button) findViewById(R.id.add_button);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://m.gatech.edu/api/gtplaces/buildings", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray array = new JSONArray(responseString);
                    LinkedList<String> list = new LinkedList<>();
                    for (int i = 0; i < array.length(); i++) {
                        list.add(array.getJSONObject(i).getString("name"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTutorListingAcitivity.this, android.R.layout.simple_list_item_1, list);
                    building.setAdapter(adapter);
                    building.setThreshold(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.N)
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickDate();

                }
            }
        });
        date.setShowSoftInputOnFocus(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    pickTime();
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            pickTime();
            }
        });
        time.setShowSoftInputOnFocus(false);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String buildingName = building.getText().toString();
                    String room = roomNumber.getText().toString();
                    String topic = description.getText().toString();
                    String timestamp = date.getText().toString();
                    timestamp += (" @ " + time.getText().toString());
                    ClassModel cm = new ClassModel(course, buildingName, Long.valueOf(room), topic, 1L, 1L);
                    cm.setType("T");
                    cm.setDate(timestamp);
                    cm.setCost(Long.valueOf(cost.getText().toString()));
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    cm.setHostUID(user.getUid());
                    // Todo add real display name
                    cm.setHost("Thomas Lilly");
                    cm.setKey(FirebaseDatabase.getInstance().getReference().push().getKey());
                    FirebaseDatabase.getInstance().getReference("tutor_list/"+cm.getKey()+"/info").setValue(cm);
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

    @TargetApi(Build.VERSION_CODES.N)
    private void pickDate() {
        DatePickerDialog dialog = new DatePickerDialog(AddTutorListingAcitivity.this);
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(String.format(Locale.US, "%d/%d/%d", month, dayOfMonth, year));
            }
        });
        dialog.show();
    }
    @TargetApi(Build.VERSION_CODES.N)
    private void pickTime() {
        TimePickerDialog dialog = new TimePickerDialog(AddTutorListingAcitivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String _24HourTime = String.format(Locale.US, "%d:%02d", hourOfDay, minute);
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm", Locale.US);
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.US);
                Date _24HourDt = null;
                try {
                    _24HourDt = _24HourSDF.parse(_24HourTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                time.setText(_12HourSDF.format(_24HourDt));
            }
        }, 12, 0, false);
        dialog.show();
    }


}
