package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.User;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hzg on 2016/11/5.
 */
public class AppointDoctorInfoActivity extends Activity {
    private ImageView im_back;
    private EditText et_disease;
    private DatePicker datepicker;
    private TextView tv;
    private TextView tv_zixun;
    private Button bt_appoint;
    private Button bt_cancel;

    private String disease;
    private String seeTime;
    private UserInfo doctor;
    private UserInfo user;
    private String patientId;//获取当前用户user的id然后转为String类型
    private String doctorId;
    Gson gson = new Gson();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentdoctor_info);
        doctor = gson.fromJson(getIntent().getStringExtra("doctorStr"), UserInfo.class);
        init();
        initMotion();
    }

    private void init() {
        im_back = (ImageView) findViewById(R.id.img_appoint_back);
        et_disease = (EditText) findViewById(R.id.et_disease);
        datepicker = (DatePicker) findViewById(R.id.datepicker);
        tv = (TextView) findViewById(R.id.tv);
        tv_zixun = (TextView) findViewById(R.id.tv_appointmentdoctorinfo_zixun);
        bt_appoint = (Button) findViewById(R.id.bt_appoint);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
    }

    private void initMotion() {
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointDoctorInfoActivity.this, ChatToDoctorActivity.class);
                intent.putExtra("username", doctor.getPhone()+"");
                intent.putExtra("username1", doctor.getName());
                startActivity(intent);
            }
        });
        datepicker.init(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                long mindate = System.currentTimeMillis() - 1000L;
                view.setMinDate(mindate);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                String date = TimeUtil.dateToStrNoTime(calendar.getTime());
                tv.setText(date);
                seeTime = date;
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (disease.equals("")) {
                    Toast.makeText(AppointDoctorInfoActivity.this, "请填写病症！", Toast.LENGTH_SHORT).show();
                } else if (tv.getText().equals("未选择")) {
                    Toast.makeText(AppointDoctorInfoActivity.this, "请选择预约时间！", Toast.LENGTH_SHORT).show();
                } else {
                    getConnect();
                }
            }
        });
    }

    private void getConnect() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param patientIdParam = new OkHttpUtils.Param("patientId", patientId);
        OkHttpUtils.Param doctorIdParam = new OkHttpUtils.Param("doctorId", doctorId);
        OkHttpUtils.Param seeTimeParam = new OkHttpUtils.Param("seeTime", seeTime);
        OkHttpUtils.Param diseaseParam = new OkHttpUtils.Param("disease", disease);
        list.add(patientIdParam);
        list.add(doctorIdParam);
        list.add(seeTimeParam);
        list.add(diseaseParam);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post(APPConfig.addAppointment, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        if (handler.sendMessage(message)) {
//                            Toast.makeText(AppointInfoActivity.this, "发送数据成功！", Toast.LENGTH_SHORT).show();
                        }else {
//                            Toast.makeText(AppointInfoActivity.this, "发送数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppointDoctorInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }

    public void getData() {
        String userData = SharedPrefsUtil.getValue(AppointDoctorInfoActivity.this, APPConfig.USERDATA, "读取失败");

        user = gson.fromJson(userData, UserInfo.class);
        patientId = "" + user.getId();

        doctorId = "" + doctor.getId();
        disease = et_disease.getText().toString().trim();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String result = (String) msg.obj;
                    if (result.equals("addAppointment_success")) {
//                        Toast.makeText(AppointDoctorInfoActivity.this, "预约成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(AppointDoctorInfoActivity.this, AppointSuccessActivity.class);
                        intent.putExtra("disease", disease);
                        intent.putExtra("seeTime",seeTime);
                        intent.putExtra("userName",user.getName());
                        intent.putExtra("doctorName",doctor.getName());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AppointDoctorInfoActivity.this, "预约失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };
}