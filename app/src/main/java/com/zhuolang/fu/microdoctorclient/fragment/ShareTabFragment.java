package com.zhuolang.fu.microdoctorclient.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhuolang.fu.microdoctorclient.DemoApplication;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AddContactActivity;
import com.zhuolang.fu.microdoctorclient.activity.ChatActivity;
import com.zhuolang.fu.microdoctorclient.activity.ContactActivity;
import com.zhuolang.fu.microdoctorclient.activity.ConversationActivity;
import com.zhuolang.fu.microdoctorclient.activity.NewFriendsMsgActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.db.EaseUser;
import com.zhuolang.fu.microdoctorclient.db.InviteMessage;
import com.zhuolang.fu.microdoctorclient.db.InviteMessgeDao;
import com.zhuolang.fu.microdoctorclient.db.UserDao;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.EaseCommonUtils;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by wnf on 2016/10/29.
 * “我”界面的fragment
 */


public class ShareTabFragment extends Fragment implements View.OnClickListener{

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;


    private ImageView imageView=null;
    private View view = null;
    private int userType=0;

    protected List<EaseUser> contactList = new ArrayList<EaseUser>();
    protected ListView listView;
    private Map<String, EaseUser> contactsMap;
    private ContactAdapter adapter;
    public String userNameStrAll1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        view=new View(getActivity());
        view = inflater.inflate(R.layout.activity_contact, container, false);
        Log.d("testrun", "CustomWaitDialog.show(getContext(),获取数据中。。。1");
        CustomWaitDialog.show(getContext(),"获取数据中。。。");
        Log.d("testrun", "CustomWaitDialog.show(getContext(),获取数据中。。。2");
//        setContentView(R.layout.activity_contact);

//        view.findViewById(R.id.contactactivity_img_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddContactActivity.class));
            }
        });
        view.findViewById(R.id.btn_agreeshen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),NewFriendsMsgActivity.class));
            }
        });
        listView = (ListView) view.findViewById(R.id.listView);
        if (contactList != null && contactList.size() > 0) {
            contactList.clear();
        }
        getContactList();
        adapter = new ContactAdapter(getContext(), contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				startActivity(new Intent(ContactActivity.this,ChatActivity.class).putExtra("username", adapter.getItem(arg2).getUsername()));
//				finish();
                // 进入聊天页面
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("username", adapter.getItem(arg2).getUsername());
//				intent.putExtra("username1", userNameStrAll1);
                startActivity(intent);
//                getActivity().finish();
            }

        });
        CustomWaitDialog.miss();
        Log.d("testrun", "CustomWaitDialog.show(getContext(),获取数据中。。。3");
//        initView(view);
//        initData();
        return view;
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    protected void getContactList() {
        Log.d("testrun", "CustomWaitDialog.show(getContext(),获取数据中。。。4");
        contactList.clear();
        // 获取联系人列表
        contactsMap = DemoApplication.getInstance().getContactList();
        if (contactsMap == null) {
            return;
        }
        synchronized (this.contactsMap) {
            Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
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
            Log.d("testrun", "CustomWaitDialog.show(getContext(),获取数据中。。。5");
//            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_contact, parent, false);

//            }
            String phone = getItem(position).getUsername();
//			getUserName(phone);
//			Log.d("testRun", "userNameStrAll1------"+userNameStrAll1);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_name.setText(phone);

            return convertView;
        }

    }

//    public void getUserName(String phone){
//        final String[] userName=new String[1];
//        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
//        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone",phone);
//        list.add(phoneParam);
//        //post方式连接  url
//        OkHttpUtils.post(APPConfig.findUserByPhone, new OkHttpUtils.ResultCallback() {
//            @Override
//            public void onSuccess(Object response) {
//                Message message = new Message();
//                message.what = 0;
//                message.obj = response;
//                String userData= response.toString();
////				//将userData的json串直接缓存到本地
////				SharedPrefsUtil.putValue(ContactActivity.this,APPConfig.USERDATA, userData);
////				Log.d("testRun","MainActivity user信息缓存成功 "+userData);
////				Gson gson=new Gson();
////				UserInfo userInfo = new UserInfo();
////				userInfo=gson.fromJson(userData,UserInfo.class);
////				if (userInfo != null) {
////					userName[0] =userInfo.getName();
////					Log.d("testRun", "userName[0]------"+userName[0]);
////					return userName[0];
////				}
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.d("testRun", "网络请求失败------");
////                        CustomWaitDialog.miss();
//                Toast.makeText(getContext(), "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
//            }
//        }, list);
//    }
//
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
//            switch (msg.what){
//                case 0:
//                    String result = (String)msg.obj;
//                    //将userData的json串直接缓存到本地
//                    SharedPrefsUtil.putValue(getContext(),APPConfig.USERDATA, result);
//                    Log.d("testRun","MainActivity user信息缓存成功 "+result);
//                    Gson gson=new Gson();
//                    UserInfo userInfo = new UserInfo();
//                    userInfo=gson.fromJson(result,UserInfo.class);
//                    if (userInfo != null) {
//                        userNameStrAll1 =userInfo.getName();
//                        Log.d("testRun", "handler userNameStrAll1------"+userNameStrAll1);
//                    }
//                    break;
//            }
//        }
//
//    };

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
//
//        view=new View(getActivity());
//
//        view = inflater.inflate(R.layout.share, container, false);
//
//
//
////        view.findViewById(R.id.fshare_ll_conversation).setOnClickListener(this);
////        view.findViewById(R.id.fshare_ll_contact).setOnClickListener(this);
////
////        view.findViewById(R.id.fshare_ll_newfriend).setOnClickListener(this);
//////        view.findViewById(R.id.btn_logout).setOnClickListener(this);
////        inviteMessgeDao = new InviteMessgeDao(getContext());
////        userDao = new UserDao(getContext());
////        //注册联系人变动监听
////        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
//        return view;
//
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.fshare_ll_conversation:
//                startActivity(new Intent(getContext(),ConversationActivity.class));
//                break;
//            case R.id.fshare_ll_contact:
//                startActivity(new Intent(getContext(),ContactActivity.class));
//                break;
//            case R.id.fshare_ll_newfriend:
//                startActivity(new Intent(getContext(),NewFriendsMsgActivity.class));
//                break;
//
//        }
//
//    }
//
//    /***
//     * 好友变化listener
//     *
//     */
//    public class MyContactListener implements EMContactListener {
//
//        @Override
//        public void onContactAdded(final String username) {
//            // 保存增加的联系人
//            Map<String, EaseUser> localUsers = DemoApplication.getInstance().getContactList();
//            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
//            EaseUser user = new EaseUser(username);
//            // 添加好友时可能会回调added方法两次
//            if (!localUsers.containsKey(username)) {
//                userDao.saveContact(user);
//            }
//            toAddUsers.put(username, user);
//            localUsers.putAll(toAddUsers);
//            getActivity().runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//                    Toast.makeText(getContext(), "增加联系人：+"+username, Toast.LENGTH_SHORT).show();
//                }
//
//
//            });
//
//
//        }
//
//        @Override
//        public void onContactDeleted(final String username) {
//            // 被删除
//            Map<String, EaseUser> localUsers = DemoApplication.getInstance().getContactList();
//            localUsers.remove(username);
//            userDao.deleteContact(username);
//            inviteMessgeDao.deleteMessage(username);
//
//            getActivity().runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//                    Toast.makeText(getContext(), "删除联系人：+"+username, Toast.LENGTH_SHORT).show();
//                }
//
//
//            });
//
//        }
//
//        @Override
//        public void onContactInvited(final String username, String reason) {
//            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
//                    inviteMessgeDao.deleteMessage(username);
//                }
//            }
//            // 自己封装的javabean
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            msg.setReason(reason);
//
//            // 设置相应status
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
//            notifyNewIviteMessage(msg);
//            getActivity().runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//                    Toast.makeText(getContext(), "收到好友申请：+"+username, Toast.LENGTH_SHORT).show();
//                }
//
//
//            });
//
//        }
//
//        @Override
//        public void onFriendRequestAccepted(String s) {
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(s)) {
//                    return;
//                }
//            }
//            // 自己封装的javabean
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(s);
//            msg.setTime(System.currentTimeMillis());
//
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
//            notifyNewIviteMessage(msg);
//            getActivity().runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
////                    Toast.makeText(getContext(), "好友申请同意：+"+username, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "好友同意申请", Toast.LENGTH_SHORT).show();
//                }
//
//
//            });
//
//        }
//
//        @Override
//        public void onFriendRequestDeclined(String s) {
//            // 参考同意，被邀请实现此功能,demo未实现
//            Log.d(s, s + "拒绝了你的好友请求");
//        }
//    }
//    /**
//     * 保存并提示消息的邀请消息
//     * @param msg
//     */
//    private void notifyNewIviteMessage(InviteMessage msg){
//        if(inviteMessgeDao == null){
//            inviteMessgeDao = new InviteMessgeDao(getContext());
//        }
//        inviteMessgeDao.saveMessage(msg);
//        //保存未读数，这里没有精确计算
//        inviteMessgeDao.saveUnreadMessageCount(1);
//        // 提示有新消息
//        //响铃或其他操作
//    }
//
}
