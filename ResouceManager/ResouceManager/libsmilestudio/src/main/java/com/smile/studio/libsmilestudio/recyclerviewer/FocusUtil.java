package com.smile.studio.libsmilestudio.recyclerviewer;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by admin on 24/07/2017.
 */

public class FocusUtil {
    public static final float SCALE_RATE = 1.045f;//一
    private final static int duration = 140;

    //private final static float endScale = 1.14f;
    private final static float startScale = 1.0f;

    // public static final float SCALE_RATE = 1.0571f;

    /**
     * 当焦点发生变化
     *
     * @param view
     * @param gainFocus
     */
    public static void onFocusChange(View view, boolean gainFocus) {
        if (gainFocus) {
            onFocusIn(view, SCALE_RATE);
        } else {
            onFocusOut(view, SCALE_RATE);
        }
    }

    /**
     * 当view获得焦点
     *
     * @param view
     */
    public static void onFocusIn(final View view, float endScale) {

        ValueAnimator animIn = ValueAnimator.ofFloat(startScale, endScale);
        animIn.setDuration(duration);
        animIn.setInterpolator(new DecelerateInterpolator());
        animIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        animIn.start();

    }

    /**
     * 当view失去焦点
     *
     * @param view
     */
    public static void onFocusOut(final View view, float endScale) {
        ValueAnimator animOut = ValueAnimator.ofFloat(endScale, startScale);
        animOut.setDuration(duration);
        animOut.setInterpolator(new DecelerateInterpolator());
        animOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });

        animOut.start();
    }
}
