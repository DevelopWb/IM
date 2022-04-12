package com.juntai.wisdom.im.chat_module.searchchat;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MessageBodyBean_;
import com.juntai.wisdom.im.chat_module.chat.PrivateChatActivity;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述
 * @date 2022-01-25 10:37
 */
public class SearchPrivateMsgRecordActivity extends BaseSearchActivity {

    @Override
    protected boolean commitSearch(String s) {
        List<MessageBodyBean> arrays = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(0)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactBean.getId(), UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactBean.getId(), UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.content.equal(s))
                        )).build().find();
        adapter.setNewData(arrays);
        return false;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchChatAdapter(R.layout.item_chat_list);
    }

    @Override
    protected int getSearchType() {
        return 1;
    }

    @Override
    protected void onAdapterItemClick(BaseQuickAdapter adapter, int position) {
        MessageBodyBean messageBodyBean = (MessageBodyBean) adapter.getItem(position);
        // : 2022-01-25 跳转到聊天界面
        startActivity(new Intent(mContext, PrivateChatActivity.class).putExtra(BaseActivity.BASE_PARCELABLE, contactBean).putExtra(BASE_ID2, messageBodyBean.getId()));

    }
}
