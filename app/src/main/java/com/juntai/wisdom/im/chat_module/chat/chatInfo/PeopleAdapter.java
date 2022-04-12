package com.juntai.wisdom.im.chat_module.chat.chatInfo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @Author: tobato
 * @Description: 作用描述  成员适配器
 * @CreateDate: 2021-12-10 16:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-10 16:39
 */
public class PeopleAdapter extends BaseQuickAdapter<PeopleBean, BaseViewHolder> {
    public PeopleAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleBean item) {
        helper.setVisible(R.id.people_name_tv,false);
        switch (item.getType()) {
            case 0:
                helper.setVisible(R.id.people_name_tv,true);
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(item.getHead()), helper.getView(R.id.people_head_iv));
                helper.setText(R.id.people_name_tv, item.getName());
                break;
            case 1:
                ImageLoadUtil.loadSquareImage(mContext, R.mipmap.add_people_icon, helper.getView(R.id.people_head_iv));
                break;
            case 2:
                ImageLoadUtil.loadSquareImage(mContext, R.mipmap.remove_people_icon, helper.getView(R.id.people_head_iv));
                break;
            default:
                break;
        }

    }
}
