package com.juntai.wisdom.im.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Desc:九宫格分割线   无最右边与最下边线
 * Create by tobato
 * 2020-8-24
 * email:954101549@qq.com
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    private Paint mPaint;
    private int mDividerHeight = 1;//分割线高度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL

    public GridDividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public GridDividerItemDecoration(Context context, int dividerHeight, int dividerColor) {
        this(context);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
//            if (mPaint != null) {
//                canvas.drawRect(left, top, right, bottom, mPaint);
//            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerHeight;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 是否是最后一行
     */
    private boolean isLastRow(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //有多少列
        if (layoutManager instanceof GridLayoutManager) {
            int childCount = parent.getAdapter().getItemCount();

            double count = Math.ceil((double) childCount / (double) spanCount);//总行数
            double currentCount = Math.ceil((double) (itemPosition + 1) / spanCount);//当前行数

            //最后当前数量小于总的
            if (currentCount < count) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断是否是最后一列
     */
    private boolean isLastColum(int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //有多少列
        if (layoutManager instanceof GridLayoutManager) {
            int spanCount = getSpanCount(parent);
            if ((itemPosition + 1) % spanCount == 0) {//因为是从0可以所以要将ItemPosition先加1
                return true;
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
//        LogUtil.out("++++++++++++++条目，" + parent.getChildLayoutPosition(view) + "是否最后一行" + isLastRow(parent.getChildLayoutPosition(view), parent));
//        LogUtil.out("++++++++++++++条目，" + parent.getChildLayoutPosition(view) + "是否最后一列" + isLastColum(parent.getChildLayoutPosition(view), parent));
        if (isLastRow(parent.getChildLayoutPosition(view), parent))// 如果是最后一行，则不需要绘制底部
        {
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            outRect.set(0, 0, 0, 0);
        }

        if (isLastColum(parent.getChildLayoutPosition(view), parent))// 如果是最后一列，则不需要绘制右边
        {
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            outRect.set(0, 0, 0, 0);
        }
    }
}
