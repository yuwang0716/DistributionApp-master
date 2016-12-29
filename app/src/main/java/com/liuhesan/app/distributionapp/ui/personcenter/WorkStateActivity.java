package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.liuhesan.app.distributionapp.R;

public class WorkStateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WorkStateActivity";
    private RadioButton start,rest,leave;
    boolean b_start,b_rest,b_leave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_state);
        initView();
    }

    private void initView() {
        start = (RadioButton) findViewById(R.id.workstate_start);
        rest = (RadioButton) findViewById(R.id.workstate_rest);
        leave = (RadioButton) findViewById(R.id.workstate_leave);
        start.setOnClickListener(this);
        rest.setOnClickListener(this);
        leave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.workstate_start:
                b_start = start.isChecked();
                break;
            case R.id.workstate_rest:
                b_rest = rest.isChecked();
                break;
            case R.id.workstate_leave:
                b_leave = leave.isChecked();
                break;
        }
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("b_start",b_start);
        intent.putExtra("b_rest",b_rest);
        intent.putExtra("b_leave",b_leave);
        startActivity(intent);
        finish();
    }
}
