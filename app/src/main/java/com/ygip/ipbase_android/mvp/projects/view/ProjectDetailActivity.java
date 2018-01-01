package com.ygip.ipbase_android.mvp.projects.view;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.projects.presenter.ProjectDetailPresenter;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectImageVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectTeamVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;
import com.ygip.ipbase_android.mvp.universalView.WebActivity;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import me.iwf.photopicker.widget.MultiPickResultView;

/**
 * 项目详情
 */

public class ProjectDetailActivity extends XActivity<ProjectDetailPresenter> {
    @BindView(R.id.iv_project_detail_headimg)
    ImageView ivProjectDetailHeadimg;
    @BindView(R.id.multiview)
    MultiPickResultView multiview;
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
    @BindView(R.id.btn_content)
    Button btnContent;
    @BindView(R.id.ll_members_projectdetail)
    LinearLayout llMembersProjectdetail;


    public ProjectVo getProject() {
        return project;
    }

    private List<String> imgs = new ArrayList<>();
    private ProjectVo project;

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarLlLeft.setVisibility(View.VISIBLE);
        titlebarLlRight.setVisibility(View.VISIBLE);

        project = (ProjectVo) getIntent().getSerializableExtra("data");
        imgs = new ArrayList<>();

        if (Kits.Empty.check(project)) {
            titlebarTvTitle.setText("项目详情");
        } else {
            Logger.d(project);
            titlebarTvTitle.setText(project.getProjectName() + "项目");
//            getP().loadData();
            try {

                UniversalModel.loadImage(context, project.getProjectImageVo() != null ?
                        project.getProjectImageVo().get(0).getImage() :
                        R.mipmap.ipbaselogo, ivProjectDetailHeadimg);
                for (ProjectImageVo imgsvo : project.getProjectImageVo()
                        ) {
                    imgs.add(imgsvo.getImage());
                }
                multiview.init(this, imgs.size(), MultiPickResultView.ACTION_ONLY_SHOW, (ArrayList<String>) imgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<ProjectTeamVo> members = project.getProjectTeamVoList();
                for (ProjectTeamVo member : members) {
                    if (member.getUserVo() != null) {
                        TextView tv = new TextView(context);
                        tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        tv.setPadding(5, 5, 5, 5);
                        tv.setTextSize(16);
                        tv.setTextIsSelectable(true);
                        tv.setText(member.getUserVo().getMemberName());
                        llMembersProjectdetail.addView(tv);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    public void toast(String str) {
        runOnUiThread(() -> ToastUtils.show(str));
    }


    @Override
    public ProjectDetailPresenter newP() {
        return new ProjectDetailPresenter();
    }


    @OnClick(R.id.titlebar_tv_title)
    public void onTitlebarTvTitleClicked() {
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        finish();
    }

    @OnClick(R.id.titlebar_iv_right)
    public void onTitlebarIvRightClicked() {
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
    }

    @OnClick(R.id.titlebar_tv_right)
    public void onTitlebarTvRightClicked() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_content)
    public void onBtnContentClicked() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", project.getDetail());

        StartActivityUtil.startWithData(context, WebActivity.class, map);
    }
}
