package com.cemtaskin.yamba2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cemtaskin.yamba2.Model.Timeline;
import com.cemtaskin.yamba2.R;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.text.format.DateUtils.getRelativeTimeSpanString;


/**
 * Created by ctaskin on 19/12/15.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>{

    public List<Timeline> data;


    public TimeLineAdapter(List<Timeline> data) {

        this.data = data;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.timeline_row, parent, false);

        return new TimeLineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        Timeline t = data.get(position);

        holder.txt.setText(t.txt);
        holder.txtUser.setText(t.user);

        String ago = (String) getRelativeTimeSpanString (t.created_at, Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.txtTimeAgo.setText(ago);

        Log.d("ADAPTER","**");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }




    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt) TextView txt;
        @Bind(R.id.txtUser) TextView txtUser;
        @Bind(R.id.txtTimeAgo) TextView txtTimeAgo;


        public TimeLineViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}

