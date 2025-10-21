/*
 * This file is generated under this project, "com.qualityeclipse.command". 
 *
 * @author 김현수
 * @copyright: 
 * @package: 
 * @license: 
 * @url: 
 * @require: 
 * @since: 2025. 10. 20. 오후 1:07:01
*/
package com.qualityeclipse.command.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 프로젝트 폴더 열기 핸들러
 * 
 * @author: 김현수
 * @date: 2025. 10. 20. 오후 1:07:01
 */
public class OpenFolderHandler extends AbstractHandler {

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IProject project = getSelectedProject(event);
        if (project == null)
            return null;

        IPath location = project.getLocation();
        if (location == null)
            return null;

        try {
            String path = location.toOSString();
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec("explorer " + path);
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[] { "open", path });
            } else { // linux
                Runtime.getRuntime().exec(new String[] { "xdg-open", path });
            }
        } catch (IOException e) {
            throw new ExecutionException("폴더 열기에 실패했습니다.", e);
        }

        return null;
    }

    private IProject getSelectedProject(ExecutionEvent event) {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();
            if (element instanceof IProject) {
                return (IProject) element;
            } else if (element instanceof IAdaptable) {
                return ((IAdaptable) element).getAdapter(IProject.class);
            }
        }
        return null;
    }
}
