package com.thomas.studybuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClassJoinDetail extends AppCompatActivity {
    private Button joinButton;
    private DatabaseReference mDatabase;
    private ListView listView;
    private TextView className, location, description, hostName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassModel argument = getIntent().getExtras().getParcelable("class");
        setContentView(R.layout.activity_class_join_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        joinButton = (Button) findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("class").push().setValue("3");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Join Session");
        className = (TextView) findViewById(R.id.class_name);
        className.setText(argument.getCourse());
        location = (TextView)  findViewById(R.id.location);
        location.setText(argument.getLocationView());
        description = (TextView) findViewById(R.id.description);
        description.setText(argument.getDescription());
        hostName = (TextView) findViewById(R.id.hostName);
        hostName.setText(argument.getHost());
        listView = (ListView) findViewById(R.id.particpants);
        listView.setAdapter(new ArrayAdapter<String>(this,R.layout.participant_listing, argument.getParticipants()));


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
