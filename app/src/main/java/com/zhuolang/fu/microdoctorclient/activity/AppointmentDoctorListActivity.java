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
import com.zhuolang.fu.microdoctorclient.adapter.AppointmentDoctorListAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class AppointmentDoctorListActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private AppointmentDoctorListAdapter appointmentDoctorListAdapter;
    private List<UserInfo> doctorList;
    private ImageView img_back;
    Gson gson = new Gson();
    private String office = "";
    private String hospital = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentdoctorlist);

        office = getIntent().getStringExtra("office");
        hospital = getIntent().getStringExtra("hospital");

        listView = (ListView) findViewById(R.id.appointmentdoctorlist_listview);
        //设置listview的元素被选中时的事件处理监听器
        listView.setOnItemClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_appointmentdoctorlist_back);
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
        OkHttpUtils.Param officeParam = new OkHttpUtils.Param("office", office);
        OkHttpUtils.Param hospitalParam = new OkHttpUtils.Param("hospital", hospital);
        list.add(officeParam);
        list.add(hospitalParam);
//        CustomWaitDialog.show(DoctorListActivity.this, "连接服务中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findDoctorByOfficeAndHospital, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        String userDataStr= response.toString();
                        Log.d("testrun", "AppointmentDoctorListActivity : "+userDataStr);
                        if (userDataStr.equals("nodata")) {
                            Toast.makeText(AppointmentDoctorListActivity.this, "没有医师信息", Toast.LENGTH_SHORT).show();
                        }else {
                            //将userData的json串直接缓存到本地
//                        doctorList= (List<UserInfo>) gson.fromJson(userDataStr,UserInfo.class);
//                            doctorList = gson.fromJson(userDataStr, new TypeToken<List<UserInfo>>() {}.getType());
                            handler.sendMessage(message);

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppointmentDoctorListActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
//                        CustomWaitDialog.miss();
                    }
                }, list);
            }
        }).start();
    }

    //    事件处理监听器方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(AppointmentDoctorListActivity.this, AppointDoctorInfoActivity.class);
        UserInfo doctorInfo = doctorList.get(position);
//        Gson gson = new Gson();
        String doctorDtoStr = gson.toJson(doctorInfo);
        intent.putExtra("doctorStr", doctorDtoStr);
//        intent.putExtra("flag", "false");
        startActivity(intent);
//        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            doctorList = gson.fromJson(result, new TypeToken<List<UserInfo>>() {
            }.getType());
            if (doctorList != null && doctorList.size() > 0) {
                appointmentDoctorListAdapter = new AppointmentDoctorListAdapter(AppointmentDoctorListActivity.this,doctorList );
                listView.setAdapter(appointmentDoctorListAdapter);

            }
        }
    };

}
