/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

import org.netbeans.modules.pm.docstree.action.CreateNodeAction;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.pm.docstree.action.ExportAction;
import org.netbeans.modules.pm.docstree.action.ImportAction;
import org.netbeans.modules.pm.docstree.action.SortChildrenAction;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMRootNode extends AbstractPMNode {

    private Action[] actions;

    public PMRootNode(PMNodePropertiesImpl props) {
        super(props);
        assert props.getNodeType() == PMNodeType.ROOT && "pm.node.root".equals(props.getPath());
        checkOthersFolder(props);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return super.getIcon(type);
    }

    @Override
    protected String getIconBaseName() {
        return "root";
    }

    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<Action> list = new ArrayList<>();
            list.add(CreateNodeAction.create(this, PMNodeType.GROUP));
            list.add(null);
            list.add(new SortChildrenAction(this));
            list.add(null);
            list.add(new ImportAction(this));
            list.add(new ExportAction(this));
            actions = list.toArray(new Action[list.size()]);
        }
        return actions;
    }

    private void checkOthersFolder(PMNodePropertiesImpl rootProps) {
        boolean others = false;
        for (PMNodePropertiesImpl child : rootProps.getChildren()) {
            if (child.getNodeType() == PMNodeType.OTHERS) {
                others = true;
                break;
            }
        }
        if (!others) {
            PMNodePropertiesImpl newChild = new PMNodePropertiesImpl(rootProps);
            newChild.setNodeType(PMNodeType.OTHERS);
            rootProps.addChildProperties(newChild);
        }
    }
}
