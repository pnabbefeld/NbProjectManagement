/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.ui;

/**
 *
 * @author Peter Nabbefeld
 */
public enum DocumentTypesEnum {

    PLAIN("Plain text"),
    OFFICE("LibreOffice Writer");

    private final String labelText;

    private DocumentTypesEnum(String labelText) {
        this.labelText = labelText;
    }

    @Override
    public String toString() {
        return labelText;
    }
}
