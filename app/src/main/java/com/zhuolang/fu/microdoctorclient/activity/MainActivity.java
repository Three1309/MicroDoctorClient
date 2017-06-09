package com.zhuolang.fu.microdoctorclient.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhuolang.fu.microdoctorclient.DemoApplication;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.db.DemoDBManager;
import com.zhuolang.fu.microdoctorclient.db.EaseUser;
import com.zhuolang.fu.microdoctorclient.db.InviteMessage;
import com.zhuolang.fu.microdoctorclient.db.InviteMessgeDao;
import com.zhuolang.fu.microdoctorclient.db.UserDao;
import com.zhuolang.fu.microdoctorclient.fragment.CalendarFragment;
import com.zhuolang.fu.microdoctorclient.fragment.HomeFragment;
import com.zhuolang.fu.microdoctorclient.fragment.ProfileFragment;
import com.zhuolang.fu.microdoctorclient.fragment.ShareTabFragment;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.EaseCommonUtils;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.view.ChangeColorIconWithText;
import com.zhuolang.fu.microdoctorclient.view.CustomViewPager;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;
import com.zhuolang.fu.microdoctorclient.view.ResideMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{
    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private View view;
    private ResideMenu resideMenu;
    //    private ViewPager mViewPager;
    private CustomViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private long exitTime = 0;
    private FragmentPagerAdapter mAdapter;
    private ChangeColorIconWithText one;
    private ChangeColorIconWithText two;
    private ChangeColorIconWithText three;
    private ChangeColorIconWithText four;

    HomeFragment homepageTabFragment = new HomeFragment();
    ShareTabFragment share2TabFragment = new ShareTabFragment();
    CalendarFragment shareTabFragment = new CalendarFragment();
    ProfileFragment meTabFragment = new ProfileFragment();

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

    private String account;
    private String psw;
    private boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
//        CustomWaitDialog.show(MainActivity.this,"获取数据中。。。");
//        if (SharedPrefsUtil.getValue(MainActivity.this, APPConfig.IS_LOGIN, false)) {

        account = SharedPrefsUtil.getValue(MainActivity.this, APPConfig.ACCOUNT, "");
        psw = SharedPrefsUtil.getValue(MainActivity.this, APPConfig.PSW, "");
            saveUserInfo(account);
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        //注册联系人变动监听
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());

//        }
        initView();//获取viewpager
        initDatas();//初始化数据

        mViewPager.setAdapter(mAdapter);//用adapter为viewpager赋值
        initEvent();

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
            }
        });


//        inviteMessgeDao = new InviteMessgeDao(this);
//        userDao = new UserDao(this);
//        //注册联系人变动监听
//        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
    }
    /***
          * 好友变化listener
          *
          */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(final String username) {
            // 保存增加的联系人
            Map<String, EaseUser> localUsers = DemoApplication.getInstance().getContactList();
            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
            EaseUser user = new EaseUser(username);
            // 添加好友时可能会回调added方法两次
            if (!localUsers.containsKey(username)) {
                userDao.saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "增加联系人：+"+username, Toast.LENGTH_SHORT).show();
                }


            });


        }

        @Override
        public void onContactDeleted(final String username) {
            // 被删除
            Map<String, EaseUser> localUsers = DemoApplication.getInstance().getContactList();
            localUsers.remove(username);
            userDao.deleteContact(username);
            inviteMessgeDao.deleteMessage(username);

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "删除联系人：+"+username, Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public void onContactInvited(final String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);

            // 设置相应status
            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "收到好友申请：+"+username, Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public void onFriendRequestAccepted(String s) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(s)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(s);
            msg.setTime(System.currentTimeMillis());

            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
//                    Toast.makeText(getContext(), "好友申请同意：+"+username, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "好友同意申请", Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public void onFriendRequestDeclined(String s) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(s, s + "拒绝了你的好友请求");
        }
    }
    /**
     * 保存并提示消息的邀请消息
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg){
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(this);
        }
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
        // 提示有新消息
        //响铃或其他操作
    }



    private void saveUserInfo(String phone){

        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone",phone);
        list.add(phoneParam);
        new Thread(new Runnable() {
            @Override
            public void run() {

                //post方式连接  url
                OkHttpUtils.post(APPConfig.findUserByPhone, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        String userData= response.toString();
                        //将userData的json串直接缓存到本地
                        SharedPrefsUtil.putValue(MainActivity.this,APPConfig.USERDATA, userData);
                        Log.d("testRun","MainActivity user信息缓存成功 "+userData);

//                        CustomWaitDialog.miss();
                        login();
                        // ** 免登陆情况 加载所有本地群和会话
                        //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                        //加上的话保证进了主页面会话和群组都已经load完毕
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "网络请求失败------");
//                        CustomWaitDialog.miss();
                        Toast.makeText(MainActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }
        }).start();
        CustomWaitDialog.miss();
    }

    /**
     * 初始化所有事件
     */
    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);

    }
    //为fragment传输数据
    private void initDatas() {

        mTabs.add(homepageTabFragment);
        mTabs.add(share2TabFragment);
        mTabs.add(shareTabFragment);
        mTabs.add(meTabFragment);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
    }

    /*
     *初始化数据和点击事件
     */
    private void initView(){
//        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mViewPager = (CustomViewPager) findViewById(R.id.id_viewpager);
        one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);
        two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);

//        one.setOnClickListener(this);
//        two.setOnClickListener(this);
//        three.setOnClickListener(this);

        one.setIconAlpha(1.0f);

    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs(){
        for (int i = 0; i < mTabIndicators.size(); i++){
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

//    /*
//     *从左到右和从右到左滑动，透明度变化
//     */
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
//        // + positionOffset);
//        if (positionOffset > 0) {
//            ChangeColorIconWithText left = mTabIndicators.get(position);
//            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
//            left.setIconAlpha(1 - positionOffset);
//            right.setIconAlpha(positionOffset);
//        }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 登录
     */
    public void login() {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
//        currentUsername = usernameEditText.getText().toString().trim();
//        currentPassword = passwordEditText.getText().toString().trim();

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("testrun", "EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
//        pd.setMessage(getString(R.string.Is_landing));
        pd.setMessage("获取数据中。。。");
        pd.show();
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();
        // reset current user name before login
        DemoApplication.getInstance().setCurrentUserName(account);
        // 调用sdk登陆方法登陆聊天服务器
        Log.d("testrun", "EMClient.getInstance().login");
        EMClient.getInstance().login(account, "123456", new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("testrun", "login: onSuccess");

                if (!MainActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }

                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                getFriends();

//                // 进入主页面
//                Intent intent = new Intent(MainActivity.this,
//                        MainActivity.class);
//                startActivity(intent);
//
//                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("testrun", "环信login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d("testrun", "login: onError: " + code);
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Log.d("testrun", "环信login: onError");
//                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private  void  getFriends(){
        try {
            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            Map<String ,EaseUser> users=new HashMap<String ,EaseUser>();
            for(String username:usernames){
                EaseUser user=new EaseUser(username);
                users.put(username, user);


            }

            DemoApplication.getInstance().setContactList(users);


        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }
    private void logout() {
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoApplication.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
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
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                logout();
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
