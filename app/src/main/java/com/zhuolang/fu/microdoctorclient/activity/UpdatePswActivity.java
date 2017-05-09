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
public class UpdatePswActivity extends Activity {

    private Button btUpdatePsw;
    private EditText etOldPsw;
    private EditText etNewPsw;
    private ImageView ivBack;
    private String oldPsw;
    private String newPsw;
    private String userId;
    private String userDataStr;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_updatepsw);
        initView();
        initDatas();

    }
    private void initView() {
        userDataStr= SharedPrefsUtil.getValue(this, APPConfig.USERDATA, "");
        Gson gson=new Gson();
        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        etNewPsw= (EditText) findViewById(R.id.et_updatepsw_newpsd);
        etOldPsw= (EditText) findViewById(R.id.et_updatepsw_oldpsw);
        btUpdatePsw= (Button) findViewById(R.id.bt_updatepsw_pss);
        ivBack=(ImageView)findViewById(R.id.image_updatepsw_back);
    }

    private void initDatas() {
        btUpdatePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取账号 密码
                userId= ""+userInfo.getId();
                oldPsw= etOldPsw.getText().toString().trim();
                newPsw= etNewPsw.getText().toString().trim();
                if (oldPsw.equals("")||newPsw.equals("")){
                    Toast.makeText(UpdatePswActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    //运用okhttp框架 子线程获取后台数据
                    final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param userIdParam = new OkHttpUtils.Param("id", userId);
                    OkHttpUtils.Param oldPswParam = new OkHttpUtils.Param("oldPassword", oldPsw);
                    OkHttpUtils.Param newPsdParam = new OkHttpUtils.Param("newPassword", newPsw);
                    list.add(userIdParam);
                    list.add(oldPswParam);
                    list.add(newPsdParam);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //post方式连接  url
                            OkHttpUtils.post(APPConfig.updatePassword, new OkHttpUtils.ResultCallback() {
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
                                    Toast.makeText(UpdatePswActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();

                                }
                            }, list);
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
                    String result = (String)msg.obj;
                    if (result.equals("updatePassword_success")){
                        Toast.makeText(UpdatePswActivity.this,"密码修改成功，请记住密码！",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(UpdatePswActivity.this,"密码修改失败，请重试！",Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }

    };
}

