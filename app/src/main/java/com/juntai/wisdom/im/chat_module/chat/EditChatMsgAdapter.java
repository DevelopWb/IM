package com.juntai.wisdom.im.chat_module.chat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.HomePageMenuBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-21 10:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-21 10:47
 */
public class EditChatMsgAdapter extends BaseQuickAdapter<HomePageMenuBean, BaseViewHolder> {
    public EditChatMsgAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageMenuBean item) {
        helper.setImageResource(R.id.item_pic_iv, item.getHeadRes());
        helper.setText(R.id.item_text_iv,item.getName());
    }
}
