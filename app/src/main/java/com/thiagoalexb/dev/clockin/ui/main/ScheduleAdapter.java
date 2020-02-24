package com.thiagoalexb.dev.clockin.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false), showEdit);
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
        ImageView editSchedule;
        Boolean showEdit;


        public ScheduleViewHolder(@NonNull View itemView, Boolean showEdit) {
            super(itemView);
            day = itemView.findViewById(R.id.day_text_view);
            entryValue = itemView.findViewById(R.id.entry_value_text_view);
            departureValue = itemView.findViewById(R.id.departure_value_text_view);
            editSchedule = itemView.findViewById(R.id.edit_schedule_image_view);
            this.showEdit = showEdit;
        }

        public void bind(Schedule schedule){
           String dateFormatted = schedule.getDate().format(
                    DateTimeFormatter.ofLocalizedDate( FormatStyle.MEDIUM )
                            .withLocale(new Locale("pt", "br")));

            day.setText(dateFormatted);
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm");
            String entryTimeFormatted = schedule.getEntryTime().format(pattern);
            entryValue.setText(entryTimeFormatted);
            if(schedule.getDepartureTime() != null)
                departureValue.setText(schedule.getDepartureTime().format(pattern));

            if(showEdit)
                editSchedule.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ID_KEY, schedule.getId());
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_editScheduleFragment, bundle);
                });
            else
                editSchedule.setVisibility(View.GONE);
        }
    }
}
