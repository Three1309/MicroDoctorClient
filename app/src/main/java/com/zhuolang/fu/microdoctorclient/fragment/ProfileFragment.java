package com.zhuolang.fu.microdoctorclient.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.zhuolang.fu.microdoctorclient.DemoApplication;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AgreeRegisterDoctotActivity;
import com.zhuolang.fu.microdoctorclient.activity.LoginActivity;
import com.zhuolang.fu.microdoctorclient.activity.MyInfoActivity;
import com.zhuolang.fu.microdoctorclient.activity.RegisterDoctorActivity;
import com.zhuolang.fu.microdoctorclient.activity.SettingsActivity;
import com.zhuolang.fu.microdoctorclient.activity.UpdatePhoneActivity;
import com.zhuolang.fu.microdoctorclient.activity.UpdatePswActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private LinearLayout ll_myinfo;
    private LinearLayout ll_setting;
    private LinearLayout ll_updatepsw;
    private LinearLayout ll_updatephone;
    private LinearLayout ll_registerdoctor;
    private LinearLayout ll_finish;
    private LinearLayout ll_logout;
    private TextView tv_name;
    private TextView tv_account;
    private TextView tv_typename;
    private TextView tv_regdoctor;


    private String userDataStr;
    private UserInfo userInfo;
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=new View(getActivity());
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d("activityID", "这个是SettingsFragment ----- : " + this.toString());

        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        userDataStr= SharedPrefsUtil.getValue(getActivity(), APPConfig.USERDATA, "");
        Log.d("activityID", "这个是SettingsFragment ----- : " + userDataStr);
        userInfo=gson.fromJson(userDataStr,UserInfo.class);

        ll_myinfo = (LinearLayout) view.findViewById(R.id.fprofile_ll_myinfo);
        ll_updatephone = (LinearLayout) view.findViewById(R.id.fprofile_ll_updatephone);
        ll_updatepsw = (LinearLayout) view.findViewById(R.id.fprofile_ll_updatepsw);
        ll_registerdoctor = (LinearLayout) view.findViewById(R.id.fprofile_ll_registerdoc);
        ll_finish= (LinearLayout) view.findViewById(R.id.fprofile_ll__finish);
        ll_logout= (LinearLayout) view.findViewById(R.id.fprofile_ll_logout);
        ll_setting= (LinearLayout) view.findViewById(R.id.fprofile_ll_setting);


        tv_account = (TextView) view.findViewById(R.id.fprofile_tv_account);
        tv_name = (TextView) view.findViewById(R.id.fprofile_tv_name);
        tv_typename = (TextView) view.findViewById(R.id.fprofile_tv_type);
        tv_regdoctor = (TextView) view.findViewById(R.id.fprofile_tv_regdoctor);

        ll_logout.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        ll_registerdoctor.setOnClickListener(this);
        ll_updatepsw.setOnClickListener(this);
        ll_updatephone.setOnClickListener(this);
        ll_myinfo.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    private void initData(){
        tv_account.setText("账号："+userInfo.getPhone());
        tv_name.setText(userInfo.getName());
        if (userInfo.getType() == 2) {
            tv_typename.setText("身份：管理员");
            tv_regdoctor.setText("验证医师");
        }
        else if (userInfo.getType() == 1) {
            tv_typename.setText("身份：医师");
        }
        else
            tv_typename.setText("身份：普通用户");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fprofile_ll_myinfo:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.fprofile_ll_setting:
                Intent intent0 = new Intent();
                intent0.setClass(getActivity(), SettingsActivity.class);
                startActivity(intent0);
                break;
            case R.id.fprofile_ll__finish:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否结束体验，退出程序？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.fprofile_ll_logout:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(getActivity());
                dialog1.setTitle("温馨提示");
                dialog1.setMessage("是否注销切换账号？");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefsUtil.putValue(getActivity(), APPConfig.IS_LOGIN, false);
//                        SharedPrefsUtil.putValue(getActivity(), APPConfig.ACCOUNT, "");
//                        SharedPrefsUtil.putValue(getActivity(), APPConfig.USERDATA, gson.toJson(new UserInfo()));

                        logout();
                        Intent intent2 = new Intent();
                        intent2.setClass(getActivity(), LoginActivity.class);
                        startActivity(intent2);
                        getActivity().finish();
                    }
                });
                dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog1.show();
                break;
            case R.id.fprofile_ll_updatepsw:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), UpdatePswActivity.class);
                startActivity(intent2);
                break;
            case R.id.fprofile_ll_updatephone:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), UpdatePhoneActivity.class);
                startActivity(intent3);
                break;
            case R.id.fprofile_ll_registerdoc:
                if (userInfo.getType() == 2) {
                    Intent intent5 = new Intent();
                    intent5.setClass(getActivity(), AgreeRegisterDoctotActivity.class);
                    startActivity(intent5);
                }else {
                    Intent intent4 = new Intent();
                    intent4.setClass(getActivity(), RegisterDoctorActivity.class);
                    startActivity(intent4);
                }
                break;
        }
    }

    private void logout() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoApplication.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
//                        finish();
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getContext(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }
}
