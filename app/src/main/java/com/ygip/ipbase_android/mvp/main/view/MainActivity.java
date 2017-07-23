package com.ygip.ipbase_android.mvp.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.dynamic.view.DynamicFragment;
import com.ygip.ipbase_android.mvp.main.present.MainPresent;
import com.ygip.ipbase_android.mvp.member.view.MemberFragment;
import com.ygip.ipbase_android.mvp.mine.view.MineFragment;
import com.ygip.ipbase_android.mvp.projects.view.ProjectsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public class MainActivity extends XActivity<MainPresent> {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.rbtn_member)
    RadioButton rbtnMember;
    @BindView(R.id.rbtn_dynamic)
    RadioButton rbtnDynamic;
    @BindView(R.id.rbtn_projects)
    RadioButton rbtnProjects;
    @BindView(R.id.rbtn_me)
    RadioButton rbtnMe;
    private MemberFragment memberFragment;
    private DynamicFragment dynamicFragment;
    private ProjectsFragment projectsFragment;
    private MineFragment mineFragment;

    XFragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    String[] titles = {"成员", "动态", "项目", "我的"};

    @Override
    public void initData(Bundle savedInstanceState) {
        initFragment();
        adapter = new XFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeTabView(int position) {
        switch (position) {
            case 0:
                rbtnMember.setChecked(true);
                break;
            case 1:
                rbtnDynamic.setChecked(true);
                break;
            case 2:
                rbtnProjects.setChecked(true);
                break;
            case 3:
                rbtnMe.setChecked(true);
                break;
        }
    }

    private void initFragment() {
        memberFragment = MemberFragment.newInstance();
        dynamicFragment = DynamicFragment.newInstance();
        projectsFragment = ProjectsFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fragments.add(memberFragment);
        fragments.add(dynamicFragment);
        fragments.add(projectsFragment);
        fragments.add(mineFragment);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresent newP() {
        return new MainPresent();
    }

    @OnClick({R.id.rbtn_member, R.id.rbtn_dynamic, R.id.rbtn_projects
            , R.id.rbtn_me, R.id.ll_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rbtn_member:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.rbtn_dynamic:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.rbtn_projects:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.rbtn_me:
                viewPager.setCurrentItem(3, false);
                break;
            case R.id.ll_more:
                break;
        }
    }
}
