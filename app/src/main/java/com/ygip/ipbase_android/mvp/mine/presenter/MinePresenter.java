package com.ygip.ipbase_android.mvp.mine.presenter;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.view.MineFragment;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnFileResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.ProgressListener;
import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBeanList;
import com.ygip.ipbase_android.mvp.universalModel.bean.UpdateUserLevel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.BitmapUtils;
import com.ygip.ipbase_android.util.FileUtils;
import com.ygip.ipbase_android.util.ToastUtils;

import java.io.File;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lockyluo on 2017/7/28.
 * UniversalModel使用demo
 */

public class MinePresenter extends XPresent<MineFragment> {
    static UniversalModel universalModel = new UniversalModel();


    public void testLogin() {//测试登录
        LoginUser loginUser = new LoginUser();
        loginUser.setMemberName("张三");//这里set手机号也行
        loginUser.setPassword("123456789");
        universalModel.login(loginUser, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBeanList responseBean, Exception e) {
                if (e == null) {
                    (new Handler(Looper.getMainLooper())).postDelayed(() -> {

                    }, 100);
                }
            }
        });
    }

    UserVo userVo = new UserVo();

    public void postData() {//-----------post

        Logger.t("生成user").d(userVo);
        universalModel.postData(ApiUrl.Post.POST_USER_URL, userVo, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBeanList responseBean, Exception e) {
                Logger.t("postData").d(responseBean);
                getData();

            }
        });
    }

    public void postFileData(Uri uri, CircleImageView iv, ProgressBar progressBar) {//-----------postFile
        File file = FileUtils.Uri2File(getV().getContext(), uri);
        if (file.length()>1024*1024){
            file=BitmapUtils.getSmallerImageFile(getV().getActivity(),uri);
        }
        universalModel.postFileData(ApiUrl.Post.POST_IMAGE_URL, file,
                new ProgressListener() {
                    @Override
                    public void onProgress(long progressLength, long totalLength, boolean done) {
                        Logger.d(progressLength/totalLength);
                        getV().getActivity().runOnUiThread(()->{
                            if(!done){
                                if(progressBar.getVisibility()!= View.VISIBLE){
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                                progressBar.setMax((int)totalLength);
                                progressBar.setProgress((int)(progressLength));
                            }else {
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
                        if (fileResponseBean!=null) {

                            userVo.setImageUrl(fileResponseBean.getFile_path());
                            getV().getActivity().runOnUiThread(()->{
                                progressBar.setVisibility(View.GONE);
                                Glide.with(getV())
                                        .load(fileResponseBean.getFile_path())
                                        .dontAnimate()
                                        .placeholder(R.mipmap.ipbaselogo)
                                        .into(iv);
                            });
                        }

                        userVo = new UserVo();
                        userVo.setMemberName("666");
                        userVo.setPassword("123");
                        userVo.setPhoneNumber("13666666666");
                        userVo.setDepartment("吃瓜群众");

                        postData();

                    }
                });
    }

    public void getData() {//-----------get
        universalModel.getData(ApiUrl.Get.GET_USER_URL, new String[]{"all=true", "condition=吃瓜群众"}, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBeanList responseBean, Exception e) {
                if (e == null) {
                    userVo = (new Gson()).fromJson(responseBean.getData().get(0), UserVo.class);
                    Logger.t("getData").d(responseBean);
                    putData();
                } else {
                    Logger.t("getData").d(e);
                }

            }
        });
    }

    public void putData() {//-----------put
        UpdateUserLevel userLevel = new UpdateUserLevel();//测试put修改数据
        userLevel.setUserLevel(2);
        userLevel.setUserId(userVo.getUserId());
        Logger.d(userLevel);
        universalModel.putData(ApiUrl.Put.PUT_USER_ADMIN_URL, userLevel, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBeanList responseBean, Exception e) {
                Logger.t("putData").d(responseBean);
                if (e == null) {
                    delData();
                }
            }
        });
    }

    public void delData() {//-----------del
        universalModel.delData(ApiUrl.Del.DEL_USER_ADMIN_URL, userVo.getUserId(), new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBeanList responseBean, Exception e) {
                Logger.t("delData").d(responseBean);
                //--------------测试完毕
                getV().getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getV().toast("测试完毕");
                    }
                });
            }
        });
    }

}
