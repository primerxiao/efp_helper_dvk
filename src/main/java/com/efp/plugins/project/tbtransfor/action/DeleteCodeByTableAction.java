package com.efp.plugins.project.tbtransfor.action;

import com.efp.common.util.BookMarkUtils;
import com.efp.plugins.general.bookmark.bean.BookMarkLocal;
import com.efp.plugins.project.tbtransfor.ui.DeleteCodeByTableUI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;

/**
 * 导入标签动作
 * @author primerxiao
 */
public class DeleteCodeByTableAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DeleteCodeByTableUI deleteCodeByTableUI = new DeleteCodeByTableUI(e.getProject());
        deleteCodeByTableUI.show();
    }
}
