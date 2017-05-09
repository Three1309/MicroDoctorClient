package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class MyInfoActivity extends Activity {

    private String userDataStr;
    private UserInfo userInfo;

    private LinearLayout ll_one;
    private LinearLayout ll_two;
    private ImageView img_back;
    private TextView tv_updateinfo;

    private TextView tv_account;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        initData();
    }

    private void initView(){
        userDataStr= SharedPrefsUtil.getValue(MyInfoActivity.this, APPConfig.USERDATA, "");
        Gson gson=new Gson();
        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        ll_one = (LinearLayout) this.findViewById(R.id.amyinfo_ll_one);
        ll_two = (LinearLayout) this.findViewById(R.id.amyinfo_ll_two);
        img_back = (ImageView) this.findViewById(R.id.amyinfo_img_back);
        tv_updateinfo = (TextView) this.findViewById(R.id.amyinfo_tv_updateinfo);

        tv_account = (TextView) this.findViewById(R.id.amyinfo_tv_account);
        tv_name = (TextView) this.findViewById(R.id.amyinfo_tv_name);
        tv_nickname = (TextView) this.findViewById(R.id.amyinfo_tv_nickname);
        tv_age=(TextView) this.findViewById(R.id.amyinfo_tv_age);
        tv_gender = (TextView) this.findViewById(R.id.amyinfo_tv_gender);
        tv_phone = (TextView) this.findViewById(R.id.amyinfo_tv_phone);
        tv_address=(TextView) this.findViewById(R.id.amyinfo_tv_address);
        tv_signature = (TextView) this.findViewById(R.id.amyinfo_tv_signature);
        tv_introduction=(TextView) this.findViewById(R.id.amyinfo_tv_introduction);
        tv_hospital = (TextView) this.findViewById(R.id.amyinfo_tv_hospital);
        tv_office=(TextView) this.findViewById(R.id.amyinfo_tv_office);
        tv_amount = (TextView) this.findViewById(R.id.amyinfo_tv_amount);
        tv_likenum=(TextView) this.findViewById(R.id.amyinfo_tv_likenum);

    }

    private void initData(){
        tv_account.setText("账号："+userInfo.getPhone());
        tv_name.setText(userInfo.getName());
        tv_nickname.setText(userInfo.getNickname());
        tv_age.setText(userInfo.getAge()+"");
        if (userInfo.getGender() == 1) {
            tv_gender.setText("男");
        }else {
            tv_gender.setText("女");
        }
        tv_phone.setText(userInfo.getPhone());
        tv_address.setText(userInfo.getAddress());
        tv_signature.setText(userInfo.getSignature());
        tv_introduction.setText(userInfo.getIntroduction());

        if (userInfo.getType() == 1) {
            tv_hospital.setText(userInfo.getHospital());
            tv_office.setText(userInfo.getOffice());
            tv_amount.setText(userInfo.getAmount()+"");
            tv_likenum.setText(userInfo.getLikenum()+"");
        }
        else{
            ll_one.setVisibility(View.GONE);
            ll_two.setVisibility(View.GONE);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyInfoActivity.this,UpdateMyInfoActivity.class );
                startActivity(intent);
                finish();
            }
        });

    }
}
