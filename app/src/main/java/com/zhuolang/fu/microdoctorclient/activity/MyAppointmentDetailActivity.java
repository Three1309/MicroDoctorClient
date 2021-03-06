package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.Date;

/**
 * Created by hzg on 2016/11/3.
 */
public class MyAppointmentDetailActivity extends Activity {

    private String doctorDtoStr;
    private TextView tv_appoint_doctorName;
    private TextView tv_appoint_seeTime;
    private TextView tv_appoint_disease;
    private TextView tv_appoint_dNumber;
    private TextView tv_appoint_doctorsay;
    private TextView tv_appoint_datetime;
    private ImageView imageViewback;

    private AppointmentDto appointmentDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointdetails);
        tv_appoint_doctorName = (TextView) findViewById(R.id.tv_appoint_doctorName);
        tv_appoint_seeTime = (TextView) findViewById(R.id.tv_appoint_seeTime);
        tv_appoint_disease = (TextView) findViewById(R.id.tv_appoint_disease);
        tv_appoint_dNumber = (TextView) findViewById(R.id.tv_appoint_dNumber);
        tv_appoint_doctorsay = (TextView) findViewById(R.id.tv_appoint_doctorsay);
        tv_appoint_datetime = (TextView) findViewById(R.id.tv_appoint_datetime);
        imageViewback = (ImageView) findViewById(R.id.img_doctorList_back);

        Gson gson = new Gson();
        doctorDtoStr = getIntent().getStringExtra("doctorDtoStr");
        appointmentDto = gson.fromJson(doctorDtoStr, AppointmentDto.class);

        tv_appoint_doctorName.setText("" + appointmentDto.getDoctorName());
        Date date = TimeUtil.longToDateNoTime(appointmentDto.getSeeTime());
        tv_appoint_seeTime.setText(TimeUtil.dateToStrNoTime(date));
        tv_appoint_disease.setText(appointmentDto.getDisease());
        tv_appoint_dNumber.setText("" + appointmentDto.getdNumber());
        tv_appoint_doctorsay.setText(appointmentDto.getDoctorSay());

        Date date1 = TimeUtil.longToDate(appointmentDto.getDateTime()+"");
        tv_appoint_datetime.setText(TimeUtil.dateToString(date1));

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
