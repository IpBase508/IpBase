package com.ygip.ipbase_android.mvp.universalModel;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.login.view.LoginActivity;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.AES;
import com.ygip.ipbase_android.util.SharedPrefUtils;
import com.ygip.ipbase_android.util.StartActivityUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LockyLuo on 2017/9/20.
 * 调用前先new一个UniversalModel
 */

public class UniversalModel<T>{
    //这里是请求类型
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final MediaType MEDIA_TYPE_FORM = MediaType.parse("multipart/form-data");
    public static final MediaType MEDIA_STREAM = MediaType.parse("application/octet-stream");

    private static UserVo user = null;
    private static String token;
    private static Boolean firstLogin = true;
    private OkHttpClient client;
    private RequestBody requestBody;
    private Request request;
    private Request.Builder requestBuilder;
    private MultipartBody.Builder multipartBuilder;
    private Call call;
    private String responseString = null;
    private LoginBean loginBean;
    private Type type;
    private UniversalResponseBean<T> universalResponseBean = new UniversalResponseBean<>();
    private FileResponseBean fileResponseBean;
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();



    public static Gson getGson() {//获取一个gson实例
        return new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public static Boolean getFirstLogin() {
        return firstLogin;
    }

    public static String getToken() {
        return token;
    }

    public static UserVo getUser() {//获取当前用户，如果离线则获取本地缓存的user
        return user == null ? getLocalUser() : user;
    }

    public static void setUser(UserVo user) {
        UniversalModel.user = user;
    }


    public static UserVo getLocalUser() {//获取本地缓存的用户
        UserVo userVo = null;
        String user = SharedPrefUtils.load("loginUser");
        if (user != null) {
            Logger.i(user);
            try {
                userVo = gson.fromJson(user, UserVo.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return userVo;
    }

    public UniversalModel(){
        this.type =null;
    }

    public UniversalModel(Type type){
        this.type = type;
    }

    public void convert(UniversalResponseBean bean){
        if (bean.getData()!=null&&!bean.getData().equals("")) {
            String s=getGson().toJson(bean.getData());
            bean.setData(getGson().fromJson(s,type));
        }
    }

    /**
     * 偷懒的glide封装
     *
     * @param context
     * @param object  可以传url或者R.id
     * @param view    继承于ImageView的能显示图片的控件
     */
    public static void loadImage(Context context, Object object, ImageView view) {
        Glide.with(context).load(object)
                .placeholder(R.drawable.ic_crop_original_48dp)
                .error(R.mipmap.ipbaselogo)
                .dontAnimate().into(view);
    }

    /**
     * 注销
     *
     * @param activity
     */
    public static void logout(Activity activity) {
        Logger.d("log out");
        user = null;
        token = null;
        SharedPrefUtils.Clear();
        StartActivityUtil.start(activity, LoginActivity.class);
        activity.finish();
    }

    /**
     * @param loginUser        传入带数据的LoginUser（手机号/姓名+密码）
     * @param responseListener
     */
    public void login(LoginUser loginUser, OnResponseListener responseListener) {

        try {
            token = AES.Decrypt(SharedPrefUtils.load("token"), AKey.s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (token != null && getLocalUser() != null) {
            firstLogin = false;
        }
        client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        requestBuilder = new Request.Builder().url(ApiUrl.Post.LOGIN_USER_URL);

        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(loginUser.getPhoneNumber())) {
            map.put("phoneNumber", loginUser.getPhoneNumber());
        }
        if (!TextUtils.isEmpty(loginUser.getMemberName())) {
            map.put("memberName", loginUser.getMemberName());
        }
        map.put("password", loginUser.getPassword());
        Logger.d(map);

        requestBody = RequestBody.create(JSON, gson.toJson(map));
        request = requestBuilder.post(requestBody).build();
        if (call != null) {
            call.cancel();
        }
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (responseListener != null) {
                    finishOnMainThread(responseListener,null, e);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("login onResponse").d(responseString);
                    LoginResponseBean loginResponseBean = gson.fromJson(responseString, LoginResponseBean.class);
                    if (loginResponseBean != null)
                        loginBean = loginResponseBean.getData();
                    else {
                        if (responseListener != null) {
                            finishOnMainThread(responseListener,null, new Exception("未知错误"));
                        }
                        return;
                    }

                    if (loginBean != null) {
                        user = loginBean.getUser();
                        token = loginBean.getToken();

                    } else {
                        if (responseListener != null) {
                            finishOnMainThread(responseListener,null, new Exception(loginResponseBean.getMsg() != null ? loginResponseBean.getMsg() : ""));
                        }
                        return;
                    }

                    if (responseListener != null) {
                        finishOnMainThread(responseListener,null, null);
                    }

                    if (token != null) {
                        user.setPassword(AES.Encrypt(loginUser.getPassword(), AKey.s));
                        SharedPrefUtils.save("loginUser", gson.toJson(user));
                        SharedPrefUtils.save("token", AES.Encrypt(token, AKey.s));//持久化用户信息
                    }

                } catch (Exception e) {
                    if (responseListener != null) {
                        finishOnMainThread(responseListener,null, e);
                    }
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param apiUrl_Post      post的url
     * @param jsonData         要发的json数据
     * @param responseListener 返回 List<JsonObject> 数据
     */
    public void postData(String apiUrl_Post, Object jsonData, @NonNull OnResponseListener<T> responseListener) {
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        requestBuilder = new Request.Builder().url(apiUrl_Post);
        if (token == null) {
            Logger.e("token==null");
        } else {
            requestBuilder.addHeader("token", token);
        }
        requestBody = RequestBody.create(JSON, gson.toJson(jsonData));
        request = requestBuilder.post(requestBody).build();
        if (call != null) {
            call.cancel();
        }
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishOnMainThread(responseListener,null, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("postData onResponse").d(responseString);

                    Type type = new TypeToken<UniversalResponseBean<T>>() {
                    }.getType();
                    universalResponseBean = gson.fromJson(responseString, type);
                    if (UniversalModel.this.type !=null){
                        convert(universalResponseBean);
                    }
                    finishOnMainThread(responseListener,universalResponseBean, null);
                    if (responseString != null) {
                        checkTokenVaild(responseString);
                    }
                } catch (Exception e) {
                    finishOnMainThread(responseListener,universalResponseBean, e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 注意，上传前应压缩文件至1m左右，避免服务器处理时间太长连接中断
     *
     * @param apiUrl_Post          文件上传接口
     * @param fileData             文件
     * @param progressListener     监听进度
     * @param fileResponseListener 获取返回url
     */
    public void postFileData(String apiUrl_Post, File fileData, @NonNull ProgressListener progressListener, @NonNull OnFileResponseListener fileResponseListener) {
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        requestBuilder = new Request.Builder().url(apiUrl_Post);
        multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (token == null) {
            Logger.e("token==null");
        } else {
            requestBuilder.addHeader("token", token);
        }
        MultipartBody.Part part = MultipartBody.Part
                .createFormData("file",
                        fileData.getName(),
                        new ProgressRequestBody(RequestBody.create(MEDIA_TYPE_FORM, fileData), progressListener));

        multipartBuilder.addPart(part);
        requestBody = multipartBuilder.build();

        request = requestBuilder.post(requestBody).build();
        if (call!=null) {
            call.cancel();
        }
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fileResponseListener.onFinish(null, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("postFileData onResponse").d(responseString);
                    fileResponseBean = gson.fromJson(responseString, FileResponseBean.class);
                    finishOnMainThread(fileResponseListener,fileResponseBean, null);
                } catch (Exception e) {
                    finishOnMainThread(fileResponseListener,fileResponseBean, e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param apiUrl_Put       put的url
     * @param jsonData         要修改的json数据/string
     * @param responseListener 返回 List<JsonObject> 数据
     */
    public void putData(String apiUrl_Put, Object jsonData, @NonNull OnResponseListener<T> responseListener) {
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();

        requestBuilder = new Request.Builder().url(apiUrl_Put);

        if (token == null) {
            Logger.e("token==null");
        } else {
            requestBuilder.addHeader("token", token);
        }

        requestBody = RequestBody.create(JSON, gson.toJson(jsonData));
        request = requestBuilder.put(requestBody).build();
        if (call!=null) {
            call.cancel();
        }
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishOnMainThread(responseListener,null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    responseString = response.body().string();
                    Logger.t("putUserData onResponse").d(responseString);

                    Type type = new TypeToken<UniversalResponseBean<T>>() {
                    }.getType();
                    universalResponseBean = gson.fromJson(responseString, type);
                    if (UniversalModel.this.type !=null){
                        convert(universalResponseBean);
                    }
                    finishOnMainThread(responseListener,universalResponseBean, null);
                    if (responseString != null) {
                        checkTokenVaild(responseString);
                    }
                } catch (Exception e) {
                    finishOnMainThread(responseListener,null, e);
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * @param apiUrl_Get       get的url
     * @param conditions       筛选条件
     * @param responseListener 返回 List<JsonObject> 数据
     */
    public void getData(String apiUrl_Get, String[] conditions, @NonNull OnResponseListener<T> responseListener) {
        StringBuffer stringBuffer = new StringBuffer();
        if (conditions == null) {
            stringBuffer.append("all=true");
        } else {
            for (int i = 0; i < conditions.length; i++) {
                stringBuffer.append("&" + conditions[i]);
            }
        }
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();

        requestBuilder = new Request.Builder().url(apiUrl_Get + stringBuffer);

        if (token == null) {
            Logger.e("token==null");
        } else {
            requestBuilder.addHeader("token", token);
        }
        requestBuilder.get();

        request = requestBuilder.build();
        if (call!=null) {
            call.cancel();
        }
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishOnMainThread(responseListener,null, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("getData onResponse").d(responseString);

                    Type type = new TypeToken<UniversalResponseBean<T>>() {
                    }.getType();
                    universalResponseBean = gson.fromJson(responseString, type);
                    if (UniversalModel.this.type !=null){
                        convert(universalResponseBean);
                    }
                    finishOnMainThread(responseListener,universalResponseBean, null);
                    if (responseString != null) {
                        checkTokenVaild(responseString);
                    }
                    return;
                } catch (Exception e) {
                    finishOnMainThread(responseListener,null, e);

                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * @param apiUrl_Del
     * @param id               要删除的id
     * @param responseListener
     */
    public void delData(String apiUrl_Del, String id, @NonNull OnResponseListener<T> responseListener) {
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        requestBuilder = new Request.Builder().url(apiUrl_Del + id);

        if (token == null) {
            Logger.e("token==null");
        } else {
            requestBuilder.addHeader("token", token);
        }
        request = requestBuilder.delete().build();
        if (call!=null) {
            call.cancel();
        }
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFinish(null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("delData onResponse").d(responseString);

                    Type type = new TypeToken<UniversalResponseBean<T>>() {
                    }.getType();
                    universalResponseBean = gson.fromJson(responseString, type);

                    finishOnMainThread(responseListener,universalResponseBean, null);
                    if (responseString != null) {
                        checkTokenVaild(responseString);
                    }
                    return;
                } catch (Exception e) {
                    finishOnMainThread(responseListener,null, e);

                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 在主线程返回结果
     * @param onResponseListener
     * @param bean
     * @param e
     */
    public void finishOnMainThread(OnFileResponseListener onResponseListener,FileResponseBean bean,Exception e){
        new Handler(Looper.getMainLooper()).post(()->{
            onResponseListener.onFinish(bean, e);
        });
    }

    public void finishOnMainThread(OnResponseListener<T> onResponseListener,UniversalResponseBean<T> bean,Exception e){
        new Handler(Looper.getMainLooper()).post(()->{
            onResponseListener.onFinish(bean, e);
        });
    }

    /**
     * 根据返回的json长度判断是否是错误信息
     *
     * @param s
     */
    public void checkTokenVaild(String s) {
        if (s == null || s.length() <= 2 || s.length() > 50) {
            return;
        }
        try {//检测token是否失效
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> map = gson.fromJson(s, type);
            Logger.t("checkTokenVaild").d(map);
            if (map != null) {
                if (map.get("message").equals("401"))
                    MainActivity.showLogOut("token失效，请重新登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取消call请求
    public void cancelTask() {
        if (call != null) {
            call.cancel();
        }
        call = null;
    }
}
