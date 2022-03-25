package com.juntai.wisdom.im.mine.myCollect;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.CalendarUtil;
import com.juntai.wisdom.im.utils.MyFileProvider;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.List;

/**
 * Describe:
 * Create by tobato
 * 2020/3/7
 * email:954101549@qq.com
 */
public class MyCollectAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyCollectAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_COLLECTION_TEXT, R.layout.collect_text);
        addItemType(MultipleItem.ITEM_COLLECTION_PIC, R.layout.collect_pic);
        addItemType(MultipleItem.ITEM_COLLECTION_VIDEO, R.layout.collect_video);
        addItemType(MultipleItem.ITEM_COLLECTION_AUDIO, R.layout.collect_audio);
        addItemType(MultipleItem.ITEM_COLLECTION_FILE, R.layout.collect_file);
        addItemType(MultipleItem.ITEM_COLLECTION_LOCATE, R.layout.collect_locate);
    }


    @Override
    protected void convert(BaseViewHolder helper, MultipleItem multipleItem) {
        MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
        if (2==messageBodyBean.getChatType()) {
            //群聊
            helper.setText(R.id.collect_from_tv,messageBodyBean.getGroupNickname());
        }else {
            helper.setText(R.id.collect_from_tv,messageBodyBean.getFromNickname());
        }
        helper.setText(R.id.collect_time_tv, CalendarUtil.formatCollectDataOfChatList(messageBodyBean.getCollectionCreateTime()));
        switch (multipleItem.getItemType()) {
            case MultipleItem.ITEM_COLLECTION_TEXT:
                helper.setText(R.id.collect_msg_content_tv,messageBodyBean.getContent());
                break;
            case MultipleItem.ITEM_COLLECTION_PIC:
                if (messageBodyBean.getFromUserId()== UserInfoManager.getUserId()) {
                    ImageLoadUtil.loadSquareImage(mContext,messageBodyBean.getLocalCatchPath(),helper.getView(R.id.collect_pic_iv));

                }else {
                    ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(messageBodyBean.getContent()),helper.getView(R.id.collect_pic_iv));

                }
                break;
            case MultipleItem.ITEM_COLLECTION_VIDEO:
                if (messageBodyBean.getFromUserId()== UserInfoManager.getUserId()) {
                    ImageLoadUtil.loadVideoScreenshot(mContext, messageBodyBean.getLocalCatchPath(), helper.getView(R.id.collect_video_iv),  new ImageLoadUtil.OnImageLoadSuccess() {
                        @Override
                        public void loadSuccess(int width, int height) {
                            //加载成功
                        }
                    });
                }else {
                    if (!TextUtils.isEmpty(FileCacheUtils.isVideoFileExistsInDir(getSavedFileName(messageBodyBean),true))) {
                        //本地已经缓存
                        ImageLoadUtil.loadVideoScreenshot(mContext, FileCacheUtils.getVideoFileInDir(getSavedFileName(messageBodyBean),true), helper.getView(R.id.receiver_pic_video_iv),   new ImageLoadUtil.OnImageLoadSuccess() {
                            @Override
                            public void loadSuccess(int width, int height) {

                            }
                        });
                    }else {
                        ImageLoadUtil.loadVideoScreenshot(mContext, UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent()), helper.getView(R.id.collect_video_iv),  new ImageLoadUtil.OnImageLoadSuccess() {
                            @Override
                            public void loadSuccess(int width, int height) {
                                //加载成功
                            }
                        });
                    }


                }

                break;
            case MultipleItem.ITEM_COLLECTION_AUDIO:
                helper.setText(R.id.audio_time_iv,messageBodyBean.getDuration()+ "''");
                break;
            case MultipleItem.ITEM_COLLECTION_FILE:
                helper.setImageResource(R.id.collect_file_iv, MyFileProvider.getFileResId(messageBodyBean.getFileName()));
                helper.setText(R.id.collect_file_name_tv,messageBodyBean.getFileName());
                helper.setText(R.id.collect_file_size_tv,messageBodyBean.getFileSize());

                break;
            case MultipleItem.ITEM_COLLECTION_LOCATE:
                helper.setText(R.id.collect_addr_name_tv,messageBodyBean.getAddrName());
                helper.setText(R.id.collect_addr_des_tv,messageBodyBean.getAddrDes());
                break;
            default:
                break;
        }

    }
    /**
     * 获取文件名称
     *
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileName(MessageBodyBean messageBodyBean) {
        String content = messageBodyBean.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/") + 1, content.length());
        }
        return content;
//        return messageBodyBean.getFileName().substring(0,messageBodyBean.getFileName().lastIndexOf("."))+content;
    }
}
