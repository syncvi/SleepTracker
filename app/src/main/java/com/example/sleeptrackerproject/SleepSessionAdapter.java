package com.example.sleeptrackerproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


// adapter class that maps data of type SleepSession to the sleep_session_item.xml via SleepSessionViewHolder
public class SleepSessionAdapter extends RecyclerView.Adapter<SleepSessionViewHolder> {

    // list of sleepsession data
    private LiveData<List<SleepSession>> _sleepSessions;

    //init
    public SleepSessionAdapter(LiveData<List<SleepSession>> sleepSessions) {
        _sleepSessions = sleepSessions;
    }

    // create a new ViewHolder and inflate the sleep_session_item layout
    @NonNull
    @Override
    public SleepSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // inflate the sleep_session_item layout and pass it to the ViewHolder
        View view = inflater.inflate(R.layout.sleep_session_item, parent, false);
        return new SleepSessionViewHolder(view);
    }

    // bind the data at the given position to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull SleepSessionViewHolder holder, int position) {
        // if the sleepsession list is not null
        if(_sleepSessions.getValue() != null) {
            // get the sleepsession data at the given position
            SleepSession sleepSession = _sleepSessions.getValue().get(position);
            // bind the SleepSession data to the ViewHolder
            holder.bind(sleepSession);
        }
    }


    // count the number of items in the _sleepSessions list
    @Override
    public int getItemCount() {
        if(_sleepSessions.getValue() != null) {
            // return the number of items in the list
            return _sleepSessions.getValue().size();
        }
        return 0;
    }
}


