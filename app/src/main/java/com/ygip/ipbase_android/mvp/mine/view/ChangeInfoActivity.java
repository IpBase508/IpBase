package com.ygip.ipbase_android.mvp.mine.view;

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

import com.google.gson.JsonObject;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MineCommon;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.universalModel.AKey;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.mvp.universalView.CheckPhoneNumActivity;
import com.ygip.ipbase_android.mvp.universalView.ViewAnimation;
import com.ygip.ipbase_android.util.AES;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public class ChangeInfoActivity extends XActivity<MinePresenter> implements MineCommon {


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
    @BindView(R.id.changeInfo_tv_hint)
    TextView changeInfoTvHint;
    @BindView(R.id.changeInfo_btn_byPhone)
    Button changeInfoBtnByPhone;
    @BindView(R.id.changeInfo_btn_byPassword)
    Button changeInfoBtnByPassword;
    @BindView(R.id.changeInfo_ll_btns)
    LinearLayout changeInfoLlBtns;
    @BindView(R.id.changeInfo_ll_idConfirm)
    LinearLayout changeInfoLlIdConfirm;
    @BindView(R.id.changeInfo_ll_editAera)
    LinearLayout changeInfoLlEditAera;
    @BindView(R.id.edt_pwd_changeInfo)
    EditText edtPwdChangeInfo;
    @BindView(R.id.changeInfo_btn_byPassword_done)
    Button changeInfoBtnByPasswordDone;
    @BindView(R.id.ll_checkByPwd_changeInfo)
    LinearLayout llCheckByPwdChangeInfo;
    @BindView(R.id.tv_changeInfo_title)
    TextView tvChangeInfoTitle;
    @BindView(R.id.edt_newinfo_changeinfo)
    EditText edtNewinfoChangeinfo;
    @BindView(R.id.changeInfo_btn_done)
    Button changeInfoBtnDone;

    private String phoneResult;
    private static boolean isConfirmed;
    public static String title;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();

    }

    void initView() {
        llCheckByPwdChangeInfo.setVisibility(View.GONE);
        titlebarTvTitle.setText(title==null?"修改信息":"修改"+title);
        titlebarLlLeft.setVisibility(View.VISIBLE);
        setConfirm(isConfirmed);
    }

    void doChange(){
        if (title!=null) {
            UserVo userVo=new UserVo();
            userVo.setUserId(UniversalModel.getUser().getUserId());
            switch (title){
                case "姓名":{
                    userVo.setMemberName(edtNewinfoChangeinfo.getText().toString().trim());
                    break;
                }
                case "年级":{
                    userVo.setGrade(edtNewinfoChangeinfo.getText().toString().trim());
                    break;
                }
                case "手机号":{
                    userVo.setPhoneNumber(edtNewinfoChangeinfo.getText().toString().trim());
                    break;
                }
            }
            getP().putUserData(userVo);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_info;
    }

    public void showAnimation() {
        (new Handler()).postDelayed(() -> {
            if (isConfirmed) {
                changeInfoLlBtns.setVisibility(View.GONE);
            } else {
                changeInfoLlBtns.setVisibility(View.VISIBLE);
                changeInfoLlBtns.startAnimation(ViewAnimation.showAction());
            }
        }, 20);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == CheckPhoneNumActivity.RESULT_CONFIRMED && data != null) {
            try {
                phoneResult = (String) ((HashMap<String, Object>) data.getSerializableExtra("data")).get("phone");
                if (phoneResult.equals(UniversalModel.getUser().getPhoneNumber())) {
                    setConfirm(true);
                } else {
                    setConfirm(false);
                }
            } catch (Exception e) {
                setConfirm(false);
                e.printStackTrace();
            }

        } else {
            setConfirm(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setConfirm(boolean bo) {
        String strComfirmed = "已验证";
        String strUnComfirmed = "未验证";
        if (bo) {
            isConfirmed = true;
            runOnUiThread(() -> {
                ToastUtils.show(strComfirmed);
                changeInfoTvHint.setText(strComfirmed);
                changeInfoTvHint.setTextColor(getResources().getColor(R.color.holo_blue_dark));
            });
        } else {
            isConfirmed = false;
            runOnUiThread(() -> {
                ToastUtils.show(strUnComfirmed);
                changeInfoTvHint.setText(strUnComfirmed);
                changeInfoTvHint.setTextColor(getResources().getColor(R.color.red));
            });
        }
        showAnimation();
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


    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        finish();
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
    }

    @OnClick(R.id.changeInfo_btn_byPhone)
    public void onChangeInfoBtnByPhoneClicked() {
        getP().showPhoneVaildate(context, null, UniversalModel.getUser().getPhoneNumber());
    }


//    @OnClick(R.id.changeInfo_tv_hint)
//    public void onViewClicked() {
//        if (isConfirmed) {
//            isConfirmed = false;
//        } else {
//            isConfirmed = true;
//        }
//        showAnimation();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.changeInfo_btn_byPassword)
    public void onChangeInfoBtnByPasswordClicked() {
        llCheckByPwdChangeInfo.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.changeInfo_btn_byPassword_done)
    public void onChangeInfoBtnByPasswordDoneClicked() {
        String pwd=edtPwdChangeInfo.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)){
            ToastUtils.show("密码为空");
            return;
        }
        try {
            pwd= AES.Encrypt(pwd, AKey.s);
        } catch (Exception e) {
            e.printStackTrace();
            pwd=null;
        }
        if (pwd!=null){
            if (pwd.equals(UniversalModel.getLocalUser().getPassword())){
                setConfirm(true);
            }else {
                setConfirm(false);
                ToastUtils.show("密码错误");
            }
        }
    }

    @OnClick(R.id.changeInfo_btn_done)
    public void onChangeInfoBtnDoneClicked() {
        if (TextUtils.isEmpty(edtNewinfoChangeinfo.getText())){
            ToastUtils.show("新信息不能为空");
            return;
        }
        if (isConfirmed){
            doChange();
        }else {
            ToastUtils.show("请先验证身份");
        }
    }
}
