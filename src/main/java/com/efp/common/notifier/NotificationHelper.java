/*
 * Copyright (c) 2019 Tony Ho. Some rights reserved.
 */

package com.efp.common.notifier;

import com.efp.common.constant.PluginContants;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;

/**
 * @author 肖均辉
 */
public class NotificationHelper {

    private static NotificationHelper instance = new NotificationHelper();

    private NotificationGroup notificationGroup = new NotificationGroup(PluginContants.GENERATOR_UI_TITLE, NotificationDisplayType.BALLOON, true);

    public static NotificationHelper getInstance() {
        return instance;
    }

    public void notifyInfo(String info, Project project) {
        Notification notification = notificationGroup.createNotification(info, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification, project);
    }

    public void notifyWarn(String warn, Project project) {
        Notification notification = notificationGroup.createNotification(warn, NotificationType.WARNING);
        Notifications.Bus.notify(notification, project);
    }
    public void notifyError(String error, Project project) {
        Notification notification = notificationGroup.createNotification(error, NotificationType.ERROR);
        Notifications.Bus.notify(notification, project);
    }
}
