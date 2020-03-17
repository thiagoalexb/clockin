package com.thiagoalexb.dev.clockin.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.ItemScheduleBinding;
import com.thiagoalexb.dev.clockin.util.DateHelper;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    public static final String ID_KEY = "ID_KEY";
    private List<Schedule> schedules;
    private Boolean showEdit;

    public ScheduleAdapter(Boolean showEdit){
        this.showEdit = showEdit;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemScheduleBinding itemScheduleBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),  R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(itemScheduleBinding, showEdit);
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

        private ItemScheduleBinding itemScheduleBinding;
        private Boolean showEdit;

        public ScheduleViewHolder(@NonNull ItemScheduleBinding itemScheduleBinding, Boolean showEdit) {
            super(itemScheduleBinding.getRoot());
            this.itemScheduleBinding = itemScheduleBinding;
            this.showEdit = showEdit;
        }

        public void bind(Schedule schedule){

            itemScheduleBinding.dayTextView.setText(DateHelper.getMediumDate(schedule.getDate()));
            itemScheduleBinding.entryValueTextView.setText(DateHelper.getHourMinute(schedule.getEntryTimes().get(0)));
            ArrayList<String> departureTimes = schedule.getDepartureTimes();
            if(departureTimes != null)
                itemScheduleBinding.departureValueTextView.setText(DateHelper.getHourMinute(departureTimes.get(departureTimes.size() - 1)));

            if(showEdit)
                itemScheduleBinding.editScheduleImageView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ID_KEY, schedule.getId());
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_daySchedulesFragment, bundle);
                });
            else{
                itemScheduleBinding.scheduleInfoConstraint.setVisibility(View.VISIBLE);
                itemScheduleBinding.entriesValuesTextView.setText(String.join("\n", schedule.getEntryTimesFormatted()));

                if(departureTimes != null)
                    itemScheduleBinding.departuresValuesTextView.setText(String.join("\n", schedule.getDepartureTimesFormatted()));

                itemScheduleBinding.editScheduleImageView.setVisibility(View.GONE);
            }

        }
    }
}
