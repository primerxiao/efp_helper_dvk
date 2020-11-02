package com.efp.common.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;

/**
 * 插件提示信息工具类
 */
public class NotifyUtils {

    private final static NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup("Efp Plugin Msg", NotificationDisplayType.BALLOON, true);

    /**
     * 展示错误信息
     * @param msg 信息内容
     */
    public static Notification notifyError(String msg) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(msg, NotificationType.ERROR);
        notification.notify(null);
        return notification;
    }

    public static Notification notifyInfo(String msg) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(msg, NotificationType.INFORMATION);
        notification.notify(null);
        return notification;
    }
}
