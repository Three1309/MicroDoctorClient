package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.HealthKnowledgeAdapter;
import com.zhuolang.fu.microdoctorclient.model.HealthKnowledge;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;
import com.zhuolang.fu.microdoctorclient.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class HealthKnowledgeListActivity extends Activity implements AdapterView.OnItemClickListener{
	private ListView mListView;
	private ArrayAdapter<String> mAdapter;
	private List<HealthKnowledge> healthKnowledgeList = new ArrayList<HealthKnowledge>();
	private HealthKnowledgeAdapter healthKnowledgeAdapter;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private ImageView img_back;
	Gson gson = new Gson();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthknowledgelist);
//		geneItems();
		img_back = (ImageView) findViewById(R.id.healthknowledge_img_back);
		mListView = (ListView) findViewById(R.id.healthknowledge_xListView);
//		mListView.setPullLoadEnable(true);
//		healthKnowledgeAdapter = new HealthKnowledgeAdapter(HealthKnowledgeListActivity.this,healthKnowledgeList );
//		mListView.setAdapter(healthKnowledgeAdapter);
//		mAdapter = new ArrayAdapter<String>(this, R.layout.item_healthknowledge, items);
//		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
//		mListView.setXListViewListener(this);
//		mHandler = new Handler();
		img_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		initMotion();
	}

//	private void geneItems() {
////		for (int i = 0; i != 20; ++i) {
////			items.add("refresh cnt " + (++start));
////		}
//	}

//	private void onLoad() {
//		mListView.stopRefresh();
//		mListView.stopLoadMore();
//		mListView.setRefreshTime("刚刚");
//	}
//
//	@Override
//	public void onRefresh() {
////		mHandler.postDelayed(new Runnable() {
////			@Override
////			public void run() {
////				start = ++refreshCnt;
////				items.clear();
////				geneItems();
////				// mAdapter.notifyDataSetChanged();
////				mAdapter = new ArrayAdapter<String>(HealthKnowledgeListActivity.this, R.layout.list_item, items);
////				mListView.setAdapter(mAdapter);
////				onLoad();
////			}
////		}, 2000);
//	}
//
//	@Override
//	public void onLoadMore() {
////		mHandler.postDelayed(new Runnable() {
////			@Override
////			public void run() {
////				geneItems();
////				mAdapter.notifyDataSetChanged();
////				onLoad();
////			}
////		}, 2000);
//	}

	public void initMotion() {
		final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
		OkHttpUtils.Param typeParam = new OkHttpUtils.Param("rows", "20");
		list.add(typeParam);
        CustomWaitDialog.show(HealthKnowledgeListActivity.this, "连接服务中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
//				CustomWaitDialog.show(HealthKnowledgeListActivity.this,"正在查找。。。");
				//post方式连接  url
				OkHttpUtils.post("http://www.tngou.net/api/info/news", new OkHttpUtils.ResultCallback() {
					@Override
					public void onSuccess(Object response) {
						Message message = new Message();
						message.what = 0;
						message.obj = response;
						String healthDataStr= response.toString();
						Log.d("testrun", "HealthKnowledgeListActivity : "+healthDataStr);
						healthKnowledgeList=gson.fromJson(healthDataStr, new TypeToken<List<HealthKnowledge>>() {}.getType());
//						if (userDataStr.equals("nodata")) {
//							Toast.makeText(HealthKnowledgeListActivity.this, "没有医师信息", Toast.LENGTH_SHORT).show();
//						}else {
							//将userData的json串直接缓存到本地
//                        doctorList= (List<UserInfo>) gson.fromJson(userDataStr,UserInfo.class);
//							doctorList = gson.fromJson(userDataStr, new TypeToken<List<UserInfo>>() {}.getType());
							handler.sendMessage(message);

//						}
					}

					@Override
					public void onFailure(Exception e) {
						Toast.makeText(HealthKnowledgeListActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                        CustomWaitDialog.miss();
					}
				},list);
			}
		}).start();
	}

	//    事件处理监听器方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Intent intent = new Intent();
//		intent.setClass(AllDoctorListActivity.this, DoctorDetailActivity.class);
//		UserInfo doctorInfo = doctorList.get(position);
//		Gson gson = new Gson();
//		String doctorDtoStr = gson.toJson(doctorInfo);
//		intent.putExtra("doctorStr", doctorDtoStr);
//		intent.putExtra("flag", "false");
//		startActivity(intent);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			if (healthKnowledgeList != null && healthKnowledgeList.size() > 0) {
				healthKnowledgeAdapter = new HealthKnowledgeAdapter(HealthKnowledgeListActivity.this,healthKnowledgeList );
				mListView.setAdapter(healthKnowledgeAdapter);

			}
			CustomWaitDialog.miss();
		}
	};
}