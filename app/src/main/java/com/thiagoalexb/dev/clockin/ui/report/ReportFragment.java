package com.thiagoalexb.dev.clockin.ui.report;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;
import com.thiagoalexb.dev.clockin.util.Resource;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_EXTERNAL_STORAGE_CODE && hasExternalStoragePermission())
            reportViewModel.print();
        else
            getExternalStoragePermission();
    }

    private void setObservers() {
        reportViewModel.getSchedules().removeObservers(getViewLifecycleOwner());
        reportViewModel.getSchedules().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status);
            validateSchedules(resource.data);
        });

        reportViewModel.getSchedulesResource().removeObservers(getViewLifecycleOwner());
        reportViewModel.getSchedulesResource().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status);
            if(resource.status == Resource.Status.LOADING || resource.data == null) return;
            shareFile(ReportService.csv);
        });
    }

    private void validateSchedules(List<Schedule> schedules){
        if(schedules == null || schedules.size() == 0) {
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

            if(!hasExternalStoragePermission())
                getExternalStoragePermission();
            else
                reportViewModel.print();
        });
    }

    private void createYearDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        final List<Integer> list = reportViewModel.getYears().getValue();
        String[] years = new String[Objects.requireNonNull(list).size()];

        for (int i = 0; i < list.size(); i++) {
            years[i] = String.valueOf(list.get(i));
        }

        if(years.length == 0) {
            fragmentReportBinding.yearTextView.clearFocus();
            return;
        }

        alertDialog.setTitle(getString(R.string.year));

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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        final List<String> list = reportViewModel.getMonthNames().getValue();
        String[] months = Objects.requireNonNull(list).toArray(new String[list.size()]);

        if(months.length == 0) {
            fragmentReportBinding.monthTextView.clearFocus();
            return;
        };

        alertDialog.setTitle(getString(R.string.month));

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
        if (hasExternalStoragePermission()) return;

        requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_EXTERNAL_STORAGE_CODE);
    }

    private boolean hasExternalStoragePermission(){
        int permissionExternalStorage = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionExternalStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void shareFile(String path) {

        File file = new File(path);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));

        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));

        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.schedules));

        startActivity(Intent.createChooser(intentShareFile, getString(R.string.schedules_email_title)));

    }
}
