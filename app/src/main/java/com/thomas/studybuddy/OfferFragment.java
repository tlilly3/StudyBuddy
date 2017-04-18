package com.thomas.studybuddy;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 4/17/17.
 */

public class OfferFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_SESSION_TYPE = "session-type";
    // TODO: Customize parameters
    private int sessionType;
    private List<ClassModel> contents;
    private MyOfferRecyclerViewAdapter myOfferRecyclerViewAdapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfferFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OfferFragment newInstance(int columnCount) {
        if (columnCount == 0 || columnCount == 1) {
            OfferFragment fragment = new OfferFragment();
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
        ref.child("tutor_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Try to update the mapview
//                Log.d("Firebase Callback", dataSnapshot.child("lat").toString());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("FirebaseCallback", String.format("Adding session of type %d", sessionType));
                if (sessionType == 0 && dataSnapshot.child("info").child("hostUID").getValue().equals(user.getUid())
                                    && dataSnapshot.hasChild("offers")) {
                    for (DataSnapshot snapshot : dataSnapshot.child("offers").getChildren()) {
                        ClassModel cm = dataSnapshot.child("info").getValue(ClassModel.class);
                        cm.setOffer((Long)snapshot.getValue());
                        contents.add(cm);
                        myOfferRecyclerViewAdapter.notifyItemInserted(contents.size()-1);
                    }
                } else if (sessionType == 1 &&  dataSnapshot.hasChild("offers")
                            && dataSnapshot.child("offers").hasChild(user.getUid())) {
                    ClassModel cm = dataSnapshot.child("info").getValue(ClassModel.class);
                    contents.add(cm);
                    cm.setOffer(cm.getCost());
                    myOfferRecyclerViewAdapter.notifyItemInserted(contents.size()-1);
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
        View view = inflater.inflate(R.layout.fragment_listing_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            myOfferRecyclerViewAdapter = new MyOfferRecyclerViewAdapter(contents, mListener);
            recyclerView.setAdapter(myOfferRecyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            if (sessionType == 1) {
                ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int pos = viewHolder.getAdapterPosition();
                        contents.remove(pos);
                        myOfferRecyclerViewAdapter.notifyItemRemoved(pos);
                        ClassModel cm = ((MyOfferRecyclerViewAdapter.ViewHolder)viewHolder).getmItem();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("tutor_list/"+cm.getKey()+"/offers/"+user.getUid()).removeValue();
                    }
                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            // Get RecyclerView item from the ViewHolder
                            View itemView = viewHolder.itemView;
                            c.drawColor(Color.RED);
                            Paint p = new Paint();
                            if (dX > 0) {
                                /* Set your color for positive displacement */
                                // Draw Rect with varying right side, equal to displacement dX
                                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                        (float) itemView.getBottom(), p);
                            } else {
                                /* Set your color for negative displacement */

                                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
                            }

                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }
                };
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView((RecyclerView)view);
            }
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
