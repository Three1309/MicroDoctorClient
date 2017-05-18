package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.ShareHouseAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;
import com.zhuolang.fu.microdoctorclient.model.ShareDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;
import com.zhuolang.fu.microdoctorclient.view.MyTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class ShareHouseActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{

    private String shareDtoStr;
    private ShareDto shareDto = new ShareDto();
    private String userDataStr;
    private String userId;
    private UserInfo userInfo;
    Gson gson = new Gson();
    private List<ShareDto> shareDtoList = new ArrayList<>();
    private ShareHouseAdapter shareHouseAdapter;

    private static TextView tv_top;
    private static LinearLayout ll_top;
    private ImageView img_back;
    private ImageView img_myinfo;
    private static ImageView img_refresh;
    private ListView listView;

    private static WindowManager mWindowManager = null;
    private WindowManager.LayoutParams wmParams = null;
    // 用于显示右下角浮动图标
    private ImageView img_Float;
    private static TextView tv_Float;

    public static boolean flag = false;
    private boolean flagTop = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharehouse);

        initFloatImage();
        createFloatTextView();
        flag = false;
        initView();
        initData();
//        createFloatView();
//        mWindowManager.removeView(img_Float);  //销毁图片
    }
    public static void updateData() {

        img_refresh.performClick();
//        statice_top.performClick();
    }
    public static void clickTop() {
        ll_top.performClick();
    }

    public static void removetv_FloatView() {
        flag = true;
        mWindowManager.removeView(tv_Float);
    }

    private void initView(){
        userDataStr= SharedPrefsUtil.getValue(ShareHouseActivity.this, APPConfig.USERDATA, "");
        Log.d("activityID", "这个是SettingsFragment ----- : " + userDataStr);
        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        tv_top = (TextView) findViewById(R.id.tv_sharehouse_top);
        ll_top = (LinearLayout) findViewById(R.id.ll_sharehouse_top);
        img_back = (ImageView) findViewById(R.id.img_sharehouse_back);
        img_myinfo = (ImageView) findViewById(R.id.img_sharehouse_myinfo);
        img_refresh = (ImageView) findViewById(R.id.img_sharehouse_refresh);
        listView = (ListView) findViewById(R.id.sharehouselistview);

        listView.setOnItemClickListener(this);
        img_refresh.setOnClickListener(this);
        img_back.setOnClickListener(this);
        img_myinfo.setOnClickListener(this);
        ll_top.setOnClickListener(this);
        tv_top.setOnClickListener(this);
    }

    public void initData() {

        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
        userId = userInfo.getId()+"";
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("userId", userId);
//        OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("doctorSay", doctorSayStr);
        listP.add(idParam);
//        list.add(doctorsayParam);
        CustomWaitDialog.show(ShareHouseActivity.this,"获取数据。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findAllShare, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        shareDtoStr = response.toString();
                        Log.d("testrun", "ShareHouseActivity shareDtoStr=" + shareDtoStr);
                        if (shareDtoStr.equals("nodata")) {
                            CustomWaitDialog.miss();
                            Toast.makeText(ShareHouseActivity.this, "没有帖子", Toast.LENGTH_SHORT).show();
                        }else {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        CustomWaitDialog.miss();
                        Toast.makeText(ShareHouseActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        }).start();
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
//        DisplayMetrics metrics = new DisplayMetrics();
//        float dpToCm = (metrics.density * 2.54f *133*shareList2.size())/ metrics.xdpi;
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight+=20;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        flag = true;
        mWindowManager.removeView(tv_Float);

        String shareStr1 = gson.toJson(shareDtoList.get(position));
        Intent intent = new Intent();
        intent.setClass(ShareHouseActivity.this, ShareInfoActivity.class);
        intent.putExtra("shareStr", shareStr1);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sharehouse_top:
                if (flag == true) {
//                mWindowManager.removeView(tv_Float);
                    createFloatTextView();
                    flag = false;
                }
                break;
            case R.id.img_sharehouse_refresh:
                if (flag == true) {
//                mWindowManager.removeView(tv_Float);
                    createFloatTextView();
                    flag = false;
                }
                if (shareDtoList != null && shareDtoList.size() > 0) {
                    shareDtoList.clear();
                }
                initData();
                break;
            case R.id.img_sharehouse_back:
                mWindowManager.removeView(tv_Float);
                finish();
                break;
            case R.id.img_sharehouse_myinfo:
                flag = true;
                mWindowManager.removeView(tv_Float);

                Intent intent = new Intent();
                intent.setClass(ShareHouseActivity.this, UserShareHouseInfoActivity.class);
                intent.putExtra("userId", userInfo.getId()+"");
                intent.putExtra("where", "ShareHouse");
                startActivity(intent);

                break;

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            Log.d("testRun", "result======" + result);
            CustomWaitDialog.miss();
             shareDtoList = gson.fromJson(result, new TypeToken<List<ShareDto>>() {}.getType());
            switch (msg.what) {
                case 0:
                    if (shareDtoList != null && shareDtoList.size() > 0) {
                        shareHouseAdapter = new ShareHouseAdapter(ShareHouseActivity.this,shareDtoList );
                        listView.setAdapter(shareHouseAdapter);
                        setListViewHeight(listView);
                    }else {
                        Toast.makeText(ShareHouseActivity.this, "没有帖子", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

        }
    };



    //初始化悬浮位置
    private void initFloatImage() {
        // 获取WindowManager
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        System.out.println("*************"+wmParams.y);
        // 设置悬浮窗口长宽数据
        wmParams.width = 100;
        wmParams.height = 100;
    }

    /**
     * 创建悬浮文字按钮
     */
    private void createFloatTextView() {
        tv_Float = new TextView(this);
//        tv_Float.setImageResource(R.drawable.addshare);
//        tv_Float.setAlpha(200);
        tv_Float.setText("发帖");
        tv_Float.setGravity(Gravity.CENTER);
        tv_Float.setBackgroundResource(R.drawable.addshare);
        tv_Float.setTextColor(getResources().getColor(R.color.white));
        tv_Float.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 点击悬浮图片的事件
//                Toast.makeText(ShareHouseActivity.this, "点击", Toast.LENGTH_SHORT).show();
                flag = true;
                mWindowManager.removeView(tv_Float);
                Intent intent = new Intent();
                intent.setClass(ShareHouseActivity.this, SendShareInfoActivity.class);
                startActivity(intent);
            }
        });
        // 调整悬浮窗口位置
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        // 显示myFloatView图像
        mWindowManager.addView(tv_Float, wmParams);
    }

    /**
     * 创建悬浮图片按钮
     */
    private void createFloatView() {
        img_Float = new ImageView(this);
        img_Float.setImageResource(R.drawable.addshare);
        img_Float.setAlpha(200);
        img_Float.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 点击悬浮图片的事件
                Toast.makeText(ShareHouseActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });
        // 调整悬浮窗口
        wmParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL ;

        // 显示myFloatView图像
        mWindowManager.addView(img_Float, wmParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            mWindowManager.removeView(img_Float);
            mWindowManager.removeView(tv_Float);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
