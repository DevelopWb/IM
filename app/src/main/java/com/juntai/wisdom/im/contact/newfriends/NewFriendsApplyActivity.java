package com.juntai.wisdom.im.contact.newfriends;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.NewFriendsBean;
import com.juntai.wisdom.im.contact.addFriend.AddFriendActivity;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  新的朋友
 * @date 2021-11-06 15:53
 */
public class NewFriendsApplyActivity extends BaseRecyclerviewActivity<MainPresent> implements MainContract.IBaseView {
    private NewFriendsBean.DataBean dataBean;


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    protected void getRvAdapterData() {
        mPresenter.newFriendList(getBaseBuilder().build(), AppHttpPath.NEW_FRIEND_LIST);
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
        return new FriendsApplyAdapter(R.layout.friend_apply_item);
    }

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public void initData() {
        setTitleName("新的朋友");
        getTitleRightTv().setText("添加朋友");
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddFriendActivity.class));
            }
        });

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dataBean = (NewFriendsBean.DataBean) adapter.getData().get(position);
                switch (dataBean.getState()) {
                    case 4:
                        startActivityForResult(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, dataBean.getId()), BASE_REQUEST_RESULT);
                        break;
                    default:
                        break;
                }

            }
        });
        baseQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                dataBean = (NewFriendsBean.DataBean) adapter.getData().get(position);
                switch (dataBean.getState()) {
                    case 1:
                    case 2:
                        mPresenter.agreeFriendsApply(getBaseBuilder()
                                .add("toId", String.valueOf(dataBean.getId())).build(), AppHttpPath.AGREE_FRIEND_APPLY);
                        break;
                    default:
                        startActivityForResult(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, dataBean.getId()), BASE_REQUEST_RESULT);
                        break;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BASE_REQUEST_RESULT) {
            getRvAdapterData();
        }
    }

    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);

        switch (tag) {
            case AppHttpPath.AGREE_FRIEND_APPLY:
                ToastUtils.toast(mContext, "已成功添加好友");
                startActivityForResult(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, dataBean.getId()), BASE_REQUEST_RESULT);

                break;

            case AppHttpPath.NEW_FRIEND_LIST:
                NewFriendsBean newFriendsBean = (NewFriendsBean) o;
                if (newFriendsBean != null) {
                    List<NewFriendsBean.DataBean> friends = newFriendsBean.getData();
                    baseQuickAdapter.setNewData(friends);
                }

                break;
            default:
                break;
        }


    }
}
