package com.thiagoalexb.dev.clockin.ui.report;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.FragmentReportBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;

import java.time.LocalDateTime;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ReportFragment extends DaggerFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;

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

        this.setEvents();

        return  fragmentReportBinding.getRoot();
    }

    private void setEvents(){


        fragmentReportBinding.yearTextView.setOnFocusChangeListener((view, hasFocus) ->{
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(fragmentReportBinding.yearTextView.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(fragmentReportBinding.monthTextView.getWindowToken(), 0);

            if(!hasFocus) return;
            setDialog(view);
        });

        fragmentReportBinding.monthTextView.setOnFocusChangeListener((view, hasFocus) ->{
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(fragmentReportBinding.yearTextView.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(fragmentReportBinding.monthTextView.getWindowToken(), 0);
            
            if(!hasFocus) return;
            setDialog(view);
        });
    }

    private void setDialog(View view){

        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setTitle("Example");
        String[] types = {"By Zip", "By Category"};
        b.setItems(types, (dialog, which) -> {

            dialog.dismiss();
            switch(which){
                case 0:
                    break;
                case 1:
                    break;
            }
        });

        b.setOnDismissListener(dialog -> {
            fragmentReportBinding.yearTextView.clearFocus();
            fragmentReportBinding.monthTextView.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(fragmentReportBinding.yearTextView.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(fragmentReportBinding.monthTextView.getWindowToken(), 0);
        });

        b.show();
    }
}
