package com.ygip.ipbase_android.mvp.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.login.present.RegisterPresent;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.mvp.universalPresenter.UniversalPresenter;
import com.ygip.ipbase_android.mvp.universalView.CheckPhoneNumActivity;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by XQM on 2017/9/24.
 */

public class RegisterActivity extends XActivity<RegisterPresent> {


    public static final int RESULT_CODE = 1102;
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
    @BindView(R.id.image0)
    ImageView image0;
    @BindView(R.id.edt_register_name)
    EditText edtRegisterName;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.edt_register_pdw)
    EditText edtRegisterPdw;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.edt_register_phone)
    EditText edtRegisterPhone;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.sp_register_dept)
    Spinner spRegisterDept;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.sp_register_grade)
    Spinner spRegisterGrade;

    private String[] departments;
    private String[] grades;


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarTvTitle.setText("注册");
        titlebarLlLeft.setVisibility(View.VISIBLE);

        departments = getResources().getStringArray(R.array.departments);
        grades = getResources().getStringArray(R.array.grade);
        initView();
    }

    private void initView() {
        spRegisterDept.setSelection(spRegisterDept.getCount() - 1);
        spRegisterDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0 && id != spRegisterDept.getCount() - 1) {
                    spRegisterDept.setSelection(spRegisterDept.getCount() - 1);
                    ToastUtils.show("部门暂不开放注册");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        edtRegisterPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UniversalPresenter.showPhoneVaildate(context, "验证手机号", 100);
            }
        });
    }


    @Override
    public RegisterPresent newP() {
        return new RegisterPresent();
    }


    private boolean checkNull(String memName, String memPassword, String memPhone, String memDept, String memGrade) {
        if (TextUtils.isEmpty(memName)) {
            ToastUtils.show("请输入名字");
            return false;
        } else if (TextUtils.isEmpty(memPassword)) {
            ToastUtils.show("请输入密码");
            return false;
        } else if (TextUtils.isEmpty(memPhone)) {
            ToastUtils.show("请输入手机号码");
            return false;
        } else if (TextUtils.isEmpty(memDept)) {
            ToastUtils.show("请选择部门");
            return false;
        } else if (spRegisterGrade.getSelectedItemId() == 0) {
            ToastUtils.show("请选择年级");
            return false;
        }
        return true;
    }

    public void setResultData(UserVo data) {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(RESULT_CODE, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CheckPhoneNumActivity.RESULT_CONFIRMED && data != null) {
            try {
                edtRegisterPhone.setText((String) ((HashMap<String, Object>) data.getSerializableExtra("data")).get("phone"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showFail(String msg) {
        runOnUiThread(() -> {
            ToastUtils.show(TextUtils.isEmpty(msg) ? "未知错误" : msg);
        });
    }

    public void showSuccess(String msg, UserVo user) {
        runOnUiThread(() -> {
            ToastUtils.show(msg);
        });
        setResultData(user);
        finish();
    }


    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        String memDept = null;
        String memName = edtRegisterName.getText().toString().trim();
        String memPassword = edtRegisterPdw.getText().toString().trim();
        String memPhone = edtRegisterPhone.getText().toString().trim();
        String memGrade;
        if (spRegisterDept.getSelectedItemId() != 0) {
            memDept = departments[(int) spRegisterDept.getSelectedItemId()];
        }
        if (spRegisterGrade.getSelectedItemId() != 0) {
            memGrade = grades[(int) spRegisterGrade.getSelectedItemId()];

        } else {
            memGrade = "无";
        }
        if (checkNull(memName, memPassword, memPhone, memDept, memGrade)) {

            getP().register(memName, memPassword, memPhone, memDept, memGrade);
        }
    }


    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
