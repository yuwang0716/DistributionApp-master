package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.ui.personcenter.CompleteDetailsActivity;
import com.liuhesan.app.distributionapp.ui.personcenter.SystemSettingActivity;
import com.liuhesan.app.distributionapp.ui.personcenter.WorkStateActivity;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.ImageUtils;
import com.liuhesan.app.distributionapp.utility.MyApplication;
import com.liuhesan.app.distributionapp.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Tao on 2016/12/10.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MineFragment";
    private  View view;
    private Toolbar toolbar;
    private TextView finishorders,totalmile,totalmoney,completeDetails,systemSetting,workstate,mine_workstate;
    private static CircleImageView mCircleImageView;
    private Bitmap bitmap;
    private static Activity mContext;
    private static String token;
    private AMapLocationClient locationClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        mContext = getActivity();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        return view;
    }

    private void initView() {

        mCircleImageView = (CircleImageView) view.findViewById(R.id.circle_image);
        mCircleImageView.setOnClickListener(this);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.mipmap.mine_img_middle);

        mine_workstate = (TextView) view.findViewById(R.id.mine_workstate);
        finishorders = (TextView) view.findViewById(R.id.finishorders);
        finishorders.setText("3000单\n\t已完成");
        totalmile = (TextView) view.findViewById(R.id.totalmile);
        totalmile.setText("10145km\n\t累计里程");
        totalmoney = (TextView) view.findViewById(R.id.totalmoney);
        totalmoney.setText("6000元\n\t累计金额");
        //工作状态
        locationClient = new AMapLocationClient(getContext());
        Intent intent = getActivity().getIntent();
        boolean b_start = intent.getBooleanExtra("b_start", false);
        boolean b_rest = intent.getBooleanExtra("b_rest", false);
        boolean b_leave = intent.getBooleanExtra("b_leave", false);
        Log.i(TAG, b_start+"initView: ");
        if (b_start){
            mine_workstate.setText("\t\t工作中");
            //启动定位
            locationClient.startLocation();
        }
        if (b_rest){
            mine_workstate.setText("\t\t临时休息");
        }if (b_leave){
            mine_workstate.setText("\t\t离岗");
            locationClient.stopLocation();
        }
        workstate = (TextView) view.findViewById(R.id.workstate);
        completeDetails = (TextView) view.findViewById(R.id.complete_details);
        systemSetting = (TextView) view.findViewById(R.id.systemsetting);
        completeDetails.setOnClickListener(this);
        systemSetting.setOnClickListener(this);
        workstate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complete_details:
                startActivityForResult(new Intent(MyApplication.getInstance(), CompleteDetailsActivity.class),10);
                break;
            case R.id.systemsetting:
                startActivity(new Intent(MyApplication.getInstance(),SystemSettingActivity.class));
                break;
            case R.id.circle_image:// TODO: 2016/12/22 头像先判断登录时是否已有头像
                ImageUtils.takeOrChoosePhoto(mContext, ImageUtils.TAKE_OR_CHOOSE_PHOTO);
                break;
            case R.id.workstate:
                Log.i(TAG, "onClick: ");
                startActivity(new Intent(MyApplication.getInstance(), WorkStateActivity.class));
                break;
        }
    }
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != RESULT_OK) {
        return;
    }
    switch (requestCode) {
        case ImageUtils.TAKE_OR_CHOOSE_PHOTO:
            //获取到了原始图片
            File f = ImageUtils.getPhotoFromResult(MyApplication.getInstance(), data);
            //裁剪方法
            ImageUtils.doCropPhoto(mContext, f);
            break;
        case ImageUtils.PHOTO_PICKED_WITH_DATA:
            //获取到剪裁后的图片
            bitmap = ImageUtils.getCroppedImage();
            mCircleImageView.setImageBitmap(bitmap);
            File file = ImageUtils.saveImage(bitmap);
            List<File> files = new ArrayList<>();
            files.add(file);
            OkGo.post(API.BASEURL + "deliver/img_upload")
                    .tag(this)
                    .params("token", token)
                    .addFileParams("file", files)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i(TAG, s + "onSuccess: ");
                        }
                    });
            break;
    }
}

}
