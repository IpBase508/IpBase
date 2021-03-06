package com.ygip.ipbase_android.mvp.mine.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.mob.MobSDK;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.adapter.MineAdapter;
import com.ygip.ipbase_android.mvp.mine.presenter.MineCommon;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.universalModel.AKey;
import com.ygip.ipbase_android.util.PopupList;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by lockyluo on 2017/7/28.
 * 我的界面
 */

public class MineFragment extends XFragment<MinePresenter> implements MineCommon {
    public final static int PICK_IMAGE_REQUEST_CODE = 1004;

    @BindView(R.id.iv_mine_head)
    CircleImageView ivMineHead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.mine_ConstraintLayout)
    CoordinatorLayout mineConstraintLayout;
    @BindView(R.id.recyclerView_mine)
    RecyclerView recyclerViewMine;
    @BindView(R.id.mine_setting_btn)
    ImageButton mineSettingBtn;
    @BindView(R.id.mine_head_progressbar)
    CircleProgressBar mineHeadProgressbar;

    Unbinder unbinder;


    private MineAdapter adapter;
    private ArrayList<String> data = new ArrayList<>();
    private EventHandler eventHandler;
    static boolean phoneNumberChecked = false;
    public static Boolean requireRefresh = false;


    @Override
    public void initData(Bundle savedInstanceState) {
        toolbar.setTitle("我的");
        data = new ArrayList<>();
        initView();

        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    String msg = throwable.getMessage();
                    ToastUtils.show(msg);
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 处理
                        phoneNumberChecked = true;
                        Logger.d(data);
                    }
                }
            }
        };
    }


    public void initView() {
        permission();
        getP().loadHead(ivMineHead);
        if (adapter == null) {
            adapter = new MineAdapter(data, MineAdapter.MINE, context);
        } else {
            adapter.updateData(data);
        }
        recyclerViewMine.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewMine.setAdapter(adapter);
        recyclerViewMine.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMine.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        updateData();

    }

    public void permission() {
        getRxPermissions().request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST_CODE && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            getP().postHead(uri, ivMineHead, mineHeadProgressbar);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivity(Class clazz) {
        StartActivityUtil.start(context, clazz);
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


    public void toast(String s) {
        ToastUtils.show(s);
    }


    @OnClick(R.id.iv_mine_head)
    public void onIvMineHeadClicked() {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4及以上
            intent.setAction(Intent.ACTION_PICK);
        } else {//4.4以下
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "更换头像"), PICK_IMAGE_REQUEST_CODE);
    }


    private void updateData() {
        data = getP().getMineData(MineAdapter.MINE);
        if (adapter != null) {
            adapter.updateData(data);
        } else {
            Logger.e("adapter is null");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//懒加载数据

        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireRefresh) {
            updateData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (getP() != null) {
            getP().onDestory();
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.mine_setting_btn)
    public void onViewClicked() {
        StartActivityUtil.start(context, SettingActivity.class);
    }
}
