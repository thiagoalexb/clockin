package com.thiagoalexb.dev.clockin.ui.dayschedules;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiagoalexb.dev.clockin.R;

public class DayScheduleFragment extends Fragment {

    public DayScheduleFragment() {
    }

    public static final String ARG_OBJECT = "object";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        TextView textView = (TextView) view.findViewById(R.id.kkkkktext);

        textView.setText(Integer.toString(args.getInt(ARG_OBJECT)));
    }
}
