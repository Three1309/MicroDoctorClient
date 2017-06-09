package com.zhuolang.fu.microdoctorclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.zhuolang.fu.microdoctorclient.DemoApplication;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentMeHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.AppointmentMeListActivity;
import com.zhuolang.fu.microdoctorclient.activity.ChatActivity;
import com.zhuolang.fu.microdoctorclient.activity.DoctorAppointmentHistoryListActivity;
import com.zhuolang.fu.microdoctorclient.activity.HealthKnowledgeListActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class CalendarFragment extends Fragment  implements View.OnClickListener {

    private View view;
    private ListView listView;

    private LinearLayout ll_healthknowledge;
    private LinearLayout ll_doctorq;
    private LinearLayout ll_appointmentme;
    private LinearLayout ll_mysee;

    private Gson gson = new Gson();
    private String userDataStr;
    UserInfo userInfo = new UserInfo();

    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private EaseConversationAdapater adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_calendar, container, false);
//        setContentView(R.layout.activity_conversation);
        view=new View(getActivity());
        view = inflater.inflate(R.layout.activity_conversation, container, false);
//        view.findViewById(R.id.conversationactivity_img_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
        if (conversationList != null && conversationList.size() > 0) {
            conversationList.clear();
        }
        conversationList.addAll(loadConversationList());
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new EaseConversationAdapater(getContext(), 1, conversationList);
        listView.setAdapter(adapter);
        final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = adapter.getItem(position);
//				String username = conversation.getUserName();
                String username = conversation.conversationId();
                if (username.equals(DemoApplication.getInstance().getCurrentUserName()))
                    Toast.makeText(getContext(), st2, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("username", username);
//					intent.putExtra("username1", userNameStrAll);
                    startActivity(intent);
                }
            }
        });


//        initView(view);
//        initData();
        return view;
    }


    /**
     * 获取会话列表
     *
     //	 * @param context
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(
                            new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));

                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     //	 * @param usernames
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onClick(View v) {

    }

    public class EaseConversationAdapater extends ArrayAdapter<EMConversation> {
        private List<EMConversation> conversationList;
        private List<EMConversation> copyConversationList;

        private boolean notiyfyByFilter;
        private ViewHolder holder;
        protected int primaryColor;
        protected int secondaryColor;
        protected int timeColor;
        protected int primarySize;
        protected int secondarySize;
        protected float timeSize;

        public EaseConversationAdapater(Context context, int resource, List<EMConversation> objects) {
            super(context, resource, objects);
            conversationList = objects;
            copyConversationList = new ArrayList<EMConversation>();
            copyConversationList.addAll(objects);
        }

        @Override
        public int getCount() {
            return conversationList.size();
        }

        @Override
        public EMConversation getItem(int arg0) {
            if (arg0 < conversationList.size()) {
                return conversationList.get(arg0);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
            }
//            ViewHolder holder = (ViewHolder) convertView.getTag();
//            if (holder == null) {
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.msgState = convertView.findViewById(R.id.msg_state);
//                convertView.setTag(holder);
//            }
            // 获取与此用户/群组的会话
            EMConversation conversation = getItem(position);
            // 获取用户username或者群组groupid
            String username = conversation.conversationId();
//			getUserName(id);
//			String username = userNameStrAll;
            holder.name.setText("与 " + username + " 的会话");
            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
            if (conversation.getAllMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                holder.message.setText(lastMessage.getBody().toString());
                holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                    holder.msgState.setVisibility(View.VISIBLE);
                } else {
                    holder.msgState.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (!notiyfyByFilter) {
                copyConversationList.clear();
                copyConversationList.addAll(conversationList);
                notiyfyByFilter = false;
            }
        }

        public void setPrimaryColor(int primaryColor) {
            this.primaryColor = primaryColor;
        }

        public void setSecondaryColor(int secondaryColor) {
            this.secondaryColor = secondaryColor;
        }

        public void setTimeColor(int timeColor) {
            this.timeColor = timeColor;
        }

        public void setPrimarySize(int primarySize) {
            this.primarySize = primarySize;
        }

        public void setSecondarySize(int secondarySize) {
            this.secondarySize = secondarySize;
        }

        public void setTimeSize(float timeSize) {
            this.timeSize = timeSize;
        }

    }

    private static class ViewHolder {
        /** 和谁的聊天记录 */
        TextView name;
        /** 消息未读数 */
        TextView unreadLabel;
        /** 最后一条消息的内容 */
        TextView message;
        /** 最后一条消息的时间 */
        TextView time;
        /** 最后一条消息的发送状态 */
        View msgState;
        /** 整个list中每一行总布局 */

    }

//    private void initView(View view){
//        userDataStr= SharedPrefsUtil.getValue(getActivity(), APPConfig.USERDATA, "");
//        Log.d("testrun", "这个CalendarFragment ----- : " + userDataStr);
//        userInfo=gson.fromJson(userDataStr,UserInfo.class);
//
//        ll_healthknowledge = (LinearLayout) view.findViewById(R.id.fcalendar_ll_health);
//        ll_doctorq = (LinearLayout) view.findViewById(R.id.ll_fcalendar_doctormodle);
//        ll_appointmentme = (LinearLayout) view.findViewById(R.id.fcalendar_ll_appointmentme);
//        ll_mysee = (LinearLayout) view.findViewById(R.id.fcalendar_ll_seehistory);
//
//
//        ll_appointmentme.setOnClickListener(this);
//        ll_mysee.setOnClickListener(this);
//        ll_healthknowledge.setOnClickListener(this);
//        if(!SharedPrefsUtil.getValue(getActivity(), APPConfig.TYPE, "").equals("1")){
////        if (userInfo.getType() != 1) {
//            ll_doctorq.setVisibility(View.GONE);
//        }
//    }
//
//    private void initData(){
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.fcalendar_ll_health:
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), HealthKnowledgeListActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.fcalendar_ll_appointmentme:
//                Intent intent1 = new Intent();
//                intent1.setClass(getActivity(), AppointmentMeListActivity.class);
//                startActivity(intent1);
//                break;
//            case R.id.fcalendar_ll_seehistory:
//                Intent intent2 = new Intent();
//                intent2.setClass(getActivity(), AppointmentMeHistoryListActivity.class);
//                startActivity(intent2);
//                break;
//        }
//    }

}
