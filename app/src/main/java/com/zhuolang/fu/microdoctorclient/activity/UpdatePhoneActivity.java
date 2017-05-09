package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
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
public class UpdatePhoneActivity extends Activity {

    private Button btUpdatePhone;
    private TextView tv_account;
    private EditText et_phone;
    private ImageView ivBack;
    private String account;
    private String phone;
    private String userId;
    private String userDataStr;
    private UserInfo userInfo;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_updatephone);
        initView();
        initDatas();

    }
    private void initView() {
        userDataStr= SharedPrefsUtil.getValue(this, APPConfig.USERDATA, "");
        Gson gson=new Gson();
        userInfo=gson.fromJson(userDataStr,UserInfo.class);
        account = userInfo.getPhone();

        tv_account= (TextView) findViewById(R.id.updatephone_tv_account);
        et_phone= (EditText) findViewById(R.id.updatephone_et_phone);
        btUpdatePhone = (Button) findViewById(R.id.bt_updatephone_updateph);
        ivBack=(ImageView)findViewById(R.id.image_updatephone_back);

        tv_account.setText("原手机号："+account);
    }

    private void initDatas() {
        btUpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取账号 密码
                userId= ""+userInfo.getId();

                phone= et_phone.getText().toString().trim();
                if (phone.equals("")){
                    Toast.makeText(UpdatePhoneActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    if (phone.equals(account)) {
                        Toast.makeText(UpdatePhoneActivity.this, "新号码和旧号码相同，请重试", Toast.LENGTH_SHORT).show();
                    }else {
                        //运用okhttp框架 子线程获取后台数据
                        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                        OkHttpUtils.Param userIdParam = new OkHttpUtils.Param("id", userId);
                        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", phone);
                        list.add(userIdParam);
                        list.add(phoneParam);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //post方式连接  url
                                OkHttpUtils.post(APPConfig.updatePhone, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        Message message = new Message();
                                        message.what = 0;
                                        message.obj = response;
                                        handler.sendMessage(message);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(UpdatePhoneActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();

                                    }
                                }, list);
                            }
                        }).start();
                    }

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
                    String result = (String)msg.obj;
                    if (result.equals("updatePhone_success")){
                        Toast.makeText(UpdatePhoneActivity.this,"手机号码更换成功,请重新登录！",Toast.LENGTH_SHORT).show();
                        SharedPrefsUtil.putValue(UpdatePhoneActivity.this, APPConfig.ACCOUNT, phone);
                        userInfo.setPhone(phone);
                        SharedPrefsUtil.putValue(UpdatePhoneActivity.this, APPConfig.USERDATA, gson.toJson(userInfo));
                        finish();
                    }
                    else{
                        Toast.makeText(UpdatePhoneActivity.this,"手机号码更换失败，请重试！",Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }

    };
}

