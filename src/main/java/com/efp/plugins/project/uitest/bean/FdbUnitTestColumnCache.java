package com.efp.plugins.project.uitest.bean;

import java.io.Serializable;

/**
 * @author 86134
 */
public class FdbUnitTestColumnCache implements Serializable {
    private String name;
    private String comment;
    private int length;
    private String typeName;
    private int scale;

    @Override
    public String toString() {
        return "FdbUnitTestColumnCache{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", length=" + length +
                ", typeName='" + typeName + '\'' +
                ", scale=" + scale +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
