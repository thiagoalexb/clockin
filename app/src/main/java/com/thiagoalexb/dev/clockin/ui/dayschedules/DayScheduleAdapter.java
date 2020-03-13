package com.thiagoalexb.dev.clockin.ui.dayschedules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.ItemEditSheduleBinding;
import com.thiagoalexb.dev.clockin.databinding.ItemScheduleBinding;
import com.thiagoalexb.dev.clockin.util.DateHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DayScheduleAdapter extends RecyclerView.Adapter<DayScheduleAdapter.DayScheduleViewHolder> {

    public static final String POSITION_KEY = "POSITION_KEY";
    public static final String SCHEDULE_ID_KEY = "SCHEDULE_ID_KEY";
    public static final String IS_ENTRY_KEY = "IS_ENTRY_KEY";
    private ArrayList<String> schedules;
    private int scheduleId;
    private boolean isEntry;

    public DayScheduleAdapter() {

    }

    @NonNull
    @Override
    public DayScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemEditSheduleBinding itemEditSheduleBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_edit_shedule, parent, false);
        return new DayScheduleViewHolder(itemEditSheduleBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayScheduleViewHolder holder, int position) {
        if(schedules == null || schedules.size() == 0) return;

        String schedule = schedules.get(position);
        holder.bind(schedule, position);
    }

    @Override
    public int getItemCount() {
        return schedules == null ? 0 : schedules.size();
    }

    public void setSchedules(ArrayList<String> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    public void setArguments(int scheduleId, boolean isEntry){
        this.scheduleId = scheduleId;
        this.isEntry = isEntry;
    }

    public class DayScheduleViewHolder extends RecyclerView.ViewHolder {

        private ItemEditSheduleBinding itemEditSheduleBinding;

        public DayScheduleViewHolder(@NonNull ItemEditSheduleBinding itemEditSheduleBinding) {
            super(itemEditSheduleBinding.getRoot());
            this.itemEditSheduleBinding = itemEditSheduleBinding;        }

        public void bind(String schedule, int position) {

            LocalDateTime localDateTime = LocalDateTime.parse(schedule);

            itemEditSheduleBinding.scheduleValueTextView.setText(DateHelper.getHourMinute(localDateTime));

            itemEditSheduleBinding.editScheduleImageView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt(POSITION_KEY, position);
                bundle.putInt(SCHEDULE_ID_KEY, scheduleId);
                bundle.putBoolean(IS_ENTRY_KEY, isEntry);
                Navigation.findNavController(v).navigate(R.id.action_daySchedulesFragment_to_editScheduleFragment, bundle);
            });
        }
    }
}
