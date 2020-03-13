package com.thiagoalexb.dev.clockin.ui.dayschedules;

import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.ItemEditSheduleBinding;
import com.thiagoalexb.dev.clockin.util.DateHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DayScheduleAdapter extends RecyclerView.Adapter<DayScheduleAdapter.DayScheduleViewHolder> {

    private ArrayList<String> schedules;
    private final EditScheduleViewModel editScheduleViewModel;

    public DayScheduleAdapter(EditScheduleViewModel editScheduleViewModel) {

        this.editScheduleViewModel = editScheduleViewModel;
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

    public class DayScheduleViewHolder extends RecyclerView.ViewHolder {

        private ItemEditSheduleBinding itemEditSheduleBinding;

        public DayScheduleViewHolder(@NonNull ItemEditSheduleBinding itemEditSheduleBinding) {
            super(itemEditSheduleBinding.getRoot());
            this.itemEditSheduleBinding = itemEditSheduleBinding;
        }

        public void bind(String schedule, int position) {

            LocalDateTime localDateTime = LocalDateTime.parse(schedule);

            itemEditSheduleBinding.scheduleValueTextView.setText(DateHelper.getHourMinute(localDateTime));

            itemEditSheduleBinding.editScheduleImageView.setOnClickListener(view -> {

                setTimePickerDialog(view, position);
            });
        }

        private void setTimePickerDialog(View view, final int position){
            LocalDateTime now = LocalDateTime.now();
            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), (timePicker, hourOfDay, minute) -> {
                String entryTime = hourOfDay + ":" + minute;
                editScheduleViewModel.save(position);
            }, now.getHour(), now.getMinute(), true);

            timePickerDialog.show();
        }
    }
}
