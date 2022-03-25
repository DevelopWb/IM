package com.juntai.wisdom.im.search;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.CalendarUtil;
import com.juntai.wisdom.im.utils.MyFileProvider;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/4/15 10:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/15 10:10
 */
public class SearchAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SearchAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_TITLE, R.layout.item_layout);
        addItemType(MultipleItem.ITEM_CONTACT, R.layout.item_contact);
        addItemType(MultipleItem.ITEM_GROUP, R.layout.item_contact);


        addItemType(MultipleItem.ITEM_COLLECTION_TEXT, R.layout.collect_text);
        addItemType(MultipleItem.ITEM_COLLECTION_PIC, R.layout.collect_pic);
        addItemType(MultipleItem.ITEM_COLLECTION_VIDEO, R.layout.collect_video);
        addItemType(MultipleItem.ITEM_COLLECTION_AUDIO, R.layout.collect_audio);
        addItemType(MultipleItem.ITEM_COLLECTION_FILE, R.layout.collect_file);
        addItemType(MultipleItem.ITEM_COLLECTION_LOCATE, R.layout.collect_locate);

    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (item.getItemType()) {
            case MultipleItem.ITEM_TITLE:
                String title = (String) item.getObject();
                helper.setText(R.id.item_title_name_tv, title);

                break;
            case MultipleItem.ITEM_CONTACT:
                ContactBean contactBean = (ContactBean) item.getObject();
                helper.setText(R.id.item_name, contactBean.getRemarksNickname());
                switch (contactBean.getRemarksNickname()) {
                    case MainContract.CONTACT_NEW_FRIEND:
                        ImageLoadUtil.loadSquareImage(mContext, R.mipmap.newfriends_icon, helper.getView(R.id.item_iv));
                        break;
                    case MainContract.CONTACT_GROUP_CHAT:
                        ImageLoadUtil.loadSquareImage(mContext, R.mipmap.group_icon, helper.getView(R.id.item_iv));
                        break;
                    default:
                        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(contactBean.getHeadPortrait()), helper.getView(R.id.item_iv));
                        break;
                }
                if (contactBean.isHasEndLine()) {
                    helper.setGone(R.id.item_divider_v, true);
                } else {
                    helper.setGone(R.id.item_divider_v, false);
                }
                helper.setGone(R.id.amount_tv, false);
                break;
            case MultipleItem.ITEM_GROUP:
                GroupListBean.DataBean  groupBean = (GroupListBean.DataBean) item.getObject();
                helper.setText(R.id.item_name, groupBean.getGroupName());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(groupBean.getGroupPicture()), helper.getView(R.id.item_iv));

                if (groupBean.isHasEndLine()) {
                    helper.setGone(R.id.item_divider_v, true);
                } else {
                    helper.setGone(R.id.item_divider_v, false);
                }
                helper.setGone(R.id.amount_tv, false);
                break;




            case MultipleItem.ITEM_COLLECTION_TEXT:
                helper.setText(R.id.collect_msg_content_tv,getMessageBodyBean(helper,item).getContent());
                break;
            case MultipleItem.ITEM_COLLECTION_PIC:
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(getMessageBodyBean(helper,item).getContent()),helper.getView(R.id.collect_pic_iv));
                break;
            case MultipleItem.ITEM_COLLECTION_VIDEO:
                ImageLoadUtil.loadVideoScreenshot(mContext, UrlFormatUtil.getImageThumUrl(getMessageBodyBean(helper,item).getContent()), helper.getView(R.id.collect_video_iv),  new ImageLoadUtil.OnImageLoadSuccess() {
                    @Override
                    public void loadSuccess(int width, int height) {
                        //加载成功
                    }
                });
                break;
            case MultipleItem.ITEM_COLLECTION_AUDIO:
                helper.setText(R.id.audio_time_iv,getMessageBodyBean(helper,item).getDuration()+ "''");
                break;
            case MultipleItem.ITEM_COLLECTION_FILE:
                helper.setImageResource(R.id.collect_file_iv, MyFileProvider.getFileResId(getMessageBodyBean(helper,item).getFileName()));
                helper.setText(R.id.collect_file_name_tv,getMessageBodyBean(helper,item).getFileName());
                helper.setText(R.id.collect_file_size_tv,getMessageBodyBean(helper,item).getFileSize());

                break;
            case MultipleItem.ITEM_COLLECTION_LOCATE:

                helper.setText(R.id.collect_addr_name_tv,getMessageBodyBean(helper,item).getAddrName());
                helper.setText(R.id.collect_addr_des_tv,getMessageBodyBean(helper,item).getAddrDes());
                break;
//            case MultipleItem.ITEM_LOAD_MORE:
//                SearchMoreBean searchMoreBean = (SearchMoreBean) item.getObject();
//                String msg = searchMoreBean.getMsg();
//                if (!mContext.getString(R.string.load_more_no_data).equals(msg)) {
//                    helper.setGone(R.id.item_load_more_tv, true);
//                } else {
//                    helper.setGone(R.id.item_load_more_tv, false);
//                }
//                helper.setText(R.id.item_load_more_tv, searchMoreBean.getMsg());
//                break;





            default:
                break;
        }
    }

    private MessageBodyBean getMessageBodyBean(BaseViewHolder helper,MultipleItem item) {
        MessageBodyBean messageBodyBean = (MessageBodyBean) item.getObject();
        if (2==messageBodyBean.getChatType()) {
            //群聊
            helper.setText(R.id.collect_from_tv,messageBodyBean.getGroupNickname());
        }else {
            helper.setText(R.id.collect_from_tv,messageBodyBean.getFromNickname());
        }
        helper.setText(R.id.collect_time_tv, CalendarUtil.formatCollectDataOfChatList(messageBodyBean.getCollectionCreateTime()));
        return messageBodyBean;
    }


}