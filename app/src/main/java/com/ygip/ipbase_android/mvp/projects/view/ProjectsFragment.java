package com.ygip.ipbase_android.mvp.projects.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.activity.WeChatCaptureActivity;
import com.google.zxing.listener.ResultListener;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.projects.adapter.ProjectAdapter;
import com.ygip.ipbase_android.mvp.projects.presenter.FindProjectAsync;
import com.ygip.ipbase_android.mvp.projects.presenter.ProjectPresenter;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;
import com.ygip.ipbase_android.mvp.universalView.ViewAnimation;
import com.ygip.ipbase_android.util.MyVibrator;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.StringUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.XRecyclerViewUtil;
import com.ygip.ipbase_android.util.listener.OnActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import scut.carson_ho.searchview.BackCallBack;
import scut.carson_ho.searchview.SearchCallBack;
import scut.carson_ho.searchview.SearchView;


/**
 * Created by lockyluo on 2017/7/28.
 * 项目列表
 */

public class ProjectsFragment extends XFragment<ProjectPresenter> {

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
    XRecyclerView xRecyclerView;
    @BindView(R.id.searchview_project)
    SearchView searchviewProject;
    Unbinder unbinder;
    @BindView(R.id.titlebar_iv_left)
    ImageView titlebarIvLeft;


    private ProjectAdapter adapter;
    private ProjectVo project;
    private List<ProjectVo> projects = new ArrayList<>();
    public List<View> footerViewList;
    public static final String tag = "ProjectsFragment";
    public static Boolean requireRefresh = false;
    private FindProjectAsync findProjectAsync;
    private OnLoadListener<List<ProjectVo>> onLoadListener;


    public List<View> getFooterViewList() {
        return footerViewList;
    }

    public List<ProjectVo> getProjects() {
        return projects;
    }

    public static void setRequireRefresh(Boolean requireRefresh) {
        ProjectsFragment.requireRefresh = requireRefresh;
    }

    public void setProjects(List<ProjectVo> projects) {
        this.projects = projects;
        Logger.d(projects);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.setData(projects);
                if (xRecyclerContentLayoutProjects.getSwipeRefreshLayout().isRefreshing()) {
                    xRecyclerContentLayoutProjects.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_projects;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (requireRefresh) {
            setUserVisibleHint(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//---懒加载
        super.setUserVisibleHint(isVisibleToUser);

        if (searchviewProject != null) {
            if (isVisibleToUser && searchviewProject.getVisibility() != View.GONE) {
                setSearchview(View.VISIBLE);
            }
        }
        (new Handler()).postDelayed(() -> {

            if (isVisibleToUser) {
                if (projects == null) {
                    Logger.d("load projects is null");
                    return;
                }
                if (projects.size() == 0 || requireRefresh) {

                    Logger.d("load projects");
                    getP().loadData();
                    requireRefresh = false;
                }
            }
        }, 10);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarTvTitle.setText("项目");

        projects = new ArrayList<>();
        initAdapter();
        initView();
    }

    private void setSearchview(int vi) {
        searchviewProject.setVisibility(vi);
        if (vi == View.VISIBLE) {
            MainActivity.setSearchView(searchviewProject, true);
            searchviewProject.startAnimation(ViewAnimation.showAction());
        } else {
            MainActivity.setSearchView(null, false);
        }
    }

    private void initView() {
        titlebarLlRight.setVisibility(View.VISIBLE);
        if (UniversalModel.getUser().getUserLevel() >= 2) {
            titlebarIvRight.setVisibility(View.VISIBLE);
            titlebarIvRight.setImageResource(R.drawable.add);
        } else {
            titlebarIvRight.setVisibility(View.GONE);
        }
        titlebarIvLeft.setImageResource(R.drawable.search);
        titlebarLlLeft.setVisibility(View.VISIBLE);
        setSearchview(View.GONE);

        onLoadListener = new OnLoadListener<List<ProjectVo>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(List<ProjectVo> data, Exception e) {
                setProjects(data);
            }
        };
        searchviewProject.setOnBackClick(new BackCallBack() {
            @Override
            public void BackAciton() {
                setSearchview(View.GONE);
            }
        });
        searchviewProject.setOnSearchClick(new SearchCallBack() {
            @Override
            public void SearchAciton(String result) {
                if (findProjectAsync != null) {
                    findProjectAsync.cancel(true);
                }
                findProjectAsync = new FindProjectAsync();
                if (StringUtils.isNumeric(result)) {
                    findProjectAsync.initTask(context, FindProjectAsync.FIND_BY_NUMBER, result);

                } else {
                    findProjectAsync.initTask(context, FindProjectAsync.FIND_BY_STRING, result);

                }
                findProjectAsync.execute(onLoadListener);
                searchviewProject.et_search.setText("");
                setSearchview(View.GONE);
            }
        });
    }


    private void initAdapter() {
        OnActionListener onActionListener = new OnActionListener() {
            @Override
            public void done() {
                setUserVisibleHint(true);
            }

        };
        if (adapter == null) {
            adapter = new ProjectAdapter(context, onActionListener);
        }
        xRecyclerView = xRecyclerContentLayoutProjects.getRecyclerView();
        XRecyclerViewUtil.setCommonParams(context, xRecyclerContentLayoutProjects);
        //xRecyclerContentLayoutProjects.loadingView(new SimpleLoadMoreFooter(context));
        xRecyclerView.horizontalDivider(R.color.C9, R.dimen.divider_height);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());

        xRecyclerView.useDefLoadMoreView();

        footerViewList = xRecyclerView.getFooterViewList();

        xRecyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getP().loadData();
//                xRecyclerView.setPage(1, 2);
                //ToastUtils.show(context, "正在刷新");
            }

            @Override
            public void onLoadMore(int page) {
                getP().loadData();
            }
        });

        adapter.setData(projects.size() == 0 ? null : projects);
        adapter.setRecItemClick(new RecyclerItemCallback<ProjectVo, ProjectAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, ProjectVo model, int tag, ProjectAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                StartActivityUtil.startWithData(context, ProjectDetailActivity.class, adapter.getDataSource().get(position));
            }

            @Override
            public void onItemLongClick(int position, ProjectVo model, int tag, ProjectAdapter.ViewHolder holder) {
                super.onItemLongClick(position, model, tag, holder);
//                MyVibrator.vibrate(context);
                ToastUtils.show(context, "long click");
            }
        });

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
        setSearchview(View.VISIBLE);
        searchviewProject.setBackgroundColor(ContextCompat.getColor(context, R.color.graye6e));
        searchviewProject.et_search.setHint("输入项目名查找");
    }


    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
        try {
            if (UniversalModel.getUser().getUserLevel() >= 2) {
                StartActivityUtil.start(context, NewProjectActivity.class);
            } else {
                ToastUtils.show("仅管理员可发布项目");
            }
        } catch (Exception e) {
            ToastUtils.show("获取权限失败");
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        getP().onDestory();
        super.onDestroyView();
        unbinder.unbind();
    }


}
