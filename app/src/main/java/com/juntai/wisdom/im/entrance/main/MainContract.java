package com.juntai.wisdom.im.entrance.main;

import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.mvp.IView;

/**
 * Describe: 首页
 * Create by tobato
 * 2020-8-8
 * email:954101549@qq.com
 */
public interface MainContract {
    String DELETE_NEWS_DRAFTS = "delete_news_drafts";
    String UPLOAD_HISTORY = "upload_history";
     String UPLOAD_IMAGES = "uploadPictures";
     String UPLOAD_MAP_SHOT = "uploadmapshot";
     //发送语音
     String UPLOAD_AUDIO_FILE = "audiofile";
     String SEND_VIDEO_CALL = "request_video_call";

    String CONTACT_NEW_FRIEND = "新的朋友";
    String ADD_NEW_FRIEND = "添加朋友";
    String DELETE_CURRENT_CHAT = "删除该聊天";
    String SCAN_IT = "扫一扫";
    String CREAT_GROUP_CHAT = "发起群聊";
    String  SUGGESTION_HELP= "意见反馈";
    String CONTACT_GROUP_CHAT = "群聊";
    String CONTACT_GROUP_ALL_PEOPLE = "所有人";
    String GROUP_QRCODE = "群二维码";
    String REMOVE_BLOCK_LIST = "移除黑名单";
    String SELECT_MORE = "多选";
    String QUOTE = "引用";
    String DELETE_MSG = "删除";
    String FORWARD_MSG = "转发";
    String COPY_MSG = "复制";
    String COLLECTION_MSG = "收藏";

    interface IBaseView extends IView {
    }

    interface IMainPagePresent extends IPresenter<IBaseView> {

    }
}
