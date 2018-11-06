/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import org.netbeans.modules.pm.docstree.util.XmlUtils;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.netbeans.modules.pm.docstree.ui.FileAccessPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Peter Nabbefeld
 */
public class ExportAction extends AbstractAction {

    private final AbstractPMNode node;
    private final PMNodePropertiesImpl nodeProps;

    public ExportAction(AbstractPMNode node) {
        super("Export Tree to File ...");
        this.node = node;
        this.nodeProps = node.getLookup().lookup(PMNodePropertiesImpl.class);
        assert nodeProps.getParent() == null : "Not a root node";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        FileAccessPanel fap = new FileAccessPanel(FileAccessPanel.OpenType.REPLACE);
        String title = "Export ...";
        DialogDescriptor dd = new DialogDescriptor(fap, title);
        Object result = DialogDisplayer.getDefault().notify(dd);
        if (result == NotifyDescriptor.OK_OPTION) {
            File file = fap.getFile();
            XmlUtils.exportTo(file, nodeProps);
        }
    }
}
