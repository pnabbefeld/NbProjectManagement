/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Nabbefeld
 */
public class ProjectLocationListener extends InstallableDocumentListener {

    private static class ProjectFilesFilter implements FilenameFilter {

        private final String filesSpec;
        private final boolean folder;

        public ProjectFilesFilter(String filesSpec, boolean folder) {
            this.filesSpec = filesSpec;
            this.folder = folder;
        }

        @Override
        public boolean accept(File file, String name) {
            if (folder) {
                return file.isDirectory() && matches(file, name);
            } else {
                return file.isFile() && matches(file, name);
            }
        }

        private boolean matches(File file, String name) {
            if (!name.equals(filesSpec)) {
                String test = new File(file, name).getAbsolutePath();
                return test.endsWith(filesSpec);
            }
            return true;
        }
    }

    public ProjectLocationListener(JTextField txtProjectLocation, JTextField txtProjectName) {
        super(txtProjectLocation, txtProjectName);
    }

    @Override
    protected void modelChanged() {
        File file = new File(txtSource.getText());
        if (file.exists() && file.isDirectory()) {
            destinations[0].setText(getProjectName(file));
        } else {
            destinations[0].setText("");
        }
    }

    private String getProjectName(File file) {
        File[] list = file.listFiles(new ProjectFilesFilter("pom.xml", false));
        if (list.length == 1) {
            return getMavenProjectName(list[0]);
        } else {
            return getNbProjectName(file);
        }
    }

    private String getNbProjectName(File dir) {
        String name = dir.getName();
        File[] list = dir.listFiles(new ProjectFilesFilter("nbproject", true));
        if (list.length == 1) {
            list = new File(dir, "nbproject").listFiles(new ProjectFilesFilter("project.xml", false));
            if (list.length == 1) {
                Document doc = getXmlDocument(list[0]);
                name = getProjectNameFromProjectDocument(doc);
            }
        }
        return name;
    }

    private String getProjectNameFromProjectDocument(Document doc) {
        NodeList children = doc.getDocumentElement().getChildNodes();
        Element element = getChildElement(children, "configuration");
        if (element != null) {
            element = getChildElement(element.getChildNodes(), "data");
            if (element != null) {
                return getTextContentFromChild(element.getChildNodes(), "name");
            }
        }
        return null;
    }

    private String getMavenProjectName(File pom) {
        Document doc = getXmlDocument(pom);
        NodeList children = doc.getDocumentElement().getChildNodes();
        String name = getTextContentFromChild(children, "name");
        if (name == null) {
            name = getTextContentFromChild(children, "artifactId");
        }
        return name;
    }

    private String getTextContentFromChild(NodeList nodeList, String name) {
        Element child = getChildElement(nodeList, name);
        return child == null ? null : child.getTextContent();
    }

    private Element getChildElement(NodeList nodeList, String name) {
        int n = nodeList.getLength();
        for (int i = 0; i < n; i++) {
            Node item = nodeList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                if (item.getNodeName().equals(name)) {
                    return (Element)item;
                }
            }
        }
        return null;
    }

    private Document getXmlDocument(File file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
