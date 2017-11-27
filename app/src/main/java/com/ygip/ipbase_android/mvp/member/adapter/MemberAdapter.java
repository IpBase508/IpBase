package com.ygip.ipbase_android.mvp.member.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.member.present.MemberPresenter;
import com.ygip.ipbase_android.mvp.member.view.MemberFragment;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.mvp.universalPresenter.UniversalPresenter;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.PopupList;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.listener.OnActionListener;
import com.ygip.ipbase_android.util.listener.OnDialogListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by XQM on 2017/7/23.
 */

public class MemberAdapter extends SimpleRecAdapter<UserVo, MemberAdapter.ViewHolder> {
    private OnActionListener onActionListener;
    private String dept;

    public MemberAdapter(Activity context, OnActionListener onActionListener) {
        super(context);
        this.onActionListener = onActionListener;
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_member;
    }

    public View initChooseDeptSpinner(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.spinnerview, null);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserVo member;
        member = data.get(position);
        Glide.with(context)
                .load(TextUtils.isEmpty(member.getImageUrl()) ? R.drawable.user_1 : member.getImageUrl())
                .error(R.drawable.user_1)
                .dontAnimate()
                .into(holder.civHead);
        holder.tvName.setText(member.getMemberName());

        String grade = member.getGrade();
        if (!TextUtils.isEmpty(grade) && !grade.equals("无")) {
            holder.tvGrade.setText(grade);
            holder.tvGrade.setVisibility(View.VISIBLE);
        } else {
            holder.tvGrade.setVisibility(View.GONE);
        }
        holder.tvDepartment.setText(member.getDepartment());
        holder.tvPhone.setText(member.getPhoneNumber());
        switch (member.getUserLevel()) {
            case 2: {
                holder.tvLevel.setText("管理员");
                holder.tvLevel.setVisibility(View.VISIBLE);
                break;
            }
            case 3: {
                holder.tvLevel.setText("超级管理员");
                holder.tvLevel.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                holder.tvLevel.setVisibility(View.GONE);
                break;
            }
        }
        try {
            if (UniversalModel.getUser().getUserLevel() > member.getUserLevel()) {
                holder.btnIvDeleteMember.setVisibility(View.VISIBLE);
                holder.btnIvDeleteMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.dialogDefault(context, "删除成员?", new OnDialogListener() {
                            @Override
                            public void positive() {
                                MemberPresenter.deleteMember(member.getUserId(), new OnResponseListener() {
                                    @Override
                                    public void onFinish(UniversalResponseBean responseBean, Exception e) {
                                        MemberFragment.requireRefresh = true;
                                        if (onActionListener != null) {
                                            onActionListener.done();
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
            } else {
                holder.btnIvDeleteMember.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> popupMenuItemList = new ArrayList<>();
        PopupList popupList;
        popupMenuItemList.add("复制姓名");
        popupMenuItemList.add("复制手机号");
        if (UniversalModel.getUser().getUserLevel() > 2) {
            if (member.getUserLevel()<2) {
                popupMenuItemList.add("修改部门");
                popupMenuItemList.add("设为管理员");
            }
        }
        popupList = new PopupList(context);
        popupList.bind(holder.itemView, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int menuItemPos) {
                ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                String text = "";
                switch (menuItemPos) {
                    case 0: {
                        text = holder.tvName.getText().toString();
                        ClipData myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                        ToastUtils.show("已复制姓名");
                        break;
                    }
                    case 1: {
                        text = holder.tvPhone.getText().toString();
                        ClipData myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                        ToastUtils.show("已复制手机号");
                        break;
                    }
                    case 2:{//更改成员部门


                        String[] depts=context.getResources().getStringArray(R.array.departments);
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
                        DialogUtils.dialogWithView(context, "更改" + member.getMemberName() + "的部门", view, new OnDialogListener() {
                            @Override
                            public void positive() {
                                if (!TextUtils.isEmpty(dept))
                                {
                                    UserVo userVo=new UserVo();
                                    userVo.setUserId(member.getUserId());
                                    userVo.setDepartment(dept);
                                    UniversalPresenter universalPresenter=new UniversalPresenter(context);
                                    universalPresenter.putUserData(userVo);
                                    dept=null;
                                }else {
                                    new Handler(Looper.getMainLooper()).post(()->ToastUtils.show("未更改"));
                                }
                            }

                            @Override
                            public void negative() {

                            }
                        });
                        break;
                    }
                    case 3: {
                        DialogUtils.dialogDefault(context, "确认设置" + member.getMemberName() + "为管理员？", new OnDialogListener() {
                            @Override
                            public void positive() {
                                UserVo userVo=new UserVo();
                                userVo.setUserId(data.get(position).getUserId());
                                MemberPresenter.setAdmin(userVo);
                            }

                            @Override
                            public void negative() {

                            }
                        });
                        break;
                    }
                }


            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupList.showPopupListWindow();
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, data.get(position), 0, holder);
                }
            }
        });
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_head)
        CircleImageView civHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_grade)
        TextView tvGrade;
        @BindView(R.id.tv_department)
        TextView tvDepartment;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.btn_iv_delete_member)
        ImageView btnIvDeleteMember;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
