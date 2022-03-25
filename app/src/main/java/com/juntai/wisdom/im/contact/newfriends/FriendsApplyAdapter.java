package com.juntai.wisdom.im.contact.newfriends;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.NewFriendsBean;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-11-06 15:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-11-06 15:56
 */
public class FriendsApplyAdapter extends BaseQuickAdapter<NewFriendsBean.DataBean, BaseViewHolder> {
    public FriendsApplyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewFriendsBean.DataBean item) {
        helper.setText(R.id.item_name, item.getNickname());
        helper.setText(R.id.item_content_tv, item.getIntro());
        ImageLoadUtil.loadImage(mContext, UrlFormatUtil.getImageThumUrl(item.getHeadPortrait()), helper.getView(R.id.item_iv));
        TextView tag = helper.getView(R.id.add_tag);
        setBtStatus(helper,tag, item.getState());
    }

    /**
     * state;//1未读，2已读，3已过期，4接受
     *
     * @param status
     * @return
     */
    private void setBtStatus(BaseViewHolder helper,TextView tagTv, int status) {
        String content = null;
        switch (status) {
            case 1:
            case 2:
                helper.addOnClickListener(R.id.add_tag);
                tagTv.setBackgroundResource(R.drawable.sp_filled_gray_lighter);
                tagTv.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                content = "接受";
                break;
            case 3:
                tagTv.setBackgroundResource(0);
                tagTv.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                content = "已过期";
                break;
            case 4:
                tagTv.setBackgroundResource(0);
                tagTv.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                content = "已添加";
                break;
            default:
                break;
        }
        tagTv.setText(content);
    }
}
