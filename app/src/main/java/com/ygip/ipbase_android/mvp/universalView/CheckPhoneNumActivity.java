package com.ygip.ipbase_android.mvp.universalView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonParser;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.util.TimeButton;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 手机短信验证
 */

public class CheckPhoneNumActivity extends Activity {

    public static final int RESULT_CONFIRMED = 1005;

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
    @BindView(R.id.check_view_isChecked)
    LinearLayout checkViewIsChecked;
    @BindView(R.id.check_view_uncheck)
    LinearLayout checkViewUncheck;
    @BindView(R.id.check_btn_ok)
    Button checkBtnOk;


    private boolean isPhoneNumberChecked = false;
    private EventHandler eventHandler;

    private static String title;
    private static String phone;
    private String detail;
    private HashMap<String, Object> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_num);
        ButterKnife.bind(this);
        initData();
        initView();

//        (new Handler()).postDelayed(()->fake(),100);

    }

    private CheckPhoneNumActivity getInstance() {
        return this;
    }

    public static void setTitle(String title) {
        CheckPhoneNumActivity.title = title;
    }

    public static void setPhoneNumber(String phone) {
        CheckPhoneNumActivity.phone = phone;
    }

    void fake() {
        isPhoneNumberChecked = true;
        mData = new HashMap<>();
        mData.put("phone", phone);
        setResultData(getInstance().mData);
        updateResultView();
    }

    public void initView() {
        titlebarLlLeft.setVisibility(View.VISIBLE);

        if (title != null) {
            titlebarTvTitle.setText(title);
        } else {
            titlebarTvTitle.setText("手机号验证");
        }
        if (phone != null) {
            checkEdtPhoneNumber.setText(phone);
            checkEdtPhoneNumber.setEnabled(false);
        }else {
            checkEdtPhoneNumber.setEnabled(true);
        }

    }

    void initData() {
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    getInstance().mData = null;
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功

                        HashMap<String, Object> mData = (HashMap<String, Object>) data;
                        if (phone == mData.get("phone")) {
                            isPhoneNumberChecked = true;
                            getInstance().mData = mData;
                            setResultData(getInstance().mData);
                            updateResultView();
                        } else {
                            isPhoneNumberChecked = false;
                            runOnUiThread(() -> {
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
                    detail = ((Throwable) data).getMessage().toString();
                    detail = new JsonParser().parse(detail).getAsJsonObject().get("detail").getAsString();
                    runOnUiThread(() -> {
                        ToastUtils.show(detail);
                    });
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    void vaildate() {
        ToastUtils.show("正在认证");
        SMSSDK.submitVerificationCode("86", phone, checkEdtSmscode.getText().toString());
    }

    public void updateResultView() {
        runOnUiThread(() -> {

            checkViewIsChecked.setAnimation(ViewAnimation.showAction());
            checkViewUncheck.setVisibility(View.GONE);
            checkViewIsChecked.setVisibility(View.VISIBLE);
        });
    }

    public void setResultData(HashMap<String, Object> data) {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(RESULT_CONFIRMED, intent);
    }


    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        phone = null;
        title = null;
        checkBtnGetsms.onDestory();
        super.onDestroy();
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        finish();
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
    }

    @OnClick(R.id.check_edt_phoneNumber)
    public void onCheckEdtPhoneNumberClicked() {
    }

    @OnClick(R.id.check_btn_getsms)
    public void onCheckBtnGetsmsClicked() {
        if (!TextUtils.isEmpty(checkEdtPhoneNumber.getText())) {
            phone = checkEdtPhoneNumber.getText().toString();
            SMSSDK.getVerificationCode("86", phone);
        } else {
            ToastUtils.show("手机号不能为空");
            checkBtnGetsms.setTime(-1);
        }
    }

    @OnClick(R.id.check_edt_smscode)
    public void onCheckEdtSmscodeClicked() {

    }

    @OnClick(R.id.check_btn_confirm)
    public void onCheckBtnConfirmClicked() {
        if (phone == null) {
            if (!TextUtils.isEmpty(checkEdtPhoneNumber.getText())) {
                phone = checkEdtPhoneNumber.getText().toString();
            } else {
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

    @OnClick(R.id.check_btn_ok)
    public void onCheckBtnOkClicked() {
        finish();
    }

}
