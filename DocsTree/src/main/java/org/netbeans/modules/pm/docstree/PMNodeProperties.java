/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree;

/**
 *
 * @author Peter Nabbefeld
 */
public interface PMNodeProperties {

    public String getPath();

    public PMNodeType getNodeType();

    public String getDisplayName();

    public String getUrl();

    public int getSize();
}
