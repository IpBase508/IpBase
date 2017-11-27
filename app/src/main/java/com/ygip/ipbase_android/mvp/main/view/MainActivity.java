package com.ygip.ipbase_android.mvp.main.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ygip.ipbase_android.App;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.dynamic.view.DynamicFragment;
import com.ygip.ipbase_android.mvp.main.present.MainPresent;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.member.view.MemberFragment;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.mine.view.MineFragment;
import com.ygip.ipbase_android.mvp.projects.view.ProjectsFragment;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.listener.OnActionListener;
import com.ygip.ipbase_android.util.listener.OnDialogListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.kit.SimpleCallback;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import scut.carson_ho.searchview.SearchView;

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

    @BindView(R.id.fabopen_background)
    View fabopenBackground;
    @BindView(R.id.progressbar_signin)
    CircleProgressBar progressbarSignin;
    @BindView(R.id.tv_signin)
    TextView tvSignin;

    private static Activity instance;
    private MemberFragment memberFragment;
    private DynamicFragment dynamicFragment;
    private ProjectsFragment projectsFragment;
    private MineFragment mineFragment;
    private XFragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private String[] titles = {"成员", "动态", "项目", "我的"};
    private boolean fabOpen = false;
    private HashMap<String, String> macAdress = new HashMap<>();
    private static CountDownTimer countDownTimer;
    private static int progress;
    private static Boolean isSignin = false;
    private String[] permissionStrings;

    public static final int WAIT = 0;
    public static final int LOADING = 1;
    public static final int FAIL = 2;
    public static final int SUCCESS = 3;
    public static Boolean isSearchViewShow = false;
    public static SearchView searchView;
    private int status=WAIT;

    public static Activity getInstance() {
        return instance;
    }
    public static void setSearchView(SearchView searchView, Boolean isSearchViewShow) {
        MainActivity.searchView = searchView;
        MainActivity.isSearchViewShow = isSearchViewShow;
    }

    @IntDef({WAIT, LOADING, FAIL, SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    @interface AttendanceStatus {
    }//签到状态

    private void checkPermission() {
        PackageManager pm = getPackageManager();
        PackageInfo pack = null;
        try {
            pack = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            permissionStrings = pack.requestedPermissions;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissionStrings != null) {
            RxPermissions rxPermissions = new RxPermissions(context);
            rxPermissions.requestEach(permissionStrings).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(@NonNull Permission permission) throws Exception {

                }
            });
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        instance=context;

        changeAttendanceStatus(WAIT);
        checkPermission();

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
        viewPager.setCurrentItem(0, false);
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
    protected void onResume() {
        super.onResume();
        if (viewPager.getCurrentItem()==3&&MineFragment.requireRefresh){
            fragments.get(3).onResume();
        }
    }

    @Override
    public int getLayoutId() {//--------------
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

    private void openFab(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, -135, -135);
        animator.setDuration(300);
        animator.start();
        fabopenBackground.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.7f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setFillAfter(true);
        fabopenBackground.startAnimation(alphaAnimation);
        fabOpen = true;
    }

    private void closeFab(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -135, 0, 0);
        animator.setDuration(350);
        animator.start();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 0);
        alphaAnimation.setDuration(300);
        fabopenBackground.startAnimation(alphaAnimation);
        fabopenBackground.setVisibility(View.GONE);
        fabOpen = false;
    }


    @OnClick(R.id.progressbar_signin)
    public void onProgressbarSigninClicked() {
        if (status==SUCCESS) {
            ToastUtils.show("已签到");
            return;
        }
        if (!getP().checkLocation(context)) {
            ToastUtils.show("请连接到基地WIFI后开始签到");
            return;
        }
        if (status==LOADING) {
            ToastUtils.show("已开始签到");
            return;
        }
//        if(status==LOADING||status==SUCCESS){
//            return;
//        }
        startSignin(6000);
    }

    private void startSignin(long time) {
        changeAttendanceStatus(LOADING);
        progressbarSignin.setMax((int) time);
        countDownTimer = new CountDownTimer(time, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress = (int) (time - millisUntilFinished);
                progressbarSignin.setProgress(progress);
            }

            @Override
            public void onFinish() {

                if (getP().checkLocation(context)) {//提交后台
                    getP().attendanceCheck(new OnLoadListener<Boolean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinish(Boolean data, Exception e) {
                            if (data && e == null) {
                                ToastUtils.show("签到成功");
                                isSignin = true;
                                changeAttendanceStatus(SUCCESS);
                            }
                        }
                    });
                } else {
                    ToastUtils.show("请连接到基地WIFI后重新签到");
                    isSignin = false;
                    changeAttendanceStatus(FAIL);
                }
            }
        };
        countDownTimer.start();
    }

    private void changeAttendanceStatus(@AttendanceStatus int status) {//签到状态
        this.status=status;
        new Handler(getMainLooper()).post(()->{
            switch (status) {
                case WAIT: {
                    tvSignin.setText("签到");
                    progressbarSignin.setMax(100);
                    progressbarSignin.setProgress(0);
                    tvSignin.setTextColor(ContextCompat.getColor(context, R.color.white));
                    progressbarSignin.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    break;
                }
                case LOADING: {
                    tvSignin.setText("签到中");
                    tvSignin.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    progressbarSignin.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    break;
                }
                case FAIL: {
                    tvSignin.setText("重新签到");
                    progressbarSignin.setMax(100);
                    progressbarSignin.setProgress(0);
                    tvSignin.setTextColor(ContextCompat.getColor(context, R.color.white));
                    progressbarSignin.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    break;
                }
                case SUCCESS: {
                    tvSignin.setText("签到完成");
                    progressbarSignin.setMax(100);
                    progressbarSignin.setProgress(100);
                    tvSignin.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    progressbarSignin.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    break;
                }
            }
        });
    }

    public static void showLogOut(String s){
        if (instance==null)
        {
            new Handler(Looper.getMainLooper()).post(()->ToastUtils.show(s));
            Logger.e("instance is null");
            return;
        }
        instance.runOnUiThread(()->{
            DialogUtils.dialogDefault(instance, s, new OnDialogListener() {
                @Override
                public void positive() {
                    UniversalModel.logout(instance);
                }

                @Override
                public void negative() {

                }
            });
        });

    }

//-------------------------------------双击退出函数

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            if (isSearchViewShow && searchView != null) {
                searchView.setVisibility(View.GONE);
                searchView = null;
                isSearchViewShow = false;
                return;
            }

            isExit = true; // 准备退出
            ToastUtils.show(this, "再按一次退出到后台");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        instance=null;
        super.onDestroy();
    }
}
