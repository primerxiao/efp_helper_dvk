package com.efp.common.util;

import com.efp.plugins.general.bookmark.bean.BookMarkLocal;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.stream.Collectors;

/**
 * bookmark相关工具类函数
 */
public class BookMarkUtils {

    /**
     * 标签转换到本地数据对象封装，便于进行导出位json字符串
     *
     * @param bookmarks 标签对象列表
     * @return List<BookMarkLocal>
     */
    public static List<BookMarkLocal> covertToLocal(List<Bookmark> bookmarks) {
        return bookmarks.stream().map(b ->
                new BookMarkLocal(b.getDescription(), b.getFile().getName(), b.getFile().getPath(), b.getLine())
        ).collect(Collectors.toList());
    }

    /**
     * 加载本项目已经存在的标签
     *
     * @param project 项目对象
     * @return List<Bookmark>
     */
    public static List<Bookmark> loadBookMark(Project project) {
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        return bookmarkManager.getValidBookmarks();

    }

    public static boolean checkBookmarkExist(BookMarkLocal bookMarkLocal, Project project) {
        final List<Bookmark> bookmarks = loadBookMark(project);
        if (bookmarks.isEmpty()) {
            return false;
        }
        return bookmarks.stream().anyMatch(
                b -> b.getFile().getPath().equalsIgnoreCase(bookMarkLocal.getFileSrcPath()) && b.getLine() == bookMarkLocal.getLineIndex());
    }

}
