package com.efp.plugins.dubbo.bean;

import java.io.Serializable;

public class BookMarkData implements Serializable {
    /**
     * 行索引
     */
    private int lineIndex;
    /**
     * 标签描述
     */
    private String descript;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件的src路径
     */
    private String fileSrcPath;

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
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

    public BookMarkData() {
    }

    public BookMarkData(int lineIndex, String descript, String fileName, String fileSrcPath) {
        this.lineIndex = lineIndex;
        this.descript = descript;
        this.fileName = fileName;
        this.fileSrcPath = fileSrcPath;
    }

    public BookMarkData(int lineIndex, String fileName, String fileSrcPath) {
        this.lineIndex = lineIndex;
        this.fileName = fileName;
        this.fileSrcPath = fileSrcPath;
    }
}
