package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.ShareHouseDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */

public class MySharesCollectActivity extends Activity implements View.OnClickListener{

    private LinearLayout ll_myshare;
    private LinearLayout ll_mycollect;
    private LinearLayout ll_mydiscuss;

    private TextView tv_top;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_myjianjie;
    private TextView tv_myshares;
    private TextView tv_mycollects;
    private TextView tv_myguanzus;
    private TextView tv_mydiscusses;

    private TextView tv_likesAmount;
    private TextView tv_collectAmount;
    private TextView tv_discussAmount;

    private ImageView img_back;

    private String title;
    private String content;
    private int type;
    private String whereStr;
    private String userDataStr;
    private String userId;
    private String getIntentUserId;
    private UserInfo userInfo;
    Gson gson = new Gson();

    private ShareHouseDto shareHouseDto;
    private String shareHouseDtoStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysharescollect);

        getIntentUserId = getIntent().getStringExtra("userId");
//        whereStr = getIntent().getStringExtra("where");

        userDataStr = SharedPrefsUtil.getValue(MySharesCollectActivity.this, APPConfig.USERDATA, "");
        userInfo = gson.fromJson(userDataStr, UserInfo.class);
        userId = userInfo.getId() + "";

//        init();
//
//        initData();
    }

    /**
     * 初始化控件
     */
    private void init(){
        img_back = (ImageView) findViewById(R.id.img_usersharehouse_back);
        tv_top = (TextView) findViewById(R.id.tv_usersharehouse_top);
        tv_name = (TextView) findViewById(R.id.tv_usersharehouse_name);
        tv_type = (TextView) findViewById(R.id.tv_usersharehouse_type);
        tv_myjianjie = (TextView) findViewById(R.id.tv_usersharehouse_jianjie);
        tv_myshares = (TextView) findViewById(R.id.tv_usersharehouse_myshares);
        tv_mycollects = (TextView) findViewById(R.id.tv_usersharehouse_mycollects);
        tv_myguanzus = (TextView) findViewById(R.id.tv_usersharehouse_myguanzus);
        tv_mydiscusses = (TextView) findViewById(R.id.tv_usersharehouse_mydiscusses);
        tv_likesAmount = (TextView) findViewById(R.id.tv_usersharehouse_likesamount);
        tv_collectAmount = (TextView) findViewById(R.id.tv_usersharehouse_collectamount);
        tv_discussAmount = (TextView) findViewById(R.id.tv_usersharehouse_discussamount);

        ll_myshare = (LinearLayout) findViewById(R.id.ll_usersharehouse_myshares);
        ll_mydiscuss = (LinearLayout) findViewById(R.id.ll_usersharehouse_mydiscusses);
        ll_mycollect = (LinearLayout) findViewById(R.id.ll_usersharehouse_mycollects);

        if (!userId.equals(getIntentUserId)) {
            tv_top.setText("TA的医言堂社区");
            tv_myshares.setText("TA的帖子");
            tv_mycollects.setText("TA的收藏");
            tv_myguanzus.setText("TA的关注");
            tv_mydiscusses.setText("TA的评论");
        }

        ll_mycollect.setOnClickListener(this);
        ll_myshare.setOnClickListener(this);
        ll_mydiscuss.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }
    /**
     * 初始化数据
     */
    private void initData() {
//        Toast.makeText(UserShareHouseInfoActivity.this, "dfddd", Toast.LENGTH_SHORT).show();
        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("userId", getIntentUserId);
        listP.add(idParam);
        CustomWaitDialog.show(MySharesCollectActivity.this,"获取数据。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findUserShareInfo, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        Log.d("testrun", "UserShareHouseInfoActivity response.toString()=" + response.toString());
                        if (response.toString().equals("nodata")) {
                            CustomWaitDialog.miss();
                        }else {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        CustomWaitDialog.miss();
                        Toast.makeText(MySharesCollectActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_usersharehouse_mycollects:


//                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();

                CustomWaitDialog.miss();


        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

