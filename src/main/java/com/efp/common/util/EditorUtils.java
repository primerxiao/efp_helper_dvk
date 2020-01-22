package com.efp.common.util;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;

public class EditorUtils {

    public static void closeAllEditor(AnActionEvent e){
        FileEditor[] allEditors = FileEditorManager.getInstance(e.getProject()).getAllEditors();
        if (allEditors != null && allEditors.length > 0) {
            AnAction xCloseAllEditors = ActionManager.getInstance().getAction("CloseAllEditors");
            xCloseAllEditors.actionPerformed(e);
        }
    }

}
