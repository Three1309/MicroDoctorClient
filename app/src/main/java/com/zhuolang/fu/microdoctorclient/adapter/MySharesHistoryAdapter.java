package com.zhuolang.fu.microdoctorclient.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.ShareHouseActivity;
import com.zhuolang.fu.microdoctorclient.activity.UserShareHouseInfoActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.ShareDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jat on 2016/11/6.
 * 我的预约列表适配器
 * 必须重写下面的四个方法
 */

public class MySharesHistoryAdapter extends BaseAdapter {
    private WindowManager mWindowManager = null;
    private Context context;
    private List<ShareDto> list;

    private int collectAmount;
    private String guanzu = "  + 关注  ";
    private boolean flag = false;

    private LayoutInflater inflater;
    private ViewHolder holder;
    private String userDataStr;
    private String userId;
    private UserInfo userInfo;
    Gson gson = new Gson();
    private String returnSTR="";

    //初始化把上下文，数据列表传递过来
    public MySharesHistoryAdapter(Context context, List<ShareDto> list) {
        this.context = context;
        this.list = list;
        //初始化开始初始化布局填充器
        inflater = LayoutInflater.from(context);
        //viewholder是优化listview的
        holder = new ViewHolder();

        userDataStr= SharedPrefsUtil.getValue(context, APPConfig.USERDATA, "");
        Log.d("activityID", "这个是SettingsFragment ----- : " + userDataStr);
        userInfo=gson.fromJson(userDataStr,UserInfo.class);
        userId = userInfo.getId()+"";
    }

    //返回listview有多少条数据
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    //返回每个listview的item，这个方法不重要
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 这个方法时最重要的
     * 是listview中每个item的设置内容
     * item的显示依靠这个方法
     *
     * @param position    item对应listview中的第几个
     * @param convertView listview中的item子布局
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //判断布局有没有填充过，例如一个listview有多个item，只需要在第一个item的时候创建，后面的可以使用已经创建的了，可以省时间和空间
//        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_myshareshistory, null);
            //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
            holder.name = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_name);
            holder.type = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_type);
            holder.sendTime = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_sendtime);
            holder.collect = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_collect);
            holder.title = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_content);
            holder.discussAmount = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_discussamount);
            holder.likesAmount = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_likesamount);
            holder.collectAmount = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_collectamount);
            holder.delete = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_delete);
            holder.img_like = (ImageView) convertView.findViewById(R.id.img_item_sharehouse_like);
            holder.img_myhead = (ImageView) convertView.findViewById(R.id.img_item_sharehouse_myhead);

            //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

//        collectAmount = list.get(position).getCollectAmount();
        if (list.get(position).getUserType() != 1) {
            holder.type.setVisibility(View.INVISIBLE);
            if (list.get(position).getUserNickName().equals("")) {
                holder.name.setText("未填写");
            }else {
                holder.name.setText("" + list.get(position).getUserNickName());
            }
        }else {
            if (list.get(position).getUserName().equals("")) {
                holder.name.setText("未填写");
            }else {
                holder.name.setText("" + list.get(position).getUserName());
            }
        }
        if (list.get(position).getCollectOrNot().equals("true")) {
            guanzu="  已收藏  ";
//            holder.collect.setBackgroundResource(R.drawable.textview_back2);
            list.get(position).setCollectOrNot(guanzu);
        }else if (list.get(position).getCollectOrNot().equals("false")){
            guanzu="  + 收藏  ";
//            holder.collect.setBackgroundResource(R.drawable.textview_back1);
            list.get(position).setCollectOrNot(guanzu);
        }
        holder.collect.setText(list.get(position).getCollectOrNot());
        if (userInfo.getId() != list.get(position).getUserId()) {
            holder.delete.setVisibility(View.INVISIBLE);
        }
        Date date = TimeUtil.longToDate(list.get(position).getSendTime());
        holder.sendTime.setText(""+TimeUtil.dateToString(date));

        holder.title.setText(list.get(position).getSendTitle());
        holder.content.setText(list.get(position).getSendContent());
        holder.discussAmount.setText("评论·"+list.get(position).getDiscussAmount());
        holder.likesAmount.setText("赞·"+list.get(position).getLikesAmount());
        holder.collectAmount.setText("收藏·"+list.get(position).getCollectAmount());
        if (list.get(position).getLikesOrNot().equals("true")) {
            holder.img_like.setImageResource(R.mipmap.islike01);
        }else if (list.get(position).getLikesOrNot().equals("false")){
            holder.img_like.setImageResource(R.mipmap.like01);
        }

        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                OkHttpUtils.Param idParam = new OkHttpUtils.Param("sendId", list.get(position).getSendId() + "");
                OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("collectorId", userId);
                listP.add(idParam);
                listP.add(doctorsayParam);
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateShareCollect, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        returnSTR = response.toString();
                        if (returnSTR.equals("addcollect_success")) {
//                            guanzu="  已关注  ";
//                            collectAmount = collectAmount + 1;

                            list.get(position).setCollectAmount(list.get(position).getCollectAmount()+1);
                            list.get(position).setCollectOrNot("true");
                            notifyDataSetChanged();
                        }else if (returnSTR.equals("uncollect_success")){
//                            guanzu="  + 关注  ";
//                            collectAmount = collectAmount - 1;

                            list.get(position).setCollectAmount(list.get(position).getCollectAmount()-1);
                            list.get(position).setCollectOrNot("false");
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        });

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                OkHttpUtils.Param idParam = new OkHttpUtils.Param("sendId", list.get(position).getSendId() + "");
                OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("likeserId", userId);
                listP.add(idParam);
                listP.add(doctorsayParam);
                //post方式连接  url
                OkHttpUtils.post(APPConfig.updateShareLikes, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response.toString().equals("likes_success")) {
                            list.get(position).setLikesAmount(list.get(position).getLikesAmount()+1);
                            list.get(position).setLikesOrNot("true");
                            notifyDataSetChanged();
                        }else if (response.toString().equals("dislikes_success")){
                            list.get(position).setLikesAmount(list.get(position).getLikesAmount()-1);
                            list.get(position).setLikesOrNot("false");
                            notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                },listP);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否删除该帖子？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                        OkHttpUtils.Param doctorsayParam = new OkHttpUtils.Param("sendId", list.get(position).getSendId()+"");

                        listP.add(doctorsayParam);
                        //post方式连接  url
                        OkHttpUtils.post(APPConfig.deleteShareSendBySendId, new OkHttpUtils.ResultCallback() {
                            @Override
                            public void onSuccess(Object response) {
                                if (response.toString().equals("deleteShareSend_success")) {
                                    list.remove(position);
                                    notifyDataSetChanged();
                                }else{

                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(context, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        },listP);
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

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, UserShareHouseInfoActivity.class);
                intent.putExtra("userId", list.get(position).getUserId()+"");
                intent.putExtra("where", "ShareHouseListView");
                ShareHouseActivity.removetv_FloatView();
                context.startActivity(intent);
            }
        });
        holder.img_myhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, UserShareHouseInfoActivity.class);
                intent.putExtra("userId", list.get(position).getUserId()+"");
                intent.putExtra("where", "ShareHouseListView");
                ShareHouseActivity.removetv_FloatView();
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView type;
        TextView sendTime;
        TextView collect;
        TextView title;
        TextView content;
        TextView discussAmount;
        TextView likesAmount;
        TextView collectAmount;
        TextView delete;
        ImageView img_like;
        ImageView img_myhead;
    }
}
