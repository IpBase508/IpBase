package com.ygip.ipbase_android.mvp.dynamic.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.dynamic.adapter.DynamicAdapter;
import com.ygip.ipbase_android.mvp.dynamic.presenter.DynamicPresenter;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.member.present.FindMemberAsync;
import com.ygip.ipbase_android.mvp.universalModel.bean.DynamicVo;
import com.ygip.ipbase_android.util.XRecyclerViewUtil;
import com.ygip.ipbase_android.util.listener.OnActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by XQM on 2017/7/23.
 */

public class DynamicFragment extends XFragment<DynamicPresenter> {
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
    @BindView(R.id.recyclerContentLayout_dynamic)
    XRecyclerContentLayout recyclerContentLayoutDynamic;
    Unbinder unbinder;

    private DynamicAdapter adapter;
    private List<DynamicVo> dynamics = new ArrayList<>();
    public static Boolean requireRefresh = false;
    private Boolean isVisible = false;
    private OnLoadListener<List<DynamicVo>> onLoadListener;
    private FindMemberAsync findMemberAsync;
    private XRecyclerView xRecyclerView;

    public void setDynamics(List<DynamicVo> dynamics) {
        this.dynamics = dynamics;
        recyclerContentLayoutDynamic.postDelayed(() -> {
            adapter.setData(dynamics);
            recyclerContentLayoutDynamic.getSwipeRefreshLayout().setRefreshing(false);
        }, 100);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//---懒加载
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        (new Handler(Looper.getMainLooper())).postDelayed(() -> {

            if (isVisibleToUser) {
                if (dynamics == null) {
                    getP().loadDynamics(true);
                } else if (dynamics.size() == 0 || requireRefresh) {
                    Logger.d("loadMembers");
                    getP().loadDynamics(true);
                    requireRefresh = false;
                }
            }
        }, 100);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarTvTitle.setText("动态");
        initAdapter();
        initView();
    }

    private void initView() {

    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new DynamicAdapter(context);
        }
        xRecyclerView = recyclerContentLayoutDynamic.getRecyclerView();
        XRecyclerViewUtil.setCommonParams(context, recyclerContentLayoutDynamic);
        xRecyclerView.setAdapter(adapter);
        recyclerContentLayoutDynamic.getErrorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Kits.Empty.check(dynamics)) {
                    recyclerContentLayoutDynamic.showEmpty();
                }
            }
        });
        xRecyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getP().loadDynamics(true);
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        adapter.setRecItemClick(new RecyclerItemCallback<DynamicVo, DynamicAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, DynamicVo model, int tag, DynamicAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public DynamicPresenter newP() {
        return new DynamicPresenter();
    }

    public static DynamicFragment newInstance() {
        return new DynamicFragment();
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
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.titlebar_iv_left)
    public void onTitlebarIvLeftClicked() {
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
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

    @OnClick(R.id.recyclerContentLayout_dynamic)
    public void onRecyclerContentLayoutDynamicClicked() {
    }
}
