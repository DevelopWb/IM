package com.juntai.wisdom.im.contact.searchContact;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.chat_module.searchchat.BaseSearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  搜索联系人
 * @date 2022-01-25 15:59
 */
public class SearchContactActivity extends BaseSearchActivity {
    @Override
    protected boolean commitSearch(String s) {
        // : 2022-01-25 搜索本地联系人
        List<ContactBean> searchedContacts = new ArrayList<>();
        List<ContactBean> contactBeans = getIntent().getParcelableArrayListExtra(BASE_PARCELABLE);
        for (ContactBean bean : contactBeans) {
            if (bean.getRemarksNickname().contains(s)) {
                searchedContacts.add(bean);
            }
        }
        adapter.setNewData(searchedContacts);
        return false;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchContactAdapter(R.layout.item_select_contact);
    }

    @Override
    protected int getSearchType() {
        return 3;
    }

    @Override
    protected void onAdapterItemClick(BaseQuickAdapter adapter, int position) {
        ContactBean contactBean = (ContactBean) adapter.getItem(position);
        if (0 == contactBean.getSelected()) {
            setResult(BASE_REQUEST_RESULT, new Intent().putExtra(BASE_PARCELABLE, contactBean));
            finish();
        } else {
            ToastUtils.toast(mContext, "该成员已经存在");
        }
    }
}
