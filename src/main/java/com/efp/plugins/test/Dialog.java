package com.efp.plugins.test;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.wizard.AbstractWizard;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author HIFeng
 */
public class Dialog extends AbstractWizard<ModuleWizardStep> {
    public Dialog(String title, Component dialogParent) {
        super(title, dialogParent);
    }

    public Dialog(String title, @Nullable Project project) {
        super(title, project);
        addStep(new FirstStep());
        init();
        show();
    }

    @Nullable
    @Override
    protected String getHelpID() {
        return this.getClass().getName();
    }
}
