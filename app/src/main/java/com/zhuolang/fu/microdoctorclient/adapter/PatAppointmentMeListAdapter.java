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
import com.zhuolang.fu.microdoctorclient.model.AppointmentDto;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;
import com.zhuolang.fu.microdoctorclient.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class PatAppointmentMeListAdapter extends BaseAdapter{


    private Context context;
    private List<AppointmentDto> list;

    private LayoutInflater inflater;
    private ViewHolder holder;
    private boolean flag = false;
    private String userAccount = "";

    //初始化把上下文，数据列表传递过来
    public PatAppointmentMeListAdapter(Context context, List<AppointmentDto> list) {
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
        convertView = inflater.inflate(R.layout.item_patappointme, null);
        //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
        holder.patientName = (TextView) convertView.findViewById(R.id.tv_item_item_apponintme_patname);
        holder.disease = (TextView) convertView.findViewById(R.id.tv_item_item_apponintme_disease);
        holder.time = (TextView) convertView.findViewById(R.id.tv_item_item_apponintme_time);
        holder.number = (TextView) convertView.findViewById(R.id.tv_item_apponintme_number);
//        holder.doctorsay = (TextView) convertView.findViewById(R.id.tv_item_item_apponintme_doctorsay);
//        holder.see = (TextView) convertView.findViewById(R.id.tv_item_item_apponintme_see);
        //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        holder.patientName.setText(list.get(position).getPatientName());
        holder.disease.setText(list.get(position).getDisease());
//        holder.time.setText(TimeUtil.dateToString(list.get(position).getSeeTime()));
        Date date = TimeUtil.longToDateNoTime(list.get(position).getSeeTime());
//        Date date1 = TimeUtil.stringToDate(list.get(position).getDateTime());
        holder.time.setText("预约时间："+TimeUtil.dateToStrNoTime(date));
        holder.number.setText("预约排号："+list.get(position).getdNumber());
        holder.patientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(context, DoctorDetailActivity.class);
//                UserInfo doctorInfo = list.get(position);
//                Gson gson = new Gson();
//                String doctorDtoStr = gson.toJson(doctorInfo);
//                intent.putExtra("doctorStr", doctorDtoStr);
//                intent.putExtra("flag", "false");
//                context.startActivity(intent);
            }
        });

//        holder.doctorsay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent();
////                intent.setClass(context, DoctorDetailActivity.class);
////                intent.putExtra("appointmentId", list.get(position).getId());
////                context.startActivity(intent);
//            }
//        });
//
//        holder.see.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }

    private class ViewHolder {
        TextView patientName;
        TextView disease;
        TextView time;
        TextView number;
//        TextView doctorsay;
//        TextView see;
    }
}
