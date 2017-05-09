package com.zhuolang.fu.microdoctorclient.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.LoginActivity;
import com.zhuolang.fu.microdoctorclient.activity.UpdatePswActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{

    private View view = null;
    private LinearLayout ll_finish;
    private LinearLayout ll_updatepsw;
    private LinearLayout ll_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=new View(getActivity());
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        Log.d("activityID", "这个是SettingsFragment ----- : " + this.toString());

        initView(view);
        return view;
    }

    private void initView(View view) {

        ll_finish= (LinearLayout) view.findViewById(R.id.fsettings_ll__finish);
        ll_updatepsw= (LinearLayout) view.findViewById(R.id.fsettings_ll__updatepsw);
        ll_logout= (LinearLayout) view.findViewById(R.id.fsettings_ll_logout);

        ll_logout.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        ll_updatepsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickItem(v);
    }

    /**
     * 点击图片
     *
     * @param v
     */
    private void clickItem(View v) {
        switch (v.getId()) {
            case R.id.fsettings_ll__updatepsw:
                Intent intent = new Intent();
                intent.setClass(getActivity(), UpdatePswActivity.class);
                startActivity(intent);
                break;
            case R.id.fsettings_ll__finish:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否结束体验，退出程序？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
            case R.id.fsettings_ll_logout:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(getActivity());
                dialog1.setTitle("温馨提示");
                dialog1.setMessage("是否注销切换账号？");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefsUtil.putValue(getActivity(), APPConfig.IS_LOGIN, false);
                        SharedPrefsUtil.putValue(getActivity(), APPConfig.ACCOUNT, "");
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

            default:
                break;

        }
    }
}
