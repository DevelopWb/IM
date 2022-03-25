package com.juntai.disabled;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 *
 */
public class PicVideoViewPagerAdapter extends FragmentPagerAdapter {
	 private List<Fragment> mListViews;

	public PicVideoViewPagerAdapter(FragmentManager fm,List<Fragment> mListViews) {
		super(fm);
		this.mListViews = mListViews;
	}

	@Override
	 public int getCount() {			
		return  mListViews.size();//返回页卡的数量
	 }

	@Override
	public Fragment getItem(int i) {
		return mListViews.get(i);
	}

 }