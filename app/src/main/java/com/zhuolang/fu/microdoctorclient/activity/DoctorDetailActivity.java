package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class DoctorDetailActivity extends Activity {

    private ImageView img_back;
    private String userDataStr;
    private UserInfo userInfo;

    private TextView ask;
    private TextView tv_name;
    private TextView tv_nickname;
    private TextView tv_age;
    private TextView tv_gender;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView tv_signature;
    private TextView tv_introduction;
    private TextView tv_hospital;
    private TextView tv_office;
    private TextView tv_amount;
    private TextView tv_likenum;
    private TextView tv_type;
    private LinearLayout ll_two;
    private String flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctordetail);
        userDataStr = getIntent().getStringExtra("doctorStr");
        flag= getIntent().getStringExtra("flag");
//        Log.d("testRun", "zhe这里"+userDataStr);

        initView();
        initData();
    }

    private void initView(){
        Gson gson=new Gson();
        userInfo=gson.fromJson(userDataStr,UserInfo.class);
        
        img_back = (ImageView) findViewById(R.id.img_doctordetail_back);

        ask = (TextView) this.findViewById(R.id.doctordetail_tv_ask);
        tv_phone = (TextView) this.findViewById(R.id.doctordetail_tv_account);
        tv_name = (TextView) this.findViewById(R.id.doctordetail_tv_name);
        tv_nickname = (TextView) this.findViewById(R.id.doctordetail_tv_nickname);
        tv_address=(TextView) this.findViewById(R.id.doctordetail_tv_address);
        tv_age=(TextView) this.findViewById(R.id.doctordetail_tv_age);
        tv_gender = (TextView) this.findViewById(R.id.doctordetail_tv_gender);
        tv_signature = (TextView) this.findViewById(R.id.doctordetail_tv_signature);
        tv_introduction=(TextView) this.findViewById(R.id.doctordetail_tv_introduction);
        tv_hospital = (TextView) this.findViewById(R.id.doctordetail_tv_hospital);
        tv_office=(TextView) this.findViewById(R.id.doctordetail_tv_office);
        tv_amount = (TextView) this.findViewById(R.id.doctordetail_tv_amount);
        tv_likenum=(TextView) this.findViewById(R.id.doctordetail_tv_likenum);

        tv_type=(TextView) this.findViewById(R.id.doctordetail_tv_type);
        ll_two = (LinearLayout) this.findViewById(R.id.doctordetail_ll_two);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo.getPhone().equals(SharedPrefsUtil.getValue(DoctorDetailActivity.this, APPConfig.ACCOUNT, ""))) {
                    Toast.makeText(DoctorDetailActivity.this,"不能向自己咨询",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(DoctorDetailActivity.this, ChatToDoctorActivity.class);
                    intent.putExtra("username", userInfo.getPhone());
                    intent.putExtra("username1", userInfo.getName());
                    startActivity(intent);
                }

            }
        });
    }

    private void initData() {
        tv_phone.setText("手机号：" + userInfo.getPhone());
        tv_name.setText(userInfo.getName());
        tv_nickname.setText(userInfo.getNickname());
        tv_address.setText(userInfo.getAddress());
        tv_age.setText(userInfo.getAge() + "");
        if (userInfo.getGender() == 1) {
            tv_gender.setText("男");
        } else {
            tv_gender.setText("女");
        }
        tv_signature.setText(userInfo.getSignature());
        tv_introduction.setText(userInfo.getIntroduction());
        tv_hospital.setText(userInfo.getHospital());
        tv_office.setText(userInfo.getOffice());
        tv_amount.setText(userInfo.getAmount() + "");
        tv_likenum.setText(userInfo.getLikenum() + "");
        if (flag.equals("true")) {
            tv_type.setText("身份：待验证医师身份");
            ll_two.setVisibility(View.GONE);
        }
    }

}
