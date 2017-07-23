package com.ygip.ipbase_android.mvp.projects.view;

import android.os.Bundle;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.member.present.MemberPresent;

import cn.droidlover.xdroidmvp.mvp.XLazyFragment;

/**
 * Created by XQM on 2017/7/23.
 */

public class ProjectsFragment extends XLazyFragment<MemberPresent> {
    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_projects;
    }

    @Override
    public MemberPresent newP() {
        return null;
    }

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }
}
