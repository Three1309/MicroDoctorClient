package com.zhuolang.fu.microdoctorclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AllDoctorListActivity;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentDoctorOfficeActivity;
import com.zhuolang.fu.microdoctorclient.activity.MenuActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyAppointHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyInfoActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyNowAppointListActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.ChangeColorIconWithText;
import com.zhuolang.fu.microdoctorclient.view.CustomViewPager;
import com.zhuolang.fu.microdoctorclient.view.ResideMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class HomeFragmentTab extends Fragment implements ViewPager.OnPageChangeListener{

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

    HomeFragment homepageTabFragment = new HomeFragment();
    CalendarFragment shareTabFragment = new CalendarFragment();
    ProfileFragment meTabFragment = new ProfileFragment();

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        setUpViews();
        initView(view);//获取viewpager
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
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
            }
        });
        return view;
    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
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
        mTabs.add(meTabFragment);


        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {

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
    private void initView(View view){
//        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mViewPager = (CustomViewPager) view.findViewById(R.id.id_viewpager);
        one = (ChangeColorIconWithText) view.findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        two = (ChangeColorIconWithText) view.findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        three = (ChangeColorIconWithText) view.findViewById(R.id.id_indicator_three);
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
}
