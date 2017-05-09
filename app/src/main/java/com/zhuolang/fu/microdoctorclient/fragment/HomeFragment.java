package com.zhuolang.fu.microdoctorclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.zhuolang.fu.microdoctorclient.view.ResideMenu;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ResideMenu resideMenu;
    private ImageView img_showdoctor;
    private ImageView img_appointmentdoctor;
    private ImageView img_mynowappointment;
    private ImageView img_myappointmenthist;

    private String userDataStr;
    private UserInfo userInfo;
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setUpViews();

        initView(view);
        initData();

        return view;
    }

    private void initView(View view){
//        userDataStr= SharedPrefsUtil.getValue(getActivity(), APPConfig.USERDATA, "");
//        Log.d("activityID", "这个是HomeFragment ----- : ");
//        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        img_showdoctor = (ImageView) view.findViewById(R.id.image_fhome_showdoctor);
        img_appointmentdoctor = (ImageView) view.findViewById(R.id.image_fhome_appointmentdoctor);
        img_mynowappointment = (ImageView) view.findViewById(R.id.image_fhome_mynowappointment);
        img_myappointmenthist = (ImageView) view.findViewById(R.id.image_fhome_appointhistory);

        img_myappointmenthist.setOnClickListener(this);
        img_mynowappointment.setOnClickListener(this);
        img_showdoctor.setOnClickListener(this);
        img_appointmentdoctor.setOnClickListener(this);


    }

    private void initData(){

    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

//        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//            }
//        });

        // add gesture operation's ignored views
//        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
//        resideMenu.addIgnoredView(ignored_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_fhome_showdoctor:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AllDoctorListActivity.class);
                startActivity(intent);
                break;
            case R.id.image_fhome_appointmentdoctor:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), AppointmentDoctorOfficeActivity.class);
                startActivity(intent1);
                break;
            case R.id.image_fhome_mynowappointment:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), MyNowAppointListActivity.class);
                startActivity(intent2);
                break;
            case R.id.image_fhome_appointhistory:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), MyAppointHistoryListActivity.class);
                startActivity(intent3);
                break;

        }
    }
}
