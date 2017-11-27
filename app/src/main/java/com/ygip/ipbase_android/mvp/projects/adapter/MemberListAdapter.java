package com.ygip.ipbase_android.mvp.projects.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by LockyLuo on 2017/11/4.
 */

public class MemberListAdapter extends BaseAdapter {

    private List<UserVo> data = new ArrayList<>();



    private HashSet<UserVo> selectData=new HashSet<>();
    private Context context;
    private LayoutInflater mInflater;
    private ViewHolder viewHolder;

    public MemberListAdapter(Context context, List<UserVo> data) {
        this.context = context;
        this.data = data;
        Logger.d(data);
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<UserVo> data) {
        this.data = data;
        notifyDataSetChanged();
        selectData.clear();
    }
    public HashSet<UserVo> getSelectData() {
        return selectData;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public List<UserVo> getData() {
        return data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public UserVo getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserVo userVo = data.get(position);
        viewHolder = null;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_member_poplist, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(userVo.getMemberName());
        viewHolder.tvDepartment.setText(userVo.getDepartment());
        String grade= userVo.getGrade();
        if (!TextUtils.isEmpty(grade) && !grade.equals("无")) {
            viewHolder.tvGrade.setVisibility(View.VISIBLE);
            viewHolder.tvGrade.setText(userVo.getGrade());
        } else {
            viewHolder.tvGrade.setVisibility(View.GONE);
        }
        viewHolder.checkboxTiemMemberSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectData.add(data.get(position));
                }else {
                    selectData.remove(data.get(position));
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_department)
        TextView tvDepartment;
        @BindView(R.id.tv_grade)
        TextView tvGrade;
        @BindView(R.id.checkbox_tiem_member_select)
        CheckBox checkboxTiemMemberSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
