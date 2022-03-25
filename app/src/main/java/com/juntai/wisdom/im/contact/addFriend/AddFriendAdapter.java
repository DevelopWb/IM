package com.juntai.wisdom.im.contact.addFriend;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.AddFriendMenuBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-12-13 16:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-13 16:56
 */
public class AddFriendAdapter extends BaseQuickAdapter<AddFriendMenuBean, BaseViewHolder> {
    public AddFriendAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddFriendMenuBean item) {
        helper.setGone(R.id.add_tag,false);

        ImageLoadUtil.loadSquareImage(mContext,item.getImageRes(),helper.getView(R.id.item_iv));
        helper.setText(R.id.item_name,item.getName());
        helper.setText(R.id.item_content_tv,item.getDes());

    }
}
