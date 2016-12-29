package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.GlideRoundTransform;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView logo;
    private EditText et_user,et_pwd;
    private Button login;
    private String user,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        logo = (ImageView) findViewById(R.id.logo);
        Resources res=getResources();
        Bitmap bmp=BitmapFactory.decodeResource(res, R.mipmap.logo);
        Bitmap bitmap = GlideRoundTransform.toRoundBitmap(bmp);
        logo.setImageBitmap(bitmap);
        et_user = (EditText) findViewById(R.id.user);
        et_pwd = (EditText) findViewById(R.id.pwd);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                initData();
                break;
        }
    }

    private void initData() {
        if (!TextUtils.isEmpty(et_user.getText()) && !TextUtils.isEmpty(et_pwd.getText())){
            user = et_user.getText().toString().trim();
            pwd = et_pwd.getText().toString().trim();
            OkGo.post(API.BASEURL+"deliver/login/")
                    .tag(this)
                    .params("username",user)
                    .params("password",pwd)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int errno = jsonObject.optInt("errno");
                                if (errno == 300){
                                    Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                                } if (errno == 201){
                                    Toast.makeText(LoginActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
                                }if (errno == 200){  //成功登录
                                   // startService(new Intent(LoginActivity.this, LocationService.class));
                                    JSONObject data = jsonObject.optJSONObject("data");
                                    String id = data.optString("id");
                                    String deliver = data.optString("deliver");
                                    String username = data.optString("username");
                                    String mobile = data.optString("mobile");
                                    String groupid = data.optString("groupid");
                                    String status = data.optString("status");
                                    String addtime = data.optString("addtime");
                                    String isauth = data.optString("isauth");
                                    String groupname = data.optString("groupname");
                                    String shopids = data.optString("shopids");
                                    String cuser_id = data.optString("cuser_id");
                                    String token = data.optString("token");
                                    String cdate = data.optString("cdate");
                                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString("id",id);
                                    edit.putString("deliver",deliver);
                                    edit.putString("pwd",pwd);
                                    edit.putString("username",username);
                                    edit.putString("mobile",mobile);
                                    edit.putString("groupid",groupid);
                                    edit.putString("status",status);
                                    edit.putString("addtime",addtime);
                                    edit.putString("isauth",isauth);
                                    edit.putString("groupname",groupname);
                                    edit.putString("shopids",shopids);
                                    edit.putString("cuser_id",cuser_id);
                                    edit.putString("token",token);
                                    edit.putString("cdate",cdate);
                                    edit.commit();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else {
            Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
