/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.pm.docstree.action.DeleteNodeAction;
import org.netbeans.modules.pm.docstree.action.MoveNodeAction;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMDocumentNode extends AbstractPMNode {

    private Action[] actions;

    public PMDocumentNode(PMNodePropertiesImpl props) {
        super(props);
        assert props.getNodeType() == PMNodeType.DOC && props.getUrl() != null && !"pm.node.root".equals(props.getPath());
    }

    @Override
    public Image getOpenedIcon(int type) {
        return super.getIcon(type);
    }

    @Override
    protected String getIconBaseName() {
        return super.getProperties().getUrl().endsWith(".odt") ? "document-office" : "document-general";
    }
    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<Action> list = new ArrayList<>();
            list.add(null);
            list.addAll(MoveNodeAction.createAll(this));
            list.add(new DeleteNodeAction(this));
            actions = list.toArray(new Action[list.size()]);
        }
        return actions;
    }
}
