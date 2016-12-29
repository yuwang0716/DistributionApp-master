package com.liuhesan.app.distributionapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.DateChooseWheelViewDialog;
import com.lzy.okgo.OkGo;

/**
 * Created by Tao on 2016/12/17.
 */
public class FinishOrderFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tv_startdate,tv_enddate;
    private Button all,outtime,intime;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_more_finishorder, container, false);
        initView();
        return view;
    }

    private void initView() {
        tv_startdate = (TextView) view.findViewById(R.id.date_start);
        tv_enddate = (TextView) view.findViewById(R.id.date_end);
        all = (Button) view.findViewById(R.id.all);
        outtime = (Button) view.findViewById(R.id.outtime);
        intime = (Button) view.findViewById(R.id.intime);
        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_start:
            DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(getActivity(), new DateChooseWheelViewDialog.DateChooseInterface() {
                @Override
                public void getDateTime(String time, boolean longTimeChecked) {
                    tv_startdate.setText(time);
                }
            });
            startDateChooseDialog.setDateDialogTitle("开始时间");
            startDateChooseDialog.setTimePickerGone(true);
            startDateChooseDialog.showDateChooseDialog();
            break;
            case R.id.date_end://结束时间
                DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(getActivity(),
                        new DateChooseWheelViewDialog.DateChooseInterface() {
                            @Override
                            public void getDateTime(String time, boolean longTimeChecked) {
                                tv_enddate.setText(time);
                            }
                        });
                endDateChooseDialog.setTimePickerGone(true);
                endDateChooseDialog.setDateDialogTitle("结束时间");
                endDateChooseDialog.showDateChooseDialog();
                break;
            case R.id.all:
                // TODO: 2016/12/24 查询订单
              /* OkGo.post(API.BASEURL+"deliver/completedOrder")
                        .tag(this)
                        .params()*/
                break;
            case R.id.outtime:
                break;
            case R.id.intime:
                break;
        }
    }
}
