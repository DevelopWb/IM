package com.juntai.wisdom.im.contact.group;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chat_module.chat.ChatPresent;
import com.juntai.wisdom.im.chat_module.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.contact.ContactAdapter;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.ObjectBox;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  群聊的入口
 * @date 2022-01-13 11:48
 */
public class GroupListActivity extends BaseRecyclerviewActivity<ChatPresent> implements MainContract.IBaseView {

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public void initData() {
        super.initData();
        setTitleName("群聊");
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getItem(position);
                GroupDetailInfoBean dataBean = (GroupDetailInfoBean) multipleItem.getObject();
                // : 2022-01-13 跳转到群聊消息界面
                startActivity(new Intent(mContext, GroupChatActivity.class)
                        .putExtra(BASE_ID,dataBean.getGroupId()));
            }
        });
    }


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    public void initView() {
        super.initView();
        List<GroupDetailInfoBean> arrays = ObjectBox.getAllGroupList();
        for (GroupDetailInfoBean array : arrays) {
            array.setHasEndLine(true);
            baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_GROUP, array));
        }
    }

    @Override
    protected void getRvAdapterData() {

        mPresenter.getGroupList(getBaseBuilder().build(), AppHttpPath.GET_GROUP_LIST);
    }

    @Override
    protected boolean enableRefresh() {
        return true;
    }

    @Override
    protected boolean enableLoadMore() {
        return false;
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new ContactAdapter(null);
    }

    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        GroupListBean groupListBean = (GroupListBean) o;
        if (groupListBean != null) {
            List<GroupDetailInfoBean> arrays = groupListBean.getData();
            if (arrays != null) {
                ObjectBox.addGroup(arrays);
                baseQuickAdapter.setNewData(null);
                for (GroupDetailInfoBean array : arrays) {
                    array.setHasEndLine(true);
                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_GROUP, array));
                }
            }
        }


    }
}
