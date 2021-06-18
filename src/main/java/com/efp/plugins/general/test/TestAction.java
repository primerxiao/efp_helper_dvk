package com.efp.plugins.general.test;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.BookMarkUtils;
import com.efp.common.util.JsonUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.io.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // 获取项目信息
        System.out.println("--openProjects = {Project[1]@50973} -----------------------");
        @NotNull Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
        for (Project openProject : openProjects) {
            System.out.println(openProject);
        }
        System.out.println("-------------------------");
        // 获取module信息
        Module[] modules = ModuleManager.getInstance(e.getProject()).getModules();
        for (Module module : modules) {
            System.out.println(module);
        }
        System.out.println("-------------------------");
    }


}
