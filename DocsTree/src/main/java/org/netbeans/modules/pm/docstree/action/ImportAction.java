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
import org.openide.nodes.Children;

/**
 *
 * @author Peter Nabbefeld
 */
public class ImportAction extends AbstractAction {

    private final AbstractPMNode node;
    private final PMNodePropertiesImpl nodeProps;

    public ImportAction(AbstractPMNode node) {
        super("Import Tree from File ...");
        this.node = node;
        PMNodePropertiesImpl props = node.getLookup().lookup(PMNodePropertiesImpl.class);
        if (props == null) {
            props = new PMNodePropertiesImpl(null);
        }
        assert props.getParent() == null : "Not a root node";
        this.nodeProps = props;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        FileAccessPanel fap = new FileAccessPanel(FileAccessPanel.OpenType.EXISTING);
        String title = "Import ...";
        DialogDescriptor dd = new DialogDescriptor(fap, title);
        Object result = DialogDisplayer.getDefault().notify(dd);
        if (result == NotifyDescriptor.OK_OPTION) {
            File file = fap.getFile();
            XmlUtils.importFrom(file, nodeProps);
            Children children = node.getChildren();
            children.remove(children.getNodes());
        }
    }
}
