package com.juntai.wisdom.im.chatlist;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.base.BaseRecyclerviewFragment;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.HomePageMenuBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.UnReadMsgsBean;
import com.juntai.wisdom.im.chatlist.chat.PrivateChatActivity;
import com.juntai.wisdom.im.chatlist.chat.videocall.VideoRequestActivity;
import com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @aouther tobato
 * @description 描述  homepage
 * @date 2021/4/18 14:59
 */
public class ChatListFragment extends BaseRecyclerviewFragment<MainPresent> implements MainContract.IBaseView,
        View.OnClickListener {


    private List<MultipleItem> arrays;
    private PopupWindow popupWindow;

    @Override
    protected void initView() {
        super.initView();
        mSmartrefreshlayout.setEnableLoadMore(false);
        mSmartrefreshlayout.setEnableRefresh(false);

    }

    @Override
    protected void freshlayoutOnLoadMore() {

    }

    @Override
    protected void freshlayoutOnRefresh() {

    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new HomepageChatListAdapter(null);
    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_CHAT_LIST_CONTACT:
                        ContactBean contactBean = (ContactBean) multipleItem.getObject();
                        MessageBodyBean messageBodyBean = contactBean.getMessageBodyBean();
                        messageBodyBean.setRead(true);
                        adapter.notifyItemChanged(position);
                        startActivityForResult(new Intent(mContext, PrivateChatActivity.class).putExtra(BaseActivity.BASE_PARCELABLE, contactBean), BaseActivity.BASE_REQUEST_RESULT);
                        break;

                    case MultipleItem.ITEM_CHAT_LIST_GROUP:
                        GroupDetailInfoBean dataBean = (GroupDetailInfoBean) multipleItem.getObject();
                        startActivity(new Intent(mContext, GroupChatActivity.class)
                                .putExtra(BaseActivity.BASE_ID, dataBean.getGroupId()));
                        break;
                    default:
                        break;
                }


            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

//                // : 2021-11-24  仿微信 弹出pop窗口
//                View popView = LayoutInflater.from(mContext).inflate(R.layout.home_pop, null);
//                popupWindow = new PopupWindow(popView, DisplayUtil.dp2px(mContext, 100), ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setOutsideTouchable(true);
//                RecyclerView recyclerView = popView.findViewById(R.id.home_pop_rv);
//                LinearLayout linearLayout = popView.findViewById(R.id.pop_bg_ll);
//                linearLayout.setBackgroundResource(R.drawable.sp_filled_gray_lighter);
//                HomePageMenuAdapter menuAdapter = new HomePageMenuAdapter(R.layout.item_home_menu);
//                menuAdapter.setNewData(getEditChatListData());
//                getBaseActivity().initRecyclerview(recyclerView, menuAdapter, LinearLayoutManager.VERTICAL);
//                popupWindow.showAsDropDown(view, ScreenUtils.getInstance(mContext).getScreenWidth()/2-popView.getWidth()/2,-DisplayUtil.dp2px(mContext,10));
//                menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        HomePageMenuBean menuBean = (HomePageMenuBean) adapter.getData().get(position);
//                        if (popupWindow!=null&&popupWindow.isShowing()) {
//                            popupWindow.dismiss();
//                            popupWindow = null;
//                        }
//                        switch (menuBean.getName()) {
//                            case MainContract.DELETE_CURRENT_CHAT:
//                                ToastUtils.toast(mContext, "dfa");
//                                break;
//                        }
//                    }
//                });
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_CHAT_LIST_CONTACT:
                        ContactBean contactBean = (ContactBean) multipleItem.getObject();
                        getBaseActivity().showAlertDialog("删除后,将清空该聊天的消息记录", "确定", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // : 2022-01-18 删除群聊信息
                               mPresenter.deletePrivateChatRecord(contactBean.getId());
                                adapter.remove(position);
                                if (Hawk.contains(HawkProperty.getDraftKey(contactBean.getId(),true))) {
                                    Hawk.delete(HawkProperty.getDraftKey(contactBean.getId(),true));
                                }
                            }
                        });
                        break;

                    case MultipleItem.ITEM_CHAT_LIST_GROUP:
                        GroupDetailInfoBean dataBean = (GroupDetailInfoBean) multipleItem.getObject();
                        getBaseActivity().showAlertDialog("删除后,将清空该群聊的消息记录", "确定", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // : 2022-01-18 删除群聊信息
                                mPresenter.deleteGroupInfo(dataBean.getGroupId());
                                adapter.remove(position);
                                if (Hawk.contains(HawkProperty.getDraftKey(dataBean.getGroupId(),false))) {
                                    Hawk.delete(HawkProperty.getDraftKey(dataBean.getGroupId(),false));
                                }

                            }
                        });
                        break;
                    default:
                        break;
                }


                return true;
            }
        });

    }


    private List<HomePageMenuBean> getEditChatListData() {
        List<HomePageMenuBean> arrays = new ArrayList<>();
        arrays.add(new HomePageMenuBean(MainContract.DELETE_CURRENT_CHAT, 0, false));
        return arrays;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lazyLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }
    }

    /**
     * 首次进入 检索最后一条未读消息
     *
     * @param unReadMsgsBean
     */
    public void onMessageUnReadMsg(UnReadMsgsBean unReadMsgsBean) {
        List<MessageBodyBean> msgList = unReadMsgsBean.getMsgList();
        if (msgList != null && !msgList.isEmpty()) {
            for (MessageBodyBean messageBody : msgList) {
                //未读
                messageBody.setRead(false);
                messageBody.setCanDelete(true);
                Log.d(TAG, messageBody.getContent());
                ObjectBox.addMessage(messageBody);
                if (VideoRequestActivity.EVENT_CAMERA_REQUEST.equals(messageBody.getEvent())) {
                    Log.d(TAG, "MyWsManager-----onMessage---视频通话消息");
                    Intent intent =
                            new Intent(mContext, VideoRequestActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra(VideoRequestActivity.IS_SENDER, false)
                                    .putExtra(BaseActivity.BASE_PARCELABLE,
                                            messageBody);
                    mContext.startActivity(intent);
                }
            }
        }
        List<MessageBodyBean> groupMsgListBeanList = unReadMsgsBean.getGroupMsgList();
        // : 2021-11-26 群里未读消息逻辑处理
        if (groupMsgListBeanList != null && !groupMsgListBeanList.isEmpty()) {
            for (MessageBodyBean groupMsgBean : groupMsgListBeanList) {
                //未读
                groupMsgBean.setId(0);
                groupMsgBean.setRead(false);
                groupMsgBean.setCanDelete(true);
                Log.d(TAG, groupMsgBean.getContent());
                ObjectBox.addMessage(groupMsgBean);
            }

        }

        lazyLoad();
    }


    public void onMessageReceived(MessageBodyBean messageBody) {
        Log.d(TAG, "onMessageReceived 收到mainactivity转发的消息" + messageBody.getContent());

        //未读
        messageBody.setRead(false);
        //保存消息到本地
        ObjectBox.addMessage(messageBody);
        initAdapterData();
    }


    @Override
    protected void lazyLoad() {
        NotificationTool.SHOW_NOTIFICATION = false;
        Log.d(TAG, "lazyLoad");
        initAdapterData();
    }

    /**
     * 初始化adapter数据
     */
    public void initAdapterData() {
        List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
        List<GroupDetailInfoBean> groups = Hawk.get(HawkProperty.GROUP_LIST);

        arrays = new ArrayList<>();
        List<ArrayMap<String, MultipleItem>> arrayMapList = new ArrayList<>();
        List<ArrayMap<String, MultipleItem>> arrayTopMapList = new ArrayList<>();
        if (data != null) {
            for (ContactBean datum : data) {
                if (datum.getStatusType() == 1) {
                    //联系人里面的id 和message里面的userid一样
                    MessageBodyBean messageBodyBean = null;
                    if (Hawk.contains(HawkProperty.getDraftKey(datum.getId(),true))) {
                        messageBodyBean = Hawk.get(HawkProperty.getDraftKey(datum.getId(),true));
                    }else {
                        if (mPresenter != null) {
                            messageBodyBean = mPresenter.findPrivateChatRecordLastMessage(datum.getId());
                        }
                    }
                    if (messageBodyBean != null) {
                        ArrayMap<String, MultipleItem> hashMap = new ArrayMap<>();
                        //如果包含最后一次聊天信息  首页展示
                        datum.setMessageBodyBean(messageBodyBean);
                        hashMap.put(messageBodyBean.getCreateTime(), new MultipleItem(MultipleItem.ITEM_CHAT_LIST_CONTACT, datum));
                        if (1 == datum.getIsTop()) {
                            arrayTopMapList.add(hashMap);
                        } else {
                            arrayMapList.add(hashMap);
                        }
                    }

                }
            }

        }
        if (groups!=null&&groups.size()>0) {
            // : 2022/4/1 这么写是因为数据结构更改了
            if (groups.get(0) instanceof GroupDetailInfoBean) {
                for (GroupDetailInfoBean group : groups) {
                    ArrayMap<String, MultipleItem> hashMap = new ArrayMap<>();
                    MessageBodyBean messageBodyBean = null;
                    if (Hawk.contains(HawkProperty.getDraftKey(group.getGroupId(),false))) {
                        messageBodyBean = Hawk.get(HawkProperty.getDraftKey(group.getGroupId(),false));
                        if (mPresenter != null) {
                            MessageBodyBean bodyBean = mPresenter.findGroupChatRecordLastMessage(group.getGroupId());
                            messageBodyBean.setRead(bodyBean == null || bodyBean.isRead());
                        }

                    }else {
                        if (mPresenter != null) {
                            messageBodyBean = mPresenter.findGroupChatRecordLastMessage(group.getGroupId());
                        }
                    }
                    if (messageBodyBean != null) {
                        group.setLastMessage(messageBodyBean);
                        hashMap.put(messageBodyBean.getCreateTime(), new MultipleItem(MultipleItem.ITEM_CHAT_LIST_GROUP, group));
                        if (1 == group.getIsTop()) {
                            arrayTopMapList.add(hashMap);
                        } else {
                            arrayMapList.add(hashMap);
                        }
                    }

                }
            }
        }
        sortAdapterData(arrayTopMapList);
        sortAdapterData(arrayMapList);
        Log.d(TAG, String.valueOf(arrays.size()));
        if (adapter != null) {
            adapter.setNewData(arrays);

        }
    }

    /**
     * 将消息重新整理
     *
     * @param arrayMapList
     */
    private void sortAdapterData(List<ArrayMap<String, MultipleItem>> arrayMapList) {
        Collections.sort(arrayMapList, new HomePageNewsSortBean());
        for (ArrayMap<String, MultipleItem> arrayMap : arrayMapList) {
            for (Map.Entry<String, MultipleItem> stringMultipleItemEntry : arrayMap.entrySet()) {
                Map.Entry entry = stringMultipleItemEntry;
                arrays.add((MultipleItem) entry.getValue());
            }

        }
    }

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }


    @Override
    public void onSuccess(String tag, Object o) {
    }

    @Override
    public void onError(String tag, Object o) {
        ToastUtils.error(mContext, String.valueOf(o));
    }


    @Override
    public void onClick(View v) {
    }
}
