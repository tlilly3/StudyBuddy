package com.thomas.studybuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedList;

public class ClassHostActivity extends MainActivity {
    private Button hostButton;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_host);
        setUp();
        hostButton = (Button) findViewById(R.id.host_button);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase.child("sessions").child(user.getUid()).setValue(new ClassSession("A", "B", "C", "D", new LinkedList<String>(), "E"));
            }
        });

    }
}
