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
import com.zhuolang.fu.microdoctorclient.adapter.RegisterDoctorListAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/30.
 */

public class AgreeRegisterDoctotActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private RegisterDoctorListAdapter registerDoctorListAdapter;
    private List<UserInfo> registerDoctorList;
    private ImageView img_back;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerdoctorlist);
        listView = (ListView) findViewById(R.id.agreedoctorlist_listview);
        initMotion();
        //设置listview的元素被选中时的事件处理监听器
        listView.setOnItemClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_registerdoctorlist_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initMotion() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param typeParam = new OkHttpUtils.Param("type", "1");//接口要求传类型，类型一为医师
        list.add(typeParam);
//        CustomWaitDialog.show(DoctorListActivity.this, "连接服务中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findRegisterDoctor, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        String userDataStr= response.toString();
                        Log.d("testrun", "AgreeRegisterDoctotActivity : "+userDataStr);
                        if (userDataStr.equals("nodata")) {
                            Toast.makeText(AgreeRegisterDoctotActivity.this, "没有医师申请信息", Toast.LENGTH_SHORT).show();
                        }else {
                            //将userData的json串直接缓存到本地
//                        doctorList= (List<UserInfo>) gson.fromJson(userDataStr,UserInfo.class);
                            registerDoctorList=gson.fromJson(userDataStr, new TypeToken<List<UserInfo>>() {}.getType());
                            handler.sendMessage(message);

                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AgreeRegisterDoctotActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
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
        intent.setClass(AgreeRegisterDoctotActivity.this, DoctorDetailActivity.class);
        if (registerDoctorList != null && registerDoctorList.size() > 0) {
            UserInfo doctorInfo = registerDoctorList.get(position);
            Gson gson = new Gson();
            String doctorDtoStr = gson.toJson(doctorInfo);
            intent.putExtra("doctorStr", doctorDtoStr);
            intent.putExtra("flag", "true");
            startActivity(intent);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (registerDoctorList != null && registerDoctorList.size() > 0) {

                registerDoctorListAdapter = new RegisterDoctorListAdapter(AgreeRegisterDoctotActivity.this, registerDoctorList);
                listView.setAdapter(registerDoctorListAdapter);
            }
        }
    };

}