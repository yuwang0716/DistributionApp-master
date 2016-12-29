package com.liuhesan.app.distributionapp.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;


import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.OrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.ui.personcenter.MainActivity;
import com.liuhesan.app.distributionapp.utility.ReseizeOrderJson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Tao on 2016/12/10.
 */

public class ReseizeOrderFragment extends Fragment {
    private View view;
    private ListView mListView;
    private List<Order> orders;
    private OrderAdapter orderAdapter;
    private Set set;
    private Bundle saveState;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_reseizeorder, container, false);
        initView();
        JPushInterface.init(getActivity().getApplicationContext());
        registerMessageReceiver();  // used for receive msg
        return view;
    }

    private void initView() {
        mListView = (ListView) view.findViewById(R.id.reseizeorder_listview);
        orders = new ArrayList<>();
        set = new HashSet();
    }

    private void initData() {
        AnimationSet set = new AnimationSet(false);
       Animation animation = new TranslateAnimation(1, 13, 10, 50);  //ScaleAnimation 控制尺寸伸缩的动画效果
        animation.setDuration(300);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 1);
        mListView.setLayoutAnimation(controller);   //ListView 设置动画效果
        if (orderAdapter == null){
            orderAdapter = new OrderAdapter(orders,getContext());
            mListView.setAdapter(orderAdapter);
        }else {
            orderAdapter.notifyDataSetChanged();
        }


    }
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.liuhesan.app.distributionapp.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                setCostomMsg(messge);
            }
        }
    }
    private void setCostomMsg(String msg){
        Log.i("JPush111",msg+ "setCostomMsg: ");
        List<Order> data = ReseizeOrderJson.getData(msg);
        if (set.add(data.get(0).getSn())){
           orders.addAll(data);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(getContext(),0,intent,0);
            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(getContext())
                    .setContentTitle("又有新订单了!!")
                    .setContentText(data.get(0).getName())
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.icon_get)
                    .setVibrate(new long[]{0,1000,1000,1000})
                    .build();
            notification.sound=Uri.parse("android.resource://" + getActivity().getPackageName() + "/" +R.raw.new_order);
            manager.notify(Integer.parseInt(data.get(0).getSn()),notification);
        }
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

}