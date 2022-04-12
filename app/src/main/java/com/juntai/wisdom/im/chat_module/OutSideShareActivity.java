package com.juntai.wisdom.im.chat_module;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.ActivityManagerTool;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
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
import com.juntai.wisdom.im.chat_module.chat.forwardMsg.ReceiverAdapter;
import com.juntai.wisdom.im.chat_module.chat.forwardMsg.SearchGroupAndContactActivity;
import com.juntai.wisdom.im.chat_module.chat.forwardMsg.SelectGroupAndContactFragment;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.SendMsgUtil;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  外部分享
 * @date 2022/4/11 11:26
 */
public class OutSideShareActivity extends BaseAppActivity<ChatPresent> implements MainContract.IBaseView, View.OnClickListener {
    private TextView mSearchTv;
    private SelectGroupAndContactFragment fragment;
    private AlertDialog shareMsgDialog;
    private String shareTitle;
    private String shareUrl;
    private String sharePic;
    private String shareContent;
    private String shareFromApp;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.forward_msg_activity;
    }

    @Override
    public void initView() {
        setTitleName("选择分享的对象");
        if (getIntent() != null) {
            shareTitle = getIntent().getStringExtra("title");
            shareUrl = getIntent().getStringExtra("shareUrl");
            sharePic = getIntent().getStringExtra("picPath");
            shareContent = getIntent().getStringExtra("content");
            shareFromApp = getIntent().getStringExtra("shareFromApp");

        }
        mSearchTv = (TextView) findViewById(R.id.search_tv);
        mSearchTv.setOnClickListener(this);
        fragment = (SelectGroupAndContactFragment) getSupportFragmentManager().findFragmentById(R.id.select_contact_fg);
        getTitleRightTv().setText("分享");
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
                shareMsgDialog = new AlertDialog.Builder(mContext)
                        .setView(view)
                        .setCancelable(false)
                        .show();
                shareMsgDialog.getWindow().setBackgroundDrawableResource(R.drawable.sp_filled_white_10dp);
                shareMsgDialog.getWindow().setLayout(ScreenUtils.getInstance(mContext).getScreenWidth() - DisplayUtil.dp2px(mContext, 80), LinearLayout.LayoutParams.WRAP_CONTENT);
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
                ((TextView) view.findViewById(R.id.card_name_tv)).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.card_name_tv)).setText(String.format("[链接]%s", shareTitle));


                view.findViewById(R.id.close_dialog_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareMsgDialog.dismiss();
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
    }

    @Override
    public void initData() {

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
                    sendPrivateMsg(contactBean, false, null);
                    if (!TextUtils.isEmpty(getTextViewValue(mToReceiverMsgEt))) {
                        // : 2022-02-13  单独发一条信息
                        sendPrivateMsg(contactBean, true, getTextViewValue(mToReceiverMsgEt));
                    }
                    break;
                case MultipleItem.ITEM_SELECT_GROUP:
                    GroupDetailInfoBean groupBean = (GroupDetailInfoBean) multipleItem.getObject();
                    sendGroupNormalMsg(groupBean, false, null);
                    if (!TextUtils.isEmpty(getTextViewValue(mToReceiverMsgEt))) {
                        // : 2022-02-13  单独发一条信息
                        sendGroupNormalMsg(groupBean, true, getTextViewValue(mToReceiverMsgEt));
                    }
                    break;
                default:
                    break;
            }
        }
        if (shareMsgDialog != null&&shareMsgDialog.isShowing()) {
            shareMsgDialog.dismiss();
            shareMsgDialog=null;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.shared_success_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .show();
        TextView  backThirdTv = view.findViewById(R.id.back_to_third_tv);
        backThirdTv.setText(String.format("返回%s",shareFromApp));
        TextView  stayTv = view.findViewById(R.id.stay_tv);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.sp_filled_white_10dp);
        alertDialog.getWindow().setLayout(ScreenUtils.getInstance(mContext).getScreenWidth() - DisplayUtil.dp2px(mContext, 80), LinearLayout.LayoutParams.WRAP_CONTENT);
        backThirdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mSearchTv);
                ActivityManagerTool.getInstance().finishApp();
            }
        });
        stayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mSearchTv);
                startActivity(new Intent(mContext, MainActivity.class));
            }
        });
    }

    /**
     * 发送普通信息
     * //0：text；1：image；2：video；3：语音；4视频通话；5音频通话，6位置消息，7分享名片，8文件 9合并消息
     */
    private void sendPrivateMsg(ContactBean toContactBean, boolean isTextMsg, String textContent) {
        MessageBodyBean messageBody = SendMsgUtil.getPrivateMsg(isTextMsg ? 0 : 11, toContactBean.getId(), toContactBean.getUuid(), toContactBean.getRemarksNickname(), toContactBean.getHeadPortrait(), isTextMsg ? textContent : String.format("[链接]%s", shareTitle));
        messageBody.setShareTitle(shareTitle);
        messageBody.setShareContent(shareContent);
        messageBody.setSharePic(sharePic);
        messageBody.setShareUrl(shareUrl);
        messageBody.setShareAppName(shareFromApp);
        mPresenter.sendPrivateMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        ObjectBox.addMessage(messageBody);
    }

    /**
     * 发送群聊普通信息
     */
    private void sendGroupNormalMsg(GroupDetailInfoBean groupBean, boolean isTextMsg, String textContent) {
        MessageBodyBean messageBody = SendMsgUtil.getGroupMsg(isTextMsg ? 0 : 11, groupBean.getGroupId(), groupBean.getUserNickname(), isTextMsg ? textContent : String.format("[链接]%s", shareTitle));
        messageBody.setShareTitle(shareTitle);
        messageBody.setShareContent(shareContent);
        messageBody.setSharePic(sharePic);
        messageBody.setShareUrl(shareUrl);
        messageBody.setShareAppName(shareFromApp);
        mPresenter.sendGroupMessage(SendMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        ObjectBox.addMessage(messageBody);
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManagerTool.getInstance().finishApp();
    }
}
