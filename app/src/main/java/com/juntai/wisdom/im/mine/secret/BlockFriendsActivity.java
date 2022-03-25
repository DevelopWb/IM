package com.juntai.wisdom.im.mine.secret;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.ContactListBean;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述   黑名单列表
 * @date 2021-12-12 9:08
 */
public class BlockFriendsActivity extends BaseRecyclerviewActivity<MainPresent> implements MainContract.IBaseView {

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public void initData() {
        setTitleName("通讯录黑名单");
        baseQuickAdapter.setEmptyView(getAdapterEmptyView("暂无黑名单成员",-1));
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ContactBean contactBean = (ContactBean) adapter.getData().get(position);
                startActivityForResult(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, contactBean.getId()), BASE_REQUEST_RESULT);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getRvAdapterData();
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.GET_CONTACT_LISTE:
                ContactListBean contactListBean = (ContactListBean) o;
                if (contactListBean != null) {
                    List<ContactBean> data = contactListBean.getData();
                    List<ContactBean> blockList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        ContactBean  contactBean = data.get(i);
                        if (contactBean.getStatusType()==2) {
                            if (i==data.size()-1) {
                                contactBean.setHasEndLine(false);
                            }else {
                                contactBean.setHasEndLine(true);
                            }
                            blockList.add(contactBean);
                        }
                    }
                    baseQuickAdapter.setNewData(blockList);
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    protected void getRvAdapterData() {
        mPresenter.getContactList(getBaseBuilder().build(), AppHttpPath.GET_CONTACT_LISTE);

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
        return new BlockAdapter(R.layout.item_contact);
    }
}
