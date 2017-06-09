package com.zhuolang.fu.microdoctorclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.ClickShareInfoActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.ShareDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by jat on 2016/11/6.
 * 我的预约列表适配器
 * 必须重写下面的四个方法
 */

public class MySharesDiscussAdapter extends BaseAdapter {
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
    public MySharesDiscussAdapter(Context context, List<ShareDto> list) {
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
            convertView = inflater.inflate(R.layout.item_mysharesdiscuss, null);
            //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
//            holder.name = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_name);
//            holder.type = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_type);
            holder.sendTime = (TextView) convertView.findViewById(R.id.tv_item_mysharesdiscuss_time);
            holder.sendTimes = (TextView) convertView.findViewById(R.id.tv_item_mysharesdiscuss_times);
            holder.discussContent = (TextView) convertView.findViewById(R.id.tv_item_mysharesdiscuss_discontent);
            holder.title = (TextView) convertView.findViewById(R.id.tv_item_mysharesdiscuss_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_item_mysharesdiscuss_content);
//            holder.discussAmount = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_discussamount);
//            holder.likesAmount = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_likesamount);
//            holder.collectAmount = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_collectamount);
//            holder.delete = (TextView) convertView.findViewById(R.id.tv_item_sharehouse_delete);
//            holder.img_like = (ImageView) convertView.findViewById(R.id.img_item_sharehouse_like);


            //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

//        collectAmount = list.get(position).getCollectAmount();

        Date date = TimeUtil.stringToDate(list.get(position).getCollectOrNot());
        holder.sendTime.setText(""+TimeUtil.dateToStrNoTimeDian(date));
        holder.sendTimes.setText(""+TimeUtil.dateToStrNoDay(date));
        holder.discussContent.setText("评论："+list.get(position).getLikesOrNot());
        holder.title.setText("标题："+list.get(position).getSendTitle());
        holder.content.setText("内容："+list.get(position).getSendContent());
//        holder.ll_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String collecttime=list.get(position).getCollectOrNot();
//                list.get(position).setCollectOrNot("true");
//                String shareStr1 = gson.toJson(list.get(position));
//                list.get(position).setCollectOrNot(collecttime);
//                Intent intent = new Intent();
//                intent.setClass(context, ClickShareInfoActivity.class);
//                intent.putExtra("shareStr", shareStr1);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView type;
        TextView sendTime;
        TextView sendTimes;
        TextView title;
        TextView content;
        TextView discussContent;
        LinearLayout ll_content;
    }
}
