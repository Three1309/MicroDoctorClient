package com.zhuolang.fu.microdoctorclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.fu.microdoctorclient.R;
import com.zhuolang.fu.microdoctorclient.common.APPConfig;
import com.zhuolang.fu.microdoctorclient.utils.OkHttpUtils;
import com.zhuolang.fu.microdoctorclient.utils.SharedPrefsUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2017/4/13.
 * 注册活动
 */
public class RegisterActivity extends Activity{

    private EditText et_phone;
    private EditText et_psd;
    private EditText et_oknumber;
    private TextView tv_getnumber;
    private Button bt_register;
    private int mobile_code;

    private String phone;
    private String psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        initView();
        initMotion();
    }
    private void initView() {
        //通过findViewById得到对应的控件对象
        et_phone=(EditText)findViewById(R.id.et_register_phone);
        et_psd=(EditText)findViewById(R.id.et_register_psd);
        et_oknumber=(EditText)findViewById(R.id.et_register_oknumber);
        tv_getnumber=(TextView) findViewById(R.id.tv_register_getnumber);

        bt_register=(Button)findViewById(R.id.bt_register_reg);
    }

    /**
     * 初始化监听等
     */
    private void initMotion(){

        //获取手机验证码
        tv_getnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getNumber();
                phone = et_phone.getText().toString().trim();

                final String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
                mobile_code = (int)((Math.random()*9+1)*100000);

                String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");

                final List<OkHttpUtils.Param> list1 = new ArrayList<OkHttpUtils.Param>();
                OkHttpUtils.Param apiidParam = new OkHttpUtils.Param("account", "C62138766");//查看用户名请登录用户中心->验证码、通知短信->帐户及签名设置->APIID
                OkHttpUtils.Param apikeyParam = new OkHttpUtils.Param("password", "f2f3a56a1bd4de09fd0b4fb5c7f6ba4a");
                //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
                OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("mobile", phone);
                OkHttpUtils.Param contentParam = new OkHttpUtils.Param("content", content);
                list1.add(apiidParam);
                list1.add(apikeyParam);
                list1.add(phoneParam);
                list1.add(contentParam);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //post方式连接  url
                        OkHttpUtils.post(Url, new OkHttpUtils.ResultCallback() {
                            @Override
                            public void onSuccess(Object response) {
//                                Message message = new Message();
//                                message.what = 0;
//                                message.obj = response;
//                                handler.sendMessage(message);
                                Toast.makeText(RegisterActivity.this, "短信提交成功", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Log.d("testRun", "请求失败loginActivity----new Thread(new Runnable() {------");
                                Toast.makeText(RegisterActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        },list1);
                    }

                }).start();
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                phone = et_phone.getText().toString().trim();
                psd = et_psd.getText().toString().trim();

                if (phone.equals("")||psd.equals("")){
                    Toast.makeText(RegisterActivity.this,"账号密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (et_oknumber.getText().toString().trim().equals(mobile_code + "")) {
                        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
                        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone",phone);
                        OkHttpUtils.Param psdParam = new OkHttpUtils.Param("password",psd);
                        list.add(phoneParam);
                        list.add(psdParam);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //post方式连接  url
                                OkHttpUtils.post(APPConfig.register, new OkHttpUtils.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        Message message = new Message();
                                        message.what = 0;
                                        message.obj = response;
                                        handler.sendMessage(message);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.d("testRun", "请求失败loginActivity----new Thread(new Runnable() {------");
                                        Toast.makeText(RegisterActivity.this, "服务器连接失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                },list);
                            }

                        }).start();
                    }else
                        Toast.makeText(RegisterActivity.this, "验证码错误，请确认", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }



    public void getNumber() {

        phone = et_phone.getText().toString().trim();

        String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");

        mobile_code = (int)((Math.random()*9+1)*100000);

        String content = new String("您的微医注册验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", "C62138766"), //查看用户名请登录用户中心->验证码、通知短信->帐户及签名设置->APIID
                new NameValuePair("password", "f2f3a56a1bd4de09fd0b4fb5c7f6ba4a"),  //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
                //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content),
        };
        method.setRequestBody(data);

        try {
            client.executeMethod(method);

            String SubmitResult =method.getResponseBodyAsString();

            //System.out.println(SubmitResult);

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");

            System.out.println(code);
            System.out.println(msg);
            System.out.println(smsid);

            if("2".equals(code)){
                System.out.println("短信提交成功");
                Toast.makeText(RegisterActivity.this, "短信提交成功", Toast.LENGTH_SHORT).show();
            }

        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String result = (String)msg.obj;
                    if (result.equals("register_success")){
                        //注册成功
                        Toast.makeText(RegisterActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, LoginActivity.class);
//                        Toast.makeText(RegisterActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"已存在该手机号用户,注册失败，请重试！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };



}
