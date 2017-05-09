package com.zhuolang.fu.microdoctorclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.DoctorDetailActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.List;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class AppointmentDoctorListAdapter extends BaseAdapter{


    private Context context;
    private List<UserInfo> list;

    private LayoutInflater inflater;
    private ViewHolder holder;
    private boolean flag = false;
    private String userAccount = "";

    //初始化把上下文，数据列表传递过来
    public AppointmentDoctorListAdapter(Context context, List<UserInfo> list) {
        this.context = context;
        this.list = list;
        //初始化开始初始化布局填充器
        inflater = LayoutInflater.from(context);
        //viewholder是优化listview的
        holder = new ViewHolder();
        userAccount = SharedPrefsUtil.getValue(context, APPConfig.ACCOUNT, "");
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
        convertView = inflater.inflate(R.layout.item_appointmentdoctor, null);

        //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
        holder.tv_number = (TextView) convertView.findViewById(R.id.tv_appointmentdoctoritem_number);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_appointmentdoctoritem_name);
        holder.tv_hospital = (TextView) convertView.findViewById(R.id.tv_appointmentdoctoritem_hospital);
        holder.tv_office = (TextView) convertView.findViewById(R.id.tv_appointmentdoctoritem_office);
        holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_appointmentdoctoritem_amount);
        holder.tv_likenum = (TextView) convertView.findViewById(R.id.tv_appointmentdoctoritem_likenum);

        //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        if (list.size() > 0) {
            holder.tv_number.setText((position+1)+"");
            holder.tv_name.setText(list.get(position).getName());
            holder.tv_hospital.setText("医院："+list.get(position).getHospital());
            holder.tv_office .setText("科室："+list.get(position).getOffice());
            holder.tv_amount.setText("就诊量："+list.get(position).getAmount());
            holder.tv_likenum.setText("好评数："+list.get(position).getLikenum());
        }

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, DoctorDetailActivity.class);
                UserInfo doctorInfo = list.get(position);
                Gson gson = new Gson();
                String doctorDtoStr = gson.toJson(doctorInfo);
                intent.putExtra("doctorStr", doctorDtoStr);
                intent.putExtra("flag", "false");
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    private class ViewHolder {

        TextView tv_number;
        TextView tv_name;
        TextView tv_hospital;
        TextView tv_office;
        TextView tv_amount;
        TextView tv_likenum;
    }
}
