package com.ygip.ipbase_android.mvp.mine.presenter;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.mine.adapter.MineAdapter;
import com.ygip.ipbase_android.mvp.mine.view.MineFragment;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnFileResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.ProgressListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UpdateUserLevel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.mvp.universalView.CheckPhoneNumActivity;
import com.ygip.ipbase_android.util.BitmapUtils;
import com.ygip.ipbase_android.util.DateUtils;
import com.ygip.ipbase_android.util.FileUtils;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lockyluo on 2017/7/28.
 * UniversalModel使用demo
 */

public class MinePresenter extends XPresent<MineCommon> {
    private static UniversalModel universalModel = new UniversalModel();
    private ArrayList<String> data = new ArrayList<>();
    private UserVo userVo = new UserVo();
    private UserVo userVoTest = new UserVo();

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    int progress;
    private RegisterPage registerPage;

    //偷懒的通用修改信息回调
    public static OnResponseListener onPutListener = new OnResponseListener() {
        @Override
        public void onFinish(UniversalResponseBean responseBean, Exception e) {
            try {
                if (e == null) {
                    if (responseBean != null) {
                        if (responseBean.getData() != null) {
                            Type type = new TypeToken<UserVo>() {
                            }.getType();
                            Gson gson = UniversalModel.getGson();
                            try {
                                UniversalModel.setUser(gson.fromJson(gson.toJson(responseBean.getData()), type));
                            } catch (JsonSyntaxException e1) {
                                e1.printStackTrace();
                            }
                            new Handler(Looper.getMainLooper()).post(() -> {
                                ToastUtils.show("修改成功");
                            });
                            MineFragment.requireRefresh = true;
                        } else {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                ToastUtils.show(responseBean.getMsg());
                            });
                        }
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show("修改失败");
                        });
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ToastUtils.show(e.getMessage());
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };


    public void showPhoneVaildate(Activity activity, @Nullable String title, @Nullable String phone) {
        if (title != null) {
            CheckPhoneNumActivity.setTitle(title);
        }
        if (phone != null) {
            CheckPhoneNumActivity.setPhoneNumber(phone);
        }
        StartActivityUtil.startForResult(activity, CheckPhoneNumActivity.class, 100);
    }


    public ArrayList<String> getMineData(@MineAdapter.Mode int i) {
        userVo = UniversalModel.getUser();
        if (userVo == null)
            userVo = UniversalModel.getLocalUser();
        if (userVo == null)
            return null;
        data.clear();
        data.add(TextUtils.isEmpty(userVo.getMemberName()) ? "未填写姓名" : userVo.getMemberName());
        data.add(TextUtils.isEmpty(userVo.getDepartment()) ? "未填写部门" : userVo.getDepartment());
        data.add(TextUtils.isEmpty(userVo.getGrade()) ? "未填写年级" : userVo.getGrade());

        if (i == MineAdapter.MINE) {
            data.add("消息");
            data.add("修改密码");
            data.add("清除缓存");
            data.add("退出登录");
        } else {


            data.add(TextUtils.isEmpty(userVo.getPhoneNumber()) ? "未填写" : userVo.getPhoneNumber());
            data.add(userVo.getUserLevel() + "");
            data.add(DateUtils.getThisTime(userVo.getCreateTime() * 1000));
        }
        return data;
    }


    public void postHead(Uri uri, CircleImageView iv, CircleProgressBar progressBar) {
        File file = FileUtils.Uri2File(getV().getActivity(), uri);
        if (file.length() > 1024 * 1024) {
            file = BitmapUtils.getSmallerImageFile(getV().getActivity(), uri);
        }//压缩

        universalModel.postFileData(ApiUrl.Post.POST_FILE_URL, file,
                new ProgressListener() {
                    @Override
                    public void onProgress(long progressLength, long totalLength, boolean done) {

                        Logger.d(progressLength / totalLength);
                        getV().getActivity().runOnUiThread(() -> {
                            if (!done) {
                                if (progressBar.getVisibility() != View.VISIBLE) {
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                                progress = (int) (progressLength * 100 / totalLength);
                                if (progress >= 95) {
                                    progressBar.setProgress(100);
                                } else {
                                    progressBar.setProgress((int) (progressLength * 100 / totalLength));

                                }
                            } else {
                                ToastUtils.show("等待服务器处理");
                            }
                        });
                        Logger.d("--progress:" + progressLength + "---" + totalLength);
                    }
                },
                new OnFileResponseListener() {
                    @Override
                    public void onFinish(FileResponseBean fileResponseBean, Exception e) {
                        Logger.t("postFileData").d(fileResponseBean);
                        if (fileResponseBean != null) {
                            userVo = new UserVo();
                            userVo.setUserId(UniversalModel.getUser().getUserId());
                            userVo.setImageUrl(fileResponseBean.getFile_path());
                            putUserData(userVo);

                            getV().getActivity().runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                loadHead(iv, userVo.getImageUrl());
                            });
                        }
                    }
                });
    }

    public void changePassword(String phone, String smsCode, String password, OnLoadListener<Boolean> onLoadListener) {
        UniversalModel universalModel = new UniversalModel();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phoneNumber", phone);

        universalModel.putData(ApiUrl.Put.FIND_USER_URL, jsonObject, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                Gson gson = UniversalModel.getGson();
                UserVo userVo;
                if (responseBean != null && e == null) {
                    try {
                        userVo = gson.fromJson(gson.toJson(responseBean.getData()), UserVo.class);
                        jsonObject.addProperty("userId", userVo.getUserId());
                        jsonObject.addProperty("smsCode", smsCode);
                        jsonObject.addProperty("password", password);
//                        Logger.d(jsonObject);
                        universalModel.putData(ApiUrl.Put.FORGET_PASSWORD_URL, jsonObject, new OnResponseListener() {
                            @Override
                            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                                if (responseBean != null && e == null) {
                                    if (responseBean.getData() != null) {
                                        onLoadListener.onFinish(true, null);
                                    } else
                                        onLoadListener.onFinish(false, responseBean.getMsg() == null ? new Exception("修改密码失败") : new Exception(responseBean.getMsg()));
                                } else {
                                    onLoadListener.onFinish(false, e == null ? new Exception("修改密码失败") : e);
                                }
                            }
                        });
                    } catch (Exception e1) {
                        onLoadListener.onFinish(false, e1 == null ? new Exception("用户查询失败") : e1);
                        e1.printStackTrace();
                    }
                } else {
                    onLoadListener.onFinish(false, e == null ? new Exception("用户查询失败") : e);
                }
            }
        });
    }

    public static JsonObject User2Object(UserVo userVo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userVo.getUserId());
        if (userVo.getMemberName() != null)
            jsonObject.addProperty("memberName", userVo.getMemberName());
        if (userVo.getPhoneNumber() != null)
            jsonObject.addProperty("phoneNumber", userVo.getPhoneNumber());
        if (userVo.getDepartment() != null)
            jsonObject.addProperty("department", userVo.getDepartment());
        if (userVo.getImageUrl() != null)
            jsonObject.addProperty("imageUrl", userVo.getImageUrl());
        if (userVo.getDelete() != null)
            jsonObject.addProperty("delete", userVo.getDelete());
        if (userVo.getGrade() != null)
            jsonObject.addProperty("grade", userVo.getGrade());
        if (userVo.getUserLevel() != 0)
            jsonObject.addProperty("userLevel", userVo.getUserLevel());
        return jsonObject;
    }

    public void putUserData(UserVo userVo) {//更新user信息
        JsonObject jsonObject = User2Object(userVo);
        Logger.d(jsonObject);
        universalModel.putData(ApiUrl.Put.PUT_USER_URL, jsonObject, onPutListener);
    }

    public void loadHead(CircleImageView iv) {
        userVo = UniversalModel.getUser();
        try {
            Glide.with(getV().getActivity())
                    .load(userVo.getImageUrl())
                    .dontAnimate()
                    .placeholder(R.mipmap.ipbaselogo)
                    .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHead(CircleImageView iv, @NonNull String url) {

        Glide.with(getV().getActivity())
                .load(TextUtils.isEmpty(url) ? R.mipmap.ipbaselogo : url)
                .dontAnimate()
                .placeholder(R.mipmap.ipbaselogo)
                .into(iv);
    }

    //-------------------------------------以下部分为UniversalModel使用get、post、put、del的测试

    public void testLogin() {//测试登录
        LoginUser loginUser = new LoginUser();
        loginUser.setMemberName("张三");//这里set手机号也行
        loginUser.setPassword("123456789");
        universalModel.login(loginUser, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                if (e == null) {
                    (new Handler(Looper.getMainLooper())).postDelayed(() -> {
                        ToastUtils.show(UniversalModel.getUser().getMemberName() + "已登录");
                    }, 100);
                }
            }
        });
    }


    public void testPost() {
        userVo = new UserVo();
        userVo.setMemberName("666");
        userVo.setPassword("123");
        userVo.setPhoneNumber("13666666666");
        userVo.setDepartment("吃瓜群众");
        userVo.setGrade("无");
        postData(userVo);
    }

    public void postData(UserVo userVo) {//-----------post

        Logger.t("生成的user").d(userVo);
        universalModel = new UniversalModel<UserVo>();
        universalModel.postData(ApiUrl.Post.POST_USER_URL, userVo, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                Logger.t("postData").d(responseBean);

                userVoTest = gson.fromJson(gson.toJson(responseBean.getData()), UserVo.class);
                getData();

            }
        });
    }

    public void postFileData(Uri uri, CircleImageView iv, CircleProgressBar progressBar) {//-----------postFile
        File file = FileUtils.Uri2File(getV().getActivity(), uri);
        if (file.length() > 1024 * 1024) {
            file = BitmapUtils.getSmallerImageFile(getV().getActivity(), uri);
        }

        universalModel.postFileData(ApiUrl.Post.POST_FILE_URL, file,
                new ProgressListener() {
                    @Override
                    public void onProgress(long progressLength, long totalLength, boolean done) {

                        Logger.d(progressLength / totalLength);
                        getV().getActivity().runOnUiThread(() -> {
                            if (!done) {
                                if (progressBar.getVisibility() != View.VISIBLE) {
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                                progress = (int) (progressLength * 100 / totalLength);
                                if (progress >= 99) {
                                    progressBar.setProgress(100);
                                } else {
                                    progressBar.setProgress((int) (progressLength * 100 / totalLength));

                                }
                            } else {
                                ToastUtils.show("等待服务器处理");
                            }
                        });
                        Logger.d("--progress:" + progressLength + "---" + totalLength);
                    }
                },
                new OnFileResponseListener() {
                    @Override
                    public void onFinish(FileResponseBean fileResponseBean, Exception e) {
                        Logger.t("postFileData").d(fileResponseBean);
                        if (fileResponseBean != null) {

                            userVo.setImageUrl(fileResponseBean.getFile_path());
                            getV().getActivity().runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                Glide.with(getV().getActivity())
                                        .load(fileResponseBean.getFile_path())
                                        .dontAnimate()
                                        .placeholder(R.mipmap.ipbaselogo)
                                        .into(iv);
                            });
                        }
                    }
                });
    }

    public void getData() {//-----------get
        universalModel = new UniversalModel<ArrayList<UserVo>>();
        universalModel.getData(ApiUrl.Get.GET_USER_URL, new String[]{"all=true", "condition=吃瓜群众"}, new OnResponseListener<ArrayList<UserVo>>() {
            @Override
            public void onFinish(UniversalResponseBean<ArrayList<UserVo>> responseBean, Exception e) {
                if (e == null) {
                    Type type = new TypeToken<ArrayList<UserVo>>() {
                    }.getType();
                    Logger.t("getData").d(responseBean.getData());
                    putUserData();
                } else {
                    Logger.t("getData").d(e);
                }
            }
        });
    }

    public void putUserData() {//-----------put
        UpdateUserLevel userLevel = new UpdateUserLevel();//测试put修改数据
        userLevel.setUserLevel(2);
        userLevel.setUserId(userVoTest.getUserId());
        Logger.d(userLevel);
        universalModel.putData(ApiUrl.Put.PUT_USER_ADMIN_URL, userLevel, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                Logger.t("putUserData").d(responseBean);
                if (e == null) {
                    delData();
                }
            }
        });
    }

    public void delData() {//-----------del
        universalModel.delData(ApiUrl.Del.DEL_USER_ADMIN_URL, userVoTest.getUserId(), new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                Logger.t("delData").d(responseBean);
                //--------------测试完毕
                getV().getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.show("测试完毕");
                    }
                });
            }
        });
    }

    public void onDestory() {
        if (universalModel != null) {
            universalModel.cancelTask();
        }
    }

}
