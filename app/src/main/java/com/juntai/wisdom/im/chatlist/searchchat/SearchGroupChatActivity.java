package com.juntai.wisdom.im.chatlist.searchchat;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MessageBodyBean_;
import com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  查询群聊天记录
 * @date 2022-01-25 14:23
 */
public class SearchGroupChatActivity extends BaseSearchActivity {

    @Override
    protected boolean commitSearch(String s) {
        // : 2022-01-25 群聊
        List<MessageBodyBean> groupMsges = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupBean.getGroupId())
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.content.equal(s))
                        )).build().find();
        adapter.setNewData(groupMsges);
        return false;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchChatAdapter(R.layout.item_chat_list);
    }

    @Override
    protected int getSearchType() {
        return 2;
    }

    @Override
    protected void onAdapterItemClick(BaseQuickAdapter adapter, int position) {
        // : 2022-01-25 跳转到群聊界面
        MessageBodyBean groupMsgBean = (MessageBodyBean) adapter.getItem(position);
        startActivity(new Intent(mContext, GroupChatActivity.class).putExtra(BaseActivity.BASE_ID, groupBean.getGroupId()).putExtra(BASE_ID2, groupMsgBean.getId()));

    }
}
