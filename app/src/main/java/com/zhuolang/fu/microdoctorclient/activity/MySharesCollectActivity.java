package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.MySharesCollectAdapter;
import com.zhuolang.fu.microdoctorclient.adapter.MySharesHistoryAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.ShareDto;
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

public class MySharesCollectActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{

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
    private ListView listView;
    private List<ShareDto> shareDtoList = new ArrayList<>();
    private MySharesCollectAdapter mySharesHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysharescollect);

        getIntentUserId = getIntent().getStringExtra("userId");
//        whereStr = getIntent().getStringExtra("where");

        userDataStr = SharedPrefsUtil.getValue(MySharesCollectActivity.this, APPConfig.USERDATA, "");
        userInfo = gson.fromJson(userDataStr, UserInfo.class);
        userId = userInfo.getId() + "";

        init();
        initData();
    }
    /**
     * 初始化控件
     */
    private void init(){
        img_back = (ImageView) findViewById(R.id.img_mysharescollect_back);
        tv_top = (TextView) findViewById(R.id.tv_mysharescollect_top);

        listView = (ListView) findViewById(R.id.mysharescollectlistview);

        listView.setOnItemClickListener(this);

        if (!userId.equals(getIntentUserId)) {
            tv_top.setText("TA的收藏");
        }
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
                OkHttpUtils.post(APPConfig.findMyCollectShareInfo, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        Log.d("testrun", "MySharesHistoryActivity response.toString()=" + response.toString());

                        handler.sendMessage(message);
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
            case R.id.img_mysharescollect_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            Log.d("testrun1", "mycollect result=" + result);
            CustomWaitDialog.miss();
//            shareDtoList = gson.fromJson(result, new TypeToken<List<ShareDto>>() {}.getType());
            switch (msg.what) {
                case 0:
                    if (result.equals("nodata")) {
                        Toast.makeText(MySharesCollectActivity.this, "没有收藏的帖子", Toast.LENGTH_SHORT).show();
                    }else {
                        shareDtoList = gson.fromJson(result, new TypeToken<List<ShareDto>>() {}.getType());
                        if (shareDtoList != null && shareDtoList.size() > 0) {
                            mySharesHistoryAdapter = new MySharesCollectAdapter(MySharesCollectActivity.this,shareDtoList );
                            listView.setAdapter(mySharesHistoryAdapter);
                        }else {

                        }

                    }
                    break;

            }


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

