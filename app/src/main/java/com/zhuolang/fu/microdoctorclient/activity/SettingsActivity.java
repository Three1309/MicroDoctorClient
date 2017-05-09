package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
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

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.fragment.HomeFragment;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class SettingsActivity extends Activity implements View.OnClickListener {

    private View view = null;
    private ImageView img_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        view = inflater.inflate(R.layout.fragment_settings, container, false);
//        Log.d("activityID", "这个是SettingsFragment ----- : " + this.toString());

        initView();
//        return view;
    }

    private void initView() {

        img_back = (ImageView) this.findViewById(R.id.fsettings_img_back);

        img_back.setOnClickListener(this);
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
            case R.id.fsettings_img_back:
                finish();
                break;

            default:
                break;

        }
    }
}
