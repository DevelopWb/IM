package com.juntai.wisdom.im.contact;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.OperateMsgUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;
/**
 * @aouther tobato
 * @description 描述  申请添加朋友界面
 * @date 2021-11-05 15:42
 */
public class ApplyAddFriendActivity extends BaseAppActivity<MainPresent> implements MainContract.IBaseView, View.OnClickListener {

    private EditText mApplyContentTv;
    private EditText mRemarkTv;
    /**
     * 发送
     */
    private TextView mSendApplyTv;
    private ContactBean contactBean;

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_apply_add_friend;
    }

    @Override
    public void initView() {
        if (getIntent() != null) {
            contactBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        }
        setTitleName("申请添加朋友");

        mApplyContentTv = (EditText) findViewById(R.id.apply_content_tv);
        mApplyContentTv.setText("我是"+UserInfoManager.getUserNickName());
        mRemarkTv = (EditText) findViewById(R.id.remark_tv);
        mRemarkTv.setText(contactBean.getRemarksNickname());
        mSendApplyTv = (TextView) findViewById(R.id.send_apply_tv);
        mSendApplyTv.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.APPLY_ADD_FRIEND:
                BaseResult  baseResult = (BaseResult) o;
                ToastUtils.toast(mContext, "已发送申请");
                if (baseResult != null) {
                    String message = baseResult.getMessage();
                    if (!TextUtils.isEmpty(message)) {
                        MessageBodyBean messageBody = OperateMsgUtil.getPrivateMsg(10, contactBean.getId(), null,null,null, String.format("你已成功添加%s,现在可以开始聊天了",contactBean.getNickname()));
                        ObjectBox.addMessage(messageBody);
                    }
                }
              startActivity(new Intent(mContext, MainActivity.class));
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
            case R.id.send_apply_tv:
                mPresenter.applyAddFriend(getBaseBuilder()
                        .add("toAccount", String.valueOf(contactBean.getUuid()))
                        .add("toId", String.valueOf(contactBean.getId()))
                        .add("intro",getTextViewValue(mApplyContentTv))
                        .add("remarksNickname",getTextViewValue(mRemarkTv))
                        .build(), AppHttpPath.APPLY_ADD_FRIEND);
                break;
        }
    }
}
