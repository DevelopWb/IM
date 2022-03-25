package com.juntai.disabled.basecomponent.utils;


import com.juntai.disabled.basecomponent.app.BaseApplication;

/**
 * activity - action
 */
public class ActionConfig {
    /**登录*/
    //public static String ACTION_LOGIN = BaseApplication.app.getPackageName() + ".login";
    /**地图查看*/
    public final static String ACTION_LOCATION_LOOK = BaseApplication.app.getPackageName() + ".im.location.look";
    /**地图选择*/
    public final static String ACTION_LOCATION_SELTION = BaseApplication.app.getPackageName() + ".im.location.seltion";
    //    com.juntai.wisdom.bdmap.act

    /*=====================================广播==================================*/
    /**需要重新登录*/
    public static final String BROAD_LOGIN = BaseApplication.app.getPackageName() + ".login_error";
    /**视频录制*/
    public static final String BROAD_VIDEO = BaseApplication.app.getPackageName() + ".VideoBroadcastReceiver";
    /**案件详情*/
    public static final String BROAD_CASE_DETAILS = BaseApplication.app.getPackageName() + ".CaseDetails";
    /**未读消息更新*/
    public static final String UN_READ_MESSAG_TAG = BaseApplication.app.getPackageName() + ".unReadTag";
    /**个人积分刷新*/
    public static final String UPDATE_MY_SCORE = BaseApplication.app.getPackageName() + ".updateMyScore";
    /**场所管理从业人员刷新*/
    public static final String REFRASH_SITE_EMPLOYEE_LIST = BaseApplication.app.getPackageName() + ".employeeList";
    //    /**资讯发布 视频*/
    //    public static final String PUBLISH_NEWS_VIDEO = BaseApplication.app.getPackageName() + ".publishNewsVideo";
    //    /**资讯发布 图片*/
    //    public static final String PUBLISH_NEWS_PHOTO = BaseApplication.app.getPackageName() + ".publishNewsPhoto";
    /**资讯发布 保存草稿*/
    public static final String PUBLISH_NEWS_SAVE_DRAFTS = BaseApplication.app.getPackageName() + ".publishNewsSaveDrafts";
    /**资讯发布成功后关闭*/
    public static final String FINISH_AFTER_PUBISH = BaseApplication.app.getPackageName() + ".finishAfterPublish";
    /**资讯列表刷新*/
    public static final String UPDATE_NEWS_LIST = BaseApplication.app.getPackageName() + ".updateNewsList";
    /**调解列表更新*/
    public static final String REFRASH_CONCILIATION_LIST = BaseApplication.app.getPackageName() + ".conciliationList";
    /**登录更新*/
    public static final String BROAD_LOGIN_AFTER = BaseApplication.app.getPackageName() + ".login_after";
    /**退出重置界面*/
    public static final String BROAD_LOGIN_OUT = BaseApplication.app.getPackageName() + ".login_out";

    /*====================================服务====================================*/
    /**地图选择*/
    public final static String ACTION_SERVICE_LOCATION = BaseApplication.app.getPackageName() + ".service.location_icon";

}
