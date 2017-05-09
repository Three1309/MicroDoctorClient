package com.zhuolang.fu.microdoctorclient.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.fragment.CalendarFragment;
import com.zhuolang.fu.microdoctorclient.fragment.HomeFragment;
import com.zhuolang.fu.microdoctorclient.fragment.ProfileFragment;
import com.zhuolang.fu.microdoctorclient.fragment.SettingsFragment;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.ResideMenu;
import com.zhuolang.fu.microdoctorclient.view.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */

public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    private long exitTime = 0;
    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;

    private LinearLayout ma_ll_test;
//    private ResideMenuItem itemSettings;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (SharedPrefsUtil.getValue(MenuActivity.this, APPConfig.IS_LOGIN, false)) {
            saveUserInfo(SharedPrefsUtil.getValue(MenuActivity.this, APPConfig.ACCOUNT, ""));
        }
        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());
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
                        SharedPrefsUtil.putValue(MenuActivity.this,APPConfig.USERDATA, userData);
                        Log.d("testRun","MenuActivity user信息缓存成功 "+response.toString());

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "网络请求失败------");
                        Toast.makeText(MenuActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.bg);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "主页");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "个人中心");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "发现");
//        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
//        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
         resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
//        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            changeFragment(new HomeFragment());
        }else if (view == itemProfile){
            changeFragment(new ProfileFragment());
        }else if (view == itemCalendar){
            changeFragment(new CalendarFragment());
        }
//        else if (view == itemSettings){
//            changeFragment(new SettingsFragment());
//        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {

                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}