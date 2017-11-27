package com.ygip.ipbase_android.mvp.member.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.member.adapter.MemberAdapter;
import com.ygip.ipbase_android.mvp.member.present.FindMemberAsync;
import com.ygip.ipbase_android.mvp.member.present.MemberPresenter;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.mvp.universalView.ViewAnimation;
import com.ygip.ipbase_android.util.StringUtils;
import com.ygip.ipbase_android.util.XRecyclerViewUtil;
import com.ygip.ipbase_android.util.listener.OnActionListener;

import org.angmarch.views.NiceSpinner;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
import scut.carson_ho.searchview.BackCallBack;
import scut.carson_ho.searchview.SearchCallBack;
import scut.carson_ho.searchview.SearchView;

/**
 * Created by XQM on 2017/7/23.
 */

public class MemberFragment extends XFragment<MemberPresenter> {
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.xRecyclerContentLayout)
    XRecyclerContentLayout xRecyclerContentLayout;
    @BindView(R.id.titlebar_ll_left)
    LinearLayout titlebarLlLeft;
    @BindView(R.id.searchview_member)
    SearchView searchviewMember;
    Unbinder unbinder;
    @BindView(R.id.titlebar_iv_right)
    ImageView titlebarIvRight;
    @BindView(R.id.titlebar_ll_right)
    LinearLayout titlebarLlRight;
    @BindView(R.id.titlebar_iv_left)
    ImageView titlebarIvLeft;
    @BindView(R.id.titlebar_tv_right)
    TextView titlebarTvRight;
    @BindView(R.id.sp_dept_member)
    NiceSpinner spDeptMember;

    public final static int PICK_IMAGE_REQUEST_CODE = 1004;
    private MemberAdapter adapter;
    private List<UserVo> members = new ArrayList<>();
    public static Boolean requireRefresh = false;
    private Boolean isVisible = false;
    private OnLoadListener<List<UserVo>> onLoadListener;
    private FindMemberAsync findMemberAsync;
    private XRecyclerView xRecyclerView;
    private List<String> deptsList = new LinkedList<>();
    private String[] depts = new String[]{};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_member;
    }


    public void setMembers(List<UserVo> members) {
        this.members = members;

        xRecyclerContentLayout.postDelayed(() -> {
            adapter.setData(members);
            xRecyclerContentLayout.getSwipeRefreshLayout().setRefreshing(false);
        }, 100);
    }

    @Override
    public void onResume() {
        if (members.size() == 0 || requireRefresh)
            setUserVisibleHint(true);
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//---懒加载
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (searchviewMember != null) {
            if (isVisibleToUser && searchviewMember.getVisibility() != View.GONE) {
                setSearchview(View.VISIBLE);
            }
        }
        (new Handler(Looper.getMainLooper())).postDelayed(() -> {

            if (isVisibleToUser) {
                if (members.size() == 0 || requireRefresh) {
                    Logger.d("loadMembers");
                    getP().loadMembers(true);
                    requireRefresh = false;
                }
            }
        }, 100);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titlebarTvTitle.setText("成员");
        depts = getResources().getStringArray(R.array.departments);
        depts[0] = "部门筛选";
        deptsList = new LinkedList<>(Arrays.asList(depts));
        initAdapter();
        initView();

    }

    private void setSearchview(int vi) {
        searchviewMember.setVisibility(vi);
        if (vi == View.VISIBLE) {
            MainActivity.setSearchView(searchviewMember, true);
            searchviewMember.startAnimation(ViewAnimation.showAction());
        } else {
            MainActivity.setSearchView(null, false);
        }
    }

    private void doSearch(String result) {//执行搜索
        if (findMemberAsync != null) {
            findMemberAsync.cancel(true);
        }
        findMemberAsync = new FindMemberAsync();
        if (StringUtils.isNumeric(result)) {//判断关键字是数字还是文本，按情况搜索
            findMemberAsync.initTask(context, FindMemberAsync.FIND_BY_NUMBER, result);

        } else {
            findMemberAsync.initTask(context, FindMemberAsync.FIND_BY_STRING, result);

        }
        findMemberAsync.execute(onLoadListener);//开始执行
    }

    private void initView() {
        spDeptMember.attachDataSource(deptsList, R.color.colorPrimary);
        if (UniversalModel.getUser().getUserLevel() >= 2) {
            titlebarLlRight.setVisibility(View.VISIBLE);
            titlebarIvRight.setImageResource(R.drawable.add);
        } else {
            titlebarLlRight.setVisibility(View.GONE);
        }
        titlebarLlLeft.setVisibility(View.VISIBLE);

        titlebarIvLeft.setImageResource(R.drawable.search);
        titlebarLlLeft.setBackgroundResource(R.drawable.btn_my_bg);

        onLoadListener = new OnLoadListener<List<UserVo>>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish(List<UserVo> data, Exception e) {
                setMembers(data);
            }
        };
        searchviewMember.setOnBackClick(new BackCallBack() {
            @Override
            public void BackAciton() {
                searchviewMember.et_search.setText("");
                setSearchview(View.GONE);
            }
        });
        searchviewMember.setOnSearchClick(new SearchCallBack() {
            @Override
            public void SearchAciton(String result) {
                doSearch(result);
                searchviewMember.et_search.setText("");
                setSearchview(View.GONE);
            }
        });

        spDeptMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    doSearch(depts[position]);//按部门搜索
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new MemberAdapter(context, new OnActionListener() {
                @Override
                public void done() {
                    setUserVisibleHint(true);
                }
            });
        }
        xRecyclerView = xRecyclerContentLayout.getRecyclerView();
        XRecyclerViewUtil.setCommonParams(context, xRecyclerContentLayout);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.useDefLoadMoreView();
        xRecyclerContentLayout.getErrorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Kits.Empty.check(members)) {
                    xRecyclerContentLayout.showEmpty();
                }
            }
        });
        xRecyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getP().loadMembers(true);
                spDeptMember.setSelectedIndex(0);
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        adapter.setRecItemClick(new RecyclerItemCallback<UserVo, MemberAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, UserVo model, int tag, MemberAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
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
            getP().batchImportMembers(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public MemberPresenter newP() {
        return new MemberPresenter();
    }

    public static MemberFragment newInstance() {
        return new MemberFragment();
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


    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        setSearchview(View.VISIBLE);
        searchviewMember.setBackgroundColor(ContextCompat.getColor(context, R.color.graye6e));
        searchviewMember.et_search.setHint("输入年级/手机号/姓名/部门查找");
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4及以上
            intent.setAction(Intent.ACTION_PICK);
        } else {//4.4以下
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "选择excel文件"), PICK_IMAGE_REQUEST_CODE);
    }
}
