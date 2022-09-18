package com.efp.dvk.common.lang;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;

public interface EfpDvkNotifications {
     NotificationGroup EFP_DVK_VIEW_GROUP = NotificationGroupManager.getInstance()
            .getNotificationGroup("Efp Dvk View");
}
