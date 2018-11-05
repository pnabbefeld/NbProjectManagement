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
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMOthersNode extends PMGroupNode {

    private Action[] actions;

    public PMOthersNode(PMNodePropertiesImpl props) {
        super(props, PMNodeType.OTHERS);
    }

    @Override
    protected String getIconBaseName() {
        return "others";
    }

    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<Action> list = new ArrayList<>();
            list.add(CreateNodeAction.create(this, PMNodeType.PROJECT, PMNodeType.DOC));
            actions = list.toArray(new Action[list.size()]);
        }
        return actions;
    }
}
