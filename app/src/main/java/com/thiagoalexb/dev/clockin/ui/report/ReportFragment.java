package com.thiagoalexb.dev.clockin.ui.report;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentReportBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.service.ReportService;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.main.ScheduleAdapter;
import com.thiagoalexb.dev.clockin.util.Resource;

import java.io.File;
import java.net.URLConnection;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

public class ReportFragment extends BaseFragment {

    private static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGE_CODE = 771;

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
        if(schedules == null || (schedules != null && schedules.size() == 0)) {
            fragmentReportBinding.printFloatingActionButton.hide();
            return;
        }

        fragmentReportBinding.printFloatingActionButton.show();
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

        fragmentReportBinding.printFloatingActionButton.setOnClickListener(view -> {
            if(!hasLocationPermission())
                getExternalStoragePermission();
            else{
                reportViewModel.print();
                try{
                    Thread.sleep(1000);
                    shareFile(ReportService.csv);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
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

    private void getExternalStoragePermission() {
        if (!hasLocationPermission())
            requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_EXTERNAL_STORAGE_CODE);
    }

    private boolean hasLocationPermission(){
        int permissionExternalStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionExternalStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void shareFile(String path) {

        File file = new File(path);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://"+file.getAbsolutePath()));

        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Horarios");

        startActivity(Intent.createChooser(intentShareFile, "Share File"));

    }
}
