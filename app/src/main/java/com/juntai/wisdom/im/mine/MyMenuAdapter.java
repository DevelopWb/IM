package com.juntai.wisdom.im.mine;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.MyMenuBean;

import java.util.List;

/**
 * Describe:
 * Create by tobato
 * 2020/3/7
 * email:954101549@qq.com
 */
public class MyMenuAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyMenuAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_DIVIDER, R.layout.memu_divider);
        addItemType(MultipleItem.ITEM_MYCENTER_MENUS,R.layout.my_center_menu_item);
        //聊天界面  发视频 发消息
        addItemType(MultipleItem.ITEM_PIC_TEXT_MENUS,R.layout.pic_text_item);
    }


    @Override
    protected void convert(BaseViewHolder helper, MultipleItem multipleItem) {
        switch (multipleItem.getItemType()) {
            case MultipleItem.ITEM_DIVIDER:
                break;
            case MultipleItem.ITEM_MYCENTER_MENUS:
                MyMenuBean item = (MyMenuBean) multipleItem.getObject();
                helper.setText(R.id.item_name, item.getName());

                if (item.getImageId()>0) {
                    helper.setGone(R.id.item_iv,true);
                    helper.setImageResource(R.id.item_iv, item.getImageId());
                }else {
                    helper.setGone(R.id.item_iv,false);
                }

                if (item.getNumber() > 0) {
                    helper.setVisible(R.id.item_number, true);
                    helper.setText(R.id.item_number, item.getNumber() > 99 ? "99+" : String.valueOf(item.getNumber()));
                } else {
                    helper.setVisible(R.id.item_number, false);
                }
                if (item.isHasEndLine()) {
                    helper.setGone(R.id.menu_item_divider_v, true);
                } else {
                    helper.setGone(R.id.menu_item_divider_v, false);
                }
                break;
            case MultipleItem.ITEM_PIC_TEXT_MENUS:

                MyMenuBean menuBean = (MyMenuBean) multipleItem.getObject();
                helper.setText(R.id.item_name, menuBean.getName());
                if (menuBean.getImageId()>0) {
                    helper.setGone(R.id.item_pic_iv,true);
                    helper.setImageResource(R.id.item_pic_iv, menuBean.getImageId());
                }else {
                    helper.setGone(R.id.item_pic_iv,false);
                }
                if (menuBean.isHasEndLine()) {
                    helper.setGone(R.id.divider_v, true);
                } else {
                    helper.setGone(R.id.divider_v, false);
                }


                break;
            default:
                break;
        }


    }

}
