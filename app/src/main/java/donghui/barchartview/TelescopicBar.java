package donghui.barchartview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class TelescopicBar extends View {

    public TelescopicBar(Context context) {
        super(context);
    }

    public TelescopicBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TelescopicBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 默认的rect信息
     */
    private float left, top, right, bottom;

    /**
     * 动画更新值
     */
    private float animationValue;

    private int color;

    private ValueAnimator valueAnimator;

    /**
     * 是否正在进行恢复到初始状态的动画
     */
    private boolean isRestoreAnimatorStart = false;

    private Paint paint;

    public void initialize(int color, float left, float right, float top,
                           float bottom) {
        this.color = color;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        invalidate();
    }

    public void setColor(int fillColor) {
        this.color = fillColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint != null) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            canvas.drawRect(left, top - animationValue, right, bottom, paint);
        }
    }

    public void startAnimator(float maxStretchHeight, int animationDuration) {
        if (valueAnimator != null && valueAnimator.isStarted()) {
            valueAnimator.cancel();
        }
        isRestoreAnimatorStart = false;
        valueAnimator = ValueAnimator.ofFloat(0, maxStretchHeight);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(animationDuration);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void cancelAnimator() {
        if (valueAnimator != null && !isRestoreAnimatorStart) {
            valueAnimator.cancel();
            long duration = valueAnimator.getDuration();
            valueAnimator = ValueAnimator.ofFloat(animationValue, 0);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setDuration(duration);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animationValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isRestoreAnimatorStart= true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    valueAnimator.cancel();
                    valueAnimator = null;
                    isRestoreAnimatorStart = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }
}
