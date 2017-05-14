package com.zhuolang.fu.microdoctorclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentMeHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentMeListActivity;
import com.zhuolang.fu.microdoctorclient.activity.DoctorAppointmentHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.HealthKnowledgeListActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class CalendarFragment extends Fragment  implements View.OnClickListener {

    private View view;
    private ListView listView;

    private LinearLayout ll_healthknowledge;
    private LinearLayout ll_doctorq;
    private LinearLayout ll_appointmentme;
    private LinearLayout ll_mysee;

    private Gson gson = new Gson();
    private String userDataStr;
    UserInfo userInfo = new UserInfo();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        userDataStr= SharedPrefsUtil.getValue(getActivity(), APPConfig.USERDATA, "");
        Log.d("activityID", "这个CalendarFragment ----- : " + userDataStr);
        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        ll_healthknowledge = (LinearLayout) view.findViewById(R.id.fcalendar_ll_health);
        ll_doctorq = (LinearLayout) view.findViewById(R.id.ll_fcalendar_doctormodle);
        ll_appointmentme = (LinearLayout) view.findViewById(R.id.fcalendar_ll_appointmentme);
        ll_mysee = (LinearLayout) view.findViewById(R.id.fcalendar_ll_seehistory);


        ll_appointmentme.setOnClickListener(this);
        ll_mysee.setOnClickListener(this);
        ll_healthknowledge.setOnClickListener(this);

        if (userInfo.getType() != 1) {
            ll_doctorq.setVisibility(View.GONE);
        }
    }

    private void initData(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fcalendar_ll_health:
                Intent intent = new Intent();
                intent.setClass(getActivity(), HealthKnowledgeListActivity.class);
                startActivity(intent);
                break;
            case R.id.fcalendar_ll_appointmentme:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), AppointmentMeListActivity.class);
                startActivity(intent1);
                break;
            case R.id.fcalendar_ll_seehistory:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), AppointmentMeHistoryListActivity.class);
                startActivity(intent2);
                break;
        }
    }

}
