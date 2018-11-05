/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author Peter Nabbefeld
 */
abstract class InstallableDocumentListener implements DocumentListener {

    protected final JTextField txtSource;
    protected final JTextField[] destinations;

    protected InstallableDocumentListener(JTextField txtSource, JTextField... txtDest) {
        this.txtSource = txtSource;
        this.destinations = txtDest;
    }

    public final void install() {
        txtSource.getDocument().addDocumentListener(this);
    }

    public final void uninstall() {
        txtSource.getDocument().removeDocumentListener(this);
    }

    @Override
    public final void insertUpdate(DocumentEvent evt) {
        modelChanged();
    }

    @Override
    public final void removeUpdate(DocumentEvent evt) {
        modelChanged();
    }

    @Override
    public final void changedUpdate(DocumentEvent evt) {
        modelChanged();
    }

    protected abstract void modelChanged();
}
