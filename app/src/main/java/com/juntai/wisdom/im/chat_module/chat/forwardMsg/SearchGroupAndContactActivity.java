package com.juntai.wisdom.im.chat_module.chat.forwardMsg;

import android.content.Intent;
import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chat_module.searchchat.BaseSearchActivity;
import com.juntai.wisdom.im.search.SearchAdapter;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  首页搜索模块
 * @date 2022-01-27 15:05
 */
public class SearchGroupAndContactActivity extends BaseSearchActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean commitSearch(String s) {
        //通讯录相关内容
        List<MultipleItem> data = mPresenter.getContactAndGroup(s);

        // TODO: 2022-01-27  聊天记录相关内容
        if (5 == getSearchType()) {

        }
        adapter.setNewData(data);
        return false;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchAdapter(null);
    }

    @Override
    protected int getSearchType() {
        return 5;
    }

    @Override
    protected void onAdapterItemClick(BaseQuickAdapter adapter, int position) {
        MultipleItem multipleItem1 = (MultipleItem) adapter.getItem(position);
        switch (multipleItem1.getItemType()) {
            case MultipleItem.ITEM_CONTACT:
                //联系人
                ContactBean contactBean1 = (ContactBean) multipleItem1.getObject();
                setResult(BASE_REQUEST_RESULT, new Intent().putExtra(BASE_PARCELABLE, contactBean1));
                finish();
                break;
            case MultipleItem.ITEM_GROUP:
                //群聊
                GroupDetailInfoBean groupBean = (GroupDetailInfoBean) multipleItem1.getObject();
                setResult(BASE_REQUEST_RESULT2, new Intent().putExtra(BASE_PARCELABLE, groupBean));
                finish();
                break;
            default:
                break;
        }
    }
}
