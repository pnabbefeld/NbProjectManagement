/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.pm.docstree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author Peter Nabbefeld
 */
public class PMChildList<E extends PMNodeIndex.IndexValueHolder> {

    private int indexBase;
    private ArrayList<E> delegate;

    public PMChildList() {
        this(0);
    }

    public PMChildList(int indexBase) {
        this.indexBase = indexBase;
    }

    public List<E> getElements() {
        return Collections.unmodifiableList(delegate);
    }

    public E get(int index) {
        return delegate.get(index - indexBase);
    }

    public E getLastElement() {
        return (delegate == null || delegate.isEmpty()) ? null : delegate.get(delegate.size() - 1);
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    public boolean add(E element) {
        ensureDelegateInitialized();
        synchronized (delegate) {
            element.setIndexValue(new PMNodeIndex(indexBase + delegate.size()));
            return delegate.add(element);
        }
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void add(int index, E element) {
        ensureDelegateInitialized();
        synchronized (delegate) {
            delegate.add(index - indexBase, element);
            element.setIndexValue(new PMNodeIndex(index));
            renumber(index - indexBase + 1);
        }
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void remove(int index) {
        int pos = index - indexBase;
        if (delegate == null || delegate.size() <= pos) {
            throw new NoSuchElementException();
        }
        synchronized (delegate) {
            delegate.remove(pos);
            renumber(index - indexBase);
        }
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void move(int oldIndex, int newIndex) {
        int oldPos = oldIndex - indexBase;
        int newPos = newIndex - indexBase;
        if (delegate == null || delegate.size() <= oldPos || delegate.size() <= newPos) {
            throw new NoSuchElementException();
        }
        if (newPos != oldPos) {
            synchronized (delegate) {
                int min = newPos;
                if (newPos > oldPos) {
                    min = oldPos;
                }
                E moving = delegate.remove(oldPos);
                delegate.add(newPos, moving);
                renumber(min);
            }
        }
    }

    private void renumber(int start) {
        int n = delegate.size();
        for (int i = start; i < n; i++) {
            delegate.get(i).getIndexValue().set(indexBase + i);
        }
    }

    private synchronized void ensureDelegateInitialized() {
        if (delegate == null) {
            delegate = new ArrayList<>();
        }
    }
}
