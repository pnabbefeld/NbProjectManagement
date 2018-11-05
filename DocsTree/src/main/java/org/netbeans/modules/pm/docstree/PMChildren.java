/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMChildren extends Children.Keys<String> implements ChangeListener {

    private static final String[] EMPTY = {};

    private final PMNodePropertiesImpl properties;

    @SuppressWarnings("LeakingThisInConstructor")
    public PMChildren(PMNodePropertiesImpl properties) {
        this.properties = properties;
        this.properties.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        removeNotify();
        addNotify();
    }

    @Override
    protected Node[] createNodes(String key) {
        Node node = AbstractPMNode.createNode(properties.getChild(key));
        return new Node[]{node};
    }

    @Override
    protected void removeNotify() {
        super.addNotify();
        setKeys(EMPTY);
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        String[] props = new String[properties.getSize()];
        int i = 0;
        for (PMNodePropertiesImpl child : properties.getChildren()) {
            props[i++] = child.getPath();
        }
        setKeys(props);
    }
}
