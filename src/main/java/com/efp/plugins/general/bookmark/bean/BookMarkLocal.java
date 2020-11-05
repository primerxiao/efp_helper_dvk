package com.efp.plugins.general.bookmark.bean;

/**
 * 本地表标签数据对象
 */
public class BookMarkLocal {
    /**
     * 描述
     */
    private String descript;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String fileSrcPath;

    /**
     * 行索引
     */
    private int lineIndex;

    public BookMarkLocal(String descript, String fileName, String fileSrcPath, int lineIndex) {
        this.descript = descript;
        this.fileName = fileName;
        this.fileSrcPath = fileSrcPath;
        this.lineIndex = lineIndex;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSrcPath() {
        return fileSrcPath;
    }

    public void setFileSrcPath(String fileSrcPath) {
        this.fileSrcPath = fileSrcPath;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }
}
