package com.ygip.ipbase_android.mvp.mine.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.OnDialogListener;
import com.ygip.ipbase_android.util.ToastUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lockyluo on 2017/7/28.
 */

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.ViewHolder> {

    public static final int MINE=1;
    public static final int SETTING=2;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MINE,SETTING})
    public @interface Mode {}

    public @Mode int mode;


    private ArrayList<String> data;
    private Activity activity;


    public MineAdapter(ArrayList<String> data, @Mode int mode, Activity activity) {
        this.data = data;
        this.mode=mode;
        this.activity=activity;
    }

    public void updateData(ArrayList<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public String getTitle(int i){
        String s="";
        switch (i){
            case 0:s=("姓名:");break;
            case 1:s=("部门:");break;
            case 2:s=("手机号:");break;
            case 3:s=("权限级别:");break;
            case 4:s=("注册时间:");break;
        }
        return s;
    }

    public void doAction(@Mode int type, int i){
        Logger.d(type+" "+i);
        if (type==MINE){
            switch (i){
                case 0:{
                    break;
                }
                case 1:{
                    break;
                }
                case 2:{
                    break;
                }
                case 3:{//logout
                    DialogUtils.dialogDefault(activity, "确认注销？", new OnDialogListener() {
                        @Override
                        public void positive() {
                            UniversalModel.logout(activity);
                        }

                        @Override
                        public void negative() {

                        }
                    });

                    break;
                }
            }
        }else {
            switch (i){
                case 0:{
                    break;
                }
                case 1:{
                    break;
                }
                case 2:{
                    break;
                }
                case 3:{
                    break;
                }
                case 4:{
                    break;
                }
                case 5:{
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


        if (mode==SETTING) {//设置界面
            holder.tvMineItemTitle.setVisibility(View.VISIBLE);
            holder.mineItemBtn.setVisibility(View.VISIBLE);
            holder.tvMineItemTitle.setText(getTitle(position));

        }else{//我的界面
            holder.tvMineItemTitle.setVisibility(View.GONE);
            holder.mineItemBtn.setVisibility(View.GONE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.tvMineItemContent.setGravity(Gravity.CENTER);
            }
        }
        holder.mineMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(holder.tvMineItemContent.getText().toString());
                doAction(mode,position);
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
