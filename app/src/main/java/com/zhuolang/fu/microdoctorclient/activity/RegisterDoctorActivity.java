package com.zhuolang.fu.microdoctorclient.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class RegisterDoctorActivity extends Activity {

    private Button bt_register;
    private TextView tv_account;
    private TextView tv_info;
    private EditText et_hospital;
    private EditText et_office;
    private ImageView ivBack;
    private String phone;
    private String hospital;
    private String office;
    private String userDataStr;
    private UserInfo userInfo;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registerdoctor);

        phone = SharedPrefsUtil.getValue(RegisterDoctorActivity.this, APPConfig.ACCOUNT, "");
        userDataStr= SharedPrefsUtil.getValue(RegisterDoctorActivity.this, APPConfig.USERDATA, "");
        userInfo=gson.fromJson(userDataStr,UserInfo.class);
        saveUserInfo(phone);
        initView();
        initDatas();

    }

    private void saveUserInfo(String phone){
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone",phone);
        list.add(phoneParam);
        new Thread(new Runnable() {
            @Override
            public void run() {

                //post方式连接  url
                OkHttpUtils.post(APPConfig.findUserByPhone, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        userDataStr= response.toString();
                        //将userData的json串直接缓存到本地
                        userInfo=gson.fromJson(userDataStr,UserInfo.class);
                        SharedPrefsUtil.putValue(RegisterDoctorActivity.this,APPConfig.USERDATA, userDataStr);
                        Log.d("testRun","user信息缓存成功 "+response.toString());

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "网络请求失败------");
                        userInfo=gson.fromJson(SharedPrefsUtil.getValue(RegisterDoctorActivity.this, APPConfig.USERDATA, ""),UserInfo.class);
                        Toast.makeText(RegisterDoctorActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }

    private void initView() {

        tv_info= (TextView) findViewById(R.id.registerdoctor_tv_info);
        tv_account= (TextView) findViewById(R.id.registerdoctor_tv_account);
        et_hospital= (EditText) findViewById(R.id.registerdoctor_et_hospital);
        et_office= (EditText) findViewById(R.id.registerdoctor_et_office);
        bt_register = (Button) findViewById(R.id.bt_registerdoctor_register);
        ivBack=(ImageView)findViewById(R.id.image_registerdoctor_back);

        tv_account.setText("账号："+phone);
        et_hospital.setText(userInfo.getHospital());
        et_office.setText(userInfo.getOffice());
        if (userInfo.getAmount() == -1) {
            bt_register.setText("更改验证信息");
//            bt_register.setVisibility(View.GONE);
            tv_info.setText("已申请");
        }else if (userInfo.getType() == 1) {
            bt_register.setVisibility(View.GONE);
            et_hospital.setText("医院："+userInfo.getHospital());
            et_office.setText("科室："+userInfo.getOffice());
            et_hospital.setFocusable(false);
            et_office.setFocusable(false);
            tv_info.setText("已验证");
            tv_info.setTextColor(getResources().getColor(R.color.chartreuse));

        }
    }

    private void initDatas() {
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hospital = et_hospital.getText().toString().trim();
                office = et_office.getText().toString().trim();
                if (hospital.equals("") || office.equals("")) {
                    Toast.makeText(RegisterDoctorActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //运用okhttp框架 子线程获取后台数据
                    final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", phone);
                    OkHttpUtils.Param hospitalParam = new OkHttpUtils.Param("hospital", hospital);
                    OkHttpUtils.Param officeParam = new OkHttpUtils.Param("office", office);
                    list.add(phoneParam);
                    list.add(hospitalParam);
                    list.add(officeParam);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //post方式连接  url
                            if (userInfo.getAmount() == -1) {//更改验证信息
                                OkHttpUtils.post(APPConfig.updateRegisterDoctor, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        Message message = new Message();
                                        message.what = 1;
                                        message.obj = response;
                                        handler.sendMessage(message);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(RegisterDoctorActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                }, list);
                            }else {
                                OkHttpUtils.post(APPConfig.registerDoctor, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        Message message = new Message();
                                        message.what = 0;
                                        message.obj = response;
                                        handler.sendMessage(message);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(RegisterDoctorActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                }, list);

                            }
                        }
                    }).start();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                case 1:
                    String result = (String)msg.obj;
                    if (result.equals("ask_success")){
                        if (userInfo.getAmount() == -1) {
                            Toast.makeText(RegisterDoctorActivity.this,"修改成功，请耐心等待管理员验证通过",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterDoctorActivity.this,"请求成功，请耐心等待管理员验证通过",Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterDoctorActivity.this,"请求失败，请重试！",Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }

    };
}

