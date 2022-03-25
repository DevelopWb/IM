package com.juntai.wisdom.im.chatlist.chat;

import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MyMenuBean;

/**
 * @Author: tobato
 * @Description: 作用描述  聊天界面 底部编辑聊天记录的适配器
 * @CreateDate: 2022-02-13 11:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-13 11:49
 */
public class BottomEditMsgAdapter extends BaseQuickAdapter<MyMenuBean, BaseViewHolder> {
    public BottomEditMsgAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMenuBean item) {
        helper.setImageResource(R.id.delete_msg_iv, item.getImageId());
    }
}
