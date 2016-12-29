package com.liuhesan.app.distributionapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.GotOrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.ReseizeOrderJson;
import com.liuhesan.app.distributionapp.utility.TakingOrderJson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/10.
 */

public class TakingGoodsFragment extends Fragment {
    private final static  String TAG = "TakingGoodsFragment";
    private View view;
    private ListView mListView;
    private List<Order> orders;
    private List<Order> getOrders;
    private GotOrderAdapter gotOrderAdapter;
    private MaterialRefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_takinggoods, container, false);
        initView();
        initData();
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData();
                materialRefreshLayout.finishRefresh();
            }
        });
        return view;
    }

    private void initView() {
        mListView = (ListView) view.findViewById(R.id.takinggoods_listview);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        orders = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    private void initData() {
        OkGo.post(API.BASEURL+"deliver/getOrder")
                .tag(this)
                .params("token",sharedPreferences.getString("token",""))
                .params("status","2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(TAG, s+"onSuccess: ");
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            orders =  ReseizeOrderJson.getData(s);
                            if (errno == 200) {
                                JSONObject data = jsonObject.optJSONObject("data");
                                JSONArray list = data.optJSONArray("list");
                                if (list != null) {
                                    if (gotOrderAdapter == null) {
                                        orders = TakingOrderJson.getData(list);
                                        Log.i(TAG, TakingOrderJson.getData(list)+"1onSuccess: "+list);
                                        gotOrderAdapter = new GotOrderAdapter(orders, getContext());
                                        mListView.setAdapter(gotOrderAdapter);
                                    } else {
                                        getOrders = TakingOrderJson.getData(list);

                                        gotOrderAdapter = new GotOrderAdapter(getOrders, getContext());
                                        mListView.setAdapter(gotOrderAdapter);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
