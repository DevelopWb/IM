package com.juntai.wisdom.im.contact.searchContact;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.chatlist.searchchat.BaseSearchActivity;

/**
 * @aouther tobato
 * @description 描述  搜索联系人
 * @date 2022-01-25 15:59
 */
public class SearchContactActivity extends BaseSearchActivity {
    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchContactAdapter(R.layout.item_select_contact);
    }

    @Override
    protected int getSearchType() {
        return 3;
    }
}
