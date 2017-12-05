package com.xiaxl.test.widget;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public final class IndicatorUtils {

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    /**
     * 位运算计算差值颜色
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public static int eval(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        int currentA = (startA + (int) (fraction * (endA - startA))) << 24;
        int currentR = (startR + (int) (fraction * (endR - startR))) << 16;
        int currentG = (startG + (int) (fraction * (endG - startG))) << 8;
        int currentB = startB + (int) (fraction * (endB - startB));

        return currentA | currentR | currentG | currentB;
    }


    /**
     * IPagerIndicator支持弹性效果的辅助方法
     *
     * @param positionDataList
     * @param index
     * @return
     */
    public static LinePagerIndicator.PositionData getImitativePositionData(List<LinePagerIndicator.PositionData> positionDataList, int index) {

        // 数据在正常范围内
        if (index >= 0 && index <= positionDataList.size() - 1) {
            return positionDataList.get(index);
        }
        // 数据越界，据越界后，返回假的PositionData
        else {
            LinePagerIndicator.PositionData result = new LinePagerIndicator.PositionData();
            LinePagerIndicator.PositionData referenceData;
            int offset;
            // 数据小于0
            if (index < 0) {
                offset = index;
                referenceData = positionDataList.get(0);
            }
            // 数据大于0
            else {
                offset = index - positionDataList.size() + 1;
                referenceData = positionDataList.get(positionDataList.size() - 1);
            }
            result.mLeft = referenceData.mLeft + offset * referenceData.width();
            result.mTop = referenceData.mTop;
            result.mRight = referenceData.mRight + offset * referenceData.width();
            result.mBottom = referenceData.mBottom;
            result.mContentLeft = referenceData.mContentLeft + offset * referenceData.width();
            result.mContentTop = referenceData.mContentTop;
            result.mContentRight = referenceData.mContentRight + offset * referenceData.width();
            result.mContentBottom = referenceData.mContentBottom;
            return result;
        }
    }


    /**
     * 获取list列表中View在屏幕上的位置
     *
     * @param viewList
     * @return
     */
    public static List<LinePagerIndicator.PositionData> getViewsLocation(List<View> viewList) {
        //
        final List<LinePagerIndicator.PositionData> positionList = new ArrayList<>();
        //
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);


            LinePagerIndicator.PositionData positionData = new LinePagerIndicator.PositionData();
            //
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            //
            positionData.mLeft = location[0];
            positionData.mRight = location[0] + view.getWidth();
            positionData.mTop = location[1];
            positionData.mBottom = location[1] + view.getHeight();
            //
            positionData.mContentLeft = positionData.mLeft;
            positionData.mContentRight = positionData.mRight;
            positionData.mContentTop = positionData.mTop;
            positionData.mContentBottom = positionData.mBottom;
            //
            positionList.add(positionData);
        }
        return positionList;
    }
}