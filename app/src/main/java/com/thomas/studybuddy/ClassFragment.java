package com.thomas.studybuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thomas.studybuddy.dummy.DummyContent;
import com.thomas.studybuddy.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ClassFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_SESSION_TYPE = "session-type";
    // TODO: Customize parameters
    private int sessionType;
    private List<ClassModel> contents;
    private MyClassRecyclerViewAdapter myClassRecyclerViewAdapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClassFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ClassFragment newInstance(int columnCount) {
        if (columnCount == 0 || columnCount == 1) {
            ClassFragment fragment = new ClassFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SESSION_TYPE, columnCount);
            fragment.setArguments(args);
            return fragment;
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("sessions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Try to update the mapview
//                Log.d("Firebase Callback", dataSnapshot.child("lat").toString());
                String type = (String) dataSnapshot.child("type").getValue();
                if (sessionType == 0 && type.equals("H")) {
                    ClassModel cm = dataSnapshot.getValue(ClassModel.class);
                    contents.add(cm);
                    myClassRecyclerViewAdapter.notifyItemInserted(contents.size()-1);
                } else if (sessionType == 1 && type.equals("S")) {
                    ClassModel cm = dataSnapshot.getValue(ClassModel.class);
                    contents.add(cm);
                    myClassRecyclerViewAdapter.notifyItemInserted(contents.size()-1);
                }
//                LatLng latLng = new LatLng((Double)dataSnapshot.child("lat").getValue(), (Double)dataSnapshot.child("lng").getValue());
//                mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
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
        if (getArguments() != null) {
            sessionType = getArguments().getInt(ARG_SESSION_TYPE);
        }
        if (sessionType == 0) {
            // Load Studys
            contents = new ArrayList<>();
        } else if (sessionType == 1) {
            // Load Homework Sessionsc
            contents = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            myClassRecyclerViewAdapter = new MyClassRecyclerViewAdapter(contents, mListener);
            recyclerView.setAdapter(myClassRecyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ClassModel item);
    }
}
