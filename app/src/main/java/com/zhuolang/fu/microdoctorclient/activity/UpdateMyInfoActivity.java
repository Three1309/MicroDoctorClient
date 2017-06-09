package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class UpdateMyInfoActivity extends Activity {

    private String userDataStr;
    private UserInfo userInfo;
    Gson gson=new Gson();

    private LinearLayout ll_one;
    private ImageView img_back;
    private TextView tv_updateinfo;

    private TextView tv_account;
    private EditText et_name;
    private EditText et_nickname;
    private EditText et_age;
    private EditText et_gender;
    private EditText et_phone;
    private EditText et_address;
    private EditText et_signature;
    private EditText et_introduction;
    private EditText et_hospital;
    private EditText et_office;
    private EditText et_amount;
    private EditText et_likenum;

    private String nickname;
    private String name;
    private String age;
    private String gender;
    private String address;
    private String signature;
    private String introduction;
    private String hospital;
    private String office;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatemyinfo);
        initView();
        initData();
    }

    private void initView(){
        userDataStr= SharedPrefsUtil.getValue(UpdateMyInfoActivity.this, APPConfig.USERDATA, "");
        Gson gson=new Gson();
        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        ll_one = (LinearLayout) this.findViewById(R.id.updatemyinfo_ll_one);
        img_back = (ImageView) this.findViewById(R.id.updatemyinfo_img_back);
        tv_updateinfo = (TextView) this.findViewById(R.id.updatemyinfo_et_updateinfo);
        tv_account = (TextView) this.findViewById(R.id.updatemyinfo_tv_account);

        et_name = (EditText) this.findViewById(R.id.updatemyinfo_et_name);
        et_nickname = (EditText) this.findViewById(R.id.updatemyinfo_et_nickname);
        et_age=(EditText) this.findViewById(R.id.updatemyinfo_et_age);
        et_gender = (EditText) this.findViewById(R.id.updatemyinfo_et_gender);
        et_address=(EditText) this.findViewById(R.id.updatemyinfo_et_address);
        et_signature = (EditText) this.findViewById(R.id.updatemyinfo_et_signature);
        et_introduction=(EditText) this.findViewById(R.id.updatemyinfo_et_introduction);
        et_hospital = (EditText) this.findViewById(R.id.updatemyinfo_et_hospital);
        et_office=(EditText) this.findViewById(R.id.updatemyinfo_et_office);

    }

    private void initData(){
        tv_account.setText("账号："+userInfo.getPhone());
        et_name.setText(userInfo.getName());
        et_nickname.setText(userInfo.getNickname());
        et_age.setText(userInfo.getAge()+"");
        if (userInfo.getGender() == 1) {
           et_gender.setText("男");
        }else if (userInfo.getGender() == 0){
            et_gender.setText("女");
        }
        et_address.setText(userInfo.getAddress());
        et_signature.setText(userInfo.getSignature());
        et_introduction.setText(userInfo.getIntroduction());

        if (userInfo.getType() == 1) {
            et_hospital.setText(userInfo.getHospital());
            et_office.setText(userInfo.getOffice());
        }
        else{
            ll_one.setVisibility(View.GONE);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UpdateMyInfoActivity.this,MyInfoActivity.class );
                startActivity(intent);
                finish();
            }
        });
        tv_updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(UpdateMyInfoActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else {

//                    //此方法传入一个字符串String类型的参数，返回成功或失败的一个Boolean类型的返回值
//                    EMClient.getInstance().updateCurrentUserNick(name);

                    nickname = et_nickname.getText().toString().trim();
                    age = et_age.getText().toString().trim();
                    gender = et_gender.getText().toString().trim();
                    if (gender.equals("女")) {
                        gender = "0";
                    } else {
                        gender = "1";
                    }
                    address = et_address.getText().toString().trim();
                    signature = et_signature.getText().toString().trim();
                    introduction = et_introduction.getText().toString().trim();
                    if (userInfo.getType() == 1) {
                        hospital = et_hospital.getText().toString().trim();
                        office = et_office.getText().toString().trim();
                    } else {
                        hospital = "";
                        office = "";
                    }

                    final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param userIdParam = new OkHttpUtils.Param("id", "" + userInfo.getId());
                    OkHttpUtils.Param nicknameParam = new OkHttpUtils.Param("nickname", nickname);
                    OkHttpUtils.Param nameParam = new OkHttpUtils.Param("name", name);
                    OkHttpUtils.Param genderParam = new OkHttpUtils.Param("gender", gender);
                    OkHttpUtils.Param addressParam = new OkHttpUtils.Param("address", address);
                    OkHttpUtils.Param signatureParam = new OkHttpUtils.Param("signature", signature);
                    OkHttpUtils.Param introductionParam = new OkHttpUtils.Param("introduction", introduction);
                    OkHttpUtils.Param ageParam = new OkHttpUtils.Param("age", age);
                    OkHttpUtils.Param typeParam = new OkHttpUtils.Param("type", userInfo.getType() + "");
                    OkHttpUtils.Param hospitalParam;
                    OkHttpUtils.Param officeParam;
                    if (userInfo.getType() == 1) {
                        hospitalParam = new OkHttpUtils.Param("hospital", hospital);
                        officeParam = new OkHttpUtils.Param("office", office);
                    } else {
                        hospitalParam = new OkHttpUtils.Param("hospital", "");
                        officeParam = new OkHttpUtils.Param("office", "");
                    }

                    list.add(userIdParam);
                    list.add(nicknameParam);
                    list.add(nameParam);
                    list.add(genderParam);
                    list.add(addressParam);
                    list.add(signatureParam);
                    list.add(introductionParam);
                    list.add(ageParam);
                    list.add(typeParam);
                    if (userInfo.getType() == 1) {
                        list.add(hospitalParam);
                        list.add(officeParam);
                    }

//                CustomWaitDialog.show(UpdateMyInfoActivity.this,"修改信息中...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("testRun", "UpdateMyInfoActivity---------");
                            //post方式连接  url

                            OkHttpUtils.post(APPConfig.updateUser, new OkHttpUtils.ResultCallback() {
                                @Override
                                public void onSuccess(Object response) {
                                    if (response.equals("update_success")) {
                                        Toast.makeText(UpdateMyInfoActivity.this, "信息修改成功！", Toast.LENGTH_SHORT).show();
                                        userInfo.setNickname(nickname);
                                        userInfo.setName(name);
                                        userInfo.setGender(Integer.parseInt(gender));
                                        userInfo.setAddress(address);
                                        userInfo.setSignature(signature);
                                        userInfo.setIntroduction(introduction);
                                        userInfo.setAge(Integer.parseInt(age));
                                        userInfo.setHospital(hospital);
                                        userInfo.setOffice(office);

                                        SharedPrefsUtil.putValue(UpdateMyInfoActivity.this, APPConfig.USERDATA, gson.toJson(userInfo));
                                        Log.d("testRun", "UpdateMyInfoActivity  userJson==" + gson.toJson(userInfo));

                                        Intent intent1 = new Intent();
                                        intent1.setClass(UpdateMyInfoActivity.this, MyInfoActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    } else {
                                        Toast.makeText(UpdateMyInfoActivity.this, "信息修改失败，请重试！", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d("testRun", "请求失败loginActivity----new Thread(new Runnable() {------");
                                    Toast.makeText(UpdateMyInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();

                                }
                            }, list);
                        }
                    }).start();
                }
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent1 = new Intent();
            intent1.setClass(UpdateMyInfoActivity.this, MyInfoActivity.class);
            startActivity(intent1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
   
}
