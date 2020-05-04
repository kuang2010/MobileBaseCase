package com.kzy.mobilesafe.activity.gjgj.animator;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author: kuangzeyu2019
 * date: 2020/5/4
 * time: 15:02
 * desc:
 */
public class UnLockItemAnimator2 extends BaseItemAnimator2 {

    @Override
    public void animateRemoveImpl(final  RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mRemoveAnimations.add(holder);
        animation.setDuration(getRemoveDuration())
                .alpha(0).translationX(view.getWidth()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchRemoveStarting(holder);
            }

            @Override
            public void onAnimationEnd(View view) {
                animation.setListener(null);
                ViewCompat.setAlpha(view, 1);//因为复用布局，此处需还原
                ViewCompat.setTranslationX(view,0);//因为复用布局，此处需还原
                dispatchRemoveFinished(holder);
                mRemoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }
}
