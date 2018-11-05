/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.netbeans.modules.pm.docstree.PMNodeProperties;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Nabbefeld
 */
public class OpenProjectAction extends AbstractAction {

    private final AbstractPMNode node;

    public OpenProjectAction(AbstractPMNode node) {
        this.node = node;
        putValue(Action.NAME, "Open Project");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            PMNodeProperties props = node.getLookup().lookup(PMNodeProperties.class);
            File file = new File(props.getUrl());
            FileObject prj = FileUtil.toFileObject(file);
            Project project = ProjectManager.getDefault().findProject(prj);
            Project[] array = new Project[1];
            array[0] = project;
            OpenProjects.getDefault().open(array, false);
        } catch (IOException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
