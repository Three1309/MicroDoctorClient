package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2017/4/13.
 * 注册活动
 */
public class RegisterActivity extends Activity{

    private EditText et_phone;
    private EditText et_psd;
    private Button bt_register;

    private String phone;
    private String psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        initView();
        initMotion();
    }
    private void initView() {
        //通过findViewById得到对应的控件对象
        et_phone=(EditText)findViewById(R.id.et_register_phone);
        et_psd=(EditText)findViewById(R.id.et_register_psd);

        bt_register=(Button)findViewById(R.id.bt_register_reg);
    }

    /**
     * 初始化监听等
     */
    private void initMotion(){

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                phone = et_phone.getText().toString().trim();
                psd = et_psd.getText().toString().trim();
                if (phone.equals("")||psd.equals("")){
                    Toast.makeText(RegisterActivity.this,"账号密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone",phone);
                    OkHttpUtils.Param psdParam = new OkHttpUtils.Param("password",psd);
                    list.add(phoneParam);
                    list.add(psdParam);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //post方式连接  url
                            OkHttpUtils.post(APPConfig.register, new OkHttpUtils.ResultCallback() {
                                @Override
                                public void onSuccess(Object response) {
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = response;
                                    handler.sendMessage(message);
                                }
                                @Override
                                public void onFailure(Exception e) {
                                    Log.d("testRun", "请求失败loginActivity----new Thread(new Runnable() {------");
                                    Toast.makeText(RegisterActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            },list);
                        }

                    }).start();

                }
            }
        });

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String result = (String)msg.obj;
                    if (result.equals("register_success")){
                        //注册成功
                        Toast.makeText(RegisterActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, LoginActivity.class);
//                        Toast.makeText(RegisterActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"已存在该手机号用户,注册失败，请重试！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };



}
