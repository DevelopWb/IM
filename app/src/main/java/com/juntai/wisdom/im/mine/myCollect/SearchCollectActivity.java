package com.juntai.wisdom.im.mine.myCollect;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.im.chatlist.searchchat.BaseSearchActivity;

/**
 * @aouther tobato
 * @description 描述  搜索收藏的内容
 * @date 2022-03-02 15:46
 */
public class SearchCollectActivity extends BaseSearchActivity {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new MyCollectAdapter(null);
    }

    @Override
    protected int getSearchType() {
        return 6;
    }
}
