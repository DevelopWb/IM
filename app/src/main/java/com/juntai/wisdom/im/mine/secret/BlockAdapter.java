package com.juntai.wisdom.im.mine.secret;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-12-12 9:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-12 9:27
 */
public class BlockAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {
    public BlockAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBean contactBean) {
        helper.setText(R.id.item_name, contactBean.getRemarksNickname());
        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(contactBean.getHeadPortrait()), helper.getView(R.id.item_iv));
        if (contactBean.isHasEndLine()) {
            helper.setGone(R.id.item_divider_v, true);
        } else {
            helper.setGone(R.id.item_divider_v, false);
        }
        helper.setGone(R.id.amount_tv, false);
    }
}
