package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.PatAppointmentMeListAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class PatientAppointmentDoctorDetailActivity extends Activity {

    private String appointmentDataStr;
    private AppointmentDto appointmentDto;
    private Gson gson = new Gson();

    private ImageView img_back;

    private TextView tv_name;
    private TextView tv_dnumber;
    private TextView tv_time;
    private TextView tv_datetime;
    private TextView tv_disease;
    private TextView tv_doctorsays;
    private EditText et_info;
    private Button bt_doctorsay;
    private Button bt_see;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentdoctorsaying);

        appointmentDataStr = getIntent().getStringExtra("appointmentDtoStr");
        appointmentDto=gson.fromJson(appointmentDataStr, AppointmentDto.class);

        initView();
    }

    private void initView(){
        img_back = (ImageView) findViewById(R.id.image_tv_appointment_doctorsaying_back);
        tv_name = (TextView) findViewById(R.id.tv_appointment_doctorsaying_patientname);
        tv_dnumber = (TextView) findViewById(R.id.tv_appointment_doctorsaying_dnumber);
        tv_datetime = (TextView) findViewById(R.id.tv_appointment_doctorsaying_datetime);
        tv_disease = (TextView) findViewById(R.id.tv_appointment_doctorsaying_disease);
        tv_time = (TextView) findViewById(R.id.tv_appointment_doctorsaying_time);
        tv_doctorsays = (TextView) findViewById(R.id.tv_appointment_doctorsaying_doctorsays);
        et_info = (EditText) findViewById(R.id.et_appointment_doctorsaying_info);
        bt_doctorsay = (Button) findViewById(R.id.bt_appointment_doctorsaying_doctorsay);
        bt_see = (Button) findViewById(R.id.bt_appointment_doctorsaying_see);

        tv_name.setText(appointmentDto.getPatientName());
        tv_disease.setText(appointmentDto.getDisease());
        tv_doctorsays.setText(appointmentDto.getDoctorSay());

        Date date = TimeUtil.longToDateNoTime(appointmentDto.getSeeTime());
        tv_datetime.setText(TimeUtil.dateToStrNoTime(date)+"");

        tv_dnumber.setText(appointmentDto.getdNumber()+"");

        Date date1 = TimeUtil.longToDate(appointmentDto.getDateTime());
        tv_time.setText(TimeUtil.dateToString(date1)+"");

        bt_doctorsay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDoctorsay();
            }
        });

        bt_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDiagnose();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PatientAppointmentDoctorDetailActivity.this, AppointmentMeListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void updateDoctorsay() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        String doctorSayStr = et_info.getText().toString().trim();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", appointmentDto.getId() + "");
        OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("doctorSay", doctorSayStr);
        list.add(idParam);
        list.add(doctorsayParam);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateDoctorsay, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(PatientAppointmentDoctorDetailActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }

    public void updateDiagnose() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        String diagnoseStr = et_info.getText().toString().trim();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", appointmentDto.getId() + "");
        OkHttpUtils.Param doctorIdParam = new OkHttpUtils.Param("doctorId", appointmentDto.getDoctorId()+"");
        OkHttpUtils.Param diagnoseParam = new OkHttpUtils.Param("diagnose",diagnoseStr);
        list.add(idParam);
        list.add(doctorIdParam);
        list.add(diagnoseParam);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateDiagnose, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = response;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(PatientAppointmentDoctorDetailActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(PatientAppointmentDoctorDetailActivity.this, AppointmentMeListActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            Log.d("testRun", "result======" + result);
            switch (msg.what) {
                case 0:
                    if (result.equals("updateDoctorSay_success")){
                        Toast.makeText(PatientAppointmentDoctorDetailActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                        String doctorSayStr = et_info.getText().toString().trim();
                        tv_doctorsays.setText(doctorSayStr);
                    }else
                        Toast.makeText(PatientAppointmentDoctorDetailActivity.this, "留言失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    if (result.equals("updateDiagnose_success")){
                        Toast.makeText(PatientAppointmentDoctorDetailActivity.this, "病人就诊成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(PatientAppointmentDoctorDetailActivity.this, AppointmentMeListActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                        Toast.makeText(PatientAppointmentDoctorDetailActivity.this, "病人就诊失败", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };

}
