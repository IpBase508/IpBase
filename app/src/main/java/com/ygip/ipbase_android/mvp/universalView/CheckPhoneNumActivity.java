package com.ygip.ipbase_android.mvp.universalView;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.util.TimeButton;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class CheckPhoneNumActivity extends Activity {

    public static final int CONFIRM_RESULT=1005;

    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.titlebar_ll_left)
    LinearLayout titlebarLlLeft;
    @BindView(R.id.titlebar_iv_right)
    ImageView titlebarIvRight;
    @BindView(R.id.titlebar_ll_right)
    LinearLayout titlebarLlRight;
    @BindView(R.id.titlebar_tv_right)
    TextView titlebarTvRight;
    @BindView(R.id.check_edt_phoneNumber)
    EditText checkEdtPhoneNumber;
    @BindView(R.id.check_btn_getsms)
    TimeButton checkBtnGetsms;
    @BindView(R.id.check_edt_smscode)
    EditText checkEdtSmscode;
    @BindView(R.id.check_btn_confirm)
    Button checkBtnConfirm;


    private boolean isPhoneNumberChecked=false;
    private EventHandler eventHandler;
    private String phone;
    private String detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_num);
        ButterKnife.bind(this);
        initData();
    }

    void initData() {
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
//                Logger.d(event+" "+result+" "+data);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    Logger.d(data);
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        HashMap<String, Object> mData = (HashMap<String, Object>) data;
                        if(phone==mData.get("phone")){
                            isPhoneNumberChecked=true;
                            runOnUiThread(()->{
                                ToastUtils.show("验证成功");
                            });
                        }else{
                            isPhoneNumberChecked=false;
                            runOnUiThread(()->{
                                ToastUtils.show("验证失败");
                            });
                        }
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    detail=((Throwable)data).getMessage().toString();
                    detail= new JsonParser().parse(detail).getAsJsonObject().get("detail").getAsString();
                    runOnUiThread(()->{
                        ToastUtils.show(detail);
                    });
                    ;
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    void vaildate(){
        ToastUtils.show("正在认证");
        SMSSDK.submitVerificationCode("86",phone,checkEdtSmscode.getText().toString());

    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
    }

    @OnClick(R.id.check_edt_phoneNumber)
    public void onCheckEdtPhoneNumberClicked() {
    }

    @OnClick(R.id.check_btn_getsms)
    public void onCheckBtnGetsmsClicked() {
        if (TextUtils.isEmpty(checkEdtPhoneNumber.getText()))
        {
            phone=checkEdtPhoneNumber.getText().toString();
            SMSSDK.getVerificationCode("86",phone);
        }
        else
            ToastUtils.show("手机号不能为空");
    }

    @OnClick(R.id.check_edt_smscode)
    public void onCheckEdtSmscodeClicked() {

    }

    @OnClick(R.id.check_btn_confirm)
    public void onCheckBtnConfirmClicked() {
        if (phone==null){
            if (!TextUtils.isEmpty(checkEdtPhoneNumber.getText()))
            {
                phone=checkEdtPhoneNumber.getText().toString();
            }else
            {
                ToastUtils.show("姿势水平不够高");
                return;
            }
        }
        if (!TextUtils.isEmpty(checkEdtSmscode.getText())) {
            {
                vaildate();
            }
        } else {
            ToastUtils.show("验证码不能为空");
        }
    }
}
