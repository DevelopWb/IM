package com.juntai.wisdom.im.chatlist.groupchat.joinGroup;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupBean;
import com.juntai.wisdom.im.bean.GroupDetailBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.contact.searchContact.SearchContactActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.SendMsgUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  选择群聊名单
 * @date 2022-01-12 9:45
 */
public class SelectGroupChatPeopleActivity extends BaseAppActivity<ChatPresent> implements MainContract.IBaseView {

    private SelectGroupPeoplesFragment selectContactFragment;
    private ArrayList<PeopleBean> groupPeoples;
    //进入群聊的方式  0是发起群聊 1是加入群聊
    private int joinGroupType = 0;
    private String groupUuid;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_select_group_chat_people;
    }

    @Override
    public void initView() {


        if (getIntent() != null) {
            groupPeoples = getIntent().getParcelableArrayListExtra(BASE_PARCELABLE);
            joinGroupType = getIntent().getIntExtra(BASE_ID, 0);
            groupUuid = getIntent().getStringExtra(BASE_STRING);
        }

        if (0 == joinGroupType) {
            setTitleName("发起群聊");
            getTitleRightTv().setText("完成");
        } else if (1 == joinGroupType) {
            setTitleName("选择联系人");
            getTitleRightTv().setText("完成");
        } else {
            setTitleName(String.format("群聊成员"));
            groupPeoples.remove(0);
            getTitleRightTv().setText("删除");
        }
        selectContactFragment = (SelectGroupPeoplesFragment) getSupportFragmentManager().findFragmentById(R.id.select_contact_fg);
        selectContactFragment.setGroupPeoples(groupPeoples, joinGroupType);
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2022-01-12 调用创建群聊的接口
                List<Integer> ids = selectContactFragment.getSelectedContactIds();
                if (ids.isEmpty()) {
                    ToastUtils.toast(mContext, "请选择成员");
                }

                switch (joinGroupType) {
                    case 0:
                        ids.add(UserInfoManager.getUserId());
                        if (ids.size() > 2) {
                            mPresenter.creatGroup(ids, AppHttpPath.CREAT_GROUP);
                        } else {
                            ToastUtils.toast(mContext, "创建群聊最少需要三个人");
                        }
                        break;
                    case 1:
                        if (ids.isEmpty()) {
                            ToastUtils.toast(mContext, "请选择联系人");
                            return;
                        }
                        //加入群聊
                        mPresenter.joinGroup(groupUuid, ids, AppHttpPath.JOIN_GROUP);
                        break;
                    case 2:
                        // : 2022-01-17  移除群聊
                        if (ids.isEmpty()) {
                            ToastUtils.toast(mContext, "请选择要移除的群成员");
                            return;
                        }
                        mPresenter.quitGroup(groupUuid, ids, AppHttpPath.QUIT_GROUP);
                        break;
                    default:
                        break;
                }


            }
        });
        TextView searchTv = (TextView) findViewById(R.id.search_tv);
        searchTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivityForResult(new Intent(mContext, SearchContactActivity.class).putParcelableArrayListExtra(BASE_PARCELABLE,selectContactFragment.getAllContacts()),BASE_REQUEST_RESULT);
           }
       });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==BASE_REQUEST_RESULT) {
            if (data != null) {
                ContactBean contactBean= data.getParcelableExtra(BASE_PARCELABLE);
                selectContactFragment.notifyContact(contactBean);


            }
        }
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

        switch (tag) {
            case AppHttpPath.CREAT_GROUP:
                GroupBean groupBean = (GroupBean) o;
                if (groupBean != null) {
                    GroupDetailBean.DataBean dataBean = groupBean.getData();
                    dataBean.setGroupId(dataBean.getId());
                    //本地添加一条群聊消息
                    MessageBodyBean messageBody = SendMsgUtil.getGroupMsg(10, dataBean.getId(), 0,null, "你已成功创建群聊");
                    ObjectBox.addMessage(messageBody);
                    //跳转到群聊界面
                    startActivity(new Intent(mContext, GroupChatActivity.class)
                            .putExtra(BaseActivity.BASE_ID, dataBean.getGroupId()));
                    finish();
                }
                break;
            case AppHttpPath.JOIN_GROUP:
                ToastUtils.toast(mContext, "添加成功");
                finish();
                break;
            case AppHttpPath.QUIT_GROUP:
                ToastUtils.toast(mContext, "移除成功");
                finish();
                break;
            default:
                break;
        }


    }

}
