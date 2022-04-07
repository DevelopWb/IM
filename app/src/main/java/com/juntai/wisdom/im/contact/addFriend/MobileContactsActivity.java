package com.juntai.wisdom.im.contact.addFriend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.ContactListBean;
import com.juntai.wisdom.im.bean.PhoneContactsBean;
import com.juntai.wisdom.im.contact.ApplyAddFriendActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.utils.ContactUtil;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  手机联系人
 * @date 2022-01-19 11:41
 */
public class MobileContactsActivity extends BaseRecyclerviewActivity<MainPresent> implements MainContract.IBaseView {

    // : 2022-01-25  手机联系人   待完成


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    protected void getRvAdapterData() {
        List<PhoneContactsBean> contacts = new ContactUtil(mContext).getAllContactInfo();
        mPresenter.matchMobileContacts(getBaseBuilder().add("data", GsonTools.createGsonString(contacts)).build(), AppHttpPath.MATCH_CONTACT_LISTE);

    }

    @Override
    protected boolean enableRefresh() {
        return true;
    }

    @Override
    protected boolean enableLoadMore() {
        return false;
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new MobileContactAdapter(R.layout.friend_apply_item);
    }

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public void initData() {
        super.initData();
        setTitleName("查看手机通讯录");
        mSmartrefreshlayout.setEnableLoadMore(false);
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ContactBean contactBean = (ContactBean) adapter.getItem(position);
                if (0 == contactBean.getId()) {
                    // : 2022-01-28  发送短信
                    sendSystemMsg(contactBean.getPhoneNumber(), getString(R.string.message_recommend), mContext);
                } else {
                    if (!contactBean.isFriend()) {
                        // : 2022-01-28 申请添加好友
                        startActivityForResult(new Intent(mContext, ApplyAddFriendActivity.class).putExtra(BASE_PARCELABLE, contactBean), BASE_REQUEST_RESULT);

                    }
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BASE_REQUEST_RESULT == requestCode) {
            getRvAdapterData();
        }
    }

    /**
     * 跳转系统短息
     */
    public static void sendSystemMsg(String phoneNo, String msg, Context context) {
        Uri smsToUri = Uri.parse("smsto:" + phoneNo);
        Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", msg);
        context.startActivity(mIntent);
    }

    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        switch (tag) {
            case AppHttpPath.MATCH_CONTACT_LISTE:
                ContactListBean contactListBean = (ContactListBean) o;
                if (contactListBean != null) {
                    List<ContactBean> data = contactListBean.getData();
                    baseQuickAdapter.setNewData(data);
                }
                break;
            default:
                break;
        }


    }
}
