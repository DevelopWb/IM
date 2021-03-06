package com.juntai.wisdom.im.chat_module.chat.chatRecord;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.video.img.PicDisplayActivity;
import com.juntai.disabled.video.player.VideoNetPlayerActivity;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述   消息类型 聊天记录的详情
 * @date 2022-02-20 10:30
 */
public class ChatRecordDetailActivity extends BaseRecyclerviewActivity {
    List<MessageBodyBean> chatRecords = null;

    @Override
    public void initData() {
        super.initData();
        if (getIntent() != null) {
            String content = getIntent().getStringExtra(BASE_STRING);
            if (!TextUtils.isEmpty(content)) {
                chatRecords = changeGsonToList(content);
                MessageBodyBean chatContentBean = chatRecords.get(0);
                String title = chatContentBean.getGroupId() > 0 ? "群聊" : String.format("%s与%s", chatContentBean.getFromNickname(), chatContentBean.getToNickname());
                setTitleName(title);
                for (MessageBodyBean chatRecord : chatRecords) {
                    initAdapterDataFromMsgTypes(chatRecord);
                }
            }
        }

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
                startToMsgDetail(mContext, messageBodyBean);
            }
        });
    }



    //查看图片
    private void displayPicVideo(MessageBodyBean messagePicBean) {
        String content = messagePicBean.getContent();
        if (1 == FileCacheUtils.getFileType(content)) {
            ArrayList<String> photos = new ArrayList<>();
            photos.add(UrlFormatUtil.getImageOriginalUrl(content));
            startActivity(new Intent(mContext, PicDisplayActivity.class)
                    .putExtra(PicDisplayActivity.IMAGEPATHS, photos)
                    .putExtra(PicDisplayActivity.IMAGEITEM, 0));
        } else if (2 == FileCacheUtils.getFileType(content)) {
            startActivity(new Intent(mContext, VideoNetPlayerActivity.class).putExtra(
                    "path", UrlFormatUtil.getImageOriginalUrl(content)));
        }

    }
    private void initAdapterDataFromMsgTypes(MessageBodyBean messageBean) {
        // TODO: 2022/4/18 新增消息类型的时候 这个地方需要注意
        switch (messageBean.getMsgType()) {
            case 0:
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_TEXT_MSG, messageBean));
                break;
            case 1:
            case 2:
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_PIC_VIDEO, messageBean));
                break;
            case 3:
                //发送语音
                if (UserInfoManager.getUserId() == messageBean.getFromUserId()) {
                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_SEND_AUDIO, messageBean));
                } else {
                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_RECEIVE_AUDIO, messageBean));
                }
                break;
            case 5:
            case 4:
                //视频通话
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_VIDEO_CALL, messageBean));
                break;
            case 6:
                //位置信息
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_LOCATE, messageBean));
                break;
            case 7:
                //名片
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_CARD, messageBean));
                break;
            case 8:
                //文件
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE, messageBean));
                break;
            case 9:
                //文件
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_RECORD, messageBean));
                break;
            case 11:
                //外部分享的链接
                baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_OUTSIDE_SHARE, messageBean));
                break;
            default:
                break;
        }
    }
    public List<MessageBodyBean> changeGsonToList(String gsonString) {
        Gson gson = new Gson();
        List<MessageBodyBean> list = gson.fromJson(gsonString, new TypeToken<List<MessageBodyBean>>() {
        }.getType());
        return list;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    protected void getRvAdapterData() {

    }

    @Override
    protected boolean enableRefresh() {
        return false;
    }

    @Override
    protected boolean enableLoadMore() {
        return false;
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new ChatRecordDeatilAdapter(null);
    }
}