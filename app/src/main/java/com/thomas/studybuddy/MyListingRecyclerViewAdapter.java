package com.thomas.studybuddy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomas.studybuddy.ClassFragment.OnListFragmentInteractionListener;
import com.thomas.studybuddy.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListingRecyclerViewAdapter extends RecyclerView.Adapter<MyListingRecyclerViewAdapter.ViewHolder> {

    private final List<ClassModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyListingRecyclerViewAdapter(List<ClassModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCourseView.setText(holder.mItem.getCourse());
        holder.mLocationView.setText(holder.mItem.getLocationView());
        holder.mTopicView.setText(holder.mItem.getDescription());
        holder.mCapacityView.setText(holder.mItem.getMoneyView());
        holder.mDateView.setText(holder.mItem.getDate());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCourseView;
        public final TextView mLocationView;
        public final TextView mTopicView;
        public final TextView mCapacityView;
        public final TextView mDateView;
        public ClassModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCourseView = (TextView) view.findViewById(R.id.class_name);
            mLocationView = (TextView) view.findViewById(R.id.location);
            mTopicView = (TextView) view.findViewById(R.id.topic);
            mCapacityView = (TextView) view.findViewById(R.id.capacity);
            mDateView = (TextView) view.findViewById(R.id.date);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTopicView.getText() + "'";
        }
    }
}
