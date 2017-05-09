package com.zhuolang.fu.microdoctorclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.HealthKnowledgeListActivity;

import java.util.ArrayList;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class CalendarFragment extends Fragment  implements View.OnClickListener {

    private View view;
    private ListView listView;

    private LinearLayout ll_healthknowledge;
    private LinearLayout ll_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
//        userDataStr= SharedPrefsUtil.getValue(getActivity(), APPConfig.USERDATA, "");
//        Log.d("activityID", "这个是SettingsFragment ----- : " + userDataStr);
//        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        ll_healthknowledge = (LinearLayout) view.findViewById(R.id.fcalendar_ll_health);

        ll_healthknowledge.setOnClickListener(this);
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
        }
    }

}
