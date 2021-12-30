package com.efp.plugins.project.tbtransfor.service;

import com.efp.plugins.project.tbtransfor.bean.TbChangeInfo;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@State(name = "TbTransforCacheService")
public class TbTransforCacheService implements PersistentStateComponent<TbTransforCacheService> {

    public ArrayList<TbChangeInfo> tbChangeInfos;

    public TbTransforCacheService() {
    }

    @Override
    public TbTransforCacheService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TbTransforCacheService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public ArrayList<TbChangeInfo> getTbChangeInfos() {
        return tbChangeInfos;
    }

    public void setTbChangeInfos(ArrayList<TbChangeInfo> tbChangeInfos) {
        this.tbChangeInfos = tbChangeInfos;
    }
}
