/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import javax.swing.JMenuItem;
import javax.swing.ToolTipManager;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author Peter Nabbefeld
 */
public class SortChildrenAction extends CallableSystemAction {

    private final AbstractPMNode node;
    private final ChildrenSorter childrenSorter;
    private JMenuItem mni;

    public SortChildrenAction(AbstractPMNode node) {
        this.node = node;
        this.childrenSorter = new ChildrenSorter(node);
    }

    @Override
    public String getName() {
        return "Sort Children";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        if (mni == null) {
            mni = new JMenuItem(this);
            ToolTipManager.sharedInstance().registerComponent(mni);
            mni.setToolTipText("Sort Children alphabetically");
        }
        return mni;
    }

    @Override
    public void performAction() {
        childrenSorter.sortChildren();
    }
}
