package com.ygip.ipbase_android.mvp.universalModel;

import android.app.Activity;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.util.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LockyLuo on 2017/9/20.
 */

public class UniversalModel {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    protected static String getToken() {
        return token;
    }

    private static String token;
    OkHttpClient client;
    RequestBody body;
    Request request;
    Request.Builder requestBuilder;
    OkHttpClient.Builder okBuilder;
    Call call;
    String responseString = null;
    UserVo user = null;
    LoginResponseBean loginResponseBean;
    UniversalResponseBean universalResponseBean;

    String data;
    Gson gson = new Gson();

    public void login(Activity activity) {

        LoginUser loginUser = new LoginUser();
        loginUser.setMemberName("张三");
        loginUser.setPassword("123456789");

        client = new OkHttpClient();
        requestBuilder = new Request.Builder().url("http://139.199.220.49:8888/base/api/login");

        body = RequestBody.create(JSON, gson.toJson(loginUser));
        request = requestBuilder.post(body).build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    ToastUtils.show(e.getMessage());
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    responseString = response.body().string();
                    loginResponseBean = gson.fromJson(responseString, LoginResponseBean.class);
                    user = loginResponseBean.getUser();
                    token = loginResponseBean.getToken();
                    Logger.t("MineModel").d(token);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                activity.runOnUiThread(() -> {
                    ToastUtils.show(user.getMemberName() + "已登录");
                });
            }
        });
    }

    public void getMemberList(Activity activity,String[] conditions, OnResponseListener responseListener){
//        if(conditions==null)
//            return;
//        StringBuffer stringBuffer=new StringBuffer();
//        for(int i=0;i<conditions.length;i++){
//            stringBuffer.append("&"+conditions[i]);
//        }
        client = new OkHttpClient();
        requestBuilder = new Request.Builder().url("http://139.199.220.49:8888/base/api/user?all=true&condition=研发部");
        if(token==null){
            Logger.d("token==null");
        }
        requestBuilder.addHeader("token", token);
        requestBuilder.get();



        request = requestBuilder.build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    ToastUtils.show(e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    universalResponseBean = gson.fromJson(responseString, UniversalResponseBean.class);
                    responseListener.onFinish(universalResponseBean.getData(),null);
                } catch (Exception e) {
                    activity.runOnUiThread(() -> {
                        ToastUtils.show(e.getMessage());
                    });
                    e.printStackTrace();
                }

            }
        });
    }
}
