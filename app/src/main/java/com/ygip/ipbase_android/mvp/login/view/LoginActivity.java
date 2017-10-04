package com.ygip.ipbase_android.mvp.login.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.login.present.ICommon;
import com.ygip.ipbase_android.mvp.login.present.LoginPresent;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.util.GlideCircleTransform;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by XQM on 2017/7/22.
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
    int loginTypeNum;

    @Override
    public void initData(Bundle savedInstanceState) {
        showInitData();
        radioButtonPhone.setChecked(true);
    }

    private void showInitData() {
        Logger.i(getLocalClassName());
        Glide.with(this)
                .load(R.drawable.jidilogo)
                .transform(new GlideCircleTransform(this))
                .animate(R.anim.login_anim)
                .into(imgLogin);

        loginType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_phone:{
                        edtAccount.setHint("手机号");
                        loginTypeNum=0;
                    }
                        break;
                    case R.id.radio_button_name:{
                        edtAccount.setHint("姓名");
                        loginTypeNum=1;
                    }
                        break;
                }
            }
        });

    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @Override
    public Activity getActivity() {
        return context;
    }

    @Override
    public void startActivity(Class clazz) {
        StartActivityUtil.start(context,clazz);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
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
        LoginUser user = new LoginUser();
        String account=edtAccount.getText().toString();
        String password=edtPassword.getText().toString();
        if(account.equals("")||password.equals("")){
            ToastUtils.show("登录信息不完整");
            return;
        }

        if(loginTypeNum==0){
            user.setPhoneNumber(account);
            user.setPassword(password);
            getP().login(context, user);
        }else if (loginTypeNum==1){
            user.setMemberName(account);
            user.setPassword(password);
            getP().login(context, user);
        }

    }

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        ToastUtils.show("请与基地成员联系获取账号");
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
}
