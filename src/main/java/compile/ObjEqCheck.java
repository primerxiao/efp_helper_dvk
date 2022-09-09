// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package compile;

import com.intellij.codeInspection.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiEnumConstantImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.StringTokenizer;

import static com.siyeh.ig.psiutils.ExpressionUtils.isNullLiteral;


public class ObjEqCheck extends AbstractBaseJavaLocalInspectionTool {

  public String CHECKED_CLASSES = "java.lang.String;java.util.Date";

  public static final String QUICK_FIX_NAME = "SDK: " +
          InspectionsBundle.message("inspection.comparing.references.use.quickfix");
  private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.ComparingReferencesInspection");


  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new JavaElementVisitor() {

      @NonNls
      private final String DESCRIPTION_TEMPLATE = "SDK " +
              InspectionsBundle.message("inspection.comparing.references.problem.descriptor");


      @Override
      public void visitForStatement(PsiForStatement statement) {
        super.visitForStatement(statement);
        PsiStatement body = statement.getBody();

      }

      @Override
      public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
      }

      @Override
      public void visitBinaryExpression(PsiBinaryExpression expression) {
        super.visitBinaryExpression(expression);
        IElementType opSign = expression.getOperationTokenType();
        if (opSign == JavaTokenType.EQEQ || opSign == JavaTokenType.NE) {
          // The binary expression is the correct type for this inspection
          PsiExpression lOperand = expression.getLOperand();
          PsiExpression rOperand = expression.getROperand();
          if (rOperand == null || isNullLiteral(lOperand) || isNullLiteral(rOperand)) {
            return;
          }
          // Nothing is compared to null, now check the types being compared
          PsiType lType = lOperand.getType();
          PsiType rType = rOperand.getType();
          if (isCheckedType(lType) || isCheckedType(rType)) {
            // Identified an expression with potential problems, add to list with fix object.
            holder.registerProblem(expression,
                    DESCRIPTION_TEMPLATE, ProblemHighlightType.ERROR,new CriQuickFix());
            return;
          }
          if (lType instanceof PsiClassType) {
            PsiClassType lType1 = (PsiClassType) lType;
          }
          //左为enum 右为string
          if (lType.equalsToText("")) {

          }
        }
      }

      private boolean isCheckedType(PsiType type) {
        if (!(type instanceof PsiClassType)) {
          return false;
        }
        StringTokenizer tokenizer = new StringTokenizer(CHECKED_CLASSES, ";");
        while (tokenizer.hasMoreTokens()) {
          String className = tokenizer.nextToken();
          if (type.equalsToText(className)) {
            return true;
          }
        }
        return false;
      }
    };
  }

  private static class CriQuickFix implements LocalQuickFix {

    /**
     * Returns a partially localized string for the quick fix intention.
     * Used by the test code for this plugin.
     *
     * @return Quick fix short name.
     */
    @NotNull
    @Override
    public String getName() {
      return QUICK_FIX_NAME;
    }

    /**
     * This method manipulates the PSI tree to replace 'a==b' with 'a.equals(b)' or 'a!=b' with '!a.equals(b)'.
     *
     * @param project    The project that contains the file being edited.
     * @param descriptor A problem found by this inspection.
     */
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      try {
        PsiBinaryExpression binaryExpression = (PsiBinaryExpression) descriptor.getPsiElement();
        IElementType opSign = binaryExpression.getOperationTokenType();
        PsiExpression lExpr = binaryExpression.getLOperand();
        PsiExpression rExpr = binaryExpression.getROperand();
        if (rExpr == null) {
          return;
        }

        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiMethodCallExpression equalsCall =
                (PsiMethodCallExpression) factory.createExpressionFromText("a.equals(b)", null);

        equalsCall.getMethodExpression().getQualifierExpression().replace(lExpr);
        equalsCall.getArgumentList().getExpressions()[0].replace(rExpr);

        PsiExpression result = (PsiExpression) binaryExpression.replace(equalsCall);

        if (opSign == JavaTokenType.NE) {
          PsiPrefixExpression negation = (PsiPrefixExpression) factory.createExpressionFromText("!a", null);
          negation.getOperand().replace(result);
          result.replace(negation);
        }
      } catch (IncorrectOperationException e) {
        LOG.error(e);
      }
    }

    @NotNull
    public String getFamilyName() {
      return getName();
    }

  }

}
