package com.juntai.wisdom.im.chat_module.chat.chatRecord;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.utils.OperateMsgUtil;
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

    public ChatRecordAdapter(int layoutResId, boolean changeTextSize) {
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
        } else {
            sigleTextTv.setTextSize(12);
        }
        String content = OperateMsgUtil.getContent( messageBodyBean);
        if (0 == messageBodyBean.getMsgType()) {
            EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.single_text_tv), content, 14);
        } else {
            helper.setText(R.id.single_text_tv, content);

        }

    }

}
