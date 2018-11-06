/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.netbeans.modules.pm.docstree.PMNodeProperties;
import org.netbeans.modules.pm.docstree.PMNodeType;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;

/**
 *
 * @author peter
 */
class ChildrenSorter {

    private final PMNodePropertiesImpl nodeProps;

    public ChildrenSorter(AbstractPMNode node) {
        nodeProps = (PMNodePropertiesImpl)node.getLookup().lookup(PMNodeProperties.class);
    }

    public void sortChildren() {
        Collection<PMNodePropertiesImpl> children = nodeProps.getChildren();
        Map</*sortKey*/String, /*path*/ String> sorted = new TreeMap<>();
        String sortkey;
        for (PMNodeProperties child : children) {
            sortkey = getSortKey(child.getNodeType(), child.getDisplayName());
            sorted.put(sortkey, child.getPath());
        }
        nodeProps.reorder(sorted.values());
    }

    private String getSortKey(PMNodeType nodeType, String displayName) {
        String sortPrefix;
        switch (nodeType) {
            case DOC:
                sortPrefix = "50";
                break;
            case GROUP:
                sortPrefix = "10";
                break;
            case OTHERS:
                sortPrefix = "99";
                break;
            case PROJECT:
                sortPrefix = "20";
                break;
            default:
                throw new AssertionError();
        }
        return sortPrefix + ':' + displayName;
    }
}
