package com.juntai.wisdom.im.chatlist.searchchat;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.CollectMessagesBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MessageBodyBean_;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.chatlist.chat.PrivateChatActivity;
import com.juntai.wisdom.im.chatlist.chat.displayFile.FileDetailActivity;
import com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  搜索聊天记录
 * @date 2022-01-25 9:32
 */
public abstract class BaseSearchActivity extends BaseAppActivity<ChatPresent> implements MainContract.IBaseView, View.OnClickListener {

    private SearchView mSearchContentSv;
    /**
     * 取消
     */
    private TextView mBackTv;
    private RecyclerView mRecyclerview;
    private ContactBean contactBean;
    private BaseQuickAdapter adapter;
    private GroupListBean.DataBean groupBean;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_search_chat_record;
    }

    @Override
    public void initView() {

        initToolbarAndStatusBar(false);
        mImmersionBar.statusBarDarkFont(true).init();
        mSearchContentSv = (SearchView) findViewById(R.id.search_content_sv);
        mBackTv = (TextView) findViewById(R.id.cancel_tv);
        mBackTv.setOnClickListener(this);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = getAdapter();
        initRecyclerview(mRecyclerview, adapter, LinearLayoutManager.VERTICAL);
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) mSearchContentSv.findViewById(R.id.search_src_text);
        textView.setTextSize(14);
        mSearchContentSv.requestFocus();
        mSearchContentSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (commitSearch(s)) return false;
                getViewFocus(mRecyclerview);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (commitSearch(s)) return false;
                return false;
            }
        });


    }

    private boolean commitSearch(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        switch (getSearchType()) {
            case 1:
                List<MessageBodyBean> arrays = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                        MessageBodyBean_.groupId.equal(0)
                                .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                        .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactBean.getId(), UserInfoManager.getUserId()}))
                                        .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactBean.getId(), UserInfoManager.getUserId()}))
                                        .and(MessageBodyBean_.content.equal(s))
                                )).build().find();
                adapter.setNewData(arrays);
                break;
            case 2:
                // : 2022-01-25 群聊
                List<MessageBodyBean> groupMsges = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                        MessageBodyBean_.groupId.equal(groupBean.getGroupId())
                                .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                        .and(MessageBodyBean_.content.equal(s))
                                )).build().find();
                adapter.setNewData(groupMsges);
                break;
            case 3:
                // : 2022-01-25 搜索本地联系人
                List<ContactBean> searchedContacts = new ArrayList<>();
                List<ContactBean> contactBeans = getIntent().getParcelableArrayListExtra(BASE_PARCELABLE);
                for (ContactBean bean : contactBeans) {
                    if (bean.getRemarksNickname().contains(s)) {
                        searchedContacts.add(bean);
                    }
                }
                adapter.setNewData(searchedContacts);
                break;

            case 4:
                List<MultipleItem> contactAndGroups = getContactAndGroup(s);
                adapter.setNewData(contactAndGroups);
                // : 2022-03-02 查找对应的收藏数据  暂时不分页
                mPresenter.getAllCollection(getBaseBuilder().add("keyword", s).build(), AppHttpPath.ALL_COLLECTS);
                break;
            case 5:

                //通讯录相关内容
                List<MultipleItem> data = getContactAndGroup(s);

                // TODO: 2022-01-27  聊天记录相关内容
                if (5 == getSearchType()) {

                }
                adapter.setNewData(data);

                break;
            case 6:
                // : 2022-03-02 查找对应的收藏数据  暂时不分页
                mPresenter.getAllCollection(getBaseBuilder().add("keyword", s).build(), AppHttpPath.ALL_COLLECTS);


                break;
            default:
                break;
        }

        return false;
    }

    private List<MultipleItem> getContactAndGroup(String s) {
        List<MultipleItem> data = new ArrayList<>();
        int position = 0;
        List<ContactBean> localContacts = Hawk.get(HawkProperty.getContactListKey());
        for (ContactBean bean : localContacts) {
            if (bean.getRemarksNickname().contains(s)) {
                data.add(new MultipleItem(MultipleItem.ITEM_CONTACT, bean));
            }
        }
        if (data.size() > 0) {
            data.add(0, new MultipleItem(MultipleItem.ITEM_TITLE, "通讯录"));
        }
        position = data.size();
        // : 2022-02-12 群聊
        List<GroupListBean.DataBean> groups = Hawk.get(HawkProperty.GROUP_LIST);
        for (GroupListBean.DataBean group : groups) {
            if (group.getGroupName().contains(s)) {
                data.add(new MultipleItem(MultipleItem.ITEM_GROUP, group));
            }

        }
        if (data.size() > position) {
            data.add(position, new MultipleItem(MultipleItem.ITEM_TITLE, "群聊"));
        }
        return data;
    }

    protected abstract BaseQuickAdapter getAdapter();

    /**
     * chatType  聊天类型（1：搜索私聊信息；2搜索公聊信息； 3搜索联系人  4首页搜索  搜索各种类型数据  5消息转发的时候跳转的界面  里面包含群组和通讯录  6 收藏的内容
     *
     * @return
     */
    protected abstract int getSearchType();

    @Override
    public void initData() {
        switch (getSearchType()) {
            case 1:
                if (getIntent() != null) {
                    contactBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
                }
                break;
            case 2:
                // : 2022-01-25 群聊记录相关逻辑
                if (getIntent() != null) {
                    groupBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
                }
                break;
            case 3:
                break;
            default:
                break;
        }
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (getSearchType()) {
                    case 1:
                        MessageBodyBean messageBodyBean = (MessageBodyBean) adapter.getItem(position);
                        // : 2022-01-25 跳转到聊天界面
                        startActivity(new Intent(mContext, PrivateChatActivity.class).putExtra(BaseActivity.BASE_PARCELABLE, contactBean).putExtra(BASE_ID2, messageBodyBean.getId()));

                        break;
                    case 2:
                        // : 2022-01-25 跳转到群聊界面
                        MessageBodyBean groupMsgBean = (MessageBodyBean) adapter.getItem(position);
                        startActivity(new Intent(mContext, GroupChatActivity.class).putExtra(BaseActivity.BASE_ID, groupBean.getGroupId()).putExtra(BASE_ID2, groupMsgBean.getId()));

                        break;
                    case 3:
                        ContactBean contactBean = (ContactBean) adapter.getItem(position);
                        if (0 == contactBean.getSelected()) {
                            setResult(BASE_REQUEST_RESULT, new Intent().putExtra(BASE_PARCELABLE, contactBean));
                            finish();
                        } else {
                            ToastUtils.toast(mContext, "该成员已经存在");
                        }
                        break;
                    case 4:

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
                        break;
                    case 5:

                        MultipleItem multipleItem1 = (MultipleItem) adapter.getItem(position);
                        switch (multipleItem1.getItemType()) {
                            case MultipleItem.ITEM_CONTACT:
                                //联系人
                                ContactBean contactBean1 = (ContactBean) multipleItem1.getObject();
                                setResult(BASE_REQUEST_RESULT, new Intent().putExtra(BASE_PARCELABLE, contactBean1));
                                finish();
                                break;
                            case MultipleItem.ITEM_GROUP:
                                //群聊
                                GroupListBean.DataBean groupBean = (GroupListBean.DataBean) multipleItem1.getObject();
                                setResult(BASE_REQUEST_RESULT2, new Intent().putExtra(BASE_PARCELABLE, groupBean));
                                finish();
                                break;
                            default:
                                break;
                        }
                        break;
                    case 6:

                        MultipleItem multipleItem6 = (MultipleItem) adapter.getItem(position);
                        switch (multipleItem6.getItemType()) {
                            case MultipleItem.ITEM_COLLECTION_PIC:
                            case MultipleItem.ITEM_COLLECTION_VIDEO:
                            case MultipleItem.ITEM_COLLECTION_AUDIO:
                            case MultipleItem.ITEM_COLLECTION_FILE:
                                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE, (MessageBodyBean) multipleItem6.getObject()));
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
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.ALL_COLLECTS:
                CollectMessagesBean collectMessagesBean = (CollectMessagesBean) o;
                CollectMessagesBean.DataBean dataBean = collectMessagesBean.getData();
                if (dataBean != null) {
                    List<MessageBodyBean> messageBodyBeans = dataBean.getList();
                    List<MultipleItem> data = new ArrayList<>();
                    if (messageBodyBeans != null) {
                        if (4 == getSearchType()) {
                            if (messageBodyBeans.size() > 0) {
                                data.add(new MultipleItem(MultipleItem.ITEM_TITLE, "我的收藏"));
                            }
                        }
                        for (MessageBodyBean messageBodyBean : messageBodyBeans) {
                            //收藏消息类型 普通文本  图片 视频  音频  8文件 6位置
                            switch (messageBodyBean.getMsgType()) {
                                case 0:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_TEXT, messageBodyBean));
                                    break;
                                case 1:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_PIC, messageBodyBean));
                                    break;
                                case 2:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_VIDEO, messageBodyBean));
                                    break;
                                case 3:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_AUDIO, messageBodyBean));
                                    break;
                                case 6:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_LOCATE, messageBodyBean));
                                    break;
                                case 8:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_FILE, messageBodyBean));
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (4 == getSearchType()) {
                            adapter.addData(data);
                        } else {
                            adapter.setNewData(data);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.cancel_tv:
                finish();
                break;
        }
    }

}
