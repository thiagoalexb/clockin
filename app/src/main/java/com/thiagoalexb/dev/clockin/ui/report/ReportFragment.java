package com.thiagoalexb.dev.clockin.ui.report;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentReportBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.main.ScheduleAdapter;
import com.thiagoalexb.dev.clockin.util.Resource;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

public class ReportFragment extends BaseFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    @Inject
    @Named("search")
    public ScheduleAdapter scheduleAdapter;

    public ReportViewModel reportViewModel;

    private FragmentReportBinding fragmentReportBinding;

    public ReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reportViewModel = ViewModelProviders.of(this, modelProviderFactory).get(ReportViewModel.class);

        fragmentReportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false);

        fragmentReportBinding.setReportViewModel(reportViewModel);

        fragmentReportBinding.setLifecycleOwner(this);

        this.setObservers();

        this.setEvents();

        this.setElements();

        return  fragmentReportBinding.getRoot();
    }

    private void setObservers() {
        reportViewModel.getSchedules().removeObservers(getViewLifecycleOwner());
        reportViewModel.getSchedules().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status == Resource.Status.LOADING);
            validateSchedules(resource.data);
        });
    }

    private void validateSchedules(List<Schedule> schedules){
        if(schedules == null) return;

        scheduleAdapter.setSchedules(schedules);
    }

    private void setEvents(){

        fragmentReportBinding.yearTextView.setOnFocusChangeListener((view, hasFocus) ->{
            if(!hasFocus) return;
            createYearDialog();
        });

        fragmentReportBinding.monthTextView.setOnFocusChangeListener((view, hasFocus) ->{
            if(!hasFocus) return;
            createMonthDialog();
        });
    }

    private void setElements() {
        scheduleAdapter.setSchedules(new ArrayList<>());
        fragmentReportBinding.schedulesRecyclerView.setAdapter(scheduleAdapter);
    }

    private void createYearDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        final List<Integer> list = reportViewModel.getYears().getValue();
        String[] years = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            years[i] = String.valueOf(list.get(i));
        }

        alertDialog.setItems(years, (dialog, which) -> {

            dialog.dismiss();
            reportViewModel.setYear(list.get(which));
            fragmentReportBinding.yearTextView.setText(years[which]);
        });

        alertDialog.setOnDismissListener(dialog -> {
            fragmentReportBinding.yearTextView.clearFocus();
        });

        alertDialog.show();
    }

    private void createMonthDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        final List<String> list = reportViewModel.getMonthNames().getValue();
        String[] months = list.toArray(new String[list.size()]);
        alertDialog.setItems(months, (dialog, which) -> {

            dialog.dismiss();
            reportViewModel.setMonth(which);
            fragmentReportBinding.monthTextView.setText(list.get(which));
        });

        alertDialog.setOnDismissListener(dialog -> {
            fragmentReportBinding.monthTextView.clearFocus();
        });

        alertDialog.show();
    }
}
