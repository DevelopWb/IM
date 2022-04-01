package com.juntai.wisdom.im.search;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chatlist.chat.displayFile.FileDetailActivity;
import com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.chatlist.searchchat.BaseSearchActivity;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  首页搜索模块
 * @date 2022-01-27 15:05
 */
public class SearchActivity extends BaseSearchActivity {


    @Override
    protected boolean commitSearch(String s) {
        // TODO: 2022/4/1 这个地方需要加上聊天记录的搜索
        List<MultipleItem> contactAndGroups = mPresenter.getContactAndGroup(s);
        adapter.setNewData(contactAndGroups);
        // : 2022-03-02 查找对应的收藏数据  暂时不分页
        mPresenter.getAllCollection(getBaseBuilder().add("keyword", s).build(), AppHttpPath.ALL_COLLECTS);
        return false;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchAdapter(null);
    }

    @Override
    protected int getSearchType() {
        return 4;
    }

    @Override
    protected void onAdapterItemClick(BaseQuickAdapter adapter, int position) {
        MultipleItem multipleItem = (MultipleItem) adapter.getItem(position);
        switch (multipleItem.getItemType()) {
            case MultipleItem.ITEM_CONTACT:
                //联系人
                ContactBean contact = (ContactBean) multipleItem.getObject();
                startActivity(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, contact.getId()));
                break;
            case MultipleItem.ITEM_GROUP:
                //群聊
                GroupListBean.DataBean groupBean = (GroupListBean.DataBean) multipleItem.getObject();
                startActivity(new Intent(mContext, GroupChatActivity.class)
                        .putExtra(BASE_ID, groupBean.getGroupId()));
                break;
            case MultipleItem.ITEM_COLLECTION_PIC:
            case MultipleItem.ITEM_COLLECTION_VIDEO:
            case MultipleItem.ITEM_COLLECTION_AUDIO:
            case MultipleItem.ITEM_COLLECTION_FILE:
                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE, (MessageBodyBean) multipleItem.getObject()));

                break;
            default:
                break;
        }
    }
}
