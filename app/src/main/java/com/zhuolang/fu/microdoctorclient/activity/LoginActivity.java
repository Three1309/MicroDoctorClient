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
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */

public class LoginActivity extends Activity {

    private EditText et_login_account;
    private EditText et_login_psd;
    private Button bt_login_login;
    private TextView tv_login_find;
    private TextView tv_login_register;

    private String account;
    private String psd;
    private int type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //获取当前登录情况，如果
//        boolean is_login = SharedPrefsUtil.getValue(this,APPConfig.IS_LOGIN,false);
//        if (is_login){
//            Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
//            this.startActivity(intent);
//            finish();
//        }{
            init();
            initMotion();
//        }

    }

    /**
     * 初始化控件
     */
    private void init(){
        et_login_account = (EditText)findViewById(R.id.et_login_account);//账号，对应phone
        et_login_psd = (EditText)findViewById(R.id.et_login_psd);
        bt_login_login = (Button)findViewById(R.id.bt_login_login);
        tv_login_find = (TextView)findViewById(R.id.tv_login_find);
        tv_login_register = (TextView)findViewById(R.id.tv_login_register);
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
                        String userData= response.toString();
                        //将userData的json串直接缓存到本地
                        SharedPrefsUtil.putValue(LoginActivity.this,APPConfig.USERDATA, userData);
                        Log.d("testRun","LoginActivity user信息缓存成功 "+response.toString());

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "网络请求失败------");
                        Toast.makeText(LoginActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }
    /**
     * 初始化监听等
     */
    private void initMotion(){
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取账号 密码
                account = et_login_account.getText().toString().trim();
                psd = et_login_psd.getText().toString().trim();
                if (account.equals("") || psd.equals("")) {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("testRun", " 登陆  loginActivity  ---  account:" + account + " psd:" + psd);
                    //运用okhttp框架 子线程获取后台数据
                    final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param accountParam = new OkHttpUtils.Param("phone", account);
                    OkHttpUtils.Param psdParam = new OkHttpUtils.Param("password", psd);
                    list.add(accountParam);
                    list.add(psdParam);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //post方式连接  url
                            OkHttpUtils.post(APPConfig.login, new OkHttpUtils.ResultCallback() {
                                @Override
                                public void onSuccess(Object response) {
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = response;
                                    if (response.toString().equals("login_success0")) {
                                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.TYPE, "0");
                                    } else if (response.toString().equals("login_success1")) {
                                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.TYPE, "1");
                                    } else if (response.toString().equals("login_success2")) {
                                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.TYPE, "2");
                                    }
                                    handler.sendMessage(message);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d("testRun", "请求失败loginActivity----new Thread(new Runnable() {------");
                                    Toast.makeText(LoginActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();

                                }
                            }, list);
                        }
                    }).start();
                }
            }
        });


        tv_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        tv_login_find.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, MenuActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String result = (String)msg.obj;
                    if (result.equals("login_success0") ||result.equals("login_success1") ||result.equals("login_success2") ){
                        //保存登录状态和当前用户信息
                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.IS_LOGIN,true);
                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.ACCOUNT, account);
                        saveUserInfo(account);
                        //登录成功
                        Toast.makeText(LoginActivity.this,"登陆成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
//                        intent.setClass(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"账号或密码错误，登陆失败！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };
}

