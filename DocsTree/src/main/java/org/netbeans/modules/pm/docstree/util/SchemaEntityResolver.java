/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.util;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Nabbefeld
 */
public class SchemaEntityResolver implements EntityResolver {

    private static final String GITHUB_BASE = "https://raw.githubusercontent.com/javaee/javahelp/master/jhMaster/JavaHelp/doc/public-spec/dtd";

    public SchemaEntityResolver() {
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (publicId.startsWith("-//Sun Microsystems Inc.//DTD JavaHelp ") && systemId.startsWith("http://java.sun.com/products/javahelp/")) {
            int p = systemId.lastIndexOf('/');
            String rawName = systemId.substring(p);
            return new InputSource(GITHUB_BASE + rawName);
        } else {
            return new InputSource(systemId);
        }
    }
}
