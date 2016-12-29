package com.liuhesan.app.distributionapp.adapter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;

import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.ui.personcenter.MainActivity;
import com.liuhesan.app.distributionapp.utility.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.data;
import static com.lzy.okgo.OkGo.getContext;

/**
 * Created by Tao on 2016/12/20.
 */

public class DistributiongOrderAdapter extends BaseAdapter {
    private List<Order> orders;
    private Context mContext;
    private String token;
    private static final String TAG = "DistributiongOrderAdapter";

    private int progress;
    private int progress_out;

    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> map = new HashMap<Integer, View>();

    public DistributiongOrderAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (map.get(position) == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_distributing, null);
            mViewHolder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.distributing_progressbar);
           mViewHolder.intime = (TextView) convertView.findViewById(R.id.intime);
            mViewHolder.outtime = (TextView) convertView.findViewById(R.id.outtime);
            mViewHolder.countdown = (TextView) convertView.findViewById(R.id.countdown);
            mViewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            mViewHolder.details = (TextView) convertView.findViewById(R.id.details);
            mViewHolder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            mViewHolder.sn = (TextView) convertView.findViewById(R.id.sn);
            mViewHolder.address_get = (TextView) convertView.findViewById(R.id.address_get);
            mViewHolder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            mViewHolder.address_send = (TextView) convertView.findViewById(R.id.address_send);
            mViewHolder.total = (TextView) convertView.findViewById(R.id.total);
            mViewHolder.paystate = (TextView) convertView.findViewById(R.id.paystate);
            mViewHolder.paystatetwo = (TextView) convertView.findViewById(R.id.paystatetwo);
            mViewHolder.distributed = (Button) convertView.findViewById(R.id.distributed);
            convertView.setTag(mViewHolder);
            map.put(position, convertView);
        } else {
            convertView = map.get(position);
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //骑手与取送距离
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
       /* double latitude = Double.parseDouble(sharedPreferences.getString("latitude", ""));
        double longitude = Double.parseDouble(sharedPreferences.getString("longitude", ""));

        LatLng latLng1 = new LatLng(latitude, longitude);
        LatLng latLng2 = new LatLng(orders.get(position).getPoi_lat(), orders.get(position).getPoi_lng());
        LatLng latLng3 = new LatLng(orders.get(position).getLat(), orders.get(position).getLng());

        float distance_shop = AMapUtils.calculateLineDistance(latLng1, latLng2);
        float distance_user = AMapUtils.calculateLineDistance(latLng1, latLng3);

        String str_distance = "我\t\t<font color='#28AAE3'>-" + distance_shop + "km-</font>\t\t取\t\t<font color='#28AAE3'>-" + distance_user + "km-</font>\t\t送";
        mViewHolder.distance.setText(Html.fromHtml(str_distance));*/
        // Log.i(TAG, str_distance+"DistributiongOrderAdapter: "+distance_user);
        //倒计时
         final Timer timer = new Timer();
        final long t = System.currentTimeMillis()/1000;
        if (orders.get(position).getPoi_name() != null) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Activity activity = (Activity) mContext;
                    activity.runOnUiThread(new Runnable() {      // UI thread
                        @Override
                        public void run() {
                            progress = mViewHolder.mProgressBar.getProgress();
                            progress = 6 - (int) (System.currentTimeMillis() / 1000 - orders.get(position).getFt());
                            mViewHolder.mProgressBar.setProgress(progress);
                        }
                    });
                }
            }, 1000, 1000);
            //超时计时
            progress = 6 - (int) (System.currentTimeMillis() / 1000 - orders.get(position).getFt());
            if (progress < 0) {
                mViewHolder.outtime.setVisibility(View.VISIBLE);
                mViewHolder.mProgressBar.setVisibility(View.GONE);
                mViewHolder.intime.setVisibility(View.GONE);
                mViewHolder.countdown.setVisibility(View.GONE);
                // TODO: 2016/12/27 登录获取超时时长

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Activity activity = (Activity) mContext;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mViewHolder.outtime.setText("超时时间： "+(0-progress)/60+"分钟");
                            }
                        });

                    }
                }, 1000, 1000);

                //超时提醒
                int i = 1;
                if ((0 - progress) / (60 * i) > 0) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(getContext())
                            .setContentTitle("订单超时提醒")
                            .setContentText("你有订单超时了！！！")
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.icon_get)
                            .setVibrate(new long[]{0, 1000, 1000, 1000})
                            .build();
                    manager.notify(0, notification);
                    i++;
                }
            }
        }
        mViewHolder.shop_name.setText(orders.get(position).getPoi_name());
        mViewHolder.sn.setText(orders.get(position).getSn());
        mViewHolder.address_get.setText(orders.get(position).getPoi_addr());
        mViewHolder.customer_name.setText(orders.get(position).getName());
        mViewHolder.address_send.setText(orders.get(position).getAddr());
        String str_total = null;
        if (orders.get(position).getPaytype() == 1) {
            str_total = "订单金额\t\t<font color='#F08C3F'>" + orders.get(position).getPrice() + "元</font>";
            mViewHolder.paystatetwo.setVisibility(View.VISIBLE);
            mViewHolder.paystate.setVisibility(View.GONE);
        } else {
            str_total = "订单金额\t\t<font color='#28AAE3'>" + orders.get(position).getPrice() + "元</font>";
            mViewHolder.paystate.setVisibility(View.VISIBLE);
            mViewHolder.paystatetwo.setVisibility(View.GONE);
        }
        mViewHolder.total.setText(Html.fromHtml(str_total));
        token = sharedPreferences.getString("token", "");
        mViewHolder.distributed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASEURL + "deliver/completedOrder/")
                        .tag(this)
                        .params("token", token)
                        .params("order_id", orders.get(position).getOrderid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int errno = jsonObject.optInt("errno");
                                    String errmsg = jsonObject.optString("errmsg");
                                    if (errno == 200) {
                                        timer.cancel();
                                        Toast.makeText(mContext, errmsg, Toast.LENGTH_SHORT).show();
                                        orders.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(mContext, errmsg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView distance, details, shop_name, sn, address_get, customer_name, address_send, total,
                paystate, paystatetwo,intime,outtime,countdown;
        Button distributed;
        ProgressBar mProgressBar;
    }
}
