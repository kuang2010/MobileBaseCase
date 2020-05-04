package com.kzy.mobilesafe.activity.gjgj.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.kzy.mobilesafe.activity.gjgj.animator.BaseItemAnimator;

/**
 * author: kuangzeyu2019
 * date: 2020/5/4
 * time: 15:02
 * desc:
 */
public class UnLockItemAnimator extends BaseItemAnimator {

    @Override
    public void animateRemoveImpl(final  RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimator animation = view.animate();
        this.mRemoveAnimations.add(holder);
        animation.setDuration(this.getRemoveDuration()).alpha(0.0f).translationX(view.getWidth()).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                dispatchRemoveStarting(holder);
            }

            public void onAnimationEnd(Animator animator) {
                animation.setListener(null);////取消监听
                //由于recyclerview的缓存复用机制，所以要恢复复用view的原状
                view.setAlpha(1.0F);//因为复用布局，此处需还原
                view.setTranslationX(0);//因为复用布局，此处需还原
                dispatchRemoveFinished(holder);//通知动画结束
                mRemoveAnimations.remove(holder);// 从动画队列中移除
                dispatchFinishedWhenDone();
            }
        }).start();
    }
}
