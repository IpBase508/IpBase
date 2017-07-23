package com.ygip.ipbase_android.mvp.mine.view;

import android.os.Bundle;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.member.present.MemberPresent;

import cn.droidlover.xdroidmvp.mvp.XLazyFragment;

/**
 * Created by XQM on 2017/7/23.
 */

public class MineFragment extends XLazyFragment<MemberPresent> {
    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public MemberPresent newP() {
        return null;
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }
}
