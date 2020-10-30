package com.efp.plugins.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * efp插件持久化数据对象
 */
@State(
        name = "com.efp.plugins.settings.EfpSettingsState",
        storages = {@Storage("EfpPluginSettings.xml")}
)
public class EfpSettingsState implements PersistentStateComponent<EfpSettingsState> {

    public String dubboRegistryAddress = "127.0.0.1:2181";
    public boolean providerCheckBox = true;
    public boolean comsumerCheckBox = true;


    public static EfpSettingsState getInstance() {
        return ServiceManager.getService(EfpSettingsState.class);
    }

    @Nullable
    @Override
    public EfpSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull EfpSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
