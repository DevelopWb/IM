package com.juntai.wisdom.im.chatlist.searchchat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.federation.R;
/**
 * @aouther tobato
 * @description 描述  查询群聊天记录
 * @date 2022-01-25 14:23
 */
public class SearchGroupChatActivity extends BaseSearchActivity {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchChatAdapter(R.layout.item_chat_list);
    }

    @Override
    protected int getSearchType() {
        return 2;
    }
}
