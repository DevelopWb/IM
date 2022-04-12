package com.juntai.wisdom.im.contact;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.widght.BaseBottomDialog;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.MyMenuBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.chat_module.chat.PrivateChatActivity;
import com.juntai.wisdom.im.chat_module.chat.chatInfo.FriendInfoSetActivity;
import com.juntai.wisdom.im.chat_module.chat.videocall.VideoRequestActivity;
import com.juntai.wisdom.im.mine.MyMenuAdapter;
import com.juntai.wisdom.im.mine.myinfo.BaseModifyActivity;
import com.juntai.wisdom.im.utils.SendMsgUtil;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  联系人信息
 * @date 2021-10-31 17:17
 */
public class ContactorInfoActivity extends BaseRecyclerviewActivity<MainPresent> implements MainContract.IBaseView {

    private ContactBean contactBean;
    private ImageView mHeardPic;
    /**
     * 昵称
     */
    private TextView mContactorNameTv;
    /**
     * 账号：
     */
    private TextView mContactorAccountTv,mContactorNickNameTv,mContactorAddrTv;
    private int contactId;
    private ImageView mSexTagIv;
    public static final int CONTACT_INFO_RESULT = 10090;
    private BaseBottomDialog baseBottomDialog;
    private BaseBottomDialog.OnItemClick onItemClick;

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }


    @Override
    public void initView() {
        super.initView();
        setTitleName("");
        if (getIntent() != null) {
            contactId = getIntent().getIntExtra(BASE_ID,0);
            mPresenter.getUserInfo(getBaseBuilder().add("toUserId", String.valueOf(contactId)).build(), AppHttpPath.GET_USER_INFO);
        }
        setRightTvDrawable(R.mipmap.item_more);
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2021-12-11 跳转到好友设置界面  资料设置
                startActivityForResult(new Intent(mContext, FriendInfoSetActivity.class).putExtra(BASE_PARCELABLE, contactBean), BASE_REQUEST_RESULT);
            }
        });

        baseQuickAdapter.setHeaderView(getHeadView());


    }

    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    public List<MultipleItem> getMenuBeans(ContactBean contactBean) {
        List<MultipleItem> menuBeans = new ArrayList<>();
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean(MultipleItem.SET_REMARK, 0, 0, true)));

        if (!contactBean.isFriend()) {
            //未添加
            menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
            menuBeans.add(new MultipleItem(MultipleItem.ITEM_PIC_TEXT_MENUS, new MyMenuBean(MultipleItem.ADD_TO_CONTRACT, 0, false)));

        }else {
            if (contactId != UserInfoManager.getUserId()) {
                menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
                menuBeans.add(new MultipleItem(MultipleItem.ITEM_PIC_TEXT_MENUS, new MyMenuBean(MultipleItem.SEND_MSG, R.mipmap.send_msg_icon, true)));
                menuBeans.add(new MultipleItem(MultipleItem.ITEM_PIC_TEXT_MENUS, new MyMenuBean(MultipleItem.CALL_IN_VIDEO, R.mipmap.send_vedio_msg, false)));
            } else {
                menuBeans.remove(1);
                getTitleRightTv().setVisibility(View.GONE);
            }

        }

        return menuBeans;
    }

    protected View getHeadView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contactor_info_head, null);
        mHeardPic = (ImageView) view.findViewById(R.id.contactor_icon_iv);
        mSexTagIv = (ImageView) view.findViewById(R.id.sex_tag_iv);

        mContactorNameTv = (TextView) view.findViewById(R.id.contactor_name_tv);
        mContactorAccountTv = (TextView) view.findViewById(R.id.contactor_account_tv);
        mContactorNickNameTv = (TextView) view.findViewById(R.id.contactor_nickname_tv);
        mContactorAddrTv = (TextView) view.findViewById(R.id.contactor_addr_tv);
        return view;
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
        return new MyMenuAdapter(null);
    }

    @Override
    public void initData() {
        super.initData();
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_MYCENTER_MENUS:
                        MyMenuBean menuBean = (MyMenuBean) multipleItem.getObject();
                        switch (menuBean.getName()) {
                            case MultipleItem.SET_REMARK:
                                // : 2021-11-01  设置备注
                                startActivityForResult(new Intent(mContext, ModifyRemarkNameActivity.class)
                                        .putExtra(BaseModifyActivity.BASE_ID, contactBean.getId())
                                        .putExtra(BaseModifyActivity.DEFAULT_HINT, contactBean.getRemarksNickname()), BASE_REQUEST_RESULT);
                                break;
//                            case MultipleItem.MORE_INFO:
//                                // : 2021-11-01   更多信息
//                                break;
                            default:
                                break;
                        }
                        break;
                    case MultipleItem.ITEM_PIC_TEXT_MENUS:
                        MyMenuBean btMenuBean = (MyMenuBean) multipleItem.getObject();
                        switch (btMenuBean.getName()) {
                            case MultipleItem.ADD_TO_CONTRACT:
                                //  2021-11-01 添加到通讯录  跳转到申请添加好友界面
                                startActivity(new Intent(mContext, ApplyAddFriendActivity.class).putExtra(BASE_PARCELABLE, contactBean));
                                break;
                            case MultipleItem.SEND_MSG:
                                // 发消息
                                startActivity(new Intent(mContext, PrivateChatActivity.class).putExtra(BASE_PARCELABLE, contactBean));
                                break;
                            case MultipleItem.CALL_IN_VIDEO:
                                // : 2021-11-01 音视频通话
                                if (baseBottomDialog == null) {
                                    baseBottomDialog = new BaseBottomDialog();
                                }
                                onItemClick = new BaseBottomDialog.OnItemClick() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        releaseDialog();
                                        // : 2021-11-23 视频通话
                                        MessageBodyBean videoMsg = SendMsgUtil.getPrivateMsg(0 == position ? 4 : 5, contactBean.getId(), contactBean.getUuid(), contactBean.getRemarksNickname(), contactBean.getHeadPortrait(), "");
                                        //跳转到等待接听界面
                                        Intent intent =
                                                new Intent(mContext, VideoRequestActivity.class)
                                                        .putExtra(VideoRequestActivity.IS_SENDER, true)
                                                        .putExtra(BASE_PARCELABLE,
                                                                videoMsg);

                                        startActivity(intent);

                                    }
                                };
                                baseBottomDialog.initBottomDg(mPresenter.getCalls(), null, null, onItemClick);
                                baseBottomDialog.show(getSupportFragmentManager(), "arrays");
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
    /**
     * 释放dialog
     */
    private void releaseDialog() {
        if (baseBottomDialog != null) {
            if (baseBottomDialog.isAdded()) {
                onItemClick = null;
                if (baseBottomDialog.getDialog().isShowing()) {
                    baseBottomDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BASE_REQUEST_RESULT) {
            mPresenter.getUserInfo(getBaseBuilder().add("toUserId", String.valueOf(contactId)).build(), AppHttpPath.GET_USER_INFO);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(ContactorInfoActivity.CONTACT_INFO_RESULT);
        super.onBackPressed();
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.GET_USER_INFO:
                UserBean userBean = (UserBean) o;
                contactBean = userBean.getData();
                if (contactBean != null) {
                    if (contactBean.getGender() > 0) {
                        mSexTagIv.setVisibility(View.VISIBLE);
                        mSexTagIv.setImageResource(contactBean.getGender() == 2 ? R.mipmap.sex_tag_girl : R.mipmap.sex_tag_boy);
                    } else {
                        mSexTagIv.setVisibility(View.GONE);
                    }
                    ImageLoadUtil.loadHeadPic(mContext, UrlFormatUtil.getImageThumUrl(contactBean.getHeadPortrait()), mHeardPic,true);
                    mContactorNameTv.setText(contactBean.getRemarksNickname());
                    mContactorAccountTv.setText("超视距号:"+contactBean.getAccountNumber());
                    mContactorNickNameTv.setText("昵称:"+contactBean.getNickname());
                    mContactorAddrTv.setText("地区:"+contactBean.getAddress());

                    baseQuickAdapter.setNewData(getMenuBeans(contactBean));
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseDialog();

    }
}
