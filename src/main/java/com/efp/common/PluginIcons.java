package com.efp.common;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginIcons {
    public static final Icon Plugin_Icon_Gameid = load("/icons/gameid2.png");

    private static Icon load(String path) {
        return IconManager.getInstance().getIcon(path, PluginIcons.class);
    }

}
