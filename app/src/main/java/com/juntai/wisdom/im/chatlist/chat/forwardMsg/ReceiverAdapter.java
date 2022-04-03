package com.juntai.wisdom.im.chatlist.chat.forwardMsg;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @Author: tobato
 * @Description: 作用描述   接收转发消息的对象适配器
 * @CreateDate: 2022-02-12 17:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-12 17:20
 */
public class ReceiverAdapter  extends BaseQuickAdapter<MultipleItem, BaseViewHolder> {
    public ReceiverAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        if (getData().size()>1) {
            helper.setGone(R.id.receiver_name_tv,false);
        }else {
            helper.setGone(R.id.receiver_name_tv,true);
        }
        switch (item.getItemType()) {
            case MultipleItem.ITEM_SELECT_CONTACT:
                ContactBean contactBean = (ContactBean) item.getObject();
                helper.setText(R.id.receiver_name_tv,contactBean.getRemarksNickname());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(contactBean.getHeadPortrait()),helper.getView(R.id.receiver_head_iv));
                break;
            case MultipleItem.ITEM_SELECT_GROUP:
                GroupDetailInfoBean groupBean = (GroupDetailInfoBean) item.getObject();
                helper.setText(R.id.receiver_name_tv,groupBean.getGroupName());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(groupBean.getGroupPicture()),helper.getView(R.id.receiver_head_iv));
                break;
            default:
                break;
        }

    }
}
