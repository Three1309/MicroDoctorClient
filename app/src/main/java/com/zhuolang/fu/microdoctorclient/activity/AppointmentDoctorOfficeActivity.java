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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.DoctorListAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.Doctor;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class AppointmentDoctorOfficeActivity extends Activity implements AdapterView.OnItemClickListener {

    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;
    private ListView listView;
    List<Doctor> doctorList = new ArrayList<>();
    private ImageView img_back;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentdoctorofficelist);
        listView = (ListView) findViewById(R.id.appointmentdoctorofficelist_listview);
        //设置listview的元素被选中时的事件处理监听器
        listView.setOnItemClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_appointmentdoctorofficelist_back);
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
        OkHttpUtils.Param typeParam = new OkHttpUtils.Param("doctorId", "1");//接口要求传类型，类型一为医师
        list.add(typeParam);
//        CustomWaitDialog.show(DoctorListActivity.this, "连接服务中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findAllOffice, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        String doctorDataStr= response.toString();
                        Log.d("testrun", "AppointmentDoctorOfficeActivity : "+doctorDataStr);
                        if (doctorDataStr.equals("nodata")) {
                            Toast.makeText(AppointmentDoctorOfficeActivity.this, "没有医师信息", Toast.LENGTH_SHORT).show();
                        }else {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppointmentDoctorOfficeActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
//                        CustomWaitDialog.miss();
                    }
                },list);
            }
        }).start();
    }

    //    事件处理监听器方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(AppointmentDoctorOfficeActivity.this, "点击"+doctorList.get(position).getOffice(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(AppointmentDoctorOfficeActivity.this, AppointmentDoctorHospitalActivity.class);
        intent.putExtra("office", doctorList.get(position).getOffice());
//        intent.putExtra("flag", "false");
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
                    map.put("office", "     "+doctorList.get(i).getOffice());
                    dataList.add(map);
                }
                simpleAdapter = new SimpleAdapter(AppointmentDoctorOfficeActivity.this,
                        dataList, android.R.layout.simple_list_item_1,
                        new String[]{"office"},
                        new int[]{android.R.id.text1});
                listView.setAdapter(simpleAdapter);
            }
        }
    };

}
