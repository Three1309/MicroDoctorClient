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
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AllDoctorListActivity;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentDoctorOfficeActivity;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentMeHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentMeListActivity;
import com.zhuolang.fu.microdoctorclient.activity.HealthKnowledgeListActivity;
import com.zhuolang.fu.microdoctorclient.activity.MenuActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyAppointHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyInfoActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyNowAppointListActivity;
import com.zhuolang.fu.microdoctorclient.activity.ShareHouseActivity;
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
    private ImageView img_sharehouse;
    private ImageView img_askdoctor;
    private ImageView img_health;
    private ImageView img_appme;
    private ImageView img_seepatient;

    private String userDataStr;
    private UserInfo userInfo;
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        setUpViews();

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
        img_sharehouse = (ImageView) view.findViewById(R.id.image_fhome_sharehouse);
        img_askdoctor = (ImageView) view.findViewById(R.id.image_fhome_asklist);
        img_health = (ImageView) view.findViewById(R.id.image_fhome_health);
        img_appme = (ImageView) view.findViewById(R.id.image_fhome_appointmentme);
        img_seepatient = (ImageView) view.findViewById(R.id.image_fhome_seepatient);

        img_appme.setOnClickListener(this);
        img_seepatient.setOnClickListener(this);
        img_health.setOnClickListener(this);
        img_askdoctor.setOnClickListener(this);
        img_sharehouse.setOnClickListener(this);
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
//                intent.putExtra("where","alldoctor");
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
            case R.id.image_fhome_sharehouse:
                Intent intent4 = new Intent();
                intent4.setClass(getActivity(), ShareHouseActivity.class);
                startActivity(intent4);
                break;
            case R.id.image_fhome_asklist:
                Intent intent5 = new Intent();
                intent5.setClass(getActivity(), AllDoctorListActivity.class);
//                intent5.putExtra("where","askdoctor");
                startActivity(intent5);
                break;
            case R.id.image_fhome_health:
                Intent intent6 = new Intent();
                intent6.setClass(getActivity(), HealthKnowledgeListActivity.class);
                startActivity(intent6);
                break;
            case R.id.image_fhome_appointmentme:
                if(SharedPrefsUtil.getValue(getActivity(), APPConfig.TYPE, "").equals("1")){
                    Intent intent7 = new Intent();
                    intent7.setClass(getActivity(), AppointmentMeListActivity.class);
                    startActivity(intent7);
                }else {
                    Toast.makeText(getContext(),"这是医师的权限功能,您没有访问权限",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.image_fhome_seepatient:
                if(SharedPrefsUtil.getValue(getActivity(), APPConfig.TYPE, "").equals("1")){
                    Intent intent8 = new Intent();
                    intent8.setClass(getActivity(), AppointmentMeHistoryListActivity.class);
                    startActivity(intent8);
                }else {
                    Toast.makeText(getContext(),"这是医师的权限功能,您没有访问权限",Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
}
