package com.juntai.disabled.basecomponent.utils;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Author:wang_sir
 * Time:2019/4/16 11:39
 * Description:This is ActivityManagerTool
 */
public class ActivityManagerTool {

    LinkedList<Activity> linkedList = new LinkedList<>();

    public static ActivityManagerTool getInstance() {
        return ActivityManagerToolHolder.activityManagerTool;
    }

    /**
     * 添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (!linkedList.contains(activity)) {
            linkedList.add(activity);
        }
    }

    /**
     * 移除activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (linkedList.contains(activity)) {
            linkedList.remove(activity);
        }
    }

    /**
     * 关闭app
     */
    public void finishApp() {
        if (linkedList.size() > 0) {
            for (Activity activity : linkedList) {

                if (!activity.isFinishing()) {
                    activity.finish();
                }

            }
        }
    }

    private static class ActivityManagerToolHolder {
        private static ActivityManagerTool activityManagerTool = new ActivityManagerTool();
    }
}
