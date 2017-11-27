package com.ygip.ipbase_android.mvp.universalView;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by LockyLuo on 2017/10/7.
 * 滑出动画
 */

public class ViewAnimation {

    public static TranslateAnimation showAction()
    {
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        return mShowAction;
    }

    public static TranslateAnimation hiddenAction()
    {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }
}
