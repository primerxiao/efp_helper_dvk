package com.efp.dvk.common.util;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class NotifyUtils {

    public static void notifyByType(@Nullable Project project,
                                    NotificationType notificationType,
                                    String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("com.efp.dvk.notification.group")
                .createNotification(content, notificationType)
                .notify(project);
    }

    public static void error(@Nullable Project project, String content) {
        notifyByType(project, NotificationType.ERROR, content);
    }

    public static void info(@Nullable Project project, String content) {
        notifyByType(project, NotificationType.INFORMATION, content);
    }

    public static void warn(@Nullable Project project, String content) {
        notifyByType(project, NotificationType.WARNING, content);
    }

}