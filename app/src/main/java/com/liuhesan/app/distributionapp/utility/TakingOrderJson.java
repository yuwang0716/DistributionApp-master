package com.liuhesan.app.distributionapp.utility;

import android.text.TextUtils;

import com.liuhesan.app.distributionapp.bean.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao on 2016/12/22.
 */

public class TakingOrderJson {
    private static List<Order> orders;
    public static List<Order>  getData(JSONArray list){
        orders = new ArrayList<>();
            for (int i = 0; i < list.length() ; i++) {
                Order order = new Order();
                JSONObject jsonObject = list.optJSONObject(i);
                String sn = jsonObject.optString("sn");
                order.setSn(sn);
                String orderid = jsonObject.optString("orderid");
                order.setOrderid(orderid);
                String poi_name = jsonObject.optString("poi_name");
                order.setPoi_name(poi_name);
                String poi_addr = jsonObject.optString("poi_addr");
                order.setPoi_addr(poi_addr);
                String name = jsonObject.optString("name");
                order.setName(name);
                String addr = jsonObject.optString("addr");
                order.setAddr(addr);
                double poi_lat = jsonObject.optDouble("poi_lat");
                order.setPoi_lat(poi_lat);
                double poi_lng = jsonObject.optDouble("poi_lng");
                order.setPoi_lng(poi_lng);
                double lat = jsonObject.optDouble("lat");
                order.setLat(lat);
                double lng = jsonObject.optDouble("lng");
                order.setLng(lng);
                String price = jsonObject.optString("price");
                order.setPrice(price);
                long ft = jsonObject.optLong("ft");
                order.setFt(ft);
                orders.add(order);
            }
        return orders;
    }
}
