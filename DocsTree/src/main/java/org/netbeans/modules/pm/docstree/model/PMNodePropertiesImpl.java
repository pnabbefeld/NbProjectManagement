/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.model;

import org.netbeans.modules.pm.docstree.PMNodeProperties;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.modules.pm.docstree.PMNodeType;
import static org.netbeans.modules.pm.docstree.util.DocsTreeGeneralConstants.*;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMNodePropertiesImpl implements PMNodeProperties, PMNodeIndex.IndexValueHolder {

    public static PMNodePropertiesImpl getLastChildOf(@NonNull PMNodePropertiesImpl props) {
        if (props.getSize() < 1) {
            return null;
        }
        return props.children.get(PROPERTIES_BASE_INDEX + props.getSize() - 1);
    }

    private final ChangeSupport cs;
    private final PMNodePropertiesImpl parent;
    private PMNodeIndex nodeIndex;
    private PMNodeType nodeType;
    private String displayName;
    private String url;
    private PMChildList<PMNodePropertiesImpl> children;

    public PMNodePropertiesImpl(PMNodeProperties parent) {
        this.cs = new ChangeSupport(this);
        this.nodeIndex = new PMNodeIndex();
        this.parent = (PMNodePropertiesImpl)parent;
    }

    public void setNodeType(PMNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public synchronized void addChildProperties(PMNodePropertiesImpl props) {
        assert nodeType != PMNodeType.DOC;
        if (children == null) {
            children = new PMChildList<>(PROPERTIES_BASE_INDEX);
        }
        PMNodePropertiesImpl lastElement = children.getLastElement();
        if (lastElement != null && lastElement.getNodeType() == PMNodeType.OTHERS) {
            children.add(children.size(), props);
        } else {
            children.add(props);
        }
        fireChange();
    }

    public synchronized void removeChildProperties(String key) {
        int p = key.lastIndexOf('.');
        int index = Integer.parseInt(key.substring(p + 1));
        assert children.get(index).getPath().equals(key) : "Path mismatch";
        children.remove(index);
        fireChange();
    }

    public void moveChildProperties(String key, int newIndex) {
        int p = key.lastIndexOf('.');
        int index = Integer.parseInt(key.substring(p + 1));
        assert children.get(index).getPath().equals(key) : "Path mismatch";
        children.move(index, newIndex);
        fireChange();
    }

    public PMNodePropertiesImpl getParent() {
        return parent;
    }

    @Override
    public String getPath() {
        return parent == null ? "pm.node.root" : parent.getPath() + "." + nodeIndex.value();
    }

    @Override
    public PMNodeType getNodeType() {
        return nodeType;
    }

    @Override
    public String getDisplayName() {
        if (displayName == null) {
            String bundlePath = "/" + this.getClass().getPackageName().replace('.', '/');
            ResourceBundle rb = ResourceBundle.getBundle(bundlePath + "/Bundle");
            switch (nodeType) {
                case ROOT:
                    displayName = rb.getString("LBL_NodeRoot");
                    break;
                case OTHERS:
                    displayName = rb.getString("LBL_NodeOthers");
                    break;
                default:
                    throw new NullPointerException("Display name is null");
            }
        }
        return displayName;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getSize() {
        return children == null ? 0 : children.size();
    }

    public Collection<PMNodePropertiesImpl> getChildren() {
        return children == null ? Collections.EMPTY_LIST : children.getElements();
    }

    public PMNodePropertiesImpl getChild(String key) {
        int p = key.lastIndexOf('.');
        int index = Integer.parseInt(key.substring(p + 1));
        assert children.get(index).getPath().equals(key) : "Path mismatch";
        return children.get(index);
    }

    @Override
    public void setIndexValue(PMNodeIndex value) {
        this.nodeIndex = value;
    }

    @Override
    public PMNodeIndex getIndexValue() {
        return this.nodeIndex;
    }

    public synchronized void reorder(Collection<String> pathValues) {
        if (!pathValues.isEmpty()) {
            PMChildList<PMNodePropertiesImpl> newChildren = new PMChildList<>(PROPERTIES_BASE_INDEX);
            pathValues.forEach((pathValue) -> {
                newChildren.add(getChild(pathValue));
            });
            List<PMNodePropertiesImpl> reorderedList = newChildren.getElements();
            int n = reorderedList.size();
            PMNodePropertiesImpl child;
            for (int i = 0; i < n; i++) {
                child = reorderedList.get(i);
                child.getIndexValue().set(i + 1);
            }
            children = newChildren;
            fireChange();
        }
    }

    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    private void fireChange() {
        cs.fireChange();
    }
}
