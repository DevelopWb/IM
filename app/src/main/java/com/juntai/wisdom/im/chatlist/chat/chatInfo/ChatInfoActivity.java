package com.juntai.wisdom.im.chatlist.chat.chatInfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.chatlist.searchchat.SearchPrivateMsgRecordActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  聊天信息
 * @date 2021-12-10 15:19
 */
public class ChatInfoActivity extends BaseRecyclerviewActivity<ChatPresent> implements MainContract.IBaseView, ChatInfoAdapter.OnChatInfoCallBack {

    private ContactBean contactBean;
    public final static  int  CHAT_INFO_REQUEST = 10092;


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

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new ChatInfoAdapter(null);
    }

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            contactBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        }
        setTitleName("聊天信息");

        mPresenter.getUserInfo(getBaseBuilder().add("toUserId", String.valueOf(contactBean.getId())).build(), AppHttpPath.GET_USER_INFO);

        ((ChatInfoAdapter) baseQuickAdapter).setOnChatInfoCallBack(this);

        ((ChatInfoAdapter) baseQuickAdapter).setJoinGroupType(0);
        
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem  multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_KEY_VALUE:
                        TextKeyValueBean keyValueBean = (TextKeyValueBean) multipleItem.getObject();
                        switch (keyValueBean.getKey()) {
                            case MultipleItem.SEARCH_CHAT_RECORD:
                                // : 2022-01-25    搜索聊天记录
                               startActivity(new Intent(mContext, SearchPrivateMsgRecordActivity.class).putExtra(BASE_PARCELABLE,contactBean));
                                break;
                            case MultipleItem.DELETE_CHAT_RECORD:
                                // : 2021-12-10  删除聊天记录
                                showAlertDialog("确定删除聊天记录吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // : 2022-01-18 删除聊天信息
                                        mPresenter.deletePrivateChatRecord(contactBean.getId());
                                        ToastUtils.toast(mContext,"已全部删除");
                                    }
                                });

                                break;
                            default:
                                break;
                        }
                        
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public List<MultipleItem> getMenuBeans(ContactBean contactBean) {
        List<MultipleItem> menuBeans = new ArrayList<>();
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_RECYCLE, getRecyclerviewData()));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH, new TextKeyValueBean(MultipleItem.CHAT_IN_TOP, null, 0==contactBean.getIsTop()?false:true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.SEARCH_CHAT_RECORD,null)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.DELETE_CHAT_RECORD,null)));
        return menuBeans;
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    public List<PeopleBean> getRecyclerviewData() {
        List<PeopleBean> peopleBeans = new ArrayList<>();
        peopleBeans.add(new PeopleBean(contactBean.getRemarksNickname(), contactBean.getHeadPortrait(), contactBean.getId(),0));
        peopleBeans.add(new PeopleBean(null, null, 0,1));
        return peopleBeans;
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.GET_USER_INFO:
                UserBean userBean = (UserBean) o;
                contactBean = userBean.getData();
                if (contactBean != null) {
                    baseQuickAdapter.setNewData(getMenuBeans(contactBean));

                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(TextKeyValueBean textKeyValueBean, CompoundButton buttonView, boolean isChecked) {
        // isTop;//是否聊天置顶（0：不置顶；1：置顶）
        if (!isChecked) {
            //取消置顶
            mPresenter.topChat(getBaseBuilder()
                    .add("isType","0")
                    .add("toUserId", String.valueOf(contactBean.getId())).build(), AppHttpPath.TOP_CHAT);
        } else {
            //置顶
            mPresenter.topChat(getBaseBuilder()
                    .add("isType","1")
                    .add("toUserId", String.valueOf(contactBean.getId())).build(), AppHttpPath.TOP_CHAT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ChatInfoAdapter) baseQuickAdapter).setOnChatInfoCallBack(null);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(BASE_PARCELABLE, contactBean);
        setResult(CHAT_INFO_REQUEST, intent);
        finish();
    }
}
