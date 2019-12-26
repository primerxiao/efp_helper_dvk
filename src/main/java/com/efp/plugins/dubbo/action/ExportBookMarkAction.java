package com.efp.plugins.dubbo.action;

import com.efp.plugins.dubbo.bean.BookMarkData;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出标签动作
 */
public class ExportBookMarkAction extends AnAction {

    private String defaulaltBookMarkXmlFilePath = "C:\\Users\\HIFeng\\Documents\\efp_plugin";

    private boolean createFlag = false;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), e.getProject(), null);
        if (virtualFile != null) {
        }
        VirtualFile data = e.getData(LangDataKeys.VIRTUAL_FILE);
        List<BookMarkData> bookMarkData = loadBookMark(e.getProject());
        if (bookMarkData.isEmpty()) {
            Messages.showErrorDialog("标签数据为空，无法进行导出！", "提示信息：");
            return;
        }
        //文件名 包名 标签行索引 标签描述
        //bookmarkManager.addTextBookmark(data, 22, "测试添加");

        File file = getBookMarksXmlFile(e);

        if (createFlag) {
            //新建
       /*     XmlTag rootTag = xmlFile.getRootTag();
            rootTag.setName("BookMarks");
            XmlTag bookMark = rootTag.createChildTag("BookMark", rootTag.getNamespace(), null, false);
            bookMark.setAttribute("lineIndex", "0");
            bookMark.setAttribute("descript", "1");
            bookMark.setAttribute("fileName", "2");
            bookMark.setAttribute("fileSrcPath", "3");
            rootTag.add(bookMark);*/
            return;
        }
        //非新建

    }

    @NotNull
    private File getBookMarksXmlFile(AnActionEvent e) {
        final Project project = e.getProject();
        File file = new File(defaulaltBookMarkXmlFilePath + "//BookMarks.xml");
        if (!file.exists()) {
            try {
                FileUtils.forceMkdir(new File(defaulaltBookMarkXmlFilePath));
                file.createNewFile();
                createFlag = true;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return file;
    }

    private List<BookMarkData> loadBookMark(Project project) {
        ArrayList<BookMarkData> bookMarkDataArrayList = new ArrayList<>();
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();
        for (Bookmark validBookmark : validBookmarks) {
            BookMarkData bookMarkData = new BookMarkData();
            bookMarkData.setDescript(validBookmark.getDescription());
            bookMarkData.setLineIndex(validBookmark.getLine());
            bookMarkData.setFileName(validBookmark.getFile().getName());
            bookMarkData.setFileSrcPath(validBookmark.getFile().getPath());
            bookMarkDataArrayList.add(bookMarkData);
        }
        return bookMarkDataArrayList;
    }

}
