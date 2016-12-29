package com.liuhesan.app.distributionapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.ui.personcenter.MainActivity;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.RemoveItemAnimUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/20.
 */

public class OrderAdapter extends BaseAdapter {
    private List<Order> orders;
    private Context mContext;
    private String token;
    private static final String TAG = "OrderAdapter";
    private MainActivity mainActivity;
    public OrderAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
        mainActivity = new MainActivity();
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
        final ViewHolder mViewHolder ;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_reseize, null);
            mViewHolder = new ViewHolder();

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
            mViewHolder.queryOrder = (Button) convertView.findViewById(R.id.reseizeorder_queryOrder);
            mViewHolder.refuseOrder = (Button) convertView.findViewById(R.id.reseizeorder_refuseorder);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
      //骑手与取送距离
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
       /* AMapLocationClient aMapLocationClient = new AMapLocationClient(mContext);
        double latitude = aMapLocationClient.getLastKnownLocation().getLatitude();
        double longitude =aMapLocationClient.getLastKnownLocation().getLongitude();*/
        double latitude = Double.parseDouble(sharedPreferences.getString("latitude", "0.0"));
        double longitude = Double.parseDouble(sharedPreferences.getString("longitude", "0.0"));
        Log.i(TAG, latitude+"getView: "+longitude);
        LatLng latLng1 = new LatLng(latitude, longitude);
        LatLng latLng2 = new LatLng(orders.get(position).getPoi_lat(), orders.get(position).getPoi_lng());
        LatLng latLng3 = new LatLng(orders.get(position).getLat(), orders.get(position).getLng());
        float distance_shop = AMapUtils.calculateLineDistance(latLng1, latLng2);
        float distance_user = AMapUtils.calculateLineDistance(latLng1, latLng3);
        String str_distance = "我\t\t<font color='#28AAE3'>-" + distance_shop + "km-</font>\t\t取\t\t<font color='#28AAE3'>-" + distance_user + "km-</font>\t\t送";
        mViewHolder.distance.setText(Html.fromHtml(str_distance));
        mViewHolder.shop_name.setText(orders.get(position).getPoi_name());
        mViewHolder.sn.setText(orders.get(position).getSn());
        mViewHolder.address_get.setText(orders.get(position).getPoi_addr());
        mViewHolder.customer_name.setText(orders.get(position).getName());
        mViewHolder.address_send.setText(orders.get(position).getAddr());
        String str_total=null;
        if (orders.get(position).getPaytype() == 1){
            str_total = "订单金额\t\t<font color='#F08C3F'>"+orders.get(position).getPrice()+"元</font>";
            mViewHolder.paystatetwo.setVisibility(View.VISIBLE);
            mViewHolder.paystate.setVisibility(View.GONE);
        }else {
            str_total = "订单金额\t\t<font color='#28AAE3'>"+orders.get(position).getPrice()+"元</font>";
            mViewHolder.paystate.setVisibility(View.VISIBLE);
            mViewHolder.paystatetwo.setVisibility(View.GONE);
        }
        mViewHolder.total.setText(Html.fromHtml(str_total));
        token = sharedPreferences.getString("token","");

        mViewHolder.queryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkGo.post(API.BASEURL+"deliver/queryOrder/")
                        .tag(this)
                        .params("token",token)
                        .params("order_id",orders.get(position).getOrderid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int errno = jsonObject.optInt("errno");
                                    String errmsg = jsonObject.optString("errmsg");
                                    if (errno == 200){
                                        Toast.makeText(mContext,errmsg,Toast.LENGTH_SHORT).show();
                                        orders.remove(position);
                                        notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(mContext,errmsg,Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        final View finalConvertView = convertView;
        mViewHolder.refuseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拒单理由
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("拒单的理由：");
                //    指定下拉列表的显示数据
                final String[] reasons = {"电量不足", "路线不清晰", "订单已达上线", "保温箱不足", "路况不好","其他"};
                //    设置一个下拉的列表选择项
                builder.setItems(reasons, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.i(TAG, reasons[which]+"onClick: ");
                        OkGo.post(API.BASEURL+"deliver/refuseOrder/")
                                .tag(this)
                                .params("token",token)
                                .params("order_id",orders.get(position).getOrderid())
                                .params("reason",reasons[which])
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.i(TAG, s+"refuseonSuccess: ");
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            int errno = jsonObject.optInt("errno");
                                            String errmsg = jsonObject.optString("errmsg");
                                            if (errno == 200){
                                                Toast.makeText(mContext,errmsg,Toast.LENGTH_SHORT).show();
                                                RemoveItemAnimUtils.deletePattern(finalConvertView,position,orders,OrderAdapter.this);
                                            }else {
                                                Toast.makeText(mContext,errmsg,Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                });
                builder.show();
            }
        });

        return convertView;
    }


    class ViewHolder {
       TextView distance,details,shop_name,sn,address_get,customer_name,address_send,total,
               paystate,paystatetwo;
        Button refuseOrder,queryOrder;
    }
}
