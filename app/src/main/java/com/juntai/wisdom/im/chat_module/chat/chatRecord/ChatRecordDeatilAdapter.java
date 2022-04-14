package com.juntai.wisdom.im.chat_module.chat.chatRecord;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.MyFileProvider;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.negier.emojifragment.util.EmojiUtils;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述  聊天记录详情适配器
 * @CreateDate: 2020/11/11 15:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/11 15:43
 */
public class ChatRecordDeatilAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public List<MessageBodyBean> changeGsonToList(String gsonString) {
        Gson gson = new Gson();
        List<MessageBodyBean> list = gson.fromJson(gsonString, new TypeToken<List<MessageBodyBean>>() {
        }.getType());
        return list;
    }
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatRecordDeatilAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_CHAT_TEXT_MSG, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_CHAT_PIC_VIDEO, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_CHAT_VIDEO_CALL, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_SEND_AUDIO, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_RECEIVE_AUDIO, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_CHAT_LOCATE, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_CHAT_CARD, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_CHAT_FILE, R.layout.item_chat_record_detail);
        addItemType(MultipleItem.ITEM_CHAT_RECORD, R.layout.item_chat_record_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        MessageBodyBean messageBodyBean = (MessageBodyBean) item.getObject();
        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(messageBodyBean.getFromHead()), helper.getView(R.id.msg_sender_iv));
        helper.setText(R.id.msg_sender_name_tv, messageBodyBean.getFromNickname());
        helper.setText(R.id.msg_sender_sendtime_tv, messageBodyBean.getCreateTime());
        FrameLayout frameLayout = helper.getView(R.id.msg_sender_content_fl);
        View view = null;
        switch (item.getItemType()) {
            case MultipleItem.ITEM_CHAT_RECORD:
                // : 2022-02-19 聊天记录
                view = View.inflate(mContext, R.layout.chat_record_multi_record, null);
                TextView  chatrecordTitle = view.findViewById(R.id.chat_record_title_tv);
                frameLayout.addView(view);
                List<MessageBodyBean> chatRecords = null;
                LinearLayoutManager manager = null;
                ChatRecordAdapter chatRecordAdapter = null;
                RecyclerView receiverRv =view.findViewById(R.id.recyclerview);
                String title = null;

                String chatRecord = messageBodyBean.getContent();
                if (!TextUtils.isEmpty(chatRecord)) {
                    chatRecords = changeGsonToList(chatRecord);
                    if (chatRecords.size() > 5) {
                        chatRecords = chatRecords.subList(0, 4);
                    }
                    MessageBodyBean chatContentBean = chatRecords.get(0);
                    title = chatContentBean.getGroupId() > 0 ? "群聊的聊天记录" : String.format("%s与%s的聊天记录", chatContentBean.getFromNickname(), chatContentBean.getToNickname());
                    chatrecordTitle.setText(title);
                    manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    chatRecordAdapter = new ChatRecordAdapter(R.layout.single_text_layout2,true);
                    chatRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                            响应父rv的item的点击事件
                            helper.itemView.performClick();


                        }
                    });
                }
                receiverRv.setLayoutManager(manager);
                receiverRv.setAdapter(chatRecordAdapter);
                chatRecordAdapter.setNewData(chatRecords);
                break;
            case MultipleItem.ITEM_CHAT_FILE:
                view = View.inflate(mContext, R.layout.chat_record_file, null);
                frameLayout.addView(view);
                ImageView fileIv = view.findViewById(R.id.chat_record_file_type_iv);
                fileIv.setImageResource(MyFileProvider.getFileResId(messageBodyBean.getFileName()));
                ((TextView) view.findViewById(R.id.chat_record_file_name_tv)).setText(messageBodyBean.getFileName());
                ((TextView) view.findViewById(R.id.chat_record_file_size_tv)).setText(messageBodyBean.getFileSize());
                break;
            case MultipleItem.ITEM_CHAT_CARD:
                view = View.inflate(mContext, R.layout.single_text_layout2, null);
                frameLayout.addView(view);
                ((TextView) view.findViewById(R.id.single_text_tv)).setText("[个人名片]");
                break;
            case MultipleItem.ITEM_CHAT_LOCATE:
                view = View.inflate(mContext, R.layout.chat_record_file, null);
                frameLayout.addView(view);
                ((ImageView) view.findViewById(R.id.chat_record_file_type_iv)).setImageResource(R.mipmap.ic_func_location);
                ((TextView) view.findViewById(R.id.chat_record_file_name_tv)).setText(messageBodyBean.getAddrName());
                ((TextView) view.findViewById(R.id.chat_record_file_size_tv)).setText(messageBodyBean.getAddrDes());
                break;
            case MultipleItem.ITEM_CHAT_TEXT_MSG:
                view = View.inflate(mContext, R.layout.single_text_layout2, null);
                TextView sigleTextTv = view.findViewById(R.id.single_text_tv);
                sigleTextTv.setAutoLinkMask(Linkify.WEB_URLS);
                frameLayout.addView(view);
                EmojiUtils.showEmojiTextView(mContext, (TextView) view.findViewById(R.id.single_text_tv), messageBodyBean.getContent(), 14);

                break;
            case MultipleItem.ITEM_CHAT_VIDEO_CALL:
                //视频通话
                break;
            case MultipleItem.ITEM_RECEIVE_AUDIO:
            case MultipleItem.ITEM_SEND_AUDIO:
                break;

            case MultipleItem.ITEM_CHAT_PIC_VIDEO:
                if (1 == messageBodyBean.getMsgType()) {
                    //图片
                    view = View.inflate(mContext, R.layout.single_pic, null);
                    frameLayout.addView(view);
                    ImageLoadUtil.loadImage(mContext, UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent()), view.findViewById(R.id.sigle_iv));
                } else {
                    //视频
                    view = View.inflate(mContext, R.layout.chat_record_video, null);
                    frameLayout.addView(view);
                    ImageLoadUtil.loadVideoScreenshotSquareImage(mContext, UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent()), view.findViewById(R.id.chat_record_video_iv), 1 * 1000 * 1000, new ImageLoadUtil.OnImageLoadSuccess() {
                        @Override
                        public void loadSuccess(int width, int height) {
                            helper.setText(R.id.chat_record_video_size_tv, messageBodyBean.getDuration());
                        }
                    });

                }
                break;
            default:
                break;
        }
    }


}
