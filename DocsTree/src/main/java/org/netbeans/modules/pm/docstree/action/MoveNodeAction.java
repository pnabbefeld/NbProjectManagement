/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.modules.pm.docstree.AbstractPMNode;
import org.netbeans.modules.pm.docstree.PMNodeProperties;
import org.netbeans.modules.pm.docstree.PMNodeType;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import static org.netbeans.modules.pm.docstree.util.DocsTreeGeneralConstants.*;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Nabbefeld
 */
public class MoveNodeAction extends AbstractAction {

    public enum Direction {
        TOP("Move to top"),
        UP("Move up"),
        DOWN("Move down"),
        BOTTOM("Move to bottom");

        private final String displayName;

        private Direction(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static Collection<? extends Action> createAll(AbstractPMNode node) {
        List<MoveNodeAction> res = new ArrayList<>(4);
        res.add(new MoveNodeAction(node, Direction.TOP));
        res.add(new MoveNodeAction(node, Direction.UP));
        res.add(new MoveNodeAction(node, Direction.DOWN));
        res.add(new MoveNodeAction(node, Direction.BOTTOM));
        return Collections.unmodifiableCollection(res);
    }

    private final AbstractPMNode node;
    private final Direction direction;
    private final PMNodePropertiesImpl props;

    public MoveNodeAction(AbstractPMNode node, Direction direction) {
        super(direction.getDisplayName());
        this.node = node;
        this.direction = direction;
        this.props = (PMNodePropertiesImpl)node.getLookup().lookup(PMNodeProperties.class);
    }

    @Override
    public boolean isEnabled() {
        PMNodeType nodeType = props.getNodeType();
        if (nodeType == PMNodeType.ROOT || nodeType == PMNodeType.OTHERS) {
            // Never move the system nodes
            return false;
        }
        PMNodePropertiesImpl parent = props.getParent();
        if (parent == null || parent.getSize() < 2) {
            // Single child, moving not possible
            return false;
        }
        int pos = props.getIndexValue().value() - PROPERTIES_BASE_INDEX;
        PMNodePropertiesImpl lastSibling = PMNodePropertiesImpl.getLastChildOf(parent);
        switch (direction) {
            case TOP:
            case UP:
                return pos > 0;
            case DOWN:
            case BOTTOM: {
                int max = parent.getSize() - 1;
                if (lastSibling.getNodeType() == PMNodeType.OTHERS) {
                    max--;
                }
                return pos < max;
            }
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        PMNodePropertiesImpl parent = props.getParent();
        try {
            switch (direction) {
                case TOP: {
                    parent.moveChildProperties(props.getPath(), PROPERTIES_BASE_INDEX);
                    break;
                }
                case UP: {
                    int newPos = props.getIndexValue().value() - 1;
                    parent.moveChildProperties(props.getPath(), newPos);
                    break;
                }
                case DOWN: {
                    int newPos = props.getIndexValue().value() + 1;
                    parent.moveChildProperties(props.getPath(), newPos);
                    break;
                }
                case BOTTOM: {
                    PMNodePropertiesImpl lastSibling = PMNodePropertiesImpl.getLastChildOf(parent);
                    int max = parent.getSize() - 1;
                    if (lastSibling.getNodeType() == PMNodeType.OTHERS) {
                        max--;
                    }
                    parent.moveChildProperties(props.getPath(), PROPERTIES_BASE_INDEX + max);
                    break;
                }
                default:
                    throw new AssertionError();
            }
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
