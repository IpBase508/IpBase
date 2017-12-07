package com.ygip.ipbase_android.mvp.projects.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.member.present.MemberPresenter;
import com.ygip.ipbase_android.mvp.projects.adapter.MemberListAdapter;
import com.ygip.ipbase_android.mvp.projects.model.ProjectUpload;
import com.ygip.ipbase_android.mvp.projects.presenter.NewProjectPresenter;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.DateUtils;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.ViewDelegateByLocky;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import me.iwf.photopicker.widget.MultiPickResultView;

/**
 * 新建项目
 */

public class NewProjectActivity extends XActivity<NewProjectPresenter> {

    ViewDelegateByLocky vDelegate;
    @BindView(R.id.newProject_projectName)
    EditText newprojectProjectName;
    @BindView(R.id.newProject_DatePicker)
    DatePicker newProjectDatePicker;
    @BindView(R.id.newProject_scrollview)
    ScrollView newProjectScrollview;
    @BindView(R.id.newproject_sp_project_type)
    Spinner newprojectSpProjectType;
    @BindView(R.id.newproject_ll_project_member)
    LinearLayout newprojectLlProjectMember;
    @BindView(R.id.newproject_ll_add_project_member)
    ImageButton newprojectLlAddProjectMember;
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
    @BindView(R.id.newProject_select_logo)
    MultiPickResultView newProjectSelectLogo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.edt_detail)
    EditText edtDetail;

    public List<String> logoUploaded = new ArrayList<>();
    public static boolean isSaved = false;
    @BindView(R.id.progressBar_all_newproject)
    ProgressBar progressBarAllNewproject;

    private ProjectUpload project;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<String> logoLocal = new ArrayList<>();
    private List<String> userId = new ArrayList<>();
    private List<UserVo> userVos = new ArrayList<>();
    private HashSet<UserVo> usersTemp = new HashSet<>();
    private HashSet<String> userIdsTemp = new HashSet<>();
    private StringBuffer sb;
    private Long finishTime;
    private MemberListAdapter memberListAdapter;
    private ListView dialogListView;
    private AlertDialog dialogChooseMembers;
    private List<TextView> textViews = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_project;
    }

    public HashSet<UserVo> getUsersTemp() {
        return usersTemp;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        initView();
        project = getP().loadEditData();
        if (project != null) {
            try {
                newprojectProjectName.setText(project.getProjectName());
                newprojectSpProjectType.setSelection(project.getProjectType() == null ? 0 : project.getProjectType());
                finishTime = project.getDeadLine();
                edtDetail.setText(project.getDetail() == null ? "" : project.getDetail());
                Logger.d(project.getImageUrl());

                if (project.getImageUrl() != null) {
                    List<String> list = project.getImageUrl();
                    if (list.size() != 0)
                        newProjectSelectLogo.showPics(list);
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        }
        initDialog();
    }

    void initDialog() {
        userVos = MemberPresenter.memberListCache == null ? new ArrayList<>() : MemberPresenter.memberListCache;
        memberListAdapter = new MemberListAdapter(context, userVos);
        dialogListView = new ListView(context);
        dialogListView.setDivider(ContextCompat.getDrawable(context, R.color.colorPrimary));
        dialogListView.setDividerHeight(1);
        dialogListView.setAdapter(memberListAdapter);
    }

    private void showDialogChooseMembers()//选择弹出对话框
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (dialogChooseMembers == null) {
            builder.setTitle("选择成员").setIcon(R.drawable.login_account_icon);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {//确认
                    dialogChooseMembers.dismiss();
                    usersTemp = memberListAdapter.getSelectData();
//                    for (TextView tv : textViews) {
//                    }
                    newprojectLlProjectMember.removeAllViews();
                    textViews.clear();
                    if (usersTemp != null) {
                        for (UserVo user : usersTemp) {//view
                            TextView tv = new TextView(context);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(user.getMemberName());
                            tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                            textViews.add(tv);
                            newprojectLlProjectMember.addView(tv);
                        }
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogChooseMembers.dismiss();
                    memberListAdapter.getSelectData().clear();
                }
            });
            builder.setView(dialogListView);
            dialogChooseMembers = builder.create();
            dialogChooseMembers.setView(dialogListView);
        }
        dialogChooseMembers.show();
        if (userVos != MemberPresenter.memberListCache) {
            userVos = MemberPresenter.memberListCache;
            memberListAdapter.setData(userVos);
        }
    }


    void initView() {
        progressBarAllNewproject.setVisibility(View.GONE);
        titlebarTvTitle.setText("新建项目详情");
        titlebarLlLeft.setVisibility(View.VISIBLE);
        //titlebarLlRight.setVisibility(View.VISIBLE);

        titlebarTvRight.setVisibility(View.VISIBLE);
        titlebarTvRight.setText("保存并上传");

        newProjectSelectLogo.init(context, 8, MultiPickResultView.ACTION_SELECT, logoLocal);//图片选择器
        getRxPermissions()
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granded -> {
                    if (granded) {
                        newProjectSelectLogo.setVisibility(View.VISIBLE);//有sd权限则显示选择器
                    } else {
                        newProjectSelectLogo.setVisibility(View.GONE);
                        ToastUtils.show("sd卡权限被拒绝，无法访问图片");
                        //finish();
                    }
                });

        newprojectSpProjectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //ToastUtils.show(context, position + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        newProjectDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                sb = new StringBuffer();
                sb.append(year);
                sb.append("-");
                sb.append(monthOfYear);
                sb.append("-");
                sb.append(dayOfMonth);
                sb.append(" 00:00:01");
                finishTime = DateUtils.getThisDate(sb.toString()) / 1000;

                Logger.d(sb.toString() + " " + finishTime);
            }
        });
    }

    private boolean checkNull() {
        if (newprojectProjectName.getText() != null
                && newprojectSpProjectType.getSelectedItemPosition() != 0
                && newProjectSelectLogo.getPhotos() != null
                ) {
            try {
                newProjectSelectLogo.getPhotos().get(0);
                return false;
            } catch (Exception e) {
                Logger.e(e.getMessage());
                ToastUtils.show("未选择图片");
            }

        } else {
            ToastUtils.show("信息未填写完整或未选择类型");
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newProjectSelectLogo.onActivityResult(requestCode, resultCode, data);
        logoLocal = newProjectSelectLogo.getPhotos();//获取返回的图片地址
    }


    @Override
    public NewProjectPresenter newP() {
        return new NewProjectPresenter();
    }

    public ViewDelegateByLocky getvDelegate() {
        if (vDelegate == null) {
            vDelegate = ViewDelegateByLocky.create(context);
        }
        return vDelegate;
    }

    /**
     * build本地project对象
     *
     * @return
     */
    private ProjectUpload buildData() {//-------------------------------------
        if (checkNull()) {
            return null;
        }
        project = new ProjectUpload();
        project.setProjectName(newprojectProjectName.getText().toString());
        userId = new ArrayList<>();

//        userId.add(UniversalModel.getUser().getUserId());//上传者自己
        userIdsTemp = new HashSet<>();
        for (UserVo user : usersTemp) {
            userIdsTemp.add(user.getUserId());
        }
        userId.addAll(userIdsTemp);
        project.setUserIds(userId);

        try {
            logoLocal = newProjectSelectLogo.getPhotos();
            project.setImageUrl(logoLocal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        project.setProjectType(newprojectSpProjectType.getSelectedItemPosition() - 1);
        project.setDeadLine(finishTime);//毫秒转成秒
        String detailStr = edtDetail.getText().toString().trim();
        project.setDetail(TextUtils.isEmpty(detailStr) ? "无介绍" : detailStr);
        return project;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!isSaved) {
                DialogUtils.dialogFinish(context);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.titlebar_tv_title)
    public void onTitlebarTvTitleClicked() {

    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        if (!isSaved) {
            DialogUtils.dialogFinish(context);
        } else {
            finish();
        }

    }

    @OnClick(R.id.titlebar_iv_right)
    public void onTitlebarIvRightClicked() {
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
    }

    @OnClick(R.id.titlebar_tv_right)
    public void onTitlebarTvRightClicked() {
        project = buildData();
        if (project != null) {
            getP().uploadProject(project);
            isSaved = true;
        }
        progressBarAllNewproject.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.newproject_ll_add_project_member)
    public void onNewprojectLlAddProjectMemberClicked() {
        showDialogChooseMembers();
    }

    public void setProgress(long progress, long max) {

        runOnUiThread(() -> {
            progressBar.setMax((int) max);
            progressBar.setProgress((int) progress);
        });
    }

    public void toast(String s,Boolean succeed) {
        runOnUiThread(() -> {
            ToastUtils.show(TextUtils.isEmpty(s) ? "" : s);
            progressBarAllNewproject.setVisibility(View.GONE);
        });
        if (succeed)
            finish();
    }


    @Override
    protected void onDestroy() {
        getP().onDestory();
        super.onDestroy();
        isSaved = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
