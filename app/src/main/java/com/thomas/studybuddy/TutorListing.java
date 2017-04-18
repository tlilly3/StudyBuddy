package com.thomas.studybuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.thomas.studybuddy.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class TutorListing extends MainActivity implements ClassFragment.OnListFragmentInteractionListener {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_listing);
        setUp();
        getSupportActionBar().setTitle("Tutor Requests");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ClassModel> item = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            final ClassModel cm = new ClassModel("CS 4001", "Howey", 101L, "Assignment 12 more physics ", 10L, 45L);
            cm.setLat(33.775179);
            cm.setLng(-84.396361);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            cm.setType("T");
            cm.setHostUID(user.getUid());
            cm.setHost("Thomas Lilly");
            cm.setDate("Monday, April 12, 2019");
            cm.setCost((long)(Math.random()*40));
            cm.setKey(FirebaseDatabase.getInstance().getReference().push().getKey());
            item.add(cm);
            FirebaseDatabase.getInstance().getReference("tutor_list/"+cm.getKey()+"/info").setValue(cm);

        }
        recyclerView.setAdapter(new MyListingRecyclerViewAdapter(item, this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }

    @Override
    public void onListFragmentInteraction(final ClassModel item) {
        // Send the Bundle
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.numberpicker_dialog, null);
        builder.setTitle("Make offer")
                .setView(dialogView)
                .setPositiveButton("Offer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NumberPicker numberPicker = (NumberPicker) ((AlertDialog) dialog).findViewById(R.id.picker);
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("tutor_list/"+item.getKey()+"/offers/").child(uid).setValue(numberPicker.getValue());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        NumberPicker picker = (NumberPicker) dialogView.findViewById(R.id.picker);
        picker.setMinValue(0);
        picker.setMaxValue(1000);
        picker.setValue(item.getCost().intValue());
        dialog.show();
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
}
