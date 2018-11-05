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
public enum PMNodeType {
    ROOT,
    GROUP,
    DOC,
    OTHERS,
    PROJECT;

    public static PMNodeType parse(String s) {
        PMNodeType res = null;
        if (s != null) {
            String test = s.toUpperCase();
            for (PMNodeType v : values()) {
                if (test.equals(v.name())) {
                    res = v;
                    break;
                }
            }
        }
        return res;
    }
}
