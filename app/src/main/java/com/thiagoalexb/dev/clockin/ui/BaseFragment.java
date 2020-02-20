package com.thiagoalexb.dev.clockin.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.thiagoalexb.dev.clockin.R;

import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    LinearLayout containerLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        containerLoading = getActivity().findViewById(R.id.container_loading_linear_layout);
    }

    protected void setLoading(Boolean isVisible){
        containerLoading.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
