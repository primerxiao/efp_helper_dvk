package com.efp.plugins.general.bookmark.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.BookMarkUtils;
import com.google.gson.GsonBuilder;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 导出标签动作
 */
public class ExportBookMarkAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final FileChooserDescriptor singleFileDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), e.getProject(), null);
        if (virtualFile == null) {
            return;
        }
        final List<Bookmark> bookmarks = BookMarkUtils.loadBookMark(e.getProject());
        if (bookmarks.isEmpty()) {
            Messages.showErrorDialog("BookMark Data Is Empty！", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        //文件名 包名 标签行索引 标签描述
        //bookmarkManager.addTextBookmark(data, 22, "测试添加");

        final File file = new File(virtualFile.getPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        final String s = new GsonBuilder().create().toJson(BookMarkUtils.covertToLocal(bookmarks));
        try {
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(s);
            fileWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
