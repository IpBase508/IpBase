package com.ygip.ipbase_android.mvp.member.present;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.member.view.MemberFragment;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnFileResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.ProgressListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.FileUtils;
import com.ygip.ipbase_android.util.StringUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.listener.OnActionListener;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.UpdateOrDeleteCallback;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by XQM on 2017/7/23.
 */

public class MemberPresenter extends XPresent<MemberFragment> {
    private UniversalModel<List<UserVo>> universalModel;
    private Gson gson = UniversalModel.getGson();
    private static List<UserVo> memberList;
    public static List<UserVo> memberListCache;

    private String dbPath;
    private File dbFile;
    private SQLiteDatabase database;

    public static List<UserVo> getMemberList() {
        return memberList;
    }


    public void loadMembers(Boolean callRefreshView) {
        universalModel = new UniversalModel<>();
        memberList = new ArrayList<>();
        universalModel.getData(ApiUrl.Get.GET_USER_URL, null, new OnResponseListener<List<UserVo>>() {
            @Override
            public void onFinish(UniversalResponseBean<List<UserVo>> responseBean, Exception e) {
                if (e == null) {
                    try {
                        String data = gson.toJson(responseBean.getData());
                        Type type = new TypeToken<ArrayList<UserVo>>() {
                        }.getType();
                        memberList = gson.fromJson(data, type);
                        if (memberList != null) {
                            memberList = sort(memberList);
                            if (callRefreshView) {
                                getV().setMembers(memberList);
                            }
                            //持久化数据
                            save2db();
                        } else {
                            getV().setMembers(getLocalMembers());
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    getV().getActivity().runOnUiThread(() -> ToastUtils.show("刷新失败"));
                    getV().setMembers(getLocalMembers());
                    Logger.d(e.getMessage());
                }
            }
        });
    }

    public List<UserVo> sort(List<UserVo> memberList) {
        String[] departments = getV().getResources().getStringArray(R.array.departments);
        List<UserVo> sortList = new ArrayList<>();

        for (int i = 1; i < departments.length; i++) {
            for (UserVo user : memberList) {
                if (user.getDepartment().equals(departments[i])) {
                    sortList.add(user);
                }
            }
        }
        memberListCache = sortList;
        return sortList;
    }

    public void save2db() {

        try {
            DataSupport.deleteAllAsync(UserVo.class).listen(new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DataSupport.saveAll(memberList);
                        }
                    }, 80);
                }
            });

            Logger.i("---renew db---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delDB(Activity context) {
        dbPath = getV().getActivity().getFilesDir().getParentFile().getAbsolutePath() + "/databases";
        dbFile = new File(dbPath + "/ipbase.db");
        dbFile.delete();
        context.finish();
    }

    public static List<UserVo> getLocalMembers() {
        memberList = DataSupport.findAll(UserVo.class);
        return memberList;
    }

    public void batchImportMembers(Uri uri) {
        File file = FileUtils.Uri2File(getV().getActivity(), uri);
        UniversalModel universalModel = new UniversalModel();
        universalModel.postFileData(ApiUrl.Post.POST_FILE_URL, file, new ProgressListener() {
                    @Override
                    public void onProgress(long progressLength, long totalLength, boolean done) {
                        new Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("上传进度：%"+progressLength*100/totalLength));
                    }
                }
                , new OnFileResponseListener() {
                    @Override
                    public void onFinish(FileResponseBean fileResponseBean, Exception e) {
                        if (e == null && !TextUtils.isEmpty(fileResponseBean.getFile_path())) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("excelUrl", fileResponseBean.getFile_path());

                            universalModel.postData(ApiUrl.Post.POST_USER_BATCH_IMPORT_URL, map, new OnResponseListener() {
                                @Override
                                public void onFinish(UniversalResponseBean responseBean, Exception e) {
                                    try {
                                        if (e == null && responseBean != null) {
                                            if (responseBean.getCode() == 200) {
                                                new Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("导入成功"));
                                            } else
                                                new Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("导入失败"));
                                        } else
                                            new Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("导入失败"+e.getMessage()));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Logger.d(e.getMessage());
                            new Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("上传失败"));
                        }

                    }
                });
    }

    public static void deleteMember(String id, OnResponseListener onResponseListener) {
        UniversalModel model = new UniversalModel();
        model.delData(ApiUrl.Del.DEL_USER_ADMIN_URL, id, onResponseListener);
    }

    public static void setAdmin(UserVo userVo) {
        userVo.setUserLevel(2);
        JsonObject jsonObject = MinePresenter.User2Object(userVo);
        UniversalModel<UserVo> universalModel = new UniversalModel<>();
        universalModel.putData(ApiUrl.Put.PUT_USER_URL, jsonObject, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                if (e == null) {
                    if (responseBean.getCode() == 200) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show("设置成功");
                        });

                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show(responseBean.getMsg() == null ? "结果为空" : responseBean.getMsg());
                        });
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ToastUtils.show(e.getMessage() == null ? "未知错误" : e.getMessage());
                    });
                }
            }
        });
    }

    public static View initChooseDeptSpinner(Activity activity){
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.spinnerview, null);
        return view;
    }
}
