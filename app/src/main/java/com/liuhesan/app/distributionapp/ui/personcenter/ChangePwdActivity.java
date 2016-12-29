package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class ChangePwdActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_oldpwd,et_newpwd,et_newpwdtwo;
    private String oldpwd,newpwd;
    private Button sure;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        initView();
    }

    private void initView() {
        et_oldpwd = (EditText) findViewById(R.id.oldpwd);
        et_newpwd = (EditText) findViewById(R.id.newpwd);
        et_newpwdtwo = (EditText) findViewById(R.id.newpwdtwo);
        back = (ImageButton) findViewById(R.id.changepwd_back);
        sure = (Button) findViewById(R.id.sure);
        sure.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changepwd_back:
                finish();
                break;
            case R.id.sure:
                final SharedPreferences sharedPreferences = ChangePwdActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                String pwd = sharedPreferences.getString("pwd", "");
                if (TextUtils.isEmpty(et_oldpwd.getText()) || TextUtils.isEmpty(et_newpwd.getText())||TextUtils.isEmpty(et_newpwdtwo.getText())){
                    Toast.makeText(ChangePwdActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (!pwd.equals(et_oldpwd.getText().toString().trim())){
                    Toast.makeText(ChangePwdActivity.this,"原始密码输入错误",Toast.LENGTH_SHORT).show();
                }else if (pwd.equals(et_newpwd.getText().toString().trim())){
                    Toast.makeText(ChangePwdActivity.this,"原始密码不能新密码相同",Toast.LENGTH_SHORT).show();
                }else if (!checkString(et_newpwd.getText().toString().trim())){
                    Toast.makeText(ChangePwdActivity.this,"密码只允许由数字、字母、下划线和.组成6位到8位密码",Toast.LENGTH_SHORT).show();
                }
                else if (!et_newpwd.getText().toString().trim().equals(et_newpwdtwo.getText().toString().trim())){
                    Toast.makeText(ChangePwdActivity.this,"两次新密码输入必须一致",Toast.LENGTH_SHORT).show();
                }else {
                    oldpwd = et_oldpwd.getText().toString().trim();
                    newpwd = et_newpwd.getText().toString().trim();
                    Log.i("TAG111",sharedPreferences.getString("token","")+"onSuccess: ");
                    OkGo.post(API.BASEURL+"deliver/editPassword/")
                            .tag(this)
                            .params("token",sharedPreferences.getString("token",""))
                            .params("oldpass",oldpwd)
                            .params("password",pwd)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        int errno = jsonObject.optInt("errno");
                                        if (errno == 200){
                                            Toast.makeText(ChangePwdActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ChangePwdActivity.this,LoginActivity.class));
                                            sharedPreferences.edit().clear().commit();
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                }
                break;
        }
    }
    private boolean checkString(String s) {
        return s.matches("^([0-9A-Za-z_.]{6,8})$");
        }
}
