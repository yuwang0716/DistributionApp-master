package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.DataCleanManager;

public class SystemSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView changPwd;
    private ImageButton back;
    private TextView clearData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.systemsetting_back);
        changPwd = (TextView) findViewById(R.id.systemsetting_changepwd);
        clearData = (TextView) findViewById(R.id.systemsetting_clearcache);
        try {
            clearData.setText(DataCleanManager.getTotalCacheSize(SystemSettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        back.setOnClickListener(this);
        changPwd.setOnClickListener(this);
        clearData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.systemsetting_back:
                finish();
                break;
            case R.id.systemsetting_changepwd:
                startActivity(new Intent(SystemSettingActivity.this, ChangePwdActivity.class));
                break;
            case R.id.systemsetting_clearcache:
                DataCleanManager.clearAllCache(SystemSettingActivity.this);
                clearData.setText("0.0kb");
                break;
        }
    }
}
