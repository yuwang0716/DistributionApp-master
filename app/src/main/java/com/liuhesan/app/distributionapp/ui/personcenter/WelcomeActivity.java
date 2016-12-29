package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.liuhesan.app.distributionapp.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (TextUtils.isEmpty(username)){
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            finish();
        }else {
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            finish();
        }
    }
}
