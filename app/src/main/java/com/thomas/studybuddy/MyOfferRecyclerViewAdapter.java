package com.thomas.studybuddy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thomas on 4/17/17.
 */

public class MyOfferRecyclerViewAdapter extends RecyclerView.Adapter<MyOfferRecyclerViewAdapter.ViewHolder> {
    private final List<ClassModel> mValues;
    private final OfferFragment.OnListFragmentInteractionListener mListener;

    public MyOfferRecyclerViewAdapter(List<ClassModel> items, OfferFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public MyOfferRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listing, parent, false);
        return new MyOfferRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyOfferRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mLocationView.setText(holder.mItem.getLocationView());
        holder.mTopicView.setText(holder.mItem.getDescription());
        holder.mCapacityView.setText(holder.mItem.getCostView());
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

        public ClassModel getmItem() {
            return mItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTopicView.getText() + "'";
        }
    }
}
