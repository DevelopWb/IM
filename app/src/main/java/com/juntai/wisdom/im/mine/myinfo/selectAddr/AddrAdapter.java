package com.juntai.wisdom.im.mine.myinfo.selectAddr;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.TextKeyValueBean;

import java.util.List;

/**
 * Describe:
 * Create by tobato
 * 2020/3/7
 * email:954101549@qq.com
 */
public class AddrAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AddrAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_DIVIDER, R.layout.memu_divider);
        addItemType(MultipleItem.ITEM_TEXT_VALUE,R.layout.item_key_value);
    }


    @Override
    protected void convert(BaseViewHolder helper, MultipleItem multipleItem) {
        switch (multipleItem.getItemType()) {
            case MultipleItem.ITEM_DIVIDER:
                TextView divTv = helper.getView(R.id.menu_divider_v);
                divTv.setText((String)multipleItem.getObject());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) divTv.getLayoutParams();
                params.height = DisplayUtil.dp2px(mContext,30);
                divTv.setLayoutParams(params);
                break;
            case MultipleItem.ITEM_TEXT_VALUE:

                LinearLayout  keyValueLl = helper.getView(R.id.key_value_ll);
                RecyclerView.LayoutParams params2 = (RecyclerView.LayoutParams) keyValueLl.getLayoutParams();
                params2.height = DisplayUtil.dp2px(mContext,45);
                keyValueLl.setLayoutParams(params2);
                TextKeyValueBean item = (TextKeyValueBean) multipleItem.getObject();
                if (SetAddrActivity.CHINA_TAG.equals(item.getKey())) {
                    helper.setGone(R.id.loc_tag_iv,true);
                }else {
                    helper.setGone(R.id.loc_tag_iv,false);
                }
                helper.setText(R.id.item_myinfo_name, item.getKey());
                TextView valueTv = helper.getView(R.id.item_myinfo_value);
                valueTv.setText(item.getValue());
                valueTv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                valueTv.setText(item.getValue());

                break;
            default:
                break;
        }


    }

}
