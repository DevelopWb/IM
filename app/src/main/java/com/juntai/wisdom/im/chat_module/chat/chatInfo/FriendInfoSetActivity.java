package com.juntai.wisdom.im.chat_module.chat.chatInfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ScreenUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.chat_module.chat.ChatPresent;
import com.juntai.wisdom.im.contact.ModifyRemarkNameActivity;
import com.juntai.wisdom.im.contact.SelectContactActivity;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.mine.myinfo.BaseModifyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.SendMsgUtil;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述   好友资料设置
 * @date 2021-12-11 10:49
 */
public class FriendInfoSetActivity extends BaseRecyclerviewActivity<ChatPresent> implements MainContract.IBaseView, ChatInfoAdapter.OnChatInfoCallBack {

    private ContactBean contactBean;
    private AlertDialog sendCardDialog;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public void initData() {
        super.initData();
        setTitleName("资料设置");
        if (getIntent() != null) {
            contactBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        }
        baseQuickAdapter.setNewData(getMenuBeans());

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem item = (MultipleItem) adapter.getData().get(position);
                switch (item.getItemType()) {
                    case MultipleItem.ITEM_KEY_VALUE:
                        TextKeyValueBean keyValueBean = (TextKeyValueBean) item.getObject();
                        switch (keyValueBean.getKey()) {
                            case MultipleItem.SET_REMARK:
                                startActivityForResult(new Intent(mContext, ModifyRemarkNameActivity.class)
                                        .putExtra(BaseModifyActivity.BASE_ID, contactBean.getId())
                                        .putExtra(BaseModifyActivity.DEFAULT_HINT, contactBean.getRemarksNickname()), BASE_REQUEST_RESULT);
                                break;

                            case MultipleItem.SHARE_TO_FRIENDS:
                                // : 2021-12-11 推荐给好友
                                startActivityForResult(new Intent(mContext, SelectContactActivity.class), SelectContactActivity.REQUEST_CONTACT);
                                break;
                            default:
                                break;
                        }

                        break;

                    case MultipleItem.ITEM_PIC_TEXT_MENUS:
                        TextKeyValueBean textPicKeyValueBean = (TextKeyValueBean) item.getObject();
                        switch (textPicKeyValueBean.getKey()) {
                            case MultipleItem.DELETE:
                                // : 2021-12-11 删除好友

                                showAlertDialog(String.format("将联系人\"%s\"删除,将同时删除与该联系人的聊天记录", contactBean.getRemarksNickname()), "确定", "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPresenter.deleteFriend(getBaseBuilder().add("toUserId", String.valueOf(contactBean.getId())).build(), AppHttpPath.DELETE_FRIEND);
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

        ((ChatInfoAdapter) baseQuickAdapter).setOnChatInfoCallBack(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case SelectContactActivity.REQUEST_CONTACT:
                if (data != null) {
                    ContactBean cardContactBean = data.getParcelableExtra(BASE_PARCELABLE);
                    View view = LayoutInflater.from(mContext).inflate(R.layout.share_card_layout, null);
                    sendCardDialog = new AlertDialog.Builder(mContext)
                            .setView(view)
                            .setCancelable(false)
                            .show();
                    sendCardDialog.getWindow().setBackgroundDrawableResource(R.drawable.sp_filled_white_10dp);
                    sendCardDialog.getWindow().setLayout(ScreenUtils.getInstance(mContext).getScreenWidth() - DisplayUtil.dp2px(mContext, 80), LinearLayout.LayoutParams.WRAP_CONTENT);
                    ImageView mReceiverHeadIv = (ImageView) view.findViewById(R.id.receiver_head_iv);
                    ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(cardContactBean.getHeadPortrait()), mReceiverHeadIv);

                    TextView mReceiverNameTv = (TextView) view.findViewById(R.id.receiver_name_tv);
                    mReceiverNameTv.setText(cardContactBean.getRemarksNickname());

                    TextView mCardNameTv = (TextView) view.findViewById(R.id.card_name_tv);
                    mCardNameTv.append(contactBean.getRemarksNickname());
                    EditText mToReceiverMsgEt = (EditText) view.findViewById(R.id.to_receiver_msg_et);
                    view.findViewById(R.id.close_dialog_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendCardDialog.dismiss();

                        }
                    });
                    view.findViewById(R.id.send_card_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 发送名片
                            sendCardDialog.dismiss();

                            // : 2021-12-07 发送名片
                            MessageBodyBean messageBody = SendMsgUtil.getPrivateMsg(7, cardContactBean.getId(), cardContactBean.getUuid(), cardContactBean.getRemarksNickname(), cardContactBean.getHeadPortrait(), "名片");
                            messageBody.setOtherUserId(contactBean.getId());
                            messageBody.setOtherAccount(contactBean.getAccountNumber());
                            messageBody.setOtherHead(contactBean.getHeadPortrait());
                            messageBody.setOtherNickname(contactBean.getNickname());
                            mPresenter.sendPrivateMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
                            ObjectBox.addMessage(messageBody);

                            if (!TextUtils.isEmpty(getTextViewValue(mToReceiverMsgEt))) {
                                //单独发一条信息
                                sendNormalMsg(getTextViewValue(mToReceiverMsgEt), cardContactBean);
                            }
                            ToastUtils.toast(mContext, "已发送");

                        }

                    });
                }

                break;
            default:
                break;
        }
    }

    /**
     * 发送普通信息
     */
    private void sendNormalMsg(String content, ContactBean contactBean) {
        MessageBodyBean messageBody = SendMsgUtil.getPrivateMsg(0, contactBean.getId(), contactBean.getUuid(), contactBean.getRemarksNickname(), contactBean.getHeadPortrait(), content);
        mPresenter.sendPrivateMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        ObjectBox.addMessage(messageBody);
    }

    public List<MultipleItem> getMenuBeans() {
        List<MultipleItem> menuBeans = new ArrayList<>();
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.SET_REMARK, null)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        if (contactBean.isFriend()) {
            menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE, new TextKeyValueBean(MultipleItem.SHARE_TO_FRIENDS, null)));
        }
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH, new TextKeyValueBean(MultipleItem.ADD_TO_BLACK_LIST, null, HawkProperty.isInBlockList(contactBean.getId()) ? true : false)));
        if (contactBean.isFriend()) {
            menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
            menuBeans.add(new MultipleItem(MultipleItem.ITEM_PIC_TEXT_MENUS, new TextKeyValueBean(MultipleItem.DELETE, null)));
        }

        return menuBeans;
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

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new ChatInfoAdapter(null);
    }


    @Override
    public void onSuccess(String tag, Object o) {

        switch (tag) {
            case AppHttpPath.DELETE_FRIEND:
                //删除好友  需要删除联系人相关的信息
                //删除联系人
                List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
                Iterator iterator = data.iterator();
                while (iterator.hasNext()) {
                    ContactBean bean = (ContactBean) iterator.next();
                    if (contactBean.getId() == bean.getId()) {
                        iterator.remove();
                    }
                }
                Hawk.put(HawkProperty.getContactListKey(), data);
                //所有的聊天记录
                //获取历史数据
                mPresenter.deletePrivateChatRecord(contactBean.getId());
                //跳转到首页
                startActivity(new Intent(mContext, MainActivity.class));

                break;

            case AppHttpPath.BLOCK_FRIEND:
                // : 2021-12-13  加入黑名单
                List<ContactBean> contactBeans = Hawk.get(HawkProperty.getContactListKey());
                Iterator beanIterator = contactBeans.iterator();
                while (beanIterator.hasNext()) {
                    ContactBean bean = (ContactBean) beanIterator.next();
                    if (contactBean.getId() == bean.getId()) {
                        beanIterator.remove();
                    }
                }
                Hawk.put(HawkProperty.getContactListKey(), contactBeans);
                ToastUtils.toast(mContext, "已成功加入黑名单");
                break;

            case MainContract.REMOVE_BLOCK_LIST:
                //移除黑名单
                ToastUtils.toast(mContext, "移除成功");
                break;
            default:
                break;
        }


    }

    @Override
    public void onCheckedChanged(TextKeyValueBean textKeyValueBean, CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            mPresenter.blockFriend(getBaseBuilder()
                    .add("statusType", "1")
                    .add("toUserId", String.valueOf(contactBean.getId())).build(), MainContract.REMOVE_BLOCK_LIST);
        } else {

            showAlertDialog("加入黑名单，你将不再收到对方的消息", "确定", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.blockFriend(getBaseBuilder()
                            .add("statusType", "2")
                            .add("toUserId", String.valueOf(contactBean.getId())).build(), AppHttpPath.BLOCK_FRIEND);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    buttonView.setChecked(false);
                }
            });
        }

    }
}
