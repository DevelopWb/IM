package com.juntai.wisdom.im.chatlist.searchchat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.federation.R;

/**
 * @aouther tobato
 * @description 描述
 * @date 2022-01-25 10:37
 */
public class SearchPrivateMsgRecordActivity extends BaseSearchActivity {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchChatAdapter(R.layout.item_chat_list);
    }

    @Override
    protected int getSearchType() {
        return 1;
    }
}
