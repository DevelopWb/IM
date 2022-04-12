package com.juntai.wisdom.im.mine.myCollect;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chat_module.chat.displayFile.FileDetailActivity;
import com.juntai.wisdom.im.chat_module.searchchat.BaseSearchActivity;

/**
 * @aouther tobato
 * @description 描述  搜索收藏的内容
 * @date 2022-03-02 15:46
 */
public class SearchCollectActivity extends BaseSearchActivity {

    @Override
    protected boolean commitSearch(String s) {
        // : 2022-03-02 查找对应的收藏数据  暂时不分页
        mPresenter.getAllCollection(getBaseBuilder().add("keyword", s).build(), AppHttpPath.ALL_COLLECTS);

        return false;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new MyCollectAdapter(null);
    }

    @Override
    protected int getSearchType() {
        return 6;
    }

    @Override
    protected void onAdapterItemClick(BaseQuickAdapter adapter, int position) {
        MultipleItem multipleItem6 = (MultipleItem) adapter.getItem(position);
        switch (multipleItem6.getItemType()) {
            case MultipleItem.ITEM_COLLECTION_PIC:
            case MultipleItem.ITEM_COLLECTION_VIDEO:
            case MultipleItem.ITEM_COLLECTION_AUDIO:
            case MultipleItem.ITEM_COLLECTION_FILE:
                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE, (MessageBodyBean) multipleItem6.getObject()));
                break;
            default:
                break;
        }
    }
}
