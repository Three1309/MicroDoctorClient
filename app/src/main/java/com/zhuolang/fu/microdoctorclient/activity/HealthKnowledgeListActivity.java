package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.HealthKnowledgeAdapter;
import com.zhuolang.fu.microdoctorclient.model.HealthInfo;
import com.zhuolang.fu.microdoctorclient.model.HealthKnowledge;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;


public class HealthKnowledgeListActivity extends Activity implements AdapterView.OnItemClickListener{

	private ListView mListView;
	private HealthInfo healthInfoList;
	private HealthKnowledgeAdapter healthKnowledgeAdapter;
	private ImageView img_back;
	Gson gson = new Gson();
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthknowledgelist);

		img_back = (ImageView) findViewById(R.id.healthknowledge_img_back);
		mListView = (ListView) findViewById(R.id.healthknowledge_xListView);
		mListView.setOnItemClickListener(this);
		initMotion();
		img_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	public void initMotion() {
//		final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
//		OkHttpUtils.Param typeParam = new OkHttpUtils.Param("rows", "20");
//		list.add(typeParam);
        CustomWaitDialog.show(HealthKnowledgeListActivity.this, "连接服务中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
//				CustomWaitDialog.show(HealthKnowledgeListActivity.this,"正在查找。。。");
				//post方式连接  url http://www.tngou.net/api/info/news
				OkHttpUtils.get("http://www.tngou.net/api/lore/news", new OkHttpUtils.ResultCallback() {
					@Override
					public void onSuccess(Object response) {
						Message message = new Message();
						message.what = 0;
						message.obj = response;
						String healthDataStr= response.toString();
						Log.d("testrun", "HealthKnowledgeListActivity : "+healthDataStr);
						healthInfoList=(HealthInfo) gson.fromJson(healthDataStr,HealthInfo.class);
						handler.sendMessage(message);

					}

					@Override
					public void onFailure(Exception e) {
						Toast.makeText(HealthKnowledgeListActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                        CustomWaitDialog.miss();
					}
				});
			}
		}).start();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Toast.makeText(HealthKnowledgeListActivity.this, "点击", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(HealthKnowledgeListActivity.this, HealthDetailActivity.class);
		HealthKnowledge healthKnowledge = healthInfoList.getList().get(position);
		Gson gson = new Gson();
		String healthKnowledgeStr = gson.toJson(healthKnowledge);
		intent.putExtra("healthKnowledgeStr", healthKnowledgeStr);
		startActivity(intent);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			CustomWaitDialog.miss();
			Log.d("testrun", "HealthKnowledgeListActivity : "+healthInfoList.getList().get(0).getTitle());
			if (healthInfoList.getList() != null && healthInfoList.getList().size() > 0) {
				healthKnowledgeAdapter = new HealthKnowledgeAdapter(HealthKnowledgeListActivity.this,healthInfoList.getList() );
				mListView.setAdapter(healthKnowledgeAdapter);

			}else {
				Toast.makeText(HealthKnowledgeListActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
			}

		}
	};

}