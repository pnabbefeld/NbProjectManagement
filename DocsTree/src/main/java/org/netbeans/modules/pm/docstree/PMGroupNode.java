/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

import org.netbeans.modules.pm.docstree.action.CreateNodeAction;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.pm.docstree.action.MoveNodeAction;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMGroupNode extends AbstractPMNode {

    private Action[] actions;

    public PMGroupNode(PMNodePropertiesImpl props) {
        this(props, PMNodeType.GROUP);
    }

    protected PMGroupNode(PMNodePropertiesImpl props, PMNodeType expectedType) {
        super(props);
        assert props.getNodeType() == expectedType && !"pm.node.root".equals(props.getPath());
    }

    @Override
    protected String getIconBaseName() {
        return "folder";
    }

    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<Action> list = new ArrayList<>();
            list.add(CreateNodeAction.create(this));
            list.add(null);
            list.addAll(MoveNodeAction.createAll(this));
            actions = list.toArray(new Action[list.size()]);
        }
        return actions;
    }
}
