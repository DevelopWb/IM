package com.juntai.wisdom.im.chat_module.searchchat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.utils.CalendarUtil;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.negier.emojifragment.util.EmojiUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-25 10:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-25 10:52
 */
public class SearchChatAdapter extends BaseQuickAdapter<MessageBodyBean, BaseViewHolder>   {
    public SearchChatAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBodyBean messageBodyBean) {
        helper.setText(R.id.item_name_tv, messageBodyBean.getFromNickname());
        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(messageBodyBean.getFromHead()), helper.getView(R.id.contact_name_iv));
        switch (messageBodyBean.getMsgType()) {
            case 0:
                EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.item_content_tv), messageBodyBean.getContent(),14);
                break;
            case 1:
                helper.setText(R.id.item_content_tv, "[图片]");
                break;
            case 2:
                helper.setText(R.id.item_content_tv, "[视频]");
                break;
            case 3:
                helper.setText(R.id.item_content_tv, "[语音]");
                break;
            case 4:
                helper.setText(R.id.item_content_tv, "[视频通话]");
                break;
            case 5:
                helper.setText(R.id.item_content_tv, "[语音通话]");
                break;
            case 6:
                helper.setText(R.id.item_content_tv, "[位置]");
                break;
            case 7:
                if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                    helper.setText(R.id.item_content_tv, String.format("你向%s推荐了%s", messageBodyBean.getToNickname(), messageBodyBean.getOtherNickname()));
                } else {
                    helper.setText(R.id.item_content_tv, UserInfoManager.getContactRemarkName(messageBodyBean) + "向你推荐了" + messageBodyBean.getOtherNickname());

                }
                break;

            case 8:
                helper.setText(R.id.item_content_tv, "[文件]");

                break;
            case 9:
                helper.setText(R.id.item_content_tv, "[聊天记录]");

                break;
            default:
                break;
        }
        helper.setGone(R.id.amount_tv, false);
        helper.setText(R.id.msg_time_tv, CalendarUtil.formatDataOfChatList(messageBodyBean.getCreateTime()));
    }
}
