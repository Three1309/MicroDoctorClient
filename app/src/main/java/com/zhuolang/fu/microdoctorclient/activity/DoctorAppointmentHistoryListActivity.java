package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.MyAppointListAdapter;
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzg on 2016/11/3.
 */
public class DoctorAppointmentHistoryListActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<AppointmentDto> appointmentDtos = new ArrayList<>();
    private ImageView img_back;
    private MyAppointListAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappointmenthistorylist);

        listView = (ListView) findViewById(R.id.lv_doctorappointmenthistory_list);
        listView.setOnItemClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_doctorappointmenthistory_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initMotion();
    }

    public void initMotion() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.setClass(AppointmentMeListActivity.this, MyAppointmentDetailActivity.class);
//        AppointmentDto appointmentDto = appointmentDtos.get(position);
//        Gson gson = new Gson();
//        String doctorDtoStr = gson.toJson(appointmentDto);
////        Log.d("testRun", "doctorDtoStr==========" + doctorDtoStr);
//        intent.putExtra("doctorDtoStr", doctorDtoStr);
//        startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
//            Log.d("testRun", "result======" + result);
//            Gson gson = new Gson();
//            appointmentDtos = gson.fromJson(result, new TypeToken<List<AppointmentDto>>() {}.getType());
//            adapter = new MyAppointListAdapter(AppointmentMeListActivity.this, appointmentDtos);
//            listView.setAdapter(adapter);
        }
    };

}
