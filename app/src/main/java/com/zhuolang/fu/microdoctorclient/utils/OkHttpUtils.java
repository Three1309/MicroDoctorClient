package com.zhuolang.fu.microdoctorclient.utils;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * Description : OkHttp网络连接封装工具类 
 * Author : ldong 
 * Date   : 16/1/31 
 */  
public class OkHttpUtils {  
  
    private static final String TAG = "OkHttpUtils";  
  
    private static OkHttpUtils mInstance;  
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private static Gson gson = null;
    private OkHttpUtils() {  
        /** 
         * 构建OkHttpClient 
         */  
        mOkHttpClient = new OkHttpClient();
        /** 
         * 设置连接的超时时间 
         */  
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        /** 
         * 设置响应的超时时间 
         */  
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        /** 
         * 请求的超时时间 
         */  
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        /** 
         * 允许使用Cookie 
         */  
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        /** 
         * 获取主线程的handler 
         */  
        mHandler = new Handler(Looper.getMainLooper());
    }  
  
    /** 
     * 通过单例模式构造对象 
     * @return OkHttpUtils 
     */  
    private synchronized static OkHttpUtils getmInstance() {  
        if (mInstance == null) {  
            mInstance = new OkHttpUtils();  
        }  
        return mInstance;  
    }  
  
    /** 
     * 构造Get请求 
     * @param url  请求的url 
     * @param callback  结果回调的方法 
     */  
    private void getRequest(String url, final ResultCallback callback) {  
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);  
    }  
  
    /** 
     * 构造post 请求 
     * @param url 请求的url 
     * @param callback 结果回调的方法 
     * @param params 请求参数 
     */  
    private void postRequest(String url, final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url, params);  
        deliveryResult(callback, request);  
    }  
  
    /** 
     * 处理请求结果的回调 
     * @param callback 
     * @param request 
     */  
    private void deliveryResult(final ResultCallback callback, Request request) {  
  
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override  
            public void onFailure(Request request, final IOException e) {
                sendFailCallback(callback, e);  
            }  
  
            @Override  
            public void onResponse(Response response) throws IOException {  
                try {  
                    String str = response.body().string();  
//                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str);  
//                    } else {
//                        Object object = new JSONObject(str);
//                        sendSuccessCallBack(callback, object);
//                    }
                } catch (final Exception e) {  
                    sendFailCallback(callback, e);
                }  
  
            }  
        });  
    }  
  
    /** 
     * 发送失败的回调 
     * @param callback 
     * @param e 
     */  
    private void sendFailCallback(final ResultCallback callback, final Exception e) {  
        mHandler.post(new Runnable() {  
            @Override  
            public void run() {  
                if (callback != null) {  
                    callback.onFailure(e);  
                }  
            }  
        });  
    }  
  
    /** 
     * 发送成功的调 
     * @param callback 
     * @param obj 
     */  
    private void sendSuccessCallBack(final ResultCallback callback, final Object obj) {  
        mHandler.post(new Runnable() {  
            @Override  
            public void run() {  
                if (callback != null) {  
                    callback.onSuccess(obj);  
                }  
            }  
        });  
    }  
  
    /** 
     * 构造post请求 
     * @param url  请求url 
     * @param params 请求的参数 
     * @return 返回 Request 
     */  
    private Request buildPostRequest(String url, List<Param> params) {  
        FormEncodingBuilder builder = new FormEncodingBuilder();  
        for (Param param : params) {  
            builder.add(param.key, param.value);  
        }  
        RequestBody requestBody = builder.build();  
        return new Request.Builder().url(url).post(requestBody).build();  
    }  
  
  
    /**********************对外接口************************/  
  
    /** 
     * get请求 
     * @param url  请求url 
     * @param callback  请求回调 
     */  
    public static void get(String url, ResultCallback callback) {  
        getmInstance().getRequest(url, callback);  
    }  
  
    /** 
     * post请求 
     * @param url       请求url 
     * @param callback  请求回调 
     * @param params    请求参数 
     */  
    public static void post(String url, final ResultCallback callback, List<Param> params) {  
        getmInstance().postRequest(url, callback, params);  
    }  
  
    /** 
     * http请求回调类,回调方法在UI线程中执行 
     * @param <T> 
     */  
    public static abstract class ResultCallback<T> {  
  
        Type mType;  
  
        public ResultCallback(){  
//            mType = getSuperclassTypeParameter(getClass());
        }  
  
        static Type getSuperclassTypeParameter(Class<?> subclass) {  
            Type superclass = subclass.getGenericSuperclass();//返回父类的类型  
            if (superclass instanceof Class) {  
                throw new RuntimeException("Missing type parameter.");  
            }  
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }  

        /** 
         * 请求成功回调 
         * @param response 
         */  
        public abstract void onSuccess(T response);  
  
        /** 
         * 请求失败回调 
         * @param e 
         */  
        public abstract void onFailure(Exception e);  
    }  
  
    /** 
     * post请求参数类 
     */  
    public static class Param {  
  
        String key;//请求的参数  
        String value;//参数的值  
  
        public Param() {  
        }  
  
        public Param(String key, String value) {  
            this.key = key;  
            this.value = value;  
        }  
  
    }


    /**
     * json转Bean
     */
    public static <T> T getEntityFromJson(String gsonString, Class<T> cls) {
        T t = null;
        try {
            if (gson != null && !TextUtils.isEmpty(gsonString)) {
                //过滤gson
                gsonString = gsonString.trim();
                if (gsonString.startsWith("ufeff")) {
                    gsonString = gsonString.substring(1);
                }
                t = gson.fromJson(gsonString, cls);
            }
            return t;
        } catch (Exception e) {
        }
        return null;
    }
    /**
     * json转成ArrayList
     */
    public static <T> ArrayList<T> getListFromJson(String gsonString, Class<T> tClass) {
        ArrayList<T> list;
        try {
            if (gson != null && !TextUtils.isEmpty(gsonString)) {
                //过滤gson
                gsonString = gsonString.trim();
                if (gsonString.startsWith("ufeff")) {
                    gsonString = gsonString.substring(1);
                }
                TypeToken<ArrayList<T>> tt = new TypeToken<ArrayList<T>>() {
                };
                list = gson.fromJson(gsonString, tt.getType());
                return list;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> List<T> getObjectList(String jsonString, Class<T> cls){
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对象转成Json
     */
    private static <T> String getJsonFromEntity(T t) {
        String result = "";
        if (gson != null && t != null) {
            result = gson.toJson(t);
        }
        return result;
    }


    /**
     * Json转成map的
     */
    private static <T> HashMap<String, T> GsonToMaps(String gsonString) {
        HashMap<String, T> map = null;
        if (gson != null && !TextUtils.isEmpty(gsonString)) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 集合转成Json
     */
    private static <T> String getJsonFromList(List<T> t) {
        String result = "";
        if (gson != null && t != null) {
            result = gson.toJson(t);
        }
        return result;
    }

    /**
     * map转成Json
     */
    public static String getJsonFromMap(HashMap<String, String> map) {
        String result = "";
        if (gson != null && map != null) {
            result = gson.toJson(map);
        }
        return result;
    }

}  