package com.juntai.disabled.basecomponent.widght;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/4/16 14:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/16 14:20
 */
public class BottomDialogAdapter extends BaseQuickAdapter<BaseMenuBean, BaseViewHolder> {

    public BottomDialogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseMenuBean item) {
        helper.setText(R.id.single_text_tv, item.getName());
    }
}
