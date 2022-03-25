package com.juntai.wisdom.im.entrance.main;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.HomePageMenuBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-12-12 15:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-12 15:42
 */
public class HomePageMenuAdapter extends BaseQuickAdapter<HomePageMenuBean, BaseViewHolder> {
    public HomePageMenuAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageMenuBean item) {
        if (item.getHeadRes()>0) {
            helper.setGone(R.id.item_head_iv,true);
            helper.setTextColor(R.id.item_name_tv, ContextCompat.getColor(mContext,R.color.white));
            ImageLoadUtil.loadImage(mContext,item.getHeadRes(),helper.getView(R.id.item_head_iv));
        }else {
            helper.setGone(R.id.item_head_iv,false);
            helper.setTextColor(R.id.item_name_tv, ContextCompat.getColor(mContext,R.color.black));

        }
        helper.setText(R.id.item_name_tv,item.getName());
        if (item.isHasEndLine()) {
            helper.setGone(R.id.item_divider_v, true);
        } else {
            helper.setGone(R.id.item_divider_v, false);
        }
    }
}
