package com.juntai.wisdom.im.contact.searchContact;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-25 10:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-25 10:52
 */
public class SearchContactAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder>  {
    public SearchContactAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBean selectContactBean) {

        helper.setText(R.id.item_name, selectContactBean.getRemarksNickname());
        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(selectContactBean.getHeadPortrait()), helper.getView(R.id.item_iv));
        if (selectContactBean.isHasEndLine()) {
            helper.setGone(R.id.item_divider_v, true);
        } else {
            helper.setGone(R.id.item_divider_v, false);
        }
        helper.setGone(R.id.amount_tv, false);
        if (1 == selectContactBean.getSelected()) {
            helper.setImageResource(R.id.select_status_iv, R.mipmap.select_icon);
        } else if (0 == selectContactBean.getSelected()) {
            helper.setImageResource(R.id.select_status_iv, R.mipmap.unselect_icon);
        } else {
            helper.setImageResource(R.id.select_status_iv, R.mipmap.select_icon_light);

        }
    }
}
