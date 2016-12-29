package com.liuhesan.app.distributionapp.fragment;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;

/**
 * Created by Tao on 2016/12/10.
 */

public class TotalFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Toolbar mToolbar;
    private TextView ranking,task_orders,task_finishorders,horsemancancel_orders,horsemancancel_details,
            today_orders,today_details,todayincome,todayincome_details,extract,extract_details,mile,
            mile_details,punctuality,delay;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_total, container, false);
        initView();
        return view;
    }

    private void initView() {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.total_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "信息", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        ranking = (TextView) view.findViewById(R.id.total_ranking);
        String str_ranking ="您的当前排名\t\t\t第<font color='#FF0000'>12</font>名";
        ranking.setText(Html.fromHtml(str_ranking));

        //本月任务
        task_orders = (TextView)view.findViewById(R.id.task_orders);
        String str_task_orders ="<font color='#FF0000'>300</font>单";
        task_orders.setText(Html.fromHtml(str_task_orders));
        task_finishorders = (TextView) view.findViewById(R.id.task_finishorders);
        String str_task_finishorders = "已完成<font color='#FF0000'>180</font>单";
        task_finishorders.setText(Html.fromHtml(str_task_finishorders));

        //骑手取消订单
        horsemancancel_orders = (TextView)view.findViewById(R.id.horsemancancel_orders);
        String str_horseman_orders ="<font color='#FF0000'>10</font>单";
        horsemancancel_orders.setText(Html.fromHtml(str_horseman_orders));
        horsemancancel_details = (TextView) view.findViewById(R.id.horsemancancel_details);
        horsemancancel_details.setOnClickListener(this);

        //今日订单
        today_orders = (TextView)view.findViewById(R.id.today_orders);
        String str_todayorder ="<font color='#FF0000'>10</font>单";
        today_orders.setText(Html.fromHtml(str_todayorder));
        today_details = (TextView) view.findViewById(R.id.today_details);
        today_details.setOnClickListener(this);


        //今日收入
        todayincome = (TextView)view.findViewById(R.id.todayincome);
        String str_todayincome ="<font color='#FF0000'>200</font>元";
        todayincome.setText(Html.fromHtml(str_todayincome));
        todayincome_details = (TextView) view.findViewById(R.id.todayincome_details);
        todayincome_details.setOnClickListener(this);

        //累计提成
        extract = (TextView) view.findViewById(R.id.extract);
        String str_extract ="<font color='#FF0000'>100</font>元";
        extract.setText(Html.fromHtml(str_extract));
        extract_details = (TextView) view.findViewById(R.id.extract_details);
        extract_details.setOnClickListener(this);

        //累计里程
        mile = (TextView) view.findViewById(R.id.mile);
        String str_mile ="<font color='#FF0000'>3000</font>km";
        mile.setText(Html.fromHtml(str_mile));
        mile_details = (TextView) view.findViewById(R.id.mile_details);
        mile_details.setOnClickListener(this);

        //工作效率
        punctuality = (TextView) view.findViewById(R.id.punctuality);
        delay = (TextView) view.findViewById(R.id.delay);
        punctuality.setText("准时率\n\r90%");
        delay.setText("延时率\n\r7%");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.horsemancancel_details:
                break;
            case R.id.today_details:
                break;
            case R.id.todayincome_details:
                break;
            case R.id.extract_details:
                break;
            case R.id.mile_details:
                break;
        }
    }
}
