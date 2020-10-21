package donghui.barchartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class BarChartView extends ViewGroup {
    /**
     * 第一个bar左侧与BarChartView左侧的距离
     */
    private float leftMargin;

    /**
     * bar左侧与左边bar的右侧的距离
     */
    private float barLeftMargin;

    /**
     * bar底部与BarChartView底部的距离
     */
    private float barBottomMargin;

    /**
     * bar的宽度
     */
    private float barWidth;

    /**
     * bar的最大高度
     */
    private float barMaxHeight;

    /**
     * bar的起始高度
     */
    private float barStartHeight;

    /**
     * bar的颜色
     */
    private int barColor, barDefaultColor, barPressedColor, barSelectedColor, barActivatedColor;

    /**
     * bar的动画时长范围
     */
    private int animatorMaxDuration, animatorMinDuration;

    public BarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarChartView);
        int barCount = typedArray.getInteger(R.styleable.BarChartView_bar_count, 0);
        leftMargin = typedArray.getDimensionPixelSize(R.styleable.BarChartView_left_margin, 0);
        barLeftMargin = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_bar_left_margin,  0);
        barBottomMargin = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_bar_bottom_margin, 0);
        barWidth = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_bar_width,  0);
        barMaxHeight = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_bar_height_max,  0);
        barStartHeight = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_bar_height_start,  0);
        barDefaultColor = typedArray.getColor(R.styleable.BarChartView_bar_color_default, Color.WHITE);
        barPressedColor = typedArray.getColor(R.styleable.BarChartView_bar_color_pressed, Color.WHITE);
        barSelectedColor = typedArray.getColor(R.styleable.BarChartView_bar_color_selected, Color.WHITE);
        barActivatedColor = typedArray.getColor(R.styleable.BarChartView_bar_color_activated, Color.WHITE);
        animatorMaxDuration = typedArray.getInteger(R.styleable.BarChartView_animation_duration_max, 300);
        animatorMinDuration = typedArray.getInteger(R.styleable.BarChartView_animation_duration_min, 300);
        typedArray.recycle();

        barColor = barDefaultColor;

        addBars(barCount);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            final int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                final TelescopicBar bar = (TelescopicBar) getChildAt(i);
                bar.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
                initBarRectAndColor(bar, i);
            }
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        Log.d("bcv", "press: " + pressed);
        if (pressed) {
            barColor = barPressedColor;
        }
        else {
            if (isSelected()) {
                barColor = barSelectedColor;
            }
            else {
                if (isActivated()) {
                    barColor = barActivatedColor;
                }
                else {
                    barColor = barDefaultColor;
                }
            }
        }
        for (int i = 0; i < getChildCount(); ++i) {
            TelescopicBar telescopicBar = (TelescopicBar) getChildAt(i);
            telescopicBar.setColor(barColor);
        }
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        if (!isPressed()) {
            if (selected) {
                barColor = barSelectedColor;
            }
            else {
                if (isActivated()) {
                    barColor = barActivatedColor;
                }
                else {
                    barColor = barDefaultColor;
                }
            }
            for (int i = 0; i < getChildCount(); ++i) {
                TelescopicBar telescopicBar = (TelescopicBar) getChildAt(i);
                telescopicBar.setColor(barColor);
            }
        }
    }

    @Override
    public void dispatchSetActivated(boolean activated) {
        super.dispatchSetActivated(activated);
        if (!isPressed()) {
            if (isSelected()) {
                barColor = barSelectedColor;
            }
            else {
                if (activated) {
                    barColor = barActivatedColor;
                }
                else {
                    barColor = barDefaultColor;
                }
            }
            for (int i = 0; i < getChildCount(); ++i) {
                TelescopicBar telescopicBar = (TelescopicBar) getChildAt(i);
                telescopicBar.setColor(barColor);
            }
        }
    }

    private void addBars(int barCount) {
        for (int i = 0; i < barCount; i++) {
            TelescopicBar bar = new TelescopicBar(getContext());
            addView(bar);
        }
    }

    private void initBarRectAndColor(TelescopicBar bar, int childPosition) {
        final float barLeft = leftMargin + childPosition * (barWidth + barLeftMargin);
        final float barRight = barLeft + barWidth;
        final float barBottom = getMeasuredHeight() - barBottomMargin;
        bar.initialize(barColor, barLeft, barRight, barBottom - barStartHeight, barBottom);
    }

    public void startAnimator() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            TelescopicBar telescopicBar = (TelescopicBar) getChildAt(i);
            float maxStretchHeight = barMaxHeight * 0.4f + (float) Math.random() * barMaxHeight * 0.4f - barStartHeight;
            int animationDuration = animatorMinDuration + (int) (Math.random() * (animatorMaxDuration - animatorMinDuration));
            telescopicBar.startAnimator(maxStretchHeight, animationDuration);
        }
    }

    public void cancelAnimator() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            TelescopicBar telescopicBar = (TelescopicBar) getChildAt(i);
            telescopicBar.cancelAnimator();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != VISIBLE) {
            cancelAnimator();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != VISIBLE) {
            cancelAnimator();
        }
    }

}
