package com.zhuolang.fu.microdoctorclient.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.fragment.CalendarFragment;
import com.zhuolang.fu.microdoctorclient.fragment.HomeFragment;
import com.zhuolang.fu.microdoctorclient.fragment.ProfileFragment;
import com.zhuolang.fu.microdoctorclient.fragment.ShareTabFragment;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.ChangeColorIconWithText;
import com.zhuolang.fu.microdoctorclient.view.CustomViewPager;
import com.zhuolang.fu.microdoctorclient.view.ResideMenu;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by wunaifu on 2017/4/28.
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

    private View view;
    private ResideMenu resideMenu;
    //    private ViewPager mViewPager;
    private CustomViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private long exitTime = 0;
    private FragmentPagerAdapter mAdapter;
    private ChangeColorIconWithText one;
    private ChangeColorIconWithText two;
    private ChangeColorIconWithText three;
    private ChangeColorIconWithText four;

    HomeFragment homepageTabFragment = new HomeFragment();
    CalendarFragment shareTabFragment = new CalendarFragment();
    ProfileFragment meTabFragment = new ProfileFragment();
    ShareTabFragment share2TabFragment = new ShareTabFragment();

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        if (SharedPrefsUtil.getValue(MainActivity.this, APPConfig.IS_LOGIN, false)) {
            saveUserInfo(SharedPrefsUtil.getValue(MainActivity.this, APPConfig.ACCOUNT, ""));
        }
        initView();//获取viewpager
        initDatas();//初始化数据


        mViewPager.setAdapter(mAdapter);//用adapter为viewpager赋值
        initEvent();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
            }
        });
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
                        SharedPrefsUtil.putValue(MainActivity.this,APPConfig.USERDATA, userData);
                        Gson gson = new Gson();
                        UserInfo userInfo = new UserInfo();
                        userInfo=gson.fromJson(userData,UserInfo.class);
                        SharedPrefsUtil.putValue(MainActivity.this, APPConfig.ACCOUNT, userInfo.getPhone());
                        Log.d("testRun","MenuActivity user信息缓存成功 "+response.toString());

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "网络请求失败------");
                        Toast.makeText(MainActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
    }
    /**
     * 初始化所有事件
     */
    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);

    }
    //为fragment传输数据
    private void initDatas() {

        mTabs.add(homepageTabFragment);
        mTabs.add(shareTabFragment);
        mTabs.add(share2TabFragment);
        mTabs.add(meTabFragment);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
    }

    /*
     *初始化数据和点击事件
     */
    private void initView(){
//        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mViewPager = (CustomViewPager) findViewById(R.id.id_viewpager);
        one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);
        three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);

//        one.setOnClickListener(this);
//        two.setOnClickListener(this);
//        three.setOnClickListener(this);

        one.setIconAlpha(1.0f);

    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs(){
        for (int i = 0; i < mTabIndicators.size(); i++){
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

//    /*
//     *从左到右和从右到左滑动，透明度变化
//     */
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
//        // + positionOffset);
//        if (positionOffset > 0) {
//            ChangeColorIconWithText left = mTabIndicators.get(position);
//            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
//            left.setIconAlpha(1 - positionOffset);
//            right.setIconAlpha(positionOffset);
//        }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
