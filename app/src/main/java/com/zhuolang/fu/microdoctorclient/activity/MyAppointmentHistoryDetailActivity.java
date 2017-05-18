package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hzg on 2016/11/3.
 */
public class MyAppointmentHistoryDetailActivity extends Activity {

    private String doctorDtoStr;
    private TextView tv_appoint_doctorName;
    private TextView tv_appoint_seeTime;
    private TextView tv_appoint_disease;
    private TextView tv_appoint_diagnose;
    private TextView tv_appoint_dNumber;
    private TextView tv_appoint_doctorsay;
    private TextView tv_appoint_likenumber;
    private TextView tv_appoint_datetime;
    private ImageView imageViewback;
    private ImageView imageViewLike;

    private int likesnumber = 0;
    private String likesOrNot = "";

    private AppointmentDto appointmentDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointhistorydetails);
        tv_appoint_doctorName = (TextView) findViewById(R.id.tv_appointhistory_doctorName);
        tv_appoint_seeTime = (TextView) findViewById(R.id.tv_appointhistory_seeTime);
        tv_appoint_disease = (TextView) findViewById(R.id.tv_appointhistory_disease);
        tv_appoint_diagnose = (TextView) findViewById(R.id.tv_appointhistory_diagnose);
        tv_appoint_dNumber = (TextView) findViewById(R.id.tv_appointhistory_dNumber);
        tv_appoint_doctorsay = (TextView) findViewById(R.id.tv_appointhistory_doctorsay);
        tv_appoint_datetime = (TextView) findViewById(R.id.tv_appointhistory_datetime);
        tv_appoint_likenumber = (TextView) findViewById(R.id.tv_appointhistory_likenumber);
        imageViewback = (ImageView) findViewById(R.id.img_appointhistoryList_back);
        imageViewLike = (ImageView) findViewById(R.id.img_appointhistorylist_likes);

        Gson gson = new Gson();
        doctorDtoStr = getIntent().getStringExtra("doctorDtoStr");
        appointmentDto = gson.fromJson(doctorDtoStr, AppointmentDto.class);

        likesnumber = appointmentDto.getdLikenum();
        likesOrNotM();
        tv_appoint_doctorName.setText("" + appointmentDto.getDoctorName());
        Date date = TimeUtil.longToDateNoTime(appointmentDto.getSeeTime());
        tv_appoint_seeTime.setText(TimeUtil.dateToStrNoTime(date));
        tv_appoint_disease.setText(appointmentDto.getDisease());
        tv_appoint_diagnose.setText(appointmentDto.getDiagnose());
        tv_appoint_dNumber.setText("" + appointmentDto.getdNumber());
        tv_appoint_doctorsay.setText(appointmentDto.getDoctorSay());
        tv_appoint_likenumber.setText(appointmentDto.getdLikenum()+"");
        tv_appoint_datetime.setText(TimeUtil.dateToString(TimeUtil.longToDate(appointmentDto.getDateTime())));


        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLikes();
            }
        });
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyAppointmentHistoryDetailActivity.this, MyAppointHistoryListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void likesOrNotM(){
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param officeParam = new OkHttpUtils.Param("appointmentId", appointmentDto.getId()+"");
        OkHttpUtils.Param hospitalParam = new OkHttpUtils.Param("doctorId", appointmentDto.getDoctorId()+"");
        list.add(officeParam);
        list.add(hospitalParam);
//        CustomWaitDialog.show(DoctorListActivity.this, "连接服务中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.likesOrNot, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = response;
                        likesOrNot= response.toString();
                        Log.d("testrun", "MyAppointmentHistoryDetailActivity : "+likesOrNot);
                        handler.sendMessage(message);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MyAppointmentHistoryDetailActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
//                        CustomWaitDialog.miss();
                    }
                }, list);
            }
        }).start();
    }

    private void clickLikes(){
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param officeParam = new OkHttpUtils.Param("appointmentId", appointmentDto.getId()+"");
        OkHttpUtils.Param hospitalParam = new OkHttpUtils.Param("doctorId", appointmentDto.getDoctorId()+"");
        list.add(officeParam);
        list.add(hospitalParam);
//        CustomWaitDialog.show(DoctorListActivity.this, "连接服务中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateDoctorlikes, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        String userDataStr= response.toString();
                        Log.d("testrun", "MyAppointmentHistoryDetailActivity : "+userDataStr);
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MyAppointmentHistoryDetailActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
//                        CustomWaitDialog.miss();
                    }
                }, list);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            switch (msg.what) {
                case 0:
                    if (result.equals("likes_success")) {
                        Toast.makeText(MyAppointmentHistoryDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                        appointmentDto.setdLikenum(appointmentDto.getdLikenum() + 1);
                        tv_appoint_likenumber.setText(appointmentDto.getdLikenum() + "");
                        imageViewLike.setImageResource(R.mipmap.islike01);
                    } else if (result.equals("dislikes_success")){
                        Toast.makeText(MyAppointmentHistoryDetailActivity.this, "已取消赞", Toast.LENGTH_SHORT).show();
                        appointmentDto.setdLikenum(appointmentDto.getdLikenum() - 1);
                        tv_appoint_likenumber.setText(appointmentDto.getdLikenum() + "");
                        imageViewLike.setImageResource(R.mipmap.like01);
                    }
                    break;
                case 1:
                    if (result.equals("dislikes")) {
                        imageViewLike.setImageResource(R.mipmap.like01);
                    }else {
                        imageViewLike.setImageResource(R.mipmap.islike01);
                    }
                    break;
            }

        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(MyAppointmentHistoryDetailActivity.this, MyAppointHistoryListActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
