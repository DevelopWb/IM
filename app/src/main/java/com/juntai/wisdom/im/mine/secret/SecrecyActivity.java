package com.juntai.wisdom.im.mine.secret;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.chatlist.chat.chatInfo.ChatInfoAdapter;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述   隐私
 * @date 2021-12-12 8:39
 */
public class SecrecyActivity extends BaseRecyclerviewActivity<MyCenterPresent> implements MyCenterContract.ICenterView,ChatInfoAdapter.OnChatInfoCallBack {

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public void initData() {
        super.initData();
        setTitleName("隐私");
        baseQuickAdapter.setNewData(getMenuBeans());

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem item = (MultipleItem) adapter.getData().get(position);
                switch (item.getItemType()) {
                    case MultipleItem.ITEM_KEY_VALUE:
                        //通讯录黑名单
                        startActivity(new Intent(mContext,BlockFriendsActivity.class));
                        break;

                }
            }
        });
        ((ChatInfoAdapter) baseQuickAdapter).setOnChatInfoCallBack(this);
    }


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    protected void getRvAdapterData() {

    }

    @Override
    protected boolean enableRefresh() {
        return false;
    }

    @Override
    protected boolean enableLoadMore() {
        return false;
    }

    public List<MultipleItem> getMenuBeans() {
        List<MultipleItem> menuBeans = new ArrayList<>();
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH, new TextKeyValueBean(MultipleItem.CHECK_WHEN_ADD, null, UserInfoManager.getUserVerifyFriend())));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH, new TextKeyValueBean(MultipleItem.RECOMMAND_CONTACT_FRIENDS, null, false)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.BLACK_LIST, null)));

        return menuBeans;
    }


    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new ChatInfoAdapter(null);
    }

    @Override
    public void onCheckedChanged(TextKeyValueBean textKeyValueBean,CompoundButton buttonView, boolean isChecked) {
        switch (textKeyValueBean.getKey()) {
            case MultipleItem.CHECK_WHEN_ADD:
                // : 2021-12-12 添加好友需要验证
                mPresenter.verifyAddFriend(getBaseBuilder().add("addFriendVerification",isChecked?"1":"0").build(), AppHttpPath.ADD_FRIEND_VERIFY);


                break;
            case MultipleItem.RECOMMAND_CONTACT_FRIENDS:
                // TODO: 2022-01-25   推荐通讯录好友
                ToastUtils.toast(mContext,"暂时还未接入");

                break;
            default:
                break;
        }
    }
}
