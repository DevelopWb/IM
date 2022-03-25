package com.juntai.wisdom.im.contact.group;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.GroupInfoByUuidBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  扫描二维码加入群聊
 * @date 2022-01-18 16:32
 */
public class JoinGroupByUuidActivity extends BaseAppActivity<MainPresent> implements MainContract.IBaseView, View.OnClickListener {

    private ImageView mGroupIconIv;
    /**
     * dfad
     */
    private TextView mGroupNameTv;
    /**
     * 加入群聊
     */
    private TextView mJoinGroupTv;
    private GroupInfoByUuidBean.DataBean dataBean;

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_join_group;
    }

    @Override
    public void initView() {

        mGroupIconIv = (ImageView) findViewById(R.id.group_icon_iv);
        mGroupNameTv = (TextView) findViewById(R.id.group_name_tv);
        mJoinGroupTv = (TextView) findViewById(R.id.join_group_tv);
        mJoinGroupTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            String uuid = getIntent().getStringExtra(BASE_STRING);
            mPresenter.joinGroupByUuid(getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.JOIN_GROUP_BY_UUID);

        }

    }


    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.JOIN_GROUP_BY_UUID:
                GroupInfoByUuidBean groupInfoByUuidBean = (GroupInfoByUuidBean) o;
                if (groupInfoByUuidBean != null) {
                    dataBean = groupInfoByUuidBean.getData();
                    if (dataBean != null) {
                        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(dataBean.getGroupPicture()), mGroupIconIv);
                        mGroupNameTv.setText(dataBean.getGroupName());

                    }
                }

                break;
            case AppHttpPath.JOIN_GROUP:
                //跳转到群聊界面
                ToastUtils.toast(mContext,"已成功加入");
                GroupListBean.DataBean groupBean = GsonTools.modelA2B(dataBean,GroupListBean.DataBean.class);
                groupBean.setGroupId(dataBean.getId());
                startActivity(new Intent(mContext, GroupChatActivity.class)
                        .putExtra(BASE_ID,groupBean.getGroupId()));
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
            case R.id.join_group_tv:
                List<Integer> ids = new ArrayList<>();
                ids.add(UserInfoManager.getUserId());
                mPresenter.joinGroup(dataBean.getUuid(), ids, AppHttpPath.JOIN_GROUP);

                break;
        }
    }
}
