package com.negier.emojifragment.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/29 0029.
 */

public class EmojiViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<GridView> itemViews;

    public EmojiViewPagerAdapter(Context context, ArrayList<GridView> itemViews) {
        this.context=context;
        this.itemViews=itemViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = itemViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return itemViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
