package com.juntai.wisdom.im.contact.addFriend;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @Author: tobato
 * @Description: 作用描述  手机联系人
 * @CreateDate: 2021-12-13 16:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-13 16:56
 */
public class MobileContactAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {
    public MobileContactAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBean item) {

        TextView tagTv = helper.getView(R.id.add_tag);
        String content = null;
        int  userId = item.getId();
        if (0==userId) {
            helper.setImageResource(R.id.item_iv,R.drawable.sp_filled_accent);
            helper.setText(R.id.item_name,item.getName());
            helper.setText(R.id.item_content_tv,item.getPhoneNumber());
            //好友还未注册超视距
            tagTv.setBackgroundResource(R.drawable.sp_filled_accent);
            tagTv.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            content = "短信邀请";
        }else {
            ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(item.getHeadPortrait()),helper.getView(R.id.item_iv));
            helper.setText(R.id.item_name,item.getRemarksNickname());
            helper.setText(R.id.item_content_tv,String.format("超视距:%s",item.getAccountNumber()));
            if (item.isFriend()) {
                tagTv.setBackgroundResource(0);
                tagTv.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                content = "已添加";
            }else {
                tagTv.setBackgroundResource(R.drawable.sp_filled_accent);
                tagTv.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                content = "添加";
            }
        }

        tagTv.setText(content);
    }
}
