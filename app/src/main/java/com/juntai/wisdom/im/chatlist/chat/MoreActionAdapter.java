package com.juntai.wisdom.im.chatlist.chat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MyMenuBean;

/**
 * @Author: tobato
 * @Description: 作用描述   更多功能适配器
 * @CreateDate: 2021-12-04 16:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-04 16:31
 */
public class MoreActionAdapter extends BaseQuickAdapter<MyMenuBean, BaseViewHolder> {
    public MoreActionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMenuBean item) {

        helper.setText(R.id.more_action_tv, item.getName());
        ImageLoadUtil.loadSquareImage(mContext, item.getImageId(), helper.getView(R.id.more_action_iv));
    }
}
