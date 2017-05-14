package com.zhuolang.fu.microdoctorclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.HealthKnowledge;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class HealthKnowledgeAdapter extends BaseAdapter{


    private Context context;
    private List<HealthKnowledge> list;

    private LayoutInflater inflater;
    private ViewHolder holder;
    private boolean flag = false;
    private String userAccount = "";

    //初始化把上下文，数据列表传递过来
    public HealthKnowledgeAdapter(Context context, List<HealthKnowledge> list) {
        this.context = context;
        this.list = list;
        //初始化开始初始化布局填充器
        inflater = LayoutInflater.from(context);
        //viewholder是优化listview的
        holder = new ViewHolder();
//        userAccount = SharedPrefsUtil.getValue(context, APPConfig.ACCOUNT, "");
    }

    @Override//返回listview有多少条数据
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override//返回每个listview的item
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
    final public View getView(final int position, View convertView, final ViewGroup parent) {
        //判断布局有没有填充过，例如一个listview有多个item，只需要在第一个item的时候创建，后面的可以使用已经创建的了，可以省时间和空间
//        if (convertView == null) {
        convertView = inflater.inflate(R.layout.item_healthknowledge, null);

        //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
        holder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_health_title);
        holder.tv_description = (TextView) convertView.findViewById(R.id.tv_item_health_description);
        holder.tv_time = (TextView) convertView.findViewById(R.id.tv_item_health_time);

        //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        if (list.size() > 0) {
            holder.tv_title.setText(list.get(position).getTitle());
            Date date = TimeUtil.longToDate(list.get(position).getTime()+"");
            holder.tv_time.setText(TimeUtil.dateToString(date)+"");
//            holder.tv_time.setText(
            holder.tv_description.setText("描述："+list.get(position).getDescription());
        }

        return convertView;
    }

    private class ViewHolder {

        TextView tv_title;
        TextView tv_time;
        TextView tv_description;
    }
}
