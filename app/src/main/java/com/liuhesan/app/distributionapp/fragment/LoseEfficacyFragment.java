package com.liuhesan.app.distributionapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuhesan.app.distributionapp.R;

/**
 * Created by Tao on 2016/12/17.
 */
public class LoseEfficacyFragment extends Fragment{
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_more_loseefficacy, container, false);
        return view;
    }
}
