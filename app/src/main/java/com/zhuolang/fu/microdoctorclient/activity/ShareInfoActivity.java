package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.adapter.ShareHouseAdapter;
import com.zhuolang.fu.microdoctorclient.adapter.ShareInfoDiscussAdapter;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.ShareDiscussDto;
import com.zhuolang.fu.microdoctorclient.model.ShareDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;
import com.zhuolang.fu.microdoctorclient.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class ShareInfoActivity extends Activity implements AdapterView.OnItemClickListener{

    private String shareStr;

    private ShareDto shareDto;
    private String userDataStr;
    private String userId;
    private UserInfo userInfo;
    private Gson gson=new Gson();
    private List<ShareDiscussDto> discussDtoList;

    private ShareInfoDiscussAdapter shareInfoDiscussAdapter;
    private ListView listView;
    private TextView tv_name;

    private TextView tv_type;
    private TextView tv_sendTime;
    private TextView tv_collect;
    private TextView tv_sendtitle;
    private TextView tv_sendcontent;
    private static TextView tv_discussAmount;
    private static int discussAmountSTATIC;
    private TextView tv_likesAmount;
    private TextView tv_collectAmount;
    private TextView tv_delete;

    private ImageView img_back;
    private ImageView img_myhead;
    private ImageView img_likes;

    private EditText et_sendDicscuss;
    private TextView tv_send;
    private TextView tv_unsend;
    private LinearLayout ll_sendDiscuss;
    private LinearLayout ll_like;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareinfo);

        shareStr = getIntent().getStringExtra("shareStr");
        shareDto = gson.fromJson(shareStr, ShareDto.class);
        discussAmountSTATIC = shareDto.getDiscussAmount();

        userDataStr = SharedPrefsUtil.getValue(ShareInfoActivity.this, APPConfig.USERDATA, "");
        userInfo = gson.fromJson(userDataStr, UserInfo.class);
        userId = userInfo.getId() + "";

        initView();
        initData();
        initModel();

    }
    public static void updateDiscussAmount() {
        discussAmountSTATIC=discussAmountSTATIC-1;
        tv_discussAmount.setText("全部评论（" + discussAmountSTATIC + "）");
//        setListViewHeight(listView);
    }
    private void initView(){
        tv_name = (TextView)findViewById(R.id.tv_activity_shareinfo_name);
        tv_type = (TextView) findViewById(R.id.tv_activity_shareinfo_type);
        tv_sendTime = (TextView) findViewById(R.id.tv_activity_shareinfo_sendtime);
        tv_collect = (TextView) findViewById(R.id.tv_activity_shareinfo_collect);
        tv_sendtitle = (TextView) findViewById(R.id.tv_activity_shareinfo_title);
        tv_sendcontent = (TextView) findViewById(R.id.tv_activity_shareinfo_content);
        tv_discussAmount = (TextView) findViewById(R.id.tv_activity_shareinfo_discussamount);
        tv_likesAmount = (TextView) findViewById(R.id.tv_activity_shareinfo_likesamount);
        tv_collectAmount = (TextView) findViewById(R.id.tv_activity_shareinfo_collectamount);
        tv_delete = (TextView) findViewById(R.id.tv_activity_shareinfo_delete);

        et_sendDicscuss = (EditText) findViewById(R.id.et_activity_shareinfo_discusscontent);
        tv_send = (TextView) findViewById(R.id.bt_activity_shareinfo_send);
        tv_unsend = (TextView) findViewById(R.id.bt_activity_shareinfo_unsend);
        ll_sendDiscuss = (LinearLayout) findViewById(R.id.ll_activity_shareinfo_senddiscuss);
        ll_like = (LinearLayout) findViewById(R.id.ll_activity_shareinfo_like);


        img_back = (ImageView) findViewById(R.id.img_shareinfo_back);
        img_myhead = (ImageView) findViewById(R.id.img_activity_shareinfo_mehead);
        img_likes = (ImageView) findViewById(R.id.img_activity_shareinfo_likes);
        listView = (ListView) findViewById(R.id.shareinfolistview);
        listView.setOnItemClickListener(this);

        ll_sendDiscuss.setVisibility(View.GONE);
        tv_send.setVisibility(View.GONE);
        tv_unsend.setVisibility(View.GONE);

        if (shareDto.getUserType() != 1) {
            tv_type.setVisibility(View.INVISIBLE);
            if (shareDto.getUserNickName().equals("")) {
                tv_name.setText("未填写");
            }else {
                tv_name.setText("" + shareDto.getUserNickName());
            }
        }else {
            if (shareDto.getUserName().equals("")) {
                tv_name.setText("未填写");
            }else {
                tv_name.setText("" + shareDto.getUserName());
            }
        }
        if (userInfo.getId() != shareDto.getUserId()) {
            tv_delete.setVisibility(View.INVISIBLE);
        }
        String guanzu = "  + 收藏  ";
        if (shareDto.getCollectOrNot().equals("true")) {
            guanzu="  已收藏  ";
            shareDto.setCollectOrNot(guanzu);
        }else if (shareDto.getCollectOrNot().equals("false")){
            guanzu="  + 收藏  ";
            shareDto.setCollectOrNot(guanzu);
        }
        tv_collect.setText(shareDto.getCollectOrNot());
        Date date = TimeUtil.longToDate(shareDto.getSendTime());
        tv_sendTime.setText(""+TimeUtil.dateToString(date));
        tv_sendtitle.setText(shareDto.getSendTitle());
        tv_sendcontent.setText(shareDto.getSendContent());
        tv_discussAmount.setText("全部评论（"+discussAmountSTATIC+"）");
        tv_likesAmount.setText("·"+shareDto.getLikesAmount());
        tv_collectAmount.setText("·"+shareDto.getCollectAmount());

        if (shareDto.getLikesOrNot().equals("true")) {
            img_likes.setImageResource(R.mipmap.islike01);
        }else if (shareDto.getLikesOrNot().equals("false")){
            img_likes.setImageResource(R.mipmap.like01);
        }
    }

    public void initModel(){
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShareInfoActivity.this, UserShareHouseInfoActivity.class);
                intent.putExtra("userId", shareDto.getUserId()+"");
                intent.putExtra("where", "ShareInfoActivity");
                startActivity(intent);
            }
        });

        img_myhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShareInfoActivity.this, UserShareHouseInfoActivity.class);
                intent.putExtra("userId", shareDto.getUserId()+"");
                intent.putExtra("where", "ShareInfoActivity");
                startActivity(intent);
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ShareInfoActivity.this);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否删除该帖子？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                        OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("sendId", shareDto.getSendId()+"");

                        listP.add(doctorsayParam);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //post方式连接  url
                                OkHttpUtils.post(APPConfig.deleteShareSendBySendId, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        if (response.toString().equals("deleteShareSend_success")) {
                                            Toast.makeText(ShareInfoActivity.this, "已成功删除该帖子及其相关信息", Toast.LENGTH_SHORT).show();

                                            ShareHouseActivity.updateData();
                                            finish();
                                        }else{
                                            Toast.makeText(ShareInfoActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(ShareInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                },listP);
                            }
                        }).start();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHouseActivity.updateData();
                finish();
            }
        });
        //收藏
        tv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                OkHttpUtils.Param idParam = new OkHttpUtils.Param("sendId", shareDto.getSendId() + "");
                OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("collectorId", userId);
                listP.add(idParam);
                listP.add(doctorsayParam);
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateShareCollect, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = response;
//                        shareDtoStr = response.toString();
                        Log.d("testrun", "ShareInfoActivity response.toString()=" +response.toString());
                        if (response.toString().equals("collect_failure")) {
                            Toast.makeText(ShareInfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }else {
                            handler.sendMessage(message);
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(ShareInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        });

        //点赞或取消赞
        img_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                OkHttpUtils.Param idParam = new OkHttpUtils.Param("sendId", shareDto.getSendId() + "");
                OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("likeserId", userId);
                listP.add(idParam);
                listP.add(doctorsayParam);
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateShareLikes, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = response;
//                        shareDtoStr = response.toString();
                        Log.d("testrun", "ShareInfoActivity response.toString()=" +response.toString());
                        if (response.toString().equals("likes_failure")) {
                            Toast.makeText(ShareInfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }else {
                            handler.sendMessage(message);
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(ShareInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discussConStr = et_sendDicscuss.getText().toString();
                if (discussConStr.equals("")) {
                    Toast.makeText(ShareInfoActivity.this, "评论内容不能为空，请确认", Toast.LENGTH_SHORT).show();
                }else {
                    final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param idParam = new OkHttpUtils.Param("sendId", shareDto.getSendId() + "");
                    OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("discusserId", userId);
                    OkHttpUtils.Param contentParam = new OkHttpUtils.Param("discussContent", discussConStr);
                    listP.add(idParam);
                    listP.add(doctorsayParam);
                    listP.add(contentParam);
                    //post方式连接  url
                    OkHttpUtils.post(APPConfig.addShareDiscuss, new OkHttpUtils.ResultCallback() {
                        @Override
                        public void onSuccess(Object response) {
//                        Message message = new Message();
//                        message.what = 2;
//                        message.obj = response;
//                        shareDtoStr = response.toString();
                            Log.d("testrun", "ShareInfoActivity response.toString()=" +response.toString());
                            if (response.toString().equals("addShareDiscuss_success")) {
                                discussAmountSTATIC = discussAmountSTATIC + 1;
                                tv_discussAmount.setText("全部评论（"+discussAmountSTATIC+"）");
                                Toast.makeText(ShareInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                if (discussDtoList != null && discussDtoList.size() > 0) {
                                    discussDtoList.clear();
                                }
                                initData();
                                et_sendDicscuss.setText("");
                                ll_sendDiscuss.setVisibility(View.GONE);
                                tv_unsend.setVisibility(View.GONE);
                                tv_send.setVisibility(View.GONE);
                                ll_like.setVisibility(View.VISIBLE);
                                et_sendDicscuss.clearFocus();

                            }else {
                                Toast.makeText(ShareInfoActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ShareInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    },listP);
                }

                //隐藏输入法编辑器
                HideSoftInput(v.getWindowToken());
//                tv_sendTime.setFocusable(true);
//                tv_sendTime.setFocusableInTouchMode(true);
//                tv_sendTime.requestFocus();
            }
        });
        tv_unsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_sendDicscuss.setText("");
                ll_sendDiscuss.setVisibility(View.GONE);
                tv_unsend.setVisibility(View.GONE);
                tv_send.setVisibility(View.GONE);
                ll_like.setVisibility(View.VISIBLE);
                et_sendDicscuss.clearFocus();
                //隐藏输入法编辑器
                HideSoftInput(v.getWindowToken());

//                tv_sendTime.setFocusable(true);
//                tv_sendTime.setFocusableInTouchMode(true);
//                tv_sendTime.requestFocus();
            }
        });
        //评论编辑框焦点事件
        et_sendDicscuss.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//没有点击编辑框时

//                    IfUserExitinDB(string);//自定义的检查文本内容的方法
                    ll_like.setVisibility(View.VISIBLE);
                    ll_sendDiscuss.setVisibility(View.GONE);
                    tv_send.setVisibility(View.GONE);
                    tv_unsend.setVisibility(View.GONE);
                }else {//点击编辑框时
                    ll_sendDiscuss.setVisibility(View.VISIBLE);
                    tv_send.setVisibility(View.VISIBLE);
                    tv_unsend.setVisibility(View.VISIBLE);
                    ll_like.setVisibility(View.GONE);
                }
            }
        });
//        //文本框重新获得焦点：
//        et_sendDicscuss.setFocusable(true);
//        et_sendDicscuss.setFocusableInTouchMode(true);
//        et_sendDicscuss.requestFocus();
//        et_sendDicscuss.clearFocus(); //失去焦点
//        et_sendDicscuss.requestFocus();//获取焦点
    }

    public void initData() {

        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();

        OkHttpUtils.Param idParam = new OkHttpUtils.Param("sendId", shareDto.getSendId()+"");
//        OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("doctorSay", doctorSayStr);
        listP.add(idParam);
//        list.add(doctorsayParam);
        CustomWaitDialog.show(ShareInfoActivity.this,"获取数据。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url
                OkHttpUtils.post(APPConfig.findAllShareDiscuss, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
//                        shareDtoStr = response.toString();
                        Log.d("testrun", "ShareInfoActivity shareDtoStr=" +response.toString());
                        if (response.toString().equals("nodata")) {
                            CustomWaitDialog.miss();
                            Toast.makeText(ShareInfoActivity.this, "没有评论", Toast.LENGTH_SHORT).show();
                        }else {
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        CustomWaitDialog.miss();
                        Toast.makeText(ShareInfoActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        }).start();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        flag = true;
//        mWindowManager.removeView(tv_Float);
//
//        String shareStr1 = gson.toJson(shareDtoList.get(position));
//        Intent intent = new Intent();
//        intent.setClass(ShareHouseActivity.this, ShareInfoActivity.class);
//        intent.putExtra("shareStr", shareStr1);
//        startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            Log.d("testRun", "result======" + result);
            switch (msg.what) {
                case 0:
                    CustomWaitDialog.miss();
                    discussDtoList = gson.fromJson(result, new TypeToken<List<ShareDiscussDto>>() {}.getType());
                    if (discussDtoList != null && discussDtoList.size() > 0) {
                        shareInfoDiscussAdapter = new ShareInfoDiscussAdapter(ShareInfoActivity.this,discussDtoList );
                        listView.setAdapter(shareInfoDiscussAdapter);
                        setListViewHeight(listView);
                    }else {
                        Toast.makeText(ShareInfoActivity.this, "没有评论", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (result.equals("addcollect_success")) {
                        shareDto.setCollectAmount(shareDto.getCollectAmount()+1);
                        String guanzu = "  已收藏  ";
                        shareDto.setCollectOrNot(guanzu);

                        tv_collect.setText(shareDto.getCollectOrNot());
                        tv_collectAmount.setText("·"+shareDto.getCollectAmount());
                    }else if (result.equals("uncollect_success")){
                        shareDto.setCollectAmount(shareDto.getCollectAmount()-1);
                        String guanzu = "  + 收藏  ";
                        shareDto.setCollectOrNot(guanzu);

                        tv_collect.setText(shareDto.getCollectOrNot());
                        tv_collectAmount.setText("·"+shareDto.getCollectAmount());
                    }
                    break;
                case 2:
                    if (result.equals("likes_success")) {
                        shareDto.setLikesAmount(shareDto.getLikesAmount()+1);
                        shareDto.setLikesOrNot("true");

                        img_likes.setImageResource(R.mipmap.islike01);
                        tv_likesAmount.setText("·"+shareDto.getLikesAmount());
                    }else if (result.equals("dislikes_success")){
                        shareDto.setLikesAmount(shareDto.getLikesAmount()-1);
                        shareDto.setLikesOrNot("false");

                        img_likes.setImageResource(R.mipmap.like01);
                        tv_likesAmount.setText("·"+shareDto.getLikesAmount());
                    }

                    break;
            }

        }
    };

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * @param listView
     */
    private void setListViewHeight(ListView listView) {
//        DisplayMetrics metrics = new DisplayMetrics();
//        float dpToCm = (metrics.density * 2.54f *133*shareList2.size())/ metrics.xdpi;
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight+=20;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            mWindowManager.removeView(img_Float);
            ShareHouseActivity.updateData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // -------------------------------------隐藏输入法-----------------------------------------------------
    // 获取点击事件
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // TODO Auto-generated method stub
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View view = getCurrentFocus();
//            if (isHideInput(view, ev)) {
//                HideSoftInput(view.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
