package com.juntai.wisdom.im.chat_module.chat.selectFile;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.FileResourceBean;

/**
 * Author:wang_sir
 * Time:2019/7/5 16:43
 * Description:This is WorkOrderTypeAdapter
 */
public class FileResourceAdapter extends BaseQuickAdapter<FileResourceBean, BaseViewHolder> {
    public FileResourceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileResourceBean item) {
        int position = helper.getAdapterPosition();
        if (position==mData.size()-1) {
            helper.setGone(R.id.file_resource_div,false);
        }else{
            helper.setGone(R.id.file_resource_div,true);
        }
        ImageLoadUtil.loadImage(mContext,item.getFileResourceIcon(),helper.getView(R.id.file_resource_iv));
        helper.setText(R.id.file_resource_name_tv, item.getFileResourceName());
        if (item.isSelected()) {
            helper.setGone(R.id.file_resource_selected_tag_tv,true);
            helper.setTextColor(R.id.file_resource_name_tv, ContextCompat.getColor(mContext,R.color.colorAccent));
        }else{
            helper.setGone(R.id.file_resource_selected_tag_tv,false);
            helper.setTextColor(R.id.file_resource_name_tv, ContextCompat.getColor(mContext,R.color.text_title));

        }
    }
}
