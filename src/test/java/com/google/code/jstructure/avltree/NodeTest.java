package com.google.code.jstructure.avltree;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class NodeTest {
    private Node<Integer> _5 = nodeWith(5);
    private Node<Integer> _7 = nodeWith(7);
    private Node<Integer> _12 = nodeWith(12);
    private Node<Integer> _25 = nodeWith(25);
    private Node<Integer> _37 = nodeWith(37);
    private Node<Integer> _50 = nodeWith(50);
    private Node<Integer> _67 = nodeWith(67);
    private Node<Integer> _75 = nodeWith(75);
    
    private Node<Integer> _100 = nodeWith(100);
    
    @Test
    public void balanceIncompleteTreeCanBe0() {
        Node<Integer> root = incompletePyramid();
        assertEquals(2, root.getLeftBalance());
        assertEquals(2, root.getRightBalance());
        assertEquals(0, root.balance());
    }
    
    @Test
    public void childrenCount() {
        incompletePyramid();
        assertEquals(5, _50.getNumberOfChildren());
        assertEquals(2, _25.getNumberOfChildren());
        assertEquals(1, _75.getNumberOfChildren());
        assertEquals(0, _12.getNumberOfChildren());
        assertEquals(0, _37.getNumberOfChildren());
        assertEquals(0, _67.getNumberOfChildren());
    }

    @Test
    public void replace() {
        incompletePyramid();
        _50.replace(_75, _100);
        assertEquals(_25, _50.getLeft()); // unchanged
        assertEquals(_100, _50.getRight());
        assertEquals(_50, _100.getParent());
        assertEquals(_100, _67.getParent());
        assertEquals(_67, _100.getLeft());
        assertNull(_100.getRight());
    }
    
    @Test
    @Ignore
    public void swapAtLeaf() {
        incompletePyramid();
        _37.swapWithParent();
        assertEquals(_37, _50.getLeft());
        assertEquals(_12, _37.getLeft());
        assertEquals(_25, _37.getRight());
        assertEquals(_37, _12.getParent());
        assertEquals(_37, _25.getParent());
        assertEquals(_50, _37.getParent());
    }

    @Test
    @Ignore
    public void swapNearTop() {
        incompletePyramid();
        _25.swapWithParent();
        assertEquals(_50, _25.getLeft());
        assertEquals(_75, _25.getRight());
        assertEquals(_12, _50.getLeft());
        assertEquals(_37, _50.getRight());
        
        assertNull(_25.getParent());
        assertEquals(_25, _50.getParent());
        assertEquals(_50, _12.getParent());
        assertEquals(_50, _37.getParent());
    }
    
    /**
     *       50
     *   25      75
     * 12  37  67
     */
    private Node<Integer> incompletePyramid() {
        _50.setLeft(_25).setRight(_75);
        _25.setLeft(_12).setRight(_37);
        _75.setLeft(_67);
        return _50;
    }
    
    @Test
    /**
     *     12
     *   5
     *     7
     */
    public void testZigZag() {
        _12.setLeft(_5);
        _5.setRight(_7);
        assertEquals(-2, _12.balance());
    }

    static Node<Integer> nodeWith(int item) {
        return new Node<Integer>(null, null, item, null);
    }
    
}
