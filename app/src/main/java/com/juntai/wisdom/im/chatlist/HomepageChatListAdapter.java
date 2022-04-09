package com.juntai.wisdom.im.chatlist;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.CalendarUtil;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.negier.emojifragment.util.EmojiUtils;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/11/11 15:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/11 15:43
 */
public class HomepageChatListAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomepageChatListAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_CHAT_LIST_CONTACT, R.layout.item_chat_list);
        addItemType(MultipleItem.ITEM_CHAT_LIST_GROUP, R.layout.item_chat_list);
    }

    public List<MessageBodyBean> changeGsonToList(String gsonString) {
        Gson gson = new Gson();
        List<MessageBodyBean> list = gson.fromJson(gsonString, new TypeToken<List<MessageBodyBean>>() {
        }.getType());
        return list;
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        helper.addOnClickListener(R.id.sender_pic_iv);
        helper.addOnClickListener(R.id.receiver_pic_iv);
        switch (item.getItemType()) {

            case MultipleItem.ITEM_CHAT_LIST_CONTACT:
                ContactBean contactChatBean = (ContactBean) item.getObject();
                helper.setText(R.id.item_name_tv, contactChatBean.getRemarksNickname());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(contactChatBean.getHeadPortrait()), helper.getView(R.id.contact_name_iv));
                MessageBodyBean messageBodyBean = contactChatBean.getMessageBodyBean();
                switch (messageBodyBean.getMsgType()) {
                    case 0:
                        if (messageBodyBean.isDraft()) {
                            EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.item_content_tv), "[草稿] "+messageBodyBean.getContent(), 14,true);
                        }else {
                            EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.item_content_tv), messageBodyBean.getContent(), 14);
                        }
                        break;
                    case 1:
                        helper.setText(R.id.item_content_tv, "[图片]");
                        break;
                    case 2:
                        helper.setText(R.id.item_content_tv, "[视频]");
                        break;
                    case 3:
                        helper.setText(R.id.item_content_tv, "[语音]");
                        break;
                    case 4:
                        helper.setText(R.id.item_content_tv, "[视频通话]");
                        break;
                    case 5:
                        helper.setText(R.id.item_content_tv, "[语音通话]");
                        break;
                    case 6:
                        helper.setText(R.id.item_content_tv, "[位置]");
                        break;
                    case 7:
                        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                            helper.setText(R.id.item_content_tv, String.format("你向%s推荐了%s", messageBodyBean.getToNickname(), messageBodyBean.getOtherNickname()));
                        } else {
                            helper.setText(R.id.item_content_tv, HawkProperty.getGlobleMap().get(messageBodyBean.getFromUserId()) + "向你推荐了" + messageBodyBean.getOtherNickname());
                        }
                        break;

                    case 8:
                        helper.setText(R.id.item_content_tv, "[文件]");

                        break;
                    case 9:
                        helper.setText(R.id.item_content_tv, "[聊天记录]");
                        break;
                    case 10:
                        helper.setText(R.id.item_content_tv, messageBodyBean.getContent());
                        break;
                    default:
                        break;
                }

                helper.setGone(R.id.amount_tv, !messageBodyBean.isRead());
                helper.setText(R.id.msg_time_tv, CalendarUtil.formatDataOfChatList(messageBodyBean.getCreateTime()));
                break;
            case MultipleItem.ITEM_CHAT_LIST_GROUP:
                GroupDetailInfoBean groupBean = (GroupDetailInfoBean) item.getObject();
                helper.setText(R.id.item_name_tv, groupBean.getGroupName());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(groupBean.getGroupPicture()), helper.getView(R.id.contact_name_iv));
                MessageBodyBean groupMsgBean = groupBean.getLastMessage();
                String name = HawkProperty.getGlobleMap().containsKey(groupMsgBean.getFromUserId()) ? HawkProperty.getGlobleMap().get(groupMsgBean.getFromUserId()) : groupMsgBean.getFromNickname();


                switch (groupMsgBean.getMsgType()) {
                    case 0:
                        String groupContent = groupMsgBean.getContent();
                        String message = String.format("%s:%s", name, groupContent);

                        String atUserId = groupMsgBean.getAtUserId();
                        if (!TextUtils.isEmpty(atUserId)) {
                            //有@
                            if (String.valueOf(UserInfoManager.getUserId()).equals(atUserId.trim()) && !groupMsgBean.isRead()) {
                                message = String.format("[有人@我]%s:%s", name, groupContent);
                            }
                            EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.item_content_tv), message, 14);
                        }else {
                            if (groupMsgBean.isDraft()) {
                                EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.item_content_tv), "[草稿] "+message, 14,true);
                            }else {
                                EmojiUtils.showEmojiTextView(mContext, helper.getView(R.id.item_content_tv), message, 14);
                            }
                        }


                        break;
                    case 1:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[图片]"));
                        break;
                    case 2:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[视频]"));
                        break;
                    case 3:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[语音]"));
                        break;
                    case 4:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[视频通话]"));
                        break;
                    case 5:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[语音通话]"));
                        break;
                    case 6:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[位置]"));
                        break;
                    case 7:
                        if (groupMsgBean.getFromUserId() == UserInfoManager.getUserId()) {
                            helper.setText(R.id.item_content_tv, String.format("你向%s推荐了%s", groupBean.getGroupName(), groupMsgBean.getOtherNickname()));
                        } else {
                            helper.setText(R.id.item_content_tv, String.format("%s向你推荐了%s", name, groupMsgBean.getOtherNickname()));

                        }
                        break;

                    case 8:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[文件]"));

                        break;
                    case 9:
                        helper.setText(R.id.item_content_tv, String.format("%s:%s", name, "[聊天记录]"));
                        break;
                    case 10:
                        helper.setText(R.id.item_content_tv, groupMsgBean.getContent());
                        break;
                    default:
                        break;
                }

                helper.setGone(R.id.amount_tv, !groupMsgBean.isRead());
                helper.setText(R.id.msg_time_tv, CalendarUtil.formatDataOfChatList(groupMsgBean.getCreateTime()));
                break;
            default:
                break;
        }
    }

}
