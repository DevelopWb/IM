package com.juntai.wisdom.im.chatlist.chat.selectFile;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.FileSelectBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.MyFileProvider;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-11 9:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-11 9:57
 */
public class SelectFileAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SelectFileAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_CHAT_FILE_DIR, R.layout.chat_item_file_dir);
        addItemType(MultipleItem.ITEM_CHAT_FILE_TEXT, R.layout.chat_item_file_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {

        FileSelectBean fileBean = (FileSelectBean) item.getObject();
        switch (item.getItemType()) {
            case MultipleItem.ITEM_CHAT_FILE_DIR:
                helper.setImageResource(R.id.dir_tag_iv, R.mipmap.dir_icon);
                helper.setText(R.id.dir_name_tv, fileBean.getFileName());
                helper.setText(R.id.dir_child_amount_tv, "文件：" + fileBean.getFileChilds());
                break;
            case MultipleItem.ITEM_CHAT_FILE_TEXT:
                helper.setImageResource(R.id.text_tag_iv, MyFileProvider.getFileResId(fileBean.getFileName()));
                helper.setText(R.id.text_name_tv, fileBean.getFileName());
                try {
                    helper.setText(R.id.text_size_tv, FileCacheUtils.formetFileSize(fileBean.getFileSize()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fileBean.isSelect()) {
                    helper.setImageResource(R.id.select_status_iv,R.mipmap.select_icon);
                }else {
                    helper.setImageResource(R.id.select_status_iv,R.mipmap.unselect_icon);
                }

                break;
            default:
                break;
        }


    }
}
