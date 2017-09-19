package com.ygip.ipbase_android.mvp.projects.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.activity.WeChatCaptureActivity;
import com.google.zxing.listener.ResultListener;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.projects.adapter.ProjectAdapter;
import com.ygip.ipbase_android.mvp.projects.model.Project;
import com.ygip.ipbase_android.mvp.projects.presenter.ProjectPresenter;
import com.ygip.ipbase_android.util.MyVibrator;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.XRecyclerViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.mvp.XLazyFragment;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;


/**
 * Created by lockyluo on 2017/7/28.
 */

public class ProjectsFragment extends XLazyFragment<ProjectPresenter> {

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
    @BindView(R.id.xRecyclerContentLayout_projects)
    XRecyclerContentLayout xRecyclerContentLayoutProjects;

    private ProjectAdapter adapter;
    private Project project;
    private List<Project> projects = new ArrayList<>();
    public List<View> footerViewList;
    public static final String tag = "ProjectsFragment";

    public List<View> getFooterViewList() {
        return footerViewList;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarTvTitle.setText("项目");
        titlebarLlRight.setVisibility(View.VISIBLE);
        titlebarTvRight.setVisibility(View.VISIBLE);
        titlebarTvRight.setText("添加");
        projects = getP().loadData();
        initAdapter();
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new ProjectAdapter(context);
        }
        XRecyclerView xRecyclerView = xRecyclerContentLayoutProjects.getRecyclerView();
        XRecyclerViewUtil.setCommonParams(context, xRecyclerContentLayoutProjects);
        //xRecyclerContentLayoutProjects.loadingView(new SimpleLoadMoreFooter(context));
        xRecyclerView.horizontalDivider(R.color.C9, R.dimen.divider_height);
        xRecyclerView.setAdapter(adapter);


        xRecyclerView.useDefLoadMoreView();

        footerViewList = xRecyclerView.getFooterViewList();

        xRecyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getP().refreshData(1, xRecyclerContentLayoutProjects, adapter, projects);
                xRecyclerView.setPage(1, 2);
                //ToastUtils.show(context, "正在刷新");
            }

            @Override
            public void onLoadMore(int page) {
                getP().loadMoreData(page, xRecyclerContentLayoutProjects, adapter, projects);
                ToastUtils.show(context, "正在加载");
            }
        });

        adapter.addData(projects);
        adapter.setRecItemClick(new RecyclerItemCallback<Project, ProjectAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, Project model, int tag, ProjectAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                StartActivityUtil.startWithData(context, ProjectDetailActivity.class, adapter.getDataSource().get(position));
            }

            @Override
            public void onItemLongClick(int position, Project model, int tag, ProjectAdapter.ViewHolder holder) {
                super.onItemLongClick(position, model, tag, holder);
                MyVibrator.vibrate(context);
                ToastUtils.show(context, "long click");
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_projects;
    }

    @Override
    public ProjectPresenter newP() {
        return new ProjectPresenter();
    }

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }


    @OnClick(R.id.titlebar_tv_title)
    public void onTitlebarTvTitleClicked() {
        WeChatCaptureActivity.init(context, new ResultListener() {
            @Override
            public void onResult(String s) {
                ToastUtils.show(tag + ":" + s);
            }
        }, getResources().getColor(R.color.colorPrimary), "二维码扫描");
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
    }

    @OnClick(R.id.titlebar_iv_right)
    public void onTitlebarIvRightClicked() {
        StartActivityUtil.startWithData(context, NewProjectActivity.class);
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
        StartActivityUtil.startWithData(context, NewProjectActivity.class);
    }

    @OnClick(R.id.titlebar_tv_right)
    public void onTitlebarTvRightClicked() {
        StartActivityUtil.startWithData(context, NewProjectActivity.class);
    }
}
