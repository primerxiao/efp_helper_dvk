package com.efp.common.constant;

import com.intellij.ui.IconManager;

import javax.swing.*;

/**
 * 图标类
 * @author primerxiao
 */
public class PluginIcons {
    private static Icon load(String path) {
        return IconManager.getInstance().getIcon(path, PluginIcons.class);
    }
    public static final class Menus {
        public final static Icon EFP_ICON = load("/icons/efp_icon.svg");
    }
}
