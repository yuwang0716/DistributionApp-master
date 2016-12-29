package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Tab;
import com.liuhesan.app.distributionapp.fragment.MineFragment;
import com.liuhesan.app.distributionapp.fragment.OrderFragment;
import com.liuhesan.app.distributionapp.fragment.TotalFragment;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.LocationUtils;
import com.liuhesan.app.distributionapp.widget.FragmentTabHost;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private LayoutInflater mInflater;
    private FragmentTabHost mTabhost;
    private List<Tab> mTabs = new ArrayList<>(3);
    private MineFragment mineFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AMapLocationClient aMapLocationClient = new AMapLocationClient(this.getApplicationContext());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                location();
            }
        }, 1000, 30000);

        setContentView(R.layout.activity_main);

        //设置colorPrimaryDark图片
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initTab();
    }

    private void location() {
        LocationUtils.initLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String latitude = Double.toString(aMapLocation.getLatitude());
                        String longitude = Double.toString(aMapLocation.getLongitude());
                        Log.i("TAGlatitude", latitude + "onLocationChanged: ");
                        if (!TextUtils.isEmpty(latitude)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("latitude", latitude);
                            edit.putString("longitude", longitude);
                            String json = "[ { \"latitude\": \""+latitude+", \"longitude\": \""+longitude+"}]";
                            String token = sharedPreferences.getString("token", "");
                            OkGo.post(API.BASEURL+"deliver/logSteps/")
                                    .tag(this)
                                    .upJson(json)
                                    .params("token",token)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.i(TAG, s+"onSuccess: ");
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }

    private void initTab() {

        mineFragment = new MineFragment();
        Tab tab_order = new Tab(OrderFragment.class,R.string.order,R.drawable.selector_icon_order);
        Tab tab_total = new Tab(TotalFragment.class,R.string.total,R.drawable.selector_icon_total);
        Tab tab_mine = new Tab(MineFragment.class,R.string.mine,R.drawable.selector_icon_mine);

        mTabs.add(tab_order);
        mTabs.add(tab_total);
        mTabs.add(tab_mine);



        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec,tab.getFragment(),null);

        }

        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);


    }


    private View buildIndicator(Tab tab){

        View view =mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return  view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mineFragment.onActivityResult(requestCode,resultCode,data);
    }

}
