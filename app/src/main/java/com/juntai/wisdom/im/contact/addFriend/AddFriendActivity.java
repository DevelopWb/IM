package com.juntai.wisdom.im.contact.addFriend;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseSearchAndListActivity;
import com.juntai.wisdom.im.bean.AddFriendMenuBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.chatlist.QRScanActivity;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;
import com.juntai.wisdom.im.contact.group.JoinGroupByUuidActivity;
import com.juntai.wisdom.im.mine.myinfo.QRCodeCardActivity;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  添加好友
 * @date 2021-11-01 15:22
 */
public class AddFriendActivity extends BaseSearchAndListActivity {

    private static final String  SCAN = "扫一扫";
    private static final String  MOBILE_CONTACT = "手机联系人";

    @Override
    protected void startSearch(String s) {
        if (!TextUtils.isEmpty(s)) {
            mPresenter.searchFriends(getBaseBuilder().add("keyword", s).build(), AppHttpPath.SEARCH_FRIENDS);
        }
    }

    @Override
    protected String getTitleName() {
        return "添加朋友";
    }



    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new AddFriendAdapter(R.layout.friend_apply_item);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        switch (tag) {
            case AppHttpPath.SEARCH_FRIENDS:
                ContactBean contactBean = (ContactBean) o;
                if (contactBean != null) {
                    startActivity(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, contactBean.getId()));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        mSearchContentSv.setQueryHint("超视距号/手机号");
        baseQuickAdapter.setHeaderView(getHeadView());
        baseQuickAdapter.setNewData(getAdapterData());

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddFriendMenuBean  friendMenuBean = (AddFriendMenuBean) adapter.getData().get(position);

                switch (friendMenuBean.getName()) {
                    case SCAN:
                        // : 2021-12-13 扫描二维码名片
//                        startActivityForResult(new Intent(mContext, QRScanActivity.class), QRScanActivity.SCAN_RESULT);
                        break;
                    case MOBILE_CONTACT:
                        // : 2022-01-25   跳转到含有手机通讯录的界面
                        startActivity(new Intent(mContext,MobileContactsActivity.class));


                        break;
                    default:
                        break;
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (QRScanActivity.SCAN_RESULT == resultCode) {
            //扫描结果
            if (data != null) {
                String result = data.getStringExtra(BASE_STRING);
                Intent intent = new Intent();
                int id = 0;
                String uuid = result.substring(result.indexOf("=") + 1, result.indexOf("&"));
                String type = result.substring(result.lastIndexOf("=") + 1, result.length());
                if ("1".equals(type)) {
                    //好友
                    mPresenter.addFriendByUuid(getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.ADD_FRIEND_BY_UUID);
                } else {
                    //群聊
                    // : 2022-01-18 群信息
                    if (!isGroupExsit(uuid)) {
                        //如果不是群成员
                        startActivity(new Intent(mContext, JoinGroupByUuidActivity.class).putExtra(BASE_STRING,uuid));
                    }else {
                        //是群成员  获取群信息 然后跳转到群聊天界面
                        mPresenter.joinGroupByUuid(getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.JOIN_GROUP_BY_UUID);
                    }
                }
            }

        }
    }

    private List<AddFriendMenuBean> getAdapterData() {
        List<AddFriendMenuBean>  arrays = new ArrayList<>();
        //高总说去掉这个入口
//        arrays.add(new AddFriendMenuBean(SCAN,"扫描二维码名片",R.mipmap-xxhdpi.add_friend_menu_scan));
        arrays.add(new AddFriendMenuBean(MOBILE_CONTACT,"添加或邀请通讯录的好友",R.mipmap.add_friend_menu_contact));
        return arrays;
    }

    private View getHeadView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.add_friend_head, null);
        view.findViewById(R.id.my_account_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2021-12-13 跳转到二维码界面
                startActivity(new Intent(mContext, QRCodeCardActivity.class));

            }
        });
        ((TextView) view.findViewById(R.id.my_account_tv)).setText("我的超视距号:" + UserInfoManager.getUserAccount());
        return view;
    }
}
