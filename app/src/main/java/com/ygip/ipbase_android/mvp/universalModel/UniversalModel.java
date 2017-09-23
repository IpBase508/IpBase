package com.ygip.ipbase_android.mvp.universalModel;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBeanList;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.AES;
import com.ygip.ipbase_android.util.SharedPrefUtils;
import com.ygip.ipbase_android.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class UniversalModel {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final MediaType MEDIA_TYPE_FORM = MediaType.parse("multipart/form-data");
    public static final MediaType MEDIA_STREAM = MediaType.parse("application/octet-stream");

    private static UserVo user = null;
    private static String token;
    private OkHttpClient client;
    private RequestBody requestBody;
    private Request request;
    private Request.Builder requestBuilder;
    private MultipartBody.Builder multipartBuilder;
    private Call call;
    private String responseString = null;
    private LoginBean loginBean;
    private UniversalResponseBeanList universalResponseBeanList;
    private UniversalResponseBean universalResponseBean;
    private FileResponseBean fileResponseBean;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static String getToken() {
        return token;
    }

    public static UserVo getUser() {
        return user;
    }

    /**
     * @param activity
     * @param loginUser        传入带数据的LoginUser（手机号/姓名+密码）
     * @param responseListener
     */
    public void login(Activity activity, LoginUser loginUser, OnResponseListener responseListener) {
        client = new OkHttpClient.Builder().connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES).build();
        requestBuilder = new Request.Builder().url(ApiUrl.Post.LOGIN_USER_URL);

        requestBody = RequestBody.create(JSON, gson.toJson(loginUser));
        request = requestBuilder.post(requestBody).build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFinish(null, e);
                activity.runOnUiThread(() -> {
                    ToastUtils.show("登录失败 " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("login onResponse").d(responseString);
                    loginBean = gson.fromJson(responseString, LoginResponseBean.class).getData();
                    user = loginBean.getUser();
                    token = loginBean.getToken();

                    responseListener.onFinish(null, null);
                    activity.runOnUiThread(() -> {
                        ToastUtils.show("欢迎 " + user.getMemberName());
                        Logger.d(token);
                    });

                    if (token != null) {
                        loginUser.setPassword(AES.Encrypt(loginUser.getPassword(), AKey.s));
                        SharedPrefUtils.save("loginUser", gson.toJson(loginUser));
                    }

                } catch (Exception e) {
                    responseListener.onFinish(null, e);
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
    public void postData(String apiUrl_Post, Object jsonData, OnResponseListener responseListener) {
        client = new OkHttpClient();
        requestBuilder = new Request.Builder().url(apiUrl_Post);
        if (token == null) {
            Logger.e("token==null");
            return;
        }
        requestBuilder.addHeader("token", token);
        requestBody = RequestBody.create(JSON, gson.toJson(jsonData));
        request = requestBuilder.post(requestBody).build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFinish(universalResponseBeanList, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("postData onResponse").d(responseString);
                    universalResponseBean = gson.fromJson(responseString, UniversalResponseBean.class);
                    universalResponseBeanList = new UniversalResponseBeanList();
                    universalResponseBeanList.setCode(universalResponseBean.getCode());
                    universalResponseBeanList.setError(universalResponseBean.getError());
                    universalResponseBeanList.setMsg(universalResponseBean.getMsg());
                    universalResponseBeanList.setSuccess(universalResponseBean.getSuccess());

                    List<JsonObject> jsonObjects = new ArrayList<>();
                    jsonObjects.add(universalResponseBean.getData());
                    universalResponseBeanList.setData(jsonObjects);
                    responseListener.onFinish(universalResponseBeanList, null);
                } catch (Exception e) {
                    responseListener.onFinish(universalResponseBeanList, e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param apiUrl_Post          文件上传接口
     * @param fileData             文件
     * @param progressListener     监听进度
     * @param fileResponseListener 获取返回url
     */
    public void postFileData(String apiUrl_Post, File fileData, ProgressListener progressListener, OnFileResponseListener fileResponseListener) {
        client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MINUTES)
                .readTimeout(3000, TimeUnit.MINUTES)
                .writeTimeout(3000, TimeUnit.MINUTES).build();
        requestBuilder = new Request.Builder().url(apiUrl_Post);
        multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (token == null) {
            Logger.e("token==null");
            return;
        }
        MultipartBody.Part part = MultipartBody.Part
                .createFormData("file",
                        fileData.getName(),
                        new ProgressRequestBody(RequestBody.create(MEDIA_TYPE_FORM, fileData), progressListener));
//                        RequestBody.create(MEDIA_TYPE_FORM,fileData));

        multipartBuilder.addPart(part);
        requestBody = multipartBuilder.build();

        request = requestBuilder.post(requestBody).build();
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

                    fileResponseListener.onFinish(fileResponseBean, null);
                } catch (Exception e) {
                    fileResponseListener.onFinish(fileResponseBean, e);
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
    public void putData(String apiUrl_Put, Object jsonData, OnResponseListener responseListener) {
        client = new OkHttpClient();
        requestBuilder = new Request.Builder().url(apiUrl_Put);
        if (token == null) {
            Logger.e("token==null");
            return;
        }
        requestBuilder.addHeader("token", token);
        requestBody = RequestBody.create(JSON, gson.toJson(jsonData));
        request = requestBuilder.put(requestBody).build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFinish(universalResponseBeanList, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("putData onResponse").d(responseString);
                    universalResponseBean = gson.fromJson(responseString, UniversalResponseBean.class);
                    universalResponseBeanList.setCode(universalResponseBean.getCode());
                    universalResponseBeanList.setError(universalResponseBean.getError());
                    universalResponseBeanList.setMsg(universalResponseBean.getMsg());
                    universalResponseBeanList.setSuccess(universalResponseBean.getSuccess());

                    List<JsonObject> jsonObjects = new ArrayList<>();
                    jsonObjects.add(universalResponseBean.getData());
                    universalResponseBeanList.setData(jsonObjects);
                    responseListener.onFinish(universalResponseBeanList, null);
                } catch (Exception e) {
                    responseListener.onFinish(universalResponseBeanList, e);
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
    public void getData(String apiUrl_Get, String[] conditions, OnResponseListener responseListener) {
        StringBuffer stringBuffer = new StringBuffer();
        if (conditions == null) {
            stringBuffer.append("all=true");
        } else {
            for (int i = 0; i < conditions.length; i++) {
                stringBuffer.append("&" + conditions[i]);
            }
        }
        client = new OkHttpClient();
        requestBuilder = new Request.Builder().url(apiUrl_Get + stringBuffer);
        if (token == null) {
            Logger.e("token==null");
            return;
        }
        requestBuilder.addHeader("token", token);
        requestBuilder.get();

        request = requestBuilder.build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFinish(universalResponseBeanList, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("getData onResponse").d(responseString);
                    universalResponseBeanList = gson.fromJson(responseString, UniversalResponseBeanList.class);
                    responseListener.onFinish(universalResponseBeanList, null);
                } catch (Exception e) {
                    responseListener.onFinish(universalResponseBeanList, e);
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
    public void delData(String apiUrl_Del, String id, OnResponseListener responseListener) {
        client = new OkHttpClient();
        requestBuilder = new Request.Builder().url(apiUrl_Del + id);
        if (token == null) {
            Logger.e("token==null");
            return;
        }
        requestBuilder.addHeader("token", token);
        request = requestBuilder.delete().build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFinish(universalResponseBeanList, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    responseString = response.body().string();
                    Logger.t("delData onResponse").d(responseString);
                    universalResponseBeanList = gson.fromJson(responseString, UniversalResponseBeanList.class);

                    responseListener.onFinish(universalResponseBeanList, null);
                } catch (Exception e) {
                    responseListener.onFinish(universalResponseBeanList, e);
                    e.printStackTrace();
                }
            }
        });

    }
}
