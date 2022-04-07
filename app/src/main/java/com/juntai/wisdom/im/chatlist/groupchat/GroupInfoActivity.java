package com.juntai.wisdom.im.chatlist.groupchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.GroupDetailBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.bean.UserInfoVoListBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.chatlist.chat.chatInfo.ChatInfoAdapter;
import com.juntai.wisdom.im.chatlist.searchchat.SearchGroupChatActivity;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.mine.myinfo.BaseModifyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  聊天信息
 * @date 2021-12-10 15:19
 */
public class GroupInfoActivity extends BaseRecyclerviewActivity<ChatPresent> implements MainContract.IBaseView, ChatInfoAdapter.OnChatInfoCallBack {

    private GroupDetailInfoBean groupBean;
    private GroupDetailInfoBean dataBean;
    public final static int GROUP_INFO_REQUEST = 10093;


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
        super.initData();
        if (getIntent() != null) {
            groupBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        }

        ((ChatInfoAdapter) baseQuickAdapter).setOnChatInfoCallBack(this);
        ((ChatInfoAdapter) baseQuickAdapter).setJoinGroupType(1);

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_KEY_VALUE:
                        TextKeyValueBean keyValueBean = (TextKeyValueBean) multipleItem.getObject();
                        switch (keyValueBean.getKey()) {
                            case MultipleItem.GROUP_CHAT_NAME:
                                // : 2021-12-10  修改群聊名称
                                startActivityForResult(new Intent(mContext, ModifyGroupNameActivity.class)
                                        .putExtra(BASE_ID, groupBean.getGroupId())
                                        .putExtra(BaseModifyActivity.DEFAULT_HINT, keyValueBean.getValue()), BASE_REQUEST_RESULT);
                                break;
                            case MultipleItem.GROUP_QRCODE:
                                // : 2021-12-10  群二维码
                                startActivity(new Intent(mContext, GroupQrcodeActivity.class).putExtra(BASE_PARCELABLE, groupBean));

                                break;
                            case MultipleItem.NICK_NAME_IN_GROUP:
                                // : 2021-12-10  我在群里的昵称
                                startActivityForResult(new Intent(mContext, ModifyNickNameOfGroupActivity.class)
                                        .putExtra(BASE_STRING, dataBean.getUuid())
                                        .putExtra(BaseModifyActivity.DEFAULT_HINT, keyValueBean.getValue()), BASE_REQUEST_RESULT);
                                break;
                            case MultipleItem.SEARCH_CHAT_RECORD:
                                // : 2022-01-25   搜索聊天记录
                                startActivity(new Intent(mContext, SearchGroupChatActivity.class)
                                        .putExtra(BASE_PARCELABLE, groupBean));
                                break;

                            default:
                                break;
                        }

                        break;

                    case MultipleItem.ITEM_PIC_TEXT_MENUS:
                        TextKeyValueBean picTextBean = (TextKeyValueBean) multipleItem.getObject();
                        switch (picTextBean.getKey()) {
                            case MultipleItem.DELETE_CHAT_RECORD:
                                // : 2021-12-10  清空聊天记录

                                showAlertDialog("确定删除群的聊天记录吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // : 2022-01-18 删除群聊信息
                                       mPresenter.deleteGroupInfo(dataBean.getGroupId());
                                        ToastUtils.toast(mContext, "已全部删除");
                                    }
                                });


                                break;
                            case MultipleItem.DELETE_QUIT_GROUP:
                                // : 2021-12-10  离开群聊
                                showAlertDialog("离开群聊?", "确定", "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // : 2022-01-18 离开群聊
                                        List<Integer> ids = new ArrayList<>();
                                        ids.add(UserInfoManager.getUserId());
                                        if (UserInfoManager.getUserId() == dataBean.getGroupCreateUserId()) {
                                            //群主退出群聊
                                            mPresenter.ownerQuitGroup(dataBean.getUuid(), UserInfoManager.getUserId(), AppHttpPath.QUIT_GROUP);
                                        } else {
                                            mPresenter.quitGroup(dataBean.getUuid(), ids, AppHttpPath.QUIT_GROUP);
                                        }
                                        mPresenter.deleteGroupInfo(dataBean.getGroupId());


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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getGroupInfo(getBaseBuilder().add("groupId", String.valueOf(groupBean.getGroupId())).build(), AppHttpPath.GET_GROUP_INFO);

    }

    public List<MultipleItem> getMenuBeans(GroupDetailInfoBean dataBean) {
        List<UserInfoVoListBean> peoples = dataBean.getUserInfoVoList();
        List<PeopleBean> peopleBeans = new ArrayList<>();
        if (peoples != null) {
            setTitleName(String.format("群聊信息(%s)", peoples.size()));
            HashMap<Integer, String> remarkNameMap = HawkProperty.getGlobleMap();
            for (UserInfoVoListBean people : peoples) {
                if (remarkNameMap.containsKey(people.getId())) {
                    peopleBeans.add(new PeopleBean(remarkNameMap.get(people.getId()), people.getHeadPortrait(), people.getId(), 0));

                } else {
                    peopleBeans.add(new PeopleBean(people.getNickname(), people.getHeadPortrait(), people.getId(), 0));

                }
            }
            peopleBeans.add(new PeopleBean(null, null, 0, 1));
            if (UserInfoManager.getUserId() == dataBean.getGroupCreateUserId()) {
                peopleBeans.add(new PeopleBean(null, null, 0, 2));
            }
        }

        List<MultipleItem> menuBeans = new ArrayList<>();
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_RECYCLE, peopleBeans));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.GROUP_CHAT_NAME, dataBean.getGroupName())));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.GROUP_QRCODE, null)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.SEARCH_CHAT_RECORD, null)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH, new TextKeyValueBean(MultipleItem.CHAT_IN_TOP, null, 0 == dataBean.getIsTop() ? false : true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.NICK_NAME_IN_GROUP, dataBean.getUserNickname())));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_PIC_TEXT_MENUS, new TextKeyValueBean(MultipleItem.DELETE_CHAT_RECORD, null)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_PIC_TEXT_MENUS, new TextKeyValueBean(MultipleItem.DELETE_QUIT_GROUP, null)));


        return menuBeans;
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);

        switch (tag) {
            case AppHttpPath.GET_GROUP_INFO:
                GroupDetailBean groupPeoplesBean = (GroupDetailBean) o;
                groupBean =groupPeoplesBean.getData();
                if (groupPeoplesBean != null) {
                    dataBean = groupPeoplesBean.getData();
                    if (dataBean != null) {
                        baseQuickAdapter.setNewData(getMenuBeans(dataBean));
                        ((ChatInfoAdapter) baseQuickAdapter).setGroupUuid(dataBean.getUuid());

                    }


                }
                break;
            case AppHttpPath.QUIT_GROUP:
                ToastUtils.toast(mContext, "已离开群聊");
                finish();
                startActivity(new Intent(mContext, MainActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(TextKeyValueBean textKeyValueBean, CompoundButton buttonView, boolean isChecked) {
        // isTop;//是否聊天置顶（0：不置顶；1：置顶）
        if (!isChecked) {
            //取消置顶群聊
            mPresenter.topGroupChat(getBaseBuilder()
                    .add("isType", "0")
                    .add("groupUuid", dataBean.getUuid()).build(), AppHttpPath.TOP_GROUP_CHAT);
        } else {
            //置顶群聊
            mPresenter.topGroupChat(getBaseBuilder()
                    .add("isType", "1")
                    .add("groupUuid", dataBean.getUuid()).build(), AppHttpPath.TOP_GROUP_CHAT);
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
        intent.putExtra(BASE_ID, groupBean.getGroupId());
        setResult(GROUP_INFO_REQUEST, intent);
        finish();
    }
}
