package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.model.HealthKnowledge;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.Date;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class HealthDetailActivity extends Activity {

    private ImageView img_back;
    private String healthKnowledgeStr;
    private HealthKnowledge healthKnowledge;

    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_message;
    private TextView tv_description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthdetail);
        healthKnowledgeStr = getIntent().getStringExtra("healthKnowledgeStr");
//        Log.d("testRun", "zhe这里"+userDataStr);

        initView();
        initData();
    }

    private void initView(){
        Gson gson=new Gson();
        healthKnowledge=gson.fromJson(healthKnowledgeStr,HealthKnowledge.class);
        
        img_back = (ImageView) findViewById(R.id.iv_healthdetail_back);

        tv_title = (TextView) this.findViewById(R.id.tv_healthdetail_title);
        tv_time = (TextView) this.findViewById(R.id.tv_healthdetail_time);
        tv_message = (TextView) this.findViewById(R.id.tv_healthdetail_content);
        tv_description = (TextView) this.findViewById(R.id.tv_healthdetail_description);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        tv_title.setText(healthKnowledge.getTitle());
        Date date = TimeUtil.longToDate(healthKnowledge.getTime()+"");
        tv_time.setText(TimeUtil.dateToString(date)+"");
        tv_message.setText("内容："+healthKnowledge.getMessage());
        tv_description.setText("简介："+healthKnowledge.getDescription());
    }

}
