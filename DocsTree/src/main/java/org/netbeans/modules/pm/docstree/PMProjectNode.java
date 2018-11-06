/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

import org.netbeans.modules.pm.docstree.action.CreateNodeAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.pm.docstree.action.MoveNodeAction;
import org.netbeans.modules.pm.docstree.action.OpenProjectAction;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMProjectNode extends PMGroupNode {

    private Action[] actions;

    public PMProjectNode(PMNodePropertiesImpl props) {
        super(props, PMNodeType.PROJECT);
    }

    @Override
    protected String getIconBaseName() {
        return "project";
    }

    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<Action> list = new ArrayList<>();
            list.add(new OpenProjectAction(this));
            list.addAll(Arrays.asList(super.getActions(context)));
            actions = list.toArray(new Action[list.size()]);
        }
        return actions;
    }

    @Override
    public Action getPreferredAction() {
        return getActions(true)[0];
    }
}
