package com.xiaxl.test.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.xiaxl.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1、####################################################
 * LinePagerIndicator.PositionData positionData01 = new LinePagerIndicator.PositionData();
 * //
 * positionData01.mLeft = tab01.getLeft();
 * positionData01.mRight = tab01.getRight();
 * positionData01.mTop = tab01.getTop();
 * positionData01.mBottom = tab01.getBottom();
 * //
 * positionData01.mContentLeft = tab01.getLeft();
 * positionData01.mContentRight = tab01.getRight();
 * positionData01.mContentTop = tab01.getTop();
 * positionData01.mContentBottom = tab01.getBottom();
 * <p>
 * <p>
 * LinePagerIndicator.PositionData positionData02 = new LinePagerIndicator.PositionData();
 * //
 * positionData02.mLeft = tab02.getLeft();
 * positionData02.mRight = tab02.getRight();
 * positionData02.mTop = tab02.getTop();
 * positionData02.mBottom = tab02.getBottom();
 * //
 * positionData02.mContentLeft = tab02.getLeft();
 * positionData02.mContentRight = tab02.getRight();
 * positionData02.mContentTop = tab02.getTop();
 * positionData02.mContentBottom = tab02.getBottom();
 * <p>
 * <p>
 * mPositionDataList.add(positionData01);
 * mPositionDataList.add(positionData02);
 * <p>
 * <p>
 * mLinePagerIndicator.setIndicatorMode(LinePagerIndicator.MODE_EXACTLY);
 * mLinePagerIndicator.setIndicatorHeight(15);
 * mLinePagerIndicator.setIndicatorWidth(100);
 * mLinePagerIndicator.setIndicatiorRadius(9);
 * mLinePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
 * mLinePagerIndicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
 * mLinePagerIndicator.setIndicatorColors(Color.parseColor("#00c853"));
 * <p>
 * <p>
 * mLinePagerIndicator.setViewPositonList(mPositionDataList);
 * <p>
 * <p>
 * 2、####################################################
 * viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 *
 * @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
 * <p>
 * mLinePagerIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
 * <p>
 * }
 * @Override public void onPageSelected(int position) {
 * <p>
 * }
 * @Override public void onPageScrollStateChanged(int state) {
 * <p>
 * }
 * });
 */
public class LinePagerIndicator extends View {

    /**
     * 三种模式
     */
    public static final int MODE_MATCH_EDGE = 0;        // 直线宽度 == mLineWidth宽度 - 2 * mXOffset
    public static final int MODE_WRAP_CONTENT = 1;      // 直线宽度 == title内容宽度 - 2 * mXOffset
    public static final int MODE_EXACTLY = 2;           // 直线宽度 == mLineWidth

    private int mMode = LinePagerIndicator.MODE_EXACTLY;  // 默认为MODE_MATCH_EDGE模式

    // 控制动画
    private Interpolator mStartInterpolator = new AccelerateInterpolator();
    private Interpolator mEndInterpolator = new DecelerateInterpolator(2.0f);

    private float mYOffset;   // 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
    private float mLineHeight;
    private float mXOffset;
    private float mLineWidth;
    private float mRoundRadius;

    private Paint mPaint;
    private List<PositionData> mPositionDataList;
    private List<Integer> mColors;

    private RectF mLineRect = new RectF();


    public LinePagerIndicator(Context context) {
        this(context, null);
    }

    public LinePagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public LinePagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //
        init(context);
        //
        initAttrs(context, attrs, defStyle);

    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        // 获取自定义属性
        TypedArray a = null;
        //
        int model = mMode;
        //
        int indicatorWidth = (int) (getResources().getDisplayMetrics().density * 15);
        int indicatorHeight = (int) (getResources().getDisplayMetrics().density * 2);
        int indicatorRoundRadius = 0;
        //
        int indicatorColor = 0xEE1A1A;

        try {
            // 获取自定义属性
            a = context.obtainStyledAttributes(attrs, R.styleable.LinePagerIndicator);
            // 模式
            model = a.getInt(R.styleable.LinePagerIndicator_indicatorModel, mMode);
            // 宽
            indicatorWidth = a.getDimensionPixelSize(R.styleable.LinePagerIndicator_indicatorWidth, indicatorWidth);
            indicatorHeight = a.getDimensionPixelSize(R.styleable.LinePagerIndicator_indicatorHeightl, indicatorHeight);
            // 圆角
            indicatorRoundRadius = a.getDimensionPixelSize(R.styleable.LinePagerIndicator_indicatorRoundRadius, indicatorRoundRadius);
            // 颜色
            indicatorColor = a.getColor(R.styleable.LinePagerIndicator_indicatorColor, 0xEE1A1A);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
        //
        setIndicatorMode(model);
        //
        setIndicatorWidth(indicatorWidth);
        setIndicatorHeight(indicatorHeight);
        setIndicatiorRadius(indicatorRoundRadius);
        //
        setIndicatorColors(indicatorColor);

    }


    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // 画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        // 滑块默认高度
        mLineHeight = IndicatorUtils.dip2px(context, 3);
        // 滑块默认宽度
        mLineWidth = IndicatorUtils.dip2px(context, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制滑块
        canvas.drawRoundRect(mLineRect, mRoundRadius, mRoundRadius, mPaint);
    }


    // #############################################################################################

    /**
     * ViewPager
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        /**
         *
         */
        // 计算颜色
        if (mColors != null && mColors.size() > 0) {
            // 当前颜色
            int currentColor = mColors.get(Math.abs(position) % mColors.size());
            // 下一个颜色
            int nextColor = mColors.get(Math.abs(position + 1) % mColors.size());
            // 位运算，计算差值颜色
            int color = IndicatorUtils.eval(positionOffset, currentColor, nextColor);
            mPaint.setColor(color);
        }

        /**
         *
         */
        // 计算当前锚点 计算下一个锚点
        PositionData current = IndicatorUtils.getImitativePositionData(mPositionDataList, position);
        PositionData next = IndicatorUtils.getImitativePositionData(mPositionDataList, position + 1);

        /**
         * 计算x坐标
         */
        //
        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;
        //
        if (mMode == MODE_MATCH_EDGE) {
            //
            leftX = current.mLeft + mXOffset;
            rightX = current.mRight - mXOffset;
            //
            nextLeftX = next.mLeft + mXOffset;
            nextRightX = next.mRight - mXOffset;
        }
        //
        else if (mMode == MODE_WRAP_CONTENT) {
            //
            leftX = current.mContentLeft + mXOffset;
            rightX = current.mContentRight - mXOffset;
            //
            nextLeftX = next.mContentLeft + mXOffset;
            nextRightX = next.mContentRight - mXOffset;
        } else {    // MODE_EXACTLY
            //
            leftX = current.mLeft + (current.width() - mLineWidth) / 2;
            rightX = current.mLeft + (current.width() + mLineWidth) / 2;
            //
            nextLeftX = next.mLeft + (next.width() - mLineWidth) / 2;
            nextRightX = next.mLeft + (next.width() + mLineWidth) / 2;
        }

        mLineRect.left = leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        mLineRect.right = rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset);
        mLineRect.top = getHeight() - mLineHeight - mYOffset;
        mLineRect.bottom = getHeight() - mYOffset;

        invalidate();
    }


    /**
     * 滑块的模式
     *
     * @param mode
     */
    public void setIndicatorMode(int mode) {
        if (mode == MODE_EXACTLY || mode == MODE_MATCH_EDGE || mode == MODE_WRAP_CONTENT) {
            mMode = mode;
        } else {
            throw new IllegalArgumentException("mode " + mode + " not supported.");
        }
    }

    public void setIndicatorWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }


    public void setIndicatorHeight(float lineHeight) {
        mLineHeight = lineHeight;
    }

    /**
     * 滑块颜色
     *
     * @param colors
     */
    public void setIndicatorColors(Integer... colors) {
        mColors = Arrays.asList(colors);
    }

    /**
     * 设置所有tab按钮
     *
     * @param views
     */
    public void setTabViews(View... views) {
        if (views == null) {
            return;
        }
        final List<View> viewList = Arrays.asList(views);
        //
        if (LinePagerIndicator.this.getWidth() == 0) {
            LinePagerIndicator.this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        LinePagerIndicator.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        LinePagerIndicator.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    List<LinePagerIndicator.PositionData> positionList = IndicatorUtils.getViewsLocation(viewList);
                    //
                    setViewPositonList(positionList);
                    //
                    onPageScrolled(0, 0, 0);
                }
            });
        } else {
            List<LinePagerIndicator.PositionData> positionList = IndicatorUtils.getViewsLocation(viewList);
            //
            setViewPositonList(positionList);
            //
            onPageScrolled(0, 0, 0);
        }
    }

    /**
     * 设置滑块要移动到的View的位置
     *
     * @param dataList
     */
    public void setViewPositonList(List<PositionData> dataList) {
        if (dataList != null && dataList.size() != 0) {
            mPositionDataList = dataList;
        }
    }

    /**
     * 滑块圆角
     *
     * @param roundRadius
     */
    public void setIndicatiorRadius(float roundRadius) {
        mRoundRadius = roundRadius;
    }


    /**
     * @param startInterpolator
     */
    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }


    /**
     * @param endInterpolator
     */
    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }

    // ##########################################################################################


    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }

    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float xOffset) {
        mXOffset = xOffset;
    }

    public float getLineHeight() {
        return mLineHeight;
    }


    public float getLineWidth() {
        return mLineWidth;
    }


    public float getRoundRadius() {
        return mRoundRadius;
    }


    public int getMode() {
        return mMode;
    }


    public Paint getPaint() {
        return mPaint;
    }

    public List<Integer> getColors() {
        return mColors;
    }


    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }


    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }


    // ########################################################################################

    public static class PositionData {
        public int mLeft;
        public int mTop;
        public int mRight;
        public int mBottom;
        public int mContentLeft;
        public int mContentTop;
        public int mContentRight;
        public int mContentBottom;

        public int width() {
            return mRight - mLeft;
        }

        public int height() {
            return mBottom - mTop;
        }

        public int contentWidth() {
            return mContentRight - mContentLeft;
        }

        public int contentHeight() {
            return mContentBottom - mContentTop;
        }

        public int horizontalCenter() {
            return mLeft + width() / 2;
        }

        public int verticalCenter() {
            return mTop + height() / 2;
        }
    }
}
