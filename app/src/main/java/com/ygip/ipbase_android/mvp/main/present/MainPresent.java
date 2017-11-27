package com.ygip.ipbase_android.mvp.main.present;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.Mac508;
import com.ygip.ipbase_android.util.NetworkAddressUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.listener.OnActionListener;

import java.util.HashMap;
import java.util.Locale;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by XQM on 2017/7/13.
 */

public class MainPresent extends XPresent<MainActivity> {
    public String wlanMac;
    public String getIp(){
        return NetworkAddressUtils.getIpAddress(getV());
    }
    private UniversalModel universalModel;
    private HashMap<String,String> attendMap=new HashMap<>();
    private static Boolean isAttendanceChecked=false;

    public void getAttendanceChecked(OnLoadListener<Boolean> onLoadListener) {
        if (isAttendanceChecked) {
            onLoadListener.onFinish(true,null);
        } else {
            universalModel=new UniversalModel<>();
        }
    }


    public void attendanceCheck(OnLoadListener<Boolean> onLoadListener){
        String ip=getIp();
        if (checkLocation(getV())&&ip!=null){
            universalModel=new UniversalModel<>();
            attendMap.clear();
            attendMap.put("checkIp",ip);
            attendMap.put("attendId",UniversalModel.getUser().getUserId());
            Logger.d(attendMap);
            universalModel.postData(ApiUrl.Post.POST_ATTENDANCE_CHECK, attendMap, new OnResponseListener() {
                @Override
                public void onFinish(UniversalResponseBean responseBean, Exception e) {
                    if (e==null)
                    {
                        try {
                            if (responseBean.getSuccess()&&responseBean.getData()!=null){
                                isAttendanceChecked=true;
                                onLoadListener.onFinish(true,null);
                            }else {
                                onLoadListener.onFinish(false,new Exception(responseBean.getMsg()));
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            isAttendanceChecked=false;
                            onLoadListener.onFinish(false,e1);
                        }
                    }else {
                        isAttendanceChecked=false;
                        onLoadListener.onFinish(false,e);
                    }
                }
            });
        }
    }

    public Boolean checkLocation(Context context){
        String[] mac508= Mac508.mac;
        wlanMac= NetworkAddressUtils.getMacM().get("wlan0");
        if (TextUtils.isEmpty(wlanMac))
        {
            Logger.e("wlanmac is null");
            return false;
        }

        for (String mac:mac508) {
            if (wlanMac.toLowerCase(Locale.getDefault()).equals(mac)&&isWifi(context)){
                return true;
            }
        }
        Logger.e("wlanmac is not ipbase");
        return false;
    }

    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
