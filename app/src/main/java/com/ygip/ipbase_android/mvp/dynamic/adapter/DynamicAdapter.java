package com.ygip.ipbase_android.mvp.dynamic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.universalView.WebActivity;
import com.ygip.ipbase_android.mvp.universalModel.bean.DynamicVo;
import com.ygip.ipbase_android.util.DateUtils;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.listener.OnActionListener;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by XQM on 2017/7/23.
 */

public class DynamicAdapter extends SimpleRecAdapter<DynamicVo, DynamicAdapter.ViewHolder> {

    

    public DynamicAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dynamic;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DynamicVo dynamicVo=data.get(position);
        try {

            Glide.with(context)
                    .load(dynamicVo.getImageUrl()==null?R.mipmap.ipbaselogo:dynamicVo.getImageUrl())
                    .error(R.mipmap.ipbaselogo)
                    .dontAnimate()
                    .into(holder.ivDynamicHeadimg);
            holder.tvDynamicName.setText(dynamicVo.getTitle());
            holder.tvDynamicLeader.setText(dynamicVo.getAnnouncer().getMemberName());
            holder.tvPublishDate.setText(DateUtils.getThisTime(dynamicVo.getCreateTime()!=null?dynamicVo.getCreateTime()*1000:0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, dynamicVo, 0, holder);
                    HashMap<String,String> map=new HashMap<String, String>();
                    map.put("data",dynamicVo.getContent());
                    StartActivityUtil.startWithData(context, WebActivity.class,map);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_dynamic_headimg)
        ImageView ivDynamicHeadimg;
        @BindView(R.id.btn_iv_delete_dynamic)
        ImageView btnIvDeleteDynamic;
        @BindView(R.id.tv_dynamic_name)
        TextView tvDynamicName;
        @BindView(R.id.tv_dynamic_leader)
        TextView tvDynamicLeader;
        @BindView(R.id.tv_dynamic_type)
        TextView tvDynamicType;
        @BindView(R.id.tv_publish_date)
        TextView tvPublishDate;
        @BindView(R.id.ll_dynamicitem)
        LinearLayout llDynamicitem;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
