package com.ygip.ipbase_android.mvp.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.login.present.ICommon;
import com.ygip.ipbase_android.mvp.login.present.LoginPresent;
import com.ygip.ipbase_android.mvp.mine.view.ChangePasswordActivity;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.GlideCircleTransform;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by XQM on 2017/7/22.
 * 登录
 */

public class LoginActivity extends XActivity<LoginPresent> implements ICommon {
    @BindView(R.id.img_login)
    ImageView imgLogin;
    @BindView(R.id.edt_account)
    EditText edtAccount;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_account_bg)
    FloatLabeledEditText edtAccountBg;
    @BindView(R.id.edt_password_bg)
    FloatLabeledEditText edtPasswordBg;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.radio_button_phone)
    RadioButton radioButtonPhone;
    @BindView(R.id.radio_button_name)
    RadioButton radioButtonName;
    @BindView(R.id.login_type)
    RadioGroup loginType;

    public static final int REQUEST_CODE = 1101;
    int loginTypeNum;
    String head;
    @BindView(R.id.progressBar_login)
    ProgressBar progressBarLogin;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        showInitData();
        initView();
        radioButtonPhone.setChecked(true);
    }

    void initView() {
        progressBarLogin.setVisibility(View.GONE);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    doLogin();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setStatusBarColor() {
//        super.setStatusBarColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void showInitData() {
        try {
            head = UniversalModel.getLocalUser().getImageUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(head)) {
            head = null;
        }
        Logger.i(getLocalClassName());
        Glide.with(this)
                .load(head != null ? head : R.mipmap.ipbaselogo)
                .transform(new GlideCircleTransform(this))
                .animate(R.anim.login_anim)
                .into(imgLogin);

        loginType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_phone: {
                        edtAccountBg.setHint("手机号");
                        edtAccount.setInputType(InputType.TYPE_CLASS_PHONE);
                        loginTypeNum = 0;
                    }
                    break;
                    case R.id.radio_button_name: {
                        edtAccountBg.setHint("姓名");
                        edtAccount.setInputType(InputType.TYPE_CLASS_TEXT);
                        loginTypeNum = 1;
                    }
                    break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.REQUEST_CODE && resultCode == RegisterActivity.RESULT_CODE) {
            if (data != null) {
                radioButtonPhone.setChecked(true);
                UserVo user;
                try {
                    user = (UserVo) data.getSerializableExtra("data");
                    edtAccount.setText(user.getPhoneNumber());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
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
    public LoginPresent newP() {
        return new LoginPresent();
    }


    @OnClick(R.id.edt_account_bg)
    public void onEdtAccountBgClicked() {
        edtAccount.requestFocus();
    }

    @OnClick(R.id.edt_password_bg)
    public void onEdtPasswordBgClicked() {
        edtPassword.requestFocus();
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {
        doLogin();

    }

    void doLogin() {
        LoginUser user = new LoginUser();
        String account = edtAccount.getText().toString();
        String password = edtPassword.getText().toString();
        if (account.equals("") || password.equals("")) {
            ToastUtils.show("登录信息不完整");
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);

        if (loginTypeNum == 0) {
            user.setPhoneNumber(account);
            user.setPassword(password);
            getP().login(context, user);
        } else if (loginTypeNum == 1) {
            user.setMemberName(account);
            user.setPassword(password);
            getP().login(context, user);
        }
    }

    public void show(String s) {
        new Handler(Looper.getMainLooper()).post(() -> {
            ToastUtils.show(s);
            progressBarLogin.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        if (getP() != null) {
            getP().onDestory();
        }
        super.onDestroy();
    }

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        StartActivityUtil.startForResult(context, RegisterActivity.class, REQUEST_CODE);
    }


    @OnClick(R.id.radio_button_phone)
    public void onRadioButtonPhoneClicked() {
    }

    @OnClick(R.id.radio_button_name)
    public void onRadioButtonNameClicked() {
    }

    @OnClick(R.id.login_type)
    public void onLoginTypeClicked() {
    }


    @OnClick(R.id.tv_forget_password)
    public void onTvForgetPasswordClicked() {
        StartActivityUtil.start(context, ChangePasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setStatusBarColor();
    }


}
