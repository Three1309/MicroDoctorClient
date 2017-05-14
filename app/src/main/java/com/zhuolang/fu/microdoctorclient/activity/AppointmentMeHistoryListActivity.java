package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.AppointmentMeHistoryListAdapter;
import com.zhuolang.fu.microdoctorclient.adapter.PatAppointmentMeListAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzg on 2016/11/3.
 */
public class AppointmentMeHistoryListActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<AppointmentDto> appointmentDtos = new ArrayList<>();
    private ImageView img_back;
    private AppointmentMeHistoryListAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentme_historylist);

        listView = (ListView) findViewById(R.id.lv_appointmentmehistory_list);
        listView.setOnItemClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_appointmentmehistory_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initMotion();
    }

    public void initMotion() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Gson gson = new Gson();
        String userData = SharedPrefsUtil.getValue(AppointmentMeHistoryListActivity.this, APPConfig.USERDATA, "读取失败");
//        if (userData.equals())
        UserInfo user = gson.fromJson(userData, UserInfo.class);
        OkHttpUtils.Param typeParam = new OkHttpUtils.Param("doctorId", user.getId() + "");//接口要求传类型，类型一为医师
        list.add(typeParam);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findDoctSeeHistory, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        if (response.toString().equals("nodata")) {
                            Toast.makeText(AppointmentMeHistoryListActivity.this, "没有找到您的看诊数据！", Toast.LENGTH_LONG).show();
                        } else {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppointmentMeHistoryListActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.setClass(AppointmentMeHistoryListActivity.this, PatientAppointmentDoctorDetailActivity.class);
//        AppointmentDto appointmentDto = appointmentDtos.get(position);
//        Gson gson = new Gson();
//        String appointmentDtoStr = gson.toJson(appointmentDto);
////        Log.d("testRun", "doctorDtoStr==========" + doctorDtoStr);
//        intent.putExtra("appointmentDtoStr", appointmentDtoStr);
//        startActivity(intent);
//        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            Log.d("testRun", "result======" + result);
            Gson gson = new Gson();
            appointmentDtos = gson.fromJson(result, new TypeToken<List<AppointmentDto>>() {}.getType());
            adapter = new AppointmentMeHistoryListAdapter(AppointmentMeHistoryListActivity.this, appointmentDtos);
            listView.setAdapter(adapter);
        }
    };

}
