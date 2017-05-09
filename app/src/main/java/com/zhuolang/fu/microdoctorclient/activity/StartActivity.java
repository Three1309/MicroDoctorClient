package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class StartActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean is_login= SharedPrefsUtil.getValue(StartActivity.this, APPConfig.IS_LOGIN,false);
                Intent intent = new Intent();
                if (is_login)
                    intent.setClass(StartActivity.this, MenuActivity.class);
                else
                    intent.setClass(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000); //停留2秒钟

    }
}
