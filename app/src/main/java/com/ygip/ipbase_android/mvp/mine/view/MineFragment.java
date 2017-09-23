package com.ygip.ipbase_android.mvp.mine.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.mvp.XLazyFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by lockyluo on 2017/7/28.
 */

public class MineFragment extends XLazyFragment<MinePresenter> {
    public final static int PICK_IMAGE_REQUEST_CODE = 1004;

    @BindView(R.id.iv_mine_head)
    CircleImageView ivMineHead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.mine_ConstraintLayout)
    CoordinatorLayout mineConstraintLayout;
    @BindView(R.id.mine_progressbar)
    ProgressBar mineProgressbar;


    @Override
    public void initData(Bundle savedInstanceState) {
        toolbar.setTitle("我的");

    }

    public void initView() {

        RxPermissions rxPermissions=new RxPermissions(context);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
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
            getP().postFileData(uri, ivMineHead, mineProgressbar);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        getP().testLogin();
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4及以上
            intent.setAction(Intent.ACTION_PICK);
        } else {//4.4以下
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @OnClick(R.id.toolbar)
    public void onToolbarClicked() {
    }




}
