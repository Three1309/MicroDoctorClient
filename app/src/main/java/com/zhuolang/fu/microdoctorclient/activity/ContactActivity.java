package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhuolang.fu.microdoctorclient.DemoApplication;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.db.EaseUser;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.EaseCommonUtils;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ContactActivity extends Activity {

	protected List<EaseUser> contactList = new ArrayList<EaseUser>();
	protected ListView listView;
	private Map<String, EaseUser> contactsMap;
	private ContactAdapter adapter;
	public String userNameStrAll1;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_contact);

//		findViewById(R.id.contactactivity_img_back).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
		this.findViewById(R.id.btn_add).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ContactActivity.this, AddContactActivity.class));
			}

		});
		listView = (ListView) this.findViewById(R.id.listView);
		getContactList();
		adapter = new ContactAdapter(this, contactList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				startActivity(new Intent(ContactActivity.this,ChatActivity.class).putExtra("username", adapter.getItem(arg2).getUsername()));
//				finish();
				// 进入聊天页面
				Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
				intent.putExtra("username", adapter.getItem(arg2).getUsername());
//				intent.putExtra("username1", userNameStrAll1);
				startActivity(intent);
				finish();
			}

		});

	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
	protected void getContactList() {
		contactList.clear();
		// 获取联系人列表
		contactsMap = DemoApplication.getInstance().getContactList();
		if (contactsMap == null) {
			return;
		}
		synchronized (this.contactsMap) {
			Iterator<Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
			List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
			while (iterator.hasNext()) {
				Entry<String, EaseUser> entry = iterator.next();
				// 兼容以前的通讯录里的已有的数据显示，加上此判断，如果是新集成的可以去掉此判断
				if (!entry.getKey().equals("item_new_friends") && !entry.getKey().equals("item_groups")
						&& !entry.getKey().equals("item_chatroom") && !entry.getKey().equals("item_robots")) {
					if (!blackList.contains(entry.getKey())) {
						// 不显示黑名单中的用户
						EaseUser user = entry.getValue();
						EaseCommonUtils.setUserInitialLetter(user);
//						getUserName(user.getUsername());
//						user.setNickname(userNameStrAll1);
						contactList.add(user);
					}
				}
			}
		}

		// 排序
		Collections.sort(contactList, new Comparator<EaseUser>() {

			@Override
			public int compare(EaseUser lhs, EaseUser rhs) {
				if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
					return lhs.getNick().compareTo(rhs.getNick());
				} else {
					if ("#".equals(lhs.getInitialLetter())) {
						return 1;
					} else if ("#".equals(rhs.getInitialLetter())) {
						return -1;
					}
					return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
				}

			}
		});

	}

	class ContactAdapter extends BaseAdapter {
		private Context context;
		private List<EaseUser> users;
		private LayoutInflater inflater;

		public ContactAdapter(Context context_, List<EaseUser> users) {

			this.context = context_;
			this.users = users;
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return users.size();
		}

		@Override
		public EaseUser getItem(int position) {
			// TODO Auto-generated method stub
			return users.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_contact, parent, false);

			}
			String phone = getItem(position).getUsername();
//			getUserName(phone);
//			Log.d("testRun", "userNameStrAll1------"+userNameStrAll1);
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_name.setText(phone);

			return convertView;
		}

	}

	public void getUserName(String phone){
		final String[] userName=new String[1];
		final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
		OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone",phone);
		list.add(phoneParam);
		//post方式连接  url
		OkHttpUtils.post(APPConfig.findUserByPhone, new OkHttpUtils.ResultCallback() {
			@Override
			public void onSuccess(Object response) {
				Message message = new Message();
				message.what = 0;
				message.obj = response;
				String userData= response.toString();
//				//将userData的json串直接缓存到本地
//				SharedPrefsUtil.putValue(ContactActivity.this,APPConfig.USERDATA, userData);
//				Log.d("testRun","MainActivity user信息缓存成功 "+userData);
//				Gson gson=new Gson();
//				UserInfo userInfo = new UserInfo();
//				userInfo=gson.fromJson(userData,UserInfo.class);
//				if (userInfo != null) {
//					userName[0] =userInfo.getName();
//					Log.d("testRun", "userName[0]------"+userName[0]);
//					return userName[0];
//				}
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Exception e) {
				Log.d("testRun", "网络请求失败------");
//                        CustomWaitDialog.miss();
				Toast.makeText(ContactActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
			}
		}, list);
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
//            super.handleMessage(msg);
			switch (msg.what){
				case 0:
					String result = (String)msg.obj;
					//将userData的json串直接缓存到本地
					SharedPrefsUtil.putValue(ContactActivity.this,APPConfig.USERDATA, result);
					Log.d("testRun","MainActivity user信息缓存成功 "+result);
					Gson gson=new Gson();
					UserInfo userInfo = new UserInfo();
					userInfo=gson.fromJson(result,UserInfo.class);
					if (userInfo != null) {
						userNameStrAll1 =userInfo.getName();
						Log.d("testRun", "handler userNameStrAll1------"+userNameStrAll1);
					}
					break;
			}
		}

	};

}
