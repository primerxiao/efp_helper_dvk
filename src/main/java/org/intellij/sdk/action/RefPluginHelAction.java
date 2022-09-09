// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.io.FileUtil;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Vladimir Kondratyev
 */
public class RefPluginHelAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //将文件写到目录中
        PluginId pluginId = PluginUtil.getInstance().findPluginId(new Throwable());
        assert pluginId != null;
        IdeaPluginDescriptor enabledPlugin = PluginManager.getInstance().findEnabledPlugin(pluginId);
        assert enabledPlugin != null;
        try (InputStream inputStream = enabledPlugin.getPluginClassLoader().getResourceAsStream("/help/SedbankkPluginHelp.doc")) {
            String outPutFilePat = PathManager.getHomePath() + "/help/SedbankkPluginHelp.doc";
            assert inputStream != null;
            IOUtil.copyCompletely(inputStream, new FileOutputStream(outPutFilePat));
            File file = new File(FileUtil.toSystemDependentName(outPutFilePat));
            if (file.exists() && file.isFile()) {
                BrowserUtil.browse(file);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}
