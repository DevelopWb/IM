package com.juntai.wisdom.im.chat_module.chat.forwardMsg;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ScreenUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chat_module.chat.ChatPresent;
import com.juntai.wisdom.im.chat_module.chat.chatInfo.ChatInfoActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.SendMsgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  转发消息 外部分享 outside
 * @date 2022-02-12 14:20
 */
public class ForwardMsgActivity extends BaseAppActivity<ChatPresent> implements MainContract.IBaseView, View.OnClickListener {

    /**
     * 搜索联系人
     */
    private TextView mSearchTv;
    private SelectGroupAndContactFragment fragment;
    private MessageBodyBean operatingMsgBean;
    /**
     * 0代表发送一条信息  1代表发送多条信息 逐条转发  2代表发送多条信息 合并转发
     */
    private int forwardType = 0;
    private AlertDialog forwardMsgDialog;

    @Override
    public int getLayoutView() {
        return R.layout.forward_msg_activity;
    }


    @Override
    public void initView() {

        if (getIntent() != null) {
            operatingMsgBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
            forwardType = getIntent().getIntExtra(BASE_ID, 0);
        }
        setTitleName("选择要转发的对象");
        getTitleRightTv().setText("发送");
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // : 2022-02-12 发送消息体
                List<MultipleItem> multipleItems = fragment.getAllSelectedItems();
                if (multipleItems.isEmpty()) {
                    ToastUtils.toast(mContext, "请选择要转发的对象");
                    return;
                }
                // : 2022-02-12 弹出发送消息的弹窗
                View view = LayoutInflater.from(mContext).inflate(R.layout.forward_msg_dialog_layout, null);
                forwardMsgDialog = new AlertDialog.Builder(mContext)
                        .setView(view)
                        .setCancelable(false)
                        .show();
                forwardMsgDialog.getWindow().setBackgroundDrawableResource(R.drawable.sp_filled_white_10dp);
                forwardMsgDialog.getWindow().setLayout(ScreenUtils.getInstance(mContext).getScreenWidth() - DisplayUtil.dp2px(mContext, 80), LinearLayout.LayoutParams.WRAP_CONTENT);
                RecyclerView forwardRv = view.findViewById(R.id.forward_msg_rv);
                ReceiverAdapter receiverAdapter = new ReceiverAdapter(R.layout.receiver_item);
                GridLayoutManager manager = null;
                if (multipleItems.size() > 1) {
                    manager = new GridLayoutManager(mContext, 5);
                } else {
                    manager = new GridLayoutManager(mContext, 1);
                }
                forwardRv.setAdapter(receiverAdapter);
                forwardRv.setLayoutManager(manager);
                receiverAdapter.setNewData(multipleItems);
                EditText mToReceiverMsgEt = view.findViewById(R.id.to_receiver_msg_et);

                ((TextView) view.findViewById(R.id.forward_title_tv)).setText(multipleItems.size() == 1 ? "发送给" : "分别发送给");
                if (0 == forwardType) {
                    switch (operatingMsgBean.getMsgType()) {
                        case 1:
                        case 2:
                            ImageView  msgIv = (ImageView) view.findViewById(R.id.msg_iv);
                            ((TextView) view.findViewById(R.id.card_name_tv)).setVisibility(View.GONE);
                            msgIv.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams  params = (LinearLayout.LayoutParams) msgIv.getLayoutParams();
                            if ("0".equals(operatingMsgBean.getRotation()) || "180".equals(operatingMsgBean.getRotation())) {
                                params.width = (view.getWidth() / 2);
                            } else {
                                params.width = (DisplayUtil.dp2px(mContext,80));
                            }
                            if (1==operatingMsgBean.getMsgType()) {
                                ImageLoadUtil.loadSquareImage(mContext,operatingMsgBean.getContent(),((ImageView) view.findViewById(R.id.msg_iv)));
                            }else {
                                ImageLoadUtil.loadVideoScreenshot(mContext, operatingMsgBean.getContent(),(ImageView) view.findViewById(R.id.msg_iv), new ImageLoadUtil.OnImageLoadSuccess() {
                                    @Override
                                    public void loadSuccess(int width, int height) {

                                    }
                                });
                            }
                            break;
                        default:
                            ((TextView) view.findViewById(R.id.card_name_tv)).setVisibility(View.VISIBLE);
                            ((TextView) view.findViewById(R.id.card_name_tv)).setText(operatingMsgBean.getContent());
                            break;
                    }

                } else if (1 == forwardType) {
                    ((TextView) view.findViewById(R.id.card_name_tv)).setText(String.format("[逐条转发]共%s条消息", SendMsgUtil.selectedToEditMsgItems.size()));
                } else {
                    ((TextView) view.findViewById(R.id.card_name_tv)).setText(String.format("[合并转发]共%s条消息", SendMsgUtil.selectedToEditMsgItems.size()));
                }

                view.findViewById(R.id.close_dialog_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forwardMsgDialog.dismiss();

                    }
                });
                view.findViewById(R.id.send_card_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // : 2022-02-12   发送消息
                        forwardMsges(mToReceiverMsgEt, fragment.getAllSelectedItems());

                    }
                });

            }
        });
        mSearchTv = (TextView) findViewById(R.id.search_tv);
        mSearchTv.setOnClickListener(this);
        fragment = (SelectGroupAndContactFragment) getSupportFragmentManager().findFragmentById(R.id.select_contact_fg);
    }

    /**
     * // : 2022-02-12 转发消息
     *
     * @param multipleItems
     */
    public void forwardMsges(EditText mToReceiverMsgEt, List<MultipleItem> multipleItems) {

        for (MultipleItem multipleItem : multipleItems) {
            switch (multipleItem.getItemType()) {
                case MultipleItem.ITEM_SELECT_CONTACT:
                    ContactBean contactBean = (ContactBean) multipleItem.getObject();
                    switch (forwardType) {
                        case 0:
                            sendPrivateMsg(contactBean, operatingMsgBean);
                            break;
                        case 1:
                            if (SendMsgUtil.selectedToEditMsgItems != null && !SendMsgUtil.selectedToEditMsgItems.isEmpty()) {
                                for (MultipleItem selectedMsge : SendMsgUtil.selectedToEditMsgItems) {
                                    // : 2022-02-13 所有类型的消息(语音，语音通话，视频通话不能转发)
                                    //0：text；1：image；2：video；3：语音；4视频通话；5音频通话，6位置消息，7分享名片，8文件
                                    MessageBodyBean messageBodyBean = (MessageBodyBean) selectedMsge.getObject();
                                    switch (messageBodyBean.getMsgType()) {
                                        case 0:
                                            // : 2022-02-13 普通文本消息
                                        case 1:
                                            // : 2022-02-13 普通图片消息
                                        case 2:
                                            // : 2022-02-13 普通视频消息
                                        case 6:
                                            // : 2022-02-13 普通位置消息
                                        case 7:
                                            // : 2022-02-13 普通名片消息
                                        case 8:
                                            // : 2022-02-13 普通文件消息
                                            sendPrivateMsg(contactBean, messageBodyBean);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            break;
                        case 2:
                            List<MessageBodyBean> messageBodyBeanList = new ArrayList<>();
                            if (SendMsgUtil.selectedToEditMsgItems.size()>20) {
                                ToastUtils.toast(mContext,"暂时只支持合并20条消息");
                                return;
                            }
                            for (MultipleItem selectedToEditMsgItem : SendMsgUtil.selectedToEditMsgItems) {
                                messageBodyBeanList.add((MessageBodyBean) selectedToEditMsgItem.getObject());
                            }
                            String content = GsonTools.createGsonString(messageBodyBeanList);
                            MessageBodyBean messageBody = SendMsgUtil.getPrivateMsg(9, contactBean.getId(), contactBean.getUuid(), contactBean.getRemarksNickname(), contactBean.getHeadPortrait(), content);
                            mPresenter.sendPrivateMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
                            ObjectBox.addMessage(messageBody);
                            break;
                        default:
                            break;
                    }
                    if (!TextUtils.isEmpty(getTextViewValue(mToReceiverMsgEt))) {
                        // : 2022-02-13  单独发一条信息
                        sendPrivateMsg( contactBean, new MessageBodyBean(getTextViewValue(mToReceiverMsgEt),0));
                    }
                    break;
                case MultipleItem.ITEM_SELECT_GROUP:
                    GroupDetailInfoBean groupBean = (GroupDetailInfoBean) multipleItem.getObject();
                    switch (forwardType) {
                        case 0:
                            sendGroupNormalMsg(groupBean, operatingMsgBean);
                            break;
                        case 1:
                            if (SendMsgUtil.selectedToEditMsgItems != null && !SendMsgUtil.selectedToEditMsgItems.isEmpty()) {
                                for (MultipleItem selectedMsge : SendMsgUtil.selectedToEditMsgItems) {
                                    // : 2022-02-13 所有类型的消息
                                    MessageBodyBean messageBodyBean = (MessageBodyBean) selectedMsge.getObject();
                                    switch (messageBodyBean.getMsgType()) {
                                        case 0:
                                            // : 2022-02-13 普通文本消息
                                        case 1:
                                            // : 2022-02-13 普通图片消息
                                        case 2:
                                            // : 2022-02-13 普通视频消息
                                        case 6:
                                            // : 2022-02-13 普通位置消息
                                        case 7:
                                            // : 2022-02-13 普通名片消息
                                        case 8:
                                            // : 2022-02-13 普通文件消息
                                            sendGroupNormalMsg(groupBean, messageBodyBean);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            break;
                        case 2:
                            List<MessageBodyBean> messageBodyBeanList = new ArrayList<>();
                            for (MultipleItem selectedToEditMsgItem : SendMsgUtil.selectedToEditMsgItems) {
                                messageBodyBeanList.add((MessageBodyBean) selectedToEditMsgItem.getObject());
                            }
                            String content = GsonTools.createGsonString(messageBodyBeanList);
                            MessageBodyBean messageBody = SendMsgUtil.getGroupMsg(9, groupBean.getGroupId(), groupBean.getUserNickname(), content);
                            mPresenter.sendPrivateMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
                            ObjectBox.addMessage(messageBody);
                            break;
                        default:
                            break;
                    }


                    if (!TextUtils.isEmpty(getTextViewValue(mToReceiverMsgEt))) {
                        // : 2022-02-13  单独发一条信息
                        sendGroupNormalMsg(groupBean, new MessageBodyBean(getTextViewValue(mToReceiverMsgEt),0));
                    }
                    break;
                default:
                    break;
            }
        }
        setResult(ChatInfoActivity.CHAT_INFO_REQUEST);
        finish();


    }

    /**
     * 发送普通信息
     * //0：text；1：image；2：video；3：语音；4视频通话；5音频通话，6位置消息，7分享名片，8文件 9合并消息
     */
    private void sendPrivateMsg( ContactBean toContactBean, MessageBodyBean olderMessageBodyBean) {
        MessageBodyBean messageBody = SendMsgUtil.getPrivateMsg(olderMessageBodyBean.getMsgType(), toContactBean.getId(), toContactBean.getUuid(), toContactBean.getRemarksNickname(), toContactBean.getHeadPortrait(), olderMessageBodyBean.getContent());
        switch (olderMessageBodyBean.getMsgType()) {
            case 1:
                messageBody.setRotation(olderMessageBodyBean.getRotation());
                break;
            case 2:
                messageBody.setRotation(olderMessageBodyBean.getRotation());
                messageBody.setDuration(olderMessageBodyBean.getDuration());
                break;
            case 6:
                messageBody.setAddrName(olderMessageBodyBean.getAddrName());
                messageBody.setAddrDes(olderMessageBodyBean.getAddrDes());
                messageBody.setLat(olderMessageBodyBean.getLat());
                messageBody.setLng(olderMessageBodyBean.getLng());
                break;
            case 7:
                messageBody.setOtherUserId(olderMessageBodyBean.getOtherUserId());
                messageBody.setOtherAccount(olderMessageBodyBean.getOtherAccount());
                messageBody.setOtherHead(olderMessageBodyBean.getOtherHead());
                messageBody.setOtherNickname(olderMessageBodyBean.getOtherNickname());
                break;
            case 8:
                messageBody.setFileSize(olderMessageBodyBean.getFileSize());
                messageBody.setFileName(olderMessageBodyBean.getFileName());
                break;
            default:
                break;
        }

        mPresenter.sendPrivateMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        ObjectBox.addMessage(messageBody);
    }

    /**
     * 发送群聊普通信息
     */
    private void sendGroupNormalMsg(GroupDetailInfoBean groupBean, MessageBodyBean olderMessageBodyBean) {
        MessageBodyBean messageBody = SendMsgUtil.getGroupMsg(olderMessageBodyBean.getMsgType(), groupBean.getGroupId(), groupBean.getUserNickname(), olderMessageBodyBean.getContent());
        switch (olderMessageBodyBean.getMsgType()) {
            case 1:
                messageBody.setRotation(olderMessageBodyBean.getRotation());
                break;
            case 2:
                messageBody.setRotation(olderMessageBodyBean.getRotation());
                messageBody.setDuration(olderMessageBodyBean.getDuration());
                break;
            case 6:
                messageBody.setAddrName(olderMessageBodyBean.getAddrName());
                messageBody.setAddrDes(olderMessageBodyBean.getAddrDes());
                messageBody.setLat(olderMessageBodyBean.getLat());
                messageBody.setLng(olderMessageBodyBean.getLng());
                break;
            case 7:
                messageBody.setOtherUserId(olderMessageBodyBean.getOtherUserId());
                messageBody.setOtherAccount(olderMessageBodyBean.getOtherAccount());
                messageBody.setOtherHead(olderMessageBodyBean.getOtherHead());
                messageBody.setOtherNickname(olderMessageBodyBean.getOtherNickname());
                break;
            case 8:
                messageBody.setFileSize(olderMessageBodyBean.getFileSize());
                messageBody.setFileName(olderMessageBodyBean.getFileName());
                break;
            default:
                break;
        }
        mPresenter.sendGroupMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        ObjectBox.addMessage(messageBody);
    }

    @Override
    public void initData() {

    }


    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseDialog();
        SendMsgUtil.selectedToEditMsgItems.clear();
    }

    private void releaseDialog() {
        if (forwardMsgDialog != null) {
            if (forwardMsgDialog.isShowing()) {
                forwardMsgDialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.search_tv:
                startActivityForResult(new Intent(mContext, SearchGroupAndContactActivity.class), BASE_REQUEST_RESULT);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case BASE_REQUEST_RESULT:
                if (data != null) {
                    ContactBean contactBean = data.getParcelableExtra(BASE_PARCELABLE);
                    fragment.notifyContact(contactBean);
                }
                break;
            case BASE_REQUEST_RESULT2:
                if (data != null) {
                    GroupDetailInfoBean groupBean = data.getParcelableExtra(BASE_PARCELABLE);
                    fragment.notifyGroup(groupBean);
                }
                break;
            default:
                break;
        }
    }

}
