package com.juntai.wisdom.im.chat_module.chat.chatRecord;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.negier.emojifragment.util.EmojiUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-19 15:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-19 15:08
 */
public class ChatRecordAdapter extends BaseQuickAdapter<MessageBodyBean, BaseViewHolder> {
    boolean changeTextSize;
    public ChatRecordAdapter(int layoutResId,boolean changeTextSize) {
        super(layoutResId);
        this.changeTextSize = changeTextSize;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBodyBean messageBodyBean) {
        TextView sigleTextTv = helper.getView(R.id.single_text_tv);
        sigleTextTv.setLines(1);
        sigleTextTv.setEllipsize(TextUtils.TruncateAt.END);
        if (changeTextSize) {
            sigleTextTv.setTextSize(10);
        }else {
            sigleTextTv.setTextSize(12);
        }
        switch (messageBodyBean.getMsgType()) {
            case 0:
                EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.single_text_tv), String.format("%s:%s",messageBodyBean.getFromNickname(),messageBodyBean.getContent()), 14);
                break;
            case 1:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[图片]"));
                break;
            case 2:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[视频]"));
                break;
            case 3:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[语音]"));
                break;
            case 4:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[视频通话]"));
                break;
            case 5:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[语音通话]"));
                break;
            case 6:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[位置]"));
                break;
            case 7:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[名片]"));
                break;

            case 8:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[文件]"));

                break;
            case 9:
                helper.setText(R.id.single_text_tv, String.format("%s:%s",messageBodyBean.getFromNickname(),"[聊天记录]"));

                break;
            case 11:
                helper.setText(R.id.single_text_tv, String.format("%s:%s%s",messageBodyBean.getFromNickname(),"[链接]",messageBodyBean.getShareTitle()));

                break;
            default:
                break;
        }

    }
}
