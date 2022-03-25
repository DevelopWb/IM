package com.juntai.wisdom.im.search;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.im.chatlist.searchchat.BaseSearchActivity;

/**
 * @aouther tobato
 * @description 描述  首页搜索模块
 * @date 2022-01-27 15:05
 */
public class SearchActivity extends BaseSearchActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchAdapter(null);
    }

    @Override
    protected int getSearchType() {
        return 4;
    }
}
