package com.efp.common.util;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Objects;

public class EditorUtils {

    public static void closeAllEditor(AnActionEvent e){
        FileEditor[] allEditors = FileEditorManager.getInstance(Objects.requireNonNull(e.getProject())).getAllEditors();
        if (allEditors.length > 0) {
            AnAction xCloseAllEditors = ActionManager.getInstance().getAction("CloseAllEditors");
            xCloseAllEditors.actionPerformed(e);
        }
    }

    public static void openFileInEditor(AnActionEvent e, VirtualFile virtualFile) {
        FileEditorManager.getInstance(Objects.requireNonNull(e.getProject())).openFile(virtualFile, true);
    }

}
