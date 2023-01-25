package com.example.sleeptrackerproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class SleepSessionAdapter extends RecyclerView.Adapter<SleepSessionViewHolder> {

    private LiveData<List<SleepSession>> _sleepSessions;

    public SleepSessionAdapter(LiveData<List<SleepSession>> sleepSessions) {
        _sleepSessions = sleepSessions;
    }

    @NonNull
    @Override
    public SleepSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sleep_session_item, parent, false);
        return new SleepSessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SleepSessionViewHolder holder, int position) {
        if(_sleepSessions.getValue() != null) {
            SleepSession sleepSession = _sleepSessions.getValue().get(position);
            holder.bind(sleepSession);
        }
    }

    @Override
    public int getItemCount() {
        if(_sleepSessions.getValue() != null) {
            return _sleepSessions.getValue().size();
        }
        return 0;
    }
}


