/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import org.netbeans.modules.pm.docstree.ui.CreateNodePanel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.netbeans.modules.pm.docstree.PMNodeType;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Peter Nabbefeld
 */
public class CreateNodeAction extends AbstractAction {

    public static Action create(AbstractPMNode node, PMNodeType... allowedNodeTypes) {
        if (allowedNodeTypes.length == 0) {
            PMNodeType[] values = PMNodeType.values();
            PMNodeType[] filtered = new PMNodeType[values.length - 2];
            for (int i = 0, j = 0; i < values.length; i++) {
                if (values[i] != PMNodeType.ROOT && values[i] != PMNodeType.OTHERS) {
                    filtered[j++] = values[i];
                }
            }
            return create(node, filtered);
        }
        return new CreateNodeAction(node, allowedNodeTypes);
    }

    private final AbstractPMNode node;
    private final PMNodeType[] allowedNodeTypes;

    private CreateNodeAction(AbstractPMNode node, PMNodeType[] allowedNodeTypes) {
        this.node = node;
        this.allowedNodeTypes = allowedNodeTypes;
        putValue(Action.NAME, "Create Node ...");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        CreateNodePanel cnp = new CreateNodePanel(allowedNodeTypes);
        String title = "Create Node ...";
        DialogDescriptor dd = new DialogDescriptor(cnp, title);
        Object result = DialogDisplayer.getDefault().notify(dd);
        if (result == NotifyDescriptor.OK_OPTION) {
            PMNodePropertiesImpl props = node.getLookup().lookup(PMNodePropertiesImpl.class);
            PMNodePropertiesImpl newProps = cnp.createProperties(props);
            props.addChildProperties(newProps);
        }
    }
}
