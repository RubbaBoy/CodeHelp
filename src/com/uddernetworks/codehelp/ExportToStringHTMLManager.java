package com.uddernetworks.codehelp;

import com.intellij.psi.PsiFile;

public class ExportToStringHTMLManager {

    public static String getHTMLFileName(PsiFile psiFile) {
        //noinspection HardCodedStringLiteral
        return psiFile.getVirtualFile().getName() + ".html";
    }

}
