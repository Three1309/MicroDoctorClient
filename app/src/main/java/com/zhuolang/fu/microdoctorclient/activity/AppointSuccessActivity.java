package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuolang.fu.microdoctorclient.R;


/**
 * Created by hzg on 2016/11/6.
 */
public class AppointSuccessActivity extends Activity {
    private ImageView img_appoint_success_back;
    private String disease;
    private String doctorName;
    private String userName;
    private String seeTime;

    private TextView tv_disease;
    private TextView tv_doctorName;
    private TextView tv_userName;
    private TextView tv_seeTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_success);

        img_appoint_success_back = (ImageView) findViewById(R.id.img_appoint_success_back);
        tv_disease= (TextView) findViewById(R.id.tv_appoint_dis);
        tv_doctorName= (TextView) findViewById(R.id.tv_appoint_doctornam);
        tv_userName= (TextView) findViewById(R.id.tv_appoint_user_nam);
        tv_seeTime= (TextView) findViewById(R.id.tv_appoint_seetim);

        disease=getIntent().getStringExtra("disease");
        userName=getIntent().getStringExtra("userName");
        seeTime=getIntent().getStringExtra("seeTime");
        doctorName=getIntent().getStringExtra("doctorName");

        tv_disease.setText(disease);
        tv_doctorName.setText(doctorName);
        tv_userName.setText(userName);
        tv_seeTime.setText(seeTime);
        img_appoint_success_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
