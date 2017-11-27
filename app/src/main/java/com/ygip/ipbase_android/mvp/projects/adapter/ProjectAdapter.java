package com.ygip.ipbase_android.mvp.projects.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.projects.view.ProjectsFragment;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.util.DateUtils;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.listener.OnActionListener;
import com.ygip.ipbase_android.util.listener.OnDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by lockyluo on 2017/7/28.
 */

public class ProjectAdapter extends SimpleRecAdapter<ProjectVo, ProjectAdapter.ViewHolder>{

    private String[] projectType;
    private Context context;
    private Map<Integer, View> mViewMap;
    private UniversalModel universalModel;
    private OnActionListener onActionListener;

    public ProjectAdapter(Context context, OnActionListener onActionListener) {
        super(context);
        this.context = context;
        this.onActionListener=onActionListener;
        projectType = context.getResources().getStringArray(R.array.projectTypeSpinner);
    }

    @Override
    public void setData(ProjectVo[] data) {

        super.setData(data);

    }


    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProjectVo projectVo=data.get(position);
//        Logger.d("onBindViewHolder");
        try {
            Glide.with(context).load(projectVo.getProjectImageVo().size() != 0 ?
                    projectVo.getProjectImageVo().get(0).getImage() :
                    R.mipmap.ipbaselogo)
                    .placeholder(R.drawable.ic_crop_original_48dp)
                    .error(R.mipmap.ipbaselogo)
                    .dontAnimate().into(holder.ivProjectHeadimg);
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (UniversalModel.getUser().getUserLevel()>=2){
            holder.btnIvDeleteProject.setVisibility(View.VISIBLE);
            holder.btnIvDeleteProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.dialogDefault(context, "删除项目“" + projectVo.getProjectName() + "”?", new OnDialogListener() {
                        @Override
                        public void positive() {
                            universalModel=new UniversalModel();
                            HashMap<String,List<String>> hashMap=new HashMap<String, List<String>>();
                            List<String> ids=new ArrayList<String>();
                            ids.add(projectVo.getProjectId());
                            hashMap.put("projectIds",ids);
                            universalModel.putData(ApiUrl.Put.DEL_PROJECT_FAKE_URL, hashMap, new OnResponseListener() {
                                @Override
                                public void onFinish(UniversalResponseBean responseBean, Exception e) {
                                    if (responseBean!=null&&e==null){
                                        ((Activity)context).runOnUiThread(()->{
                                            notifyDataSetChanged();
                                            ToastUtils.show("删除成功");
                                            ProjectsFragment.requireRefresh=true;
                                            onActionListener.done();
                                        });
                                    }else {
                                        ((Activity)context).runOnUiThread(()->{
                                            ToastUtils.show(TextUtils.isEmpty(e.getMessage())?"删除失败":e.getMessage());
                                        });
                                    }
                                }
                            });
                        }

                        @Override
                        public void negative() {

                        }
                    });
                }
            });
        }else {
            holder.btnIvDeleteProject.setVisibility(View.GONE);
        }

        holder.tvProjectName.setText(projectVo.getProjectName());
        holder.tvProjectLeader.setText((projectVo.getAnnouncer()!=null?projectVo.getAnnouncer().getMemberName():""));
        holder.tvFinishDate.setText(DateUtils.getThisTime(projectVo.getDeadLine() == null ? 0 : projectVo.getDeadLine() * 1000));
        holder.tvProjectType.setText(projectType[position + 1 >= projectType.length ? projectType.length - 1 : position + 1]);
        holder.llProjectitem.setOnClickListener(view -> {
            if (getRecItemClick() != null) {
                getRecItemClick().onItemClick(position, projectVo, 0, holder);
            }
        });
        holder.llProjectitem.setOnLongClickListener(view -> {
            if (getRecItemClick() != null) {
                getRecItemClick().onItemLongClick(position, projectVo, 0, holder);
            }
            return true;
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_project;
    }


    static public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_project_headimg)
        ImageView ivProjectHeadimg;
        @BindView(R.id.tv_project_name)
        TextView tvProjectName;
        @BindView(R.id.tv_project_leader)
        TextView tvProjectLeader;
        @BindView(R.id.ll_projectitem)
        LinearLayout llProjectitem;
        @BindView(R.id.tv_publish_date)
        TextView tvFinishDate;
        @BindView(R.id.tv_project_type)
        TextView tvProjectType;
        @BindView(R.id.btn_iv_delete_project)
        ImageView btnIvDeleteProject;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }

}
