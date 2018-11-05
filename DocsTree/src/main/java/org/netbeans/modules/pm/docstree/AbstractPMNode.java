/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import java.awt.Image;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Nabbefeld
 */
public abstract class AbstractPMNode extends AbstractNode {

    static Node createNode(PMNodePropertiesImpl props) {
//        Children children = AbstractPMNode.createNodeChildren(props);
        switch (props.getNodeType()) {
            case DOC:
                return new PMDocumentNode(props);
            case GROUP:
                return new PMGroupNode(props);
            case OTHERS:
                return new PMOthersNode(props);
            case PROJECT:
                return new PMProjectNode(props);
            case ROOT:
                return new PMRootNode(props);
            default:
                throw new AssertionError();
        }
    }

    private static Children createNodeChildren(PMNodePropertiesImpl properties) {
        return new PMChildren(properties);
    }

    private final InstanceContent lookupContent;
    private final String imageBase = this.getClass().getPackageName().replace('.', '/') + "/";
    private final String propertiesBase;
    private final PMNodePropertiesImpl props;

    public AbstractPMNode(PMNodePropertiesImpl props) {
        this(props, createNodeChildren(props), new InstanceContent());
    }

    private AbstractPMNode(PMNodePropertiesImpl props, Children children, InstanceContent content) {
        super(children, new AbstractLookup(content));
        this.props = props;
        this.propertiesBase = props.getPath();
        this.lookupContent = content;
        lookupContent.add(props);
    }

    public String getPropertiesBase() {
        return propertiesBase;
    }

    @Override
    public String getDisplayName() {
        return props.getDisplayName();
    }

    @Override
    public String getShortDescription() {
        return props.getUrl();
    }

    @Override
    public Image getIcon(int type) {
        String path = imageBase + getIconBaseName() + ".png";
        return ImageUtilities.loadImage(imageBase + getIconBaseName() + ".png", false);
    }

    @Override
    public Image getOpenedIcon(int type) {
        String path = imageBase + getIconBaseName() + "-open.png";
        return ImageUtilities.loadImage(imageBase + getIconBaseName() + "-open.png", false);
    }

    @Override
    public abstract Action[] getActions(boolean context);

    protected final PMNodePropertiesImpl getProperties() {
        return props;
    }

    protected abstract String getIconBaseName();
}
