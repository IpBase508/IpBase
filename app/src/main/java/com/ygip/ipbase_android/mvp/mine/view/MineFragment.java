package com.ygip.ipbase_android.mvp.mine.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.model.MineModel;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.mvp.XLazyFragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lockyluo on 2017/7/28.
 */

public class MineFragment extends XLazyFragment<MinePresenter> {

    @BindView(R.id.iv_mine_head)
    CircleImageView ivMineHead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.mine_ConstraintLayout)
    CoordinatorLayout mineConstraintLayout;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolbar.setTitle("我的");

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public MinePresenter newP() {
        return new MinePresenter();
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }



    public void toast(String s){
        ToastUtils.show(s);
    }
    static UniversalModel universalModel=new UniversalModel();
    @OnClick(R.id.iv_mine_head)
    public void onIvMineHeadClicked() {
        universalModel.login(context);
        (new Handler()).postDelayed(()->{
            universalModel.getMemberList(context, null, new OnResponseListener() {
                @Override
                public void onFinish(JsonArray jsonElements, Exception e) {
                    Logger.t("MineFragment").d(jsonElements.get(0).getAsJsonObject().get("department"));
                }
            });
        },500);
    }

    @OnClick(R.id.toolbar)
    public void onToolbarClicked() {
    }
}
