package com.liuhesan.app.distributionapp.ui.personcenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.liuhesan.app.distributionapp.R;

public class CommonActivity extends AppCompatActivity {
    private CheckBox news,sound,shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initView();
    }

    private void initView() {
        news = (CheckBox) findViewById(R.id.common_news);
        sound = (CheckBox) findViewById(R.id.common_sound);
        shake = (CheckBox) findViewById(R.id.common_shake);
    }
}
