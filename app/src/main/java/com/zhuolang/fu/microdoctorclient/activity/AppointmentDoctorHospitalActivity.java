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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.Doctor;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class AppointmentDoctorHospitalActivity extends Activity implements AdapterView.OnItemClickListener {

    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;
    private ListView listView;
    List<Doctor> doctorList = new ArrayList<>();
    private ImageView img_back;
    String office="";
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentdoctorhospitallist);

        office = getIntent().getStringExtra("office");
        listView = (ListView) findViewById(R.id.appointmentdoctorhospitallist_listview);
        //设置listview的元素被选中时的事件处理监听器
        listView.setOnItemClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_appointmentdoctorhospitallist_back);
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
        OkHttpUtils.Param typeParam = new OkHttpUtils.Param("office",office);
        list.add(typeParam);
//        CustomWaitDialog.show(DoctorListActivity.this, "连接服务中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findAllHospital, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        String doctorDataStr= response.toString();
                        Log.d("testrun", "AppointmentDoctorhospitalActivity : "+doctorDataStr);
                        if (doctorDataStr.equals("nodata")) {
                            Toast.makeText(AppointmentDoctorHospitalActivity.this, "没有医院信息", Toast.LENGTH_SHORT).show();
                        }else {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppointmentDoctorHospitalActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
//                        CustomWaitDialog.miss();
                    }
                },list);
            }
        }).start();
    }

    //    事件处理监听器方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(AppointmentDoctorHospitalActivity.this, "点击"+doctorList.get(position).getHospital(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(AppointmentDoctorHospitalActivity.this, AppointmentDoctorListActivity.class);
//        UserInfo doctorInfo = doctorList.get(position);
//        Gson gson = new Gson();
//        String doctorDtoStr = gson.toJson(doctorInfo);
        intent.putExtra("office", office);
        intent.putExtra("hospital", doctorList.get(position).getHospital());
        startActivity(intent);
        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            doctorList = gson.fromJson(result, new TypeToken<List<Doctor>>() {}.getType());
            dataList = new ArrayList<Map<String,Object>>();
            if (doctorList != null && doctorList.size() > 0) {
                for (int i=0;i<doctorList.size();i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("office", "     "+doctorList.get(i).getHospital());
                    dataList.add(map);
                }
                simpleAdapter = new SimpleAdapter(AppointmentDoctorHospitalActivity.this,
                        dataList, android.R.layout.simple_list_item_1,
                        new String[]{"office"},
                        new int[]{android.R.id.text1});
                listView.setAdapter(simpleAdapter);
            }
        }
    };

}
