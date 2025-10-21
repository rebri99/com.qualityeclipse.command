/*
 * This file is generated under this project, "com.qualityeclipse.command". 
 *
 * @author 김현수
 * @copyright: 
 * @package: 
 * @license: 
 * @url: 
 * @require: 
 * @since: 2025. 10. 20. 오후 1:22:14
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
 * 프로젝트 경로 터미널 열기 핸들러
 * 
 * @author: 김현수
 * @date: 2025. 10. 20. 오후 1:22:14
 */
public class OpenTerminalHandler extends AbstractHandler {

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
                new ProcessBuilder("cmd.exe", "/c", "start", "cmd", "/K", "cd /d " + path).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", "-a", "Terminal", path).start();
            } else { // linux
                new ProcessBuilder("gnome-terminal", "--working-directory=" + path).start();
            }
        } catch (IOException e) {
            throw new ExecutionException("터미널 실행에 실패했습니다.", e);
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
