package com.zhuolang.fu.microdoctorclient.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.activity.AgreeRegisterDoctotActivity;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.model.UserInfo;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wunaifu on 2017/4/30.
 */
public class RegisterDoctorListAdapter extends BaseAdapter{


    private Context context;
    private List<UserInfo> list;

    private LayoutInflater inflater;
    private ViewHolder holder;
    private boolean flag = false;

    //初始化把上下文，数据列表传递过来
    public RegisterDoctorListAdapter(Context context, List<UserInfo> list) {
        this.context = context;
        this.list = list;
        //初始化开始初始化布局填充器
        inflater = LayoutInflater.from(context);
        //viewholder是优化listview的
        holder = new ViewHolder();
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
        convertView = inflater.inflate(R.layout.item_registerdoctor, null);

        //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
        holder.tv_number = (TextView) convertView.findViewById(R.id.tv_registerdoctoritem_number);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_registerdoctoritem_name);
        holder.tv_hospital = (TextView) convertView.findViewById(R.id.tv_registerdoctoritem_hospital);
        holder.tv_office = (TextView) convertView.findViewById(R.id.tv_registerdoctoritem_office);
        holder.tv_agree = (TextView) convertView.findViewById(R.id.tv_registerdoctoritem_agree);
        holder.tv_disagree = (TextView) convertView.findViewById(R.id.tv_registerdoctoritem_disagree);

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
//            holder.tv_amount.setText("就诊量："+list.get(position).getAmount());
//            holder.tv_likenum.setText("好评数："+list.get(position).getLikenum());
        }

//        AgreeRegisterDoctotActivity.initMotion();
        holder.tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否通过"+list.get(position).getName()+"的医师申请？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                        OkHttpUtils.Param typeParam = new OkHttpUtils.Param("doctorId",list.get(position).getId()+"" );//接口要求传类型，类型一为医师
                        listP.add(typeParam);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //post方式连接  url
                                OkHttpUtils.post(APPConfig.agreeRegisterDoctor, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        Message message = new Message();
                                        message.what = 0;
                                        message.obj = response;
                                        String userDataStr= response.toString();
                                        if (userDataStr.equals("agreeask_success")) {
                                            list.remove(position);
                                            notifyDataSetChanged();
                                        }
                                        handler.sendMessage(message);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(context, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                }, listP);
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

        holder.tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否不通过"+list.get(position).getName()+"的医师申请？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final List<OkHttpUtils.Param> listP = new ArrayList<OkHttpUtils.Param>();
                        OkHttpUtils.Param typeParam = new OkHttpUtils.Param("doctorId",list.get(position).getId()+"" );//接口要求传类型，类型一为医师
                        listP.add(typeParam);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //post方式连接  url
                                OkHttpUtils.post(APPConfig.disagreeRegisterDoctor, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        Message message = new Message();
                                        message.what = 1;
                                        message.obj = response;
                                        if (response.toString().equals("disagreeask_success")) {
                                            list.remove(position);
                                            notifyDataSetChanged();
                                        }
                                        handler.sendMessage(message);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(context, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                }, listP);
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

        return convertView;
    }

    private class ViewHolder {

        TextView tv_number;
        TextView tv_name;
        TextView tv_hospital;
        TextView tv_office;
        TextView tv_agree;
        TextView tv_disagree;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    if (result.equals("agreeask_success")) {
                        Toast.makeText(context, "已通过医师申请", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (result.equals("disagreeask_success")) {
                        Toast.makeText(context, "已拒绝医师申请", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
//            notifyDataSetChanged();
        }
    };
}
