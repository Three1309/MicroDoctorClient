package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
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
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */

public class SendShareInfoActivity extends Activity {

    private EditText et_title;
    private EditText et_content;
    private TextView tv_send;
    private ImageView img_back;

    private String title;
    private String content;
    private int type;

    private String userDataStr;
    private String userId;
    private UserInfo userInfo;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendshare);
        userDataStr = SharedPrefsUtil.getValue(SendShareInfoActivity.this, APPConfig.USERDATA, "");
        userInfo = gson.fromJson(userDataStr, UserInfo.class);
        userId = userInfo.getId() + "";
        init();
    }

    /**
     * 初始化控件
     */
    private void init(){
        et_title = (EditText)findViewById(R.id.et_activity_sendshare_title);//账号，对应phone
        et_content = (EditText)findViewById(R.id.et_activity_sendshare_content);

        tv_send = (TextView)findViewById(R.id.tv_activity_sendshare_send);
        img_back = (ImageView) findViewById(R.id.img_activity_sendshare_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHouseActivity.updateData();
                finish();
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMotion();


            }
        });
    }
    /**
     * 初始化监听等
     */
    private void initMotion() {
        //获取账号 密码
        title = et_title.getText().toString();
        content = et_content.getText().toString();
        if (title.equals("") || content.equals("")) {
            Toast.makeText(SendShareInfoActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
        } else {
            //运用okhttp框架 子线程获取后台数据
            CustomWaitDialog.show(SendShareInfoActivity.this, "发表中。。。");
            final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
            OkHttpUtils.Param userIdParam = new OkHttpUtils.Param("userId", userId);
            OkHttpUtils.Param sendtitleParam = new OkHttpUtils.Param("sendTitle", title);
            OkHttpUtils.Param sendContentParam = new OkHttpUtils.Param("sendContent", content);
            list.add(userIdParam);
            list.add(sendtitleParam);
            list.add(sendContentParam);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //post方式连接  url
                    OkHttpUtils.post(APPConfig.addShareSend, new OkHttpUtils.ResultCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            Message message = new Message();
                            message.what = 0;
                            message.obj = response;
                            String result = response.toString();
                            Log.d("testrun", "SendShareInfoActivity  result===" + result);
                            if (result.equals("addShareSend_success")) {
                                handler.sendMessage(message);
                            } else {
                                CustomWaitDialog.miss();
                                Toast.makeText(SendShareInfoActivity.this, "帖子发表失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d("testRun", "请求失败loginActivity----new Thread(new Runnable() {------");
                            CustomWaitDialog.miss();
                            Toast.makeText(SendShareInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();

                        }
                    }, list);
                }
            }).start();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            CustomWaitDialog.miss();
            String result = msg.obj.toString();
            if (result.equals("addShareSend_success")) {
                et_content.setText("");
                et_title.setText("");
                Toast.makeText(SendShareInfoActivity.this, "帖子发表成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SendShareInfoActivity.this, "帖子发表失败", Toast.LENGTH_SHORT).show();
            }
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            mWindowManager.removeView(img_Float);
            ShareHouseActivity.updateData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

