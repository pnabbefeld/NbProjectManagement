/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.model;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMNodeIndex {

    public static interface IndexValueHolder {

        void setIndexValue(PMNodeIndex value);

        PMNodeIndex getIndexValue();
    }

    private int value;

    PMNodeIndex() {
        this(0);
    }

    PMNodeIndex(int value) {
        this.value = value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void increment() {
        ++value;
    }

    public void decrement() {
        --value;
    }

    public int value() {
        return value;
    }
}
