package com.thiagoalexb.dev.clockin.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> schedules;

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.bind(schedule);
    }

    @Override
    public int getItemCount() {
        return schedules == null ? 0 : schedules.size();
    }

    public void setSchedules(List<Schedule> schedules){
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView day;
        TextView entryValue;
        TextView departureValue;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day_text_view);
            entryValue = itemView.findViewById(R.id.entry_value_text_view);
            departureValue = itemView.findViewById(R.id.departure_value_text_view);
        }

        public void bind(Schedule schedule){
            String dateFormatted = schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            day.setText(dateFormatted);
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm");
            String entryTimeFormatted = schedule.getEntryTime().format(pattern);
            entryValue.setText(entryTimeFormatted);
            if(schedule.getDepartureTime() != null)
                departureValue.setText(schedule.getDepartureTime().format(pattern));
        }
    }
}
