package com.efp.plugins.project.tbtransfor.bean;

import java.io.Serializable;

/**
 * 字段变更信息
 *
 * @author primerxiao
 */
public class ClChangeInfo implements Serializable {

    private ChangeType changeType;

    private String oldClName;

    private String oldTypeName;

    private String oldComment;

    private int oldSize;

    private int oldScale;

    private String newClName;

    private String newTypeName;

    private String newComment;

    private int newSize;

    private int newScale;

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getOldClName() {
        return oldClName;
    }

    public void setOldClName(String oldClName) {
        this.oldClName = oldClName;
    }

    public String getOldTypeName() {
        return oldTypeName;
    }

    public void setOldTypeName(String oldTypeName) {
        this.oldTypeName = oldTypeName;
    }

    public String getOldComment() {
        return oldComment;
    }

    public void setOldComment(String oldComment) {
        this.oldComment = oldComment;
    }

    public int getOldSize() {
        return oldSize;
    }

    public void setOldSize(int oldSize) {
        this.oldSize = oldSize;
    }

    public int getOldScale() {
        return oldScale;
    }

    public void setOldScale(int oldScale) {
        this.oldScale = oldScale;
    }

    public String getNewClName() {
        return newClName;
    }

    public void setNewClName(String newClName) {
        this.newClName = newClName;
    }

    public String getNewTypeName() {
        return newTypeName;
    }

    public void setNewTypeName(String newTypeName) {
        this.newTypeName = newTypeName;
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public int getNewSize() {
        return newSize;
    }

    public void setNewSize(int newSize) {
        this.newSize = newSize;
    }

    public int getNewScale() {
        return newScale;
    }

    public void setNewScale(int newScale) {
        this.newScale = newScale;
    }
}
