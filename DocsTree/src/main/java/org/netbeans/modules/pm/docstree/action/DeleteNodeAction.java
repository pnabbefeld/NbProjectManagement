/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.netbeans.modules.pm.docstree.PMNodeProperties;
import org.netbeans.modules.pm.docstree.PMNodeType;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Nabbefeld
 */
public class DeleteNodeAction extends AbstractAction {

    private final AbstractPMNode node;

    public DeleteNodeAction(AbstractPMNode node) {
        super("Delete");
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            PMNodePropertiesImpl props = (PMNodePropertiesImpl)node.getLookup().lookup(PMNodeProperties.class);
            // Do never remove the system nodes
            if (props.getNodeType() != PMNodeType.ROOT && props.getNodeType() != PMNodeType.OTHERS) {
                boolean approved = true;
                if (props.getNodeType() != PMNodeType.DOC && props.getSize() > 0) {
                    approved = notifyRemovingChildren(props);
                }
                if (approved) {
                    PMNodePropertiesImpl parent = props.getParent();
                    parent.removeChildProperties(props.getPath());
                }
            }
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean notifyRemovingChildren(PMNodePropertiesImpl props) {
        String title = "WARNING";
        String msg = "You're going to remove a group node with children.\nIs this really what You want?";
        JPanel pnl = new JPanel();
        pnl.add(new JLabel(msg));
        DialogDescriptor dd = new DialogDescriptor(pnl, title);
        Object result = DialogDisplayer.getDefault().notify(dd);
        return result == NotifyDescriptor.OK_OPTION;
    }
}
