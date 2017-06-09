package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.adapter.NewFriendsMsgAdapter;
import com.zhuolang.fu.microdoctorclient.db.InviteMessage;
import com.zhuolang.fu.microdoctorclient.db.InviteMessgeDao;

import java.util.List;


/**
 * 申请与通知
 *
 */
public class AddFriendBeizuActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriendbeizu);
		findViewById(R.id.addbeizu_img_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
	}
	
	
}
