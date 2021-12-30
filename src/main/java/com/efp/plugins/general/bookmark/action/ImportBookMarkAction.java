package com.efp.plugins.general.bookmark.action;

import com.efp.common.util.BookMarkUtils;
import com.efp.plugins.general.bookmark.bean.BookMarkLocal;
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
public class ImportBookMarkAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), e.getProject(), null);
        if (virtualFile == null) {
            return;
        }
        try {
            final FileReader fileReader = new FileReader(virtualFile.getPath());
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            final StringBuilder stringBuilder = new StringBuilder();
            String readLineStr = null;
            while ((readLineStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(readLineStr);
            }
            if (StringUtils.isEmpty(stringBuilder.toString())) {
                return;
            }
            final List<BookMarkLocal> bookmarks = new Gson().fromJson(stringBuilder.toString(), new TypeToken<List<BookMarkLocal>>() {
            }.getType());
            if (bookmarks == null && bookmarks.isEmpty()) {
                return;
            }
            final BookmarkManager bookmarkManager = BookmarkManager.getInstance(e.getProject());
            for (BookMarkLocal bookmark : bookmarks) {
                //判断bookmark是否已经存在
                if (BookMarkUtils.checkBookmarkExist(bookmark,e.getProject())) {
                    continue;
                }
                bookmarkManager.addTextBookmark(VfsUtil.findFile(new File(bookmark.getFileSrcPath()).toPath(),true), bookmark.getLineIndex(), bookmark.getDescript());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
