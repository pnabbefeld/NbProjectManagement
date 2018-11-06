/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.netbeans.modules.pm.docstree.PMNodeType;
import org.netbeans.modules.pm.docstree.model.PMNodePropertiesImpl;
import org.netbeans.modules.pm.docstree.xml.ObjectFactory;
import org.netbeans.modules.pm.docstree.xml.PMDocsNodeTypeEnum;
import org.netbeans.modules.pm.docstree.xml.PMDocumentNode;
import org.netbeans.modules.pm.docstree.xml.PMNodeChildren;
import org.netbeans.modules.pm.docstree.xml.ProjectDocumentsView;
import org.openide.util.Exceptions;

/**
 *
 * @author peter
 */
public class XmlUtils {

    public static void importFrom(File file, PMNodePropertiesImpl nodeProps) {
        ProjectDocumentsView view = readViewFrom(file);
        if (view != null) {
            try {
                nodes2properties(view, nodeProps);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public static void exportTo(File file, PMNodePropertiesImpl nodeProps) {
        ObjectFactory objFact = new ObjectFactory();
        ProjectDocumentsView view = objFact.createProjectDocumentsView();
        PMDocumentNode node = createNode(objFact, nodeProps);
        view.setPmDocumentNode(node);
        writeViewTo(file, view);
    }

    private static PMDocumentNode createNode(ObjectFactory objFact, PMNodePropertiesImpl nodeProps) {
        PMDocumentNode node = objFact.createPMDocumentNode();
        node.setName(nodeProps.getDisplayName());
        node.setUrl(nodeProps.getUrl());
        exportChildren(objFact, node, nodeProps);
        return node;
    }

    private static void exportChildren(ObjectFactory objFact, PMDocumentNode node, PMNodePropertiesImpl nodeProps) {
        if (nodeProps.getSize() > 0) {
            PMNodeChildren nodeChildren = objFact.createPMNodeChildren();
            List<PMDocumentNode> listNodeChildren = nodeChildren.getPmDocumentNode();
            Collection<PMNodePropertiesImpl> propsChildren = nodeProps.getChildren();
            propsChildren.forEach((propsChild) -> {
                listNodeChildren.add(createNode(objFact, propsChild));
            });
            node.setChildren(nodeChildren);
        }
    }

    private static void writeViewTo(File file, ProjectDocumentsView view) {
        try {
            JAXBContext context = JAXBContext.newInstance("org.netbeans.modules.pm.docstree.xml", ProjectDocumentsView.class.getClassLoader());
            Marshaller m = context.createMarshaller();
            m.marshal(view, file);
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static ProjectDocumentsView readViewFrom(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance("org.netbeans.modules.pm.docstree.xml", ProjectDocumentsView.class.getClassLoader());
            Unmarshaller u = context.createUnmarshaller();
            return (ProjectDocumentsView)u.unmarshal(file);
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private static void nodes2properties(ProjectDocumentsView view, PMNodePropertiesImpl nodeProps) throws IOException {
        PMDocumentNode node = view.getPmDocumentNode();
        if (node.getType() != PMDocsNodeTypeEnum.ROOT) {
            throw new IOException("Invalid XML document");
        }
        // Make sure root does not have children
        if (nodeProps.getSize() > 0) {
            List<PMNodePropertiesImpl> children = new ArrayList<>(nodeProps.getChildren());
            int n = children.size();
            PMNodePropertiesImpl child;
            for (int i = 1; i <= n; i++) {
                // Removing from top element does not involve renumbering
                child = children.get(n - i);
                nodeProps.removeChildProperties(child.getPath());
            }
        }
        setNodeProperties(node, nodeProps);
    }

    private static void setNodeProperties(PMDocumentNode node, PMNodePropertiesImpl props) {
        props.setNodeType(PMNodeType.parse(node.getType().name()));
        props.setDisplayName(node.getName());
        props.setUrl(node.getUrl());
        node.getChildren().getPmDocumentNode().forEach((childNode) -> {
            PMNodePropertiesImpl childProps = new PMNodePropertiesImpl(props);
            setNodeProperties(childNode, childProps);
            props.addChildProperties(childProps);
        });
    }
}
