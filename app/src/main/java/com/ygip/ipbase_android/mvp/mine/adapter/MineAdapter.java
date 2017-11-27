package com.ygip.ipbase_android.mvp.mine.adapter;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.mvp.mine.view.ChangeInfoActivity;
import com.ygip.ipbase_android.mvp.mine.view.ChangePasswordActivity;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.FileUtils;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.listener.OnDialogListener;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lockyluo on 2017/7/28.
 */

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.ViewHolder> {

    public static final int MINE = 1;
    public static final int SETTING = 2;
    private static String dbPath;

    String dept;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MINE, SETTING})
    public @interface Mode {
    }

    public
    @Mode
    int mode;


    private ArrayList<String> data;
    private Activity activity;

    /**
     * 这里偷懒，设置和我的界面都共用一个adapter，用mode区分是哪一个界面
     * @param data
     * @param mode
     * @param activity
     */
    public MineAdapter(ArrayList<String> data, @Mode int mode, Activity activity) {
        this.data = data;
        this.mode = mode;
        this.activity = activity;
    }

    public void updateData(ArrayList<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public String getTitle(int i) {
        String s = "";
        switch (i) {
            case 0:
                s = ("姓名:");
                break;
            case 1:
                s = ("部门:");
                break;
            case 2:
                s = ("年级:");
                break;
            case 3:
                s = ("手机号:");
                break;
            case 4:
                s = ("权限级别:");
                break;
            case 5:
                s = ("注册时间:");
                break;
        }
        return s;
    }

    public View initChooseDeptSpinner(){
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.spinnerview, null);
        return view;
    }

    public void doAction(@Mode int type, int i) {
        Logger.d(type + " " + i);
        if (type == MINE) {//----------我的界面
            if (i == 1) {
                if (UniversalModel.getUser().getUserLevel() >= 2) {
                    String[] depts=activity.getResources().getStringArray(R.array.departments);
                    View view=initChooseDeptSpinner();
                    Spinner spChooseDept=(Spinner)view.findViewById(R.id.sp_choose_dept);
                    spChooseDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position!=0) {
                                dept=depts[position];
                            }else {
                                dept=null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    DialogUtils.dialogWithView(activity, "更改部门", view, new OnDialogListener() {
                        @Override
                        public void positive() {
                            if (dept!=null) {
                                UserVo userVo=new UserVo();
                                userVo.setUserId(UniversalModel.getUser().getUserId());
                                userVo.setDepartment(dept);
                                JsonObject jsonObject=MinePresenter.User2Object(userVo);
                                UniversalModel universalModel=new UniversalModel();
                                universalModel.putData(ApiUrl.Put.PUT_USER_URL, jsonObject,MinePresenter.onPutListener);
                            }else {
                                new Handler(Looper.getMainLooper()).post(()->ToastUtils.show("未选择部门"));
                            }
                        }

                        @Override
                        public void negative() {

                        }
                    });
                }
            }
            if (i == getItemCount() - 3) {//改密码
                ChangePasswordActivity.setTitle("修改密码");
                ChangePasswordActivity.setPhoneNumber(UniversalModel.getUser().getPhoneNumber());
                StartActivityUtil.start(activity, ChangePasswordActivity.class);
            }
            if (i == getItemCount() - 2) {//清除缓存
                try {
                    DialogUtils.dialogDefault(activity, "确认清除？这将会退出app", new OnDialogListener() {
                        @Override
                        public void positive() {
                            dbPath = activity.getFilesDir().getParentFile().getAbsolutePath() + "/databases";
                            FileUtils.deleteAllFiles(new File(dbPath));
                            ToastUtils.show("清除成功");
                            activity.finish();
                            System.exit(0);
                        }

                        @Override
                        public void negative() {

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (i == getItemCount() - 1) {//logout
                DialogUtils.dialogDefault(activity, "确认注销？", new OnDialogListener() {
                    @Override
                    public void positive() {
                        UniversalModel.logout(activity);
                    }

                    @Override
                    public void negative() {

                    }
                });
            }
        } else {//----------设置界面

            switch (i) {
                case 0: {
                    ChangeInfoActivity.title="姓名";
                    StartActivityUtil.start(activity, ChangeInfoActivity.class);
                    break;
                }
                case 1: {
                    break;
                }
                case 2: {
                    ChangeInfoActivity.title="年级";
                    StartActivityUtil.start(activity, ChangeInfoActivity.class);
                    break;
                }
                case 3: {
                    ChangeInfoActivity.title="手机号";
                    StartActivityUtil.start(activity, ChangeInfoActivity.class);
                    break;
                }
                case 4: {
                    break;
                }
                case 5: {
                    break;
                }
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMineItemContent.setText(data.get(position));


        if (mode == SETTING) {//设置界面
            holder.tvMineItemTitle.setVisibility(View.VISIBLE);
            holder.mineItemBtn.setVisibility(View.VISIBLE);
            holder.tvMineItemTitle.setText(getTitle(position));

        } else {//我的界面
            holder.tvMineItemTitle.setVisibility(View.GONE);
            holder.mineItemBtn.setVisibility(View.GONE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.tvMineItemContent.setGravity(Gravity.CENTER);
            }
        }
        holder.mineMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.show(holder.tvMineItemContent.getText().toString());
                doAction(mode, position);
            }
        });
    }

//    @OnClick({R.id.tv_mine_item_title, R.id.tv_mine_item_content})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_mine_item_title:
//                break;
//            case R.id.tv_mine_item_content:
//                break;
//        }
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_mine_item_title)
        TextView tvMineItemTitle;
        @BindView(R.id.tv_mine_item_content)
        TextView tvMineItemContent;
        @BindView(R.id.mine_menu_item)
        FrameLayout mineMenuItem;
        @BindView(R.id.mine_item_btn)
        LinearLayout mineItemBtn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
