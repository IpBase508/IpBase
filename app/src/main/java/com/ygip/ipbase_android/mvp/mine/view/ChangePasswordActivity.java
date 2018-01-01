package com.ygip.ipbase_android.mvp.mine.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.mine.presenter.MineCommon;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.TimeButton;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.smssdk.SMSSDK;

/**
 * 修改密码界面
 */

public class ChangePasswordActivity extends XActivity<MinePresenter> implements MineCommon {

    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.titlebar_iv_left)
    ImageView titlebarIvLeft;
    @BindView(R.id.titlebar_ll_left)
    LinearLayout titlebarLlLeft;
    @BindView(R.id.titlebar_iv_right)
    ImageView titlebarIvRight;
    @BindView(R.id.titlebar_ll_right)
    LinearLayout titlebarLlRight;
    @BindView(R.id.titlebar_tv_right)
    TextView titlebarTvRight;
    @BindView(R.id.change_password_edt_phoneNumber)
    EditText changePasswordEdtPhoneNumber;
    @BindView(R.id.change_password_btn_getsms)
    TimeButton changePasswordBtnGetsms;
    @BindView(R.id.change_password_edt_smscode)
    EditText changePasswordEdtSmscode;
    @BindView(R.id.change_password_edt_newpass_1)
    EditText changePasswordEdtNewpass1;
    @BindView(R.id.change_password_edt_newpass_2)
    EditText changePasswordEdtNewpass2;
    @BindView(R.id.change_password_btn_confirm)
    Button changePasswordBtnConfirm;
    @BindView(R.id.change_password_view_unchange_password)
    LinearLayout changePasswordViewUnchangePassword;
    @BindView(R.id.change_password_btn_ok)
    Button changePasswordBtnOk;
    @BindView(R.id.change_password_view_ischange_passworded)
    LinearLayout changePasswordViewIschangePassworded;


    private static String title;
    private static String phone;
    @BindView(R.id.progressBar_changepwd)
    ProgressBar progressBarChangepwd;
    private String detail;
    private HashMap<String, Object> mData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    public static void setTitle(String title) {
        ChangePasswordActivity.title = title;
    }

    public static void setPhoneNumber(String phone) {
        ChangePasswordActivity.phone = phone;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarLlLeft.setVisibility(View.VISIBLE);

        if (title != null) {
            titlebarTvTitle.setText(title);
        } else {
            titlebarTvTitle.setText("手机号验证");
        }
        if (phone != null) {
            changePasswordEdtPhoneNumber.setText(phone);
            changePasswordEdtPhoneNumber.setEnabled(false);
        } else {
            changePasswordEdtPhoneNumber.setEnabled(true);
        }
        progressBarChangepwd.setVisibility(View.GONE);
    }


    @Override
    public Activity getActivity() {
        return context;
    }

    @Override
    public void startActivity(Class clazz) {
        StartActivityUtil.start(context, clazz);
    }

    @Override
    public MinePresenter newP() {
        return new MinePresenter();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick(R.id.change_password_btn_confirm)
    public void onChangePasswordBtnConfirmClicked() {
        String phone = changePasswordEdtPhoneNumber.getText().toString().trim();
        String smsCode = changePasswordEdtSmscode.getText().toString().trim();
        String pwd1 = changePasswordEdtNewpass1.getText().toString().trim();
        String pwd2 = changePasswordEdtNewpass2.getText().toString().trim();

        if (checkFinish(phone, smsCode, pwd1, pwd2)) {
            progressBarChangepwd.setVisibility(View.VISIBLE);
            getP().changePassword(phone, smsCode, pwd1, new OnLoadListener<Boolean>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish(Boolean data, Exception e) {
                    new Handler(getMainLooper()).post(() -> {
                        progressBarChangepwd.setVisibility(View.GONE);
                        if (data) {
                            finish();
                            if (UniversalModel.getToken() != null) {
                                MainActivity.showLogOut("密码修改成功，重新登录？");
                            } else {
                                ToastUtils.show("修改成功");
                            }
                        } else {
                            ToastUtils.show(e.getMessage());
                        }
                    });
                }
            });
        }
    }

    private boolean checkFinish(String phone, String smsCode, String pwd1, String pwd2) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show("手机号为空");
            return false;
        }
        if (TextUtils.isEmpty(smsCode)) {
            ToastUtils.show("验证码为空");
            return false;
        }
        if (TextUtils.isEmpty(pwd1)) {
            ToastUtils.show("新密码为空");
            return false;
        }
        if (!pwd1.equals(pwd2)) {
            ToastUtils.show("新密码不相同");
            return false;
        }
        return true;
    }

    @OnClick(R.id.change_password_btn_getsms)
    public void onChangePasswordBtnGetsmsClicked() {
        if (!TextUtils.isEmpty(changePasswordEdtPhoneNumber.getText())) {
            phone = changePasswordEdtPhoneNumber.getText().toString();
            SMSSDK.getVerificationCode("86", phone);
        } else {
            ToastUtils.show("手机号不能为空");
            changePasswordBtnGetsms.setTime(-1);
        }
    }

    @Override
    protected void onDestroy() {
        phone = null;
        title = null;
        changePasswordBtnGetsms.onDestory();
        if (getP() != null) {
            getP().onDestory();
        }
        super.onDestroy();
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        finish();
    }

}
