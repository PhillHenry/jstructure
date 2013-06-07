package com.google.code.jstructure.avltree;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @see http://www.qmatica.com/DataStructures/Trees/AVL/AVLTree.html
 */
public class AVLTreeBagTest {
    
    private AVLTreeBag<Integer> toTest = new AVLTreeBag<Integer>();
    
    private Node<Integer> _8 = NodeTest.nodeWith(8);
    private Node<Integer> _7 = NodeTest.nodeWith(7);
    private Node<Integer> _6 = NodeTest.nodeWith(6);
    private Node<Integer> _5 = NodeTest.nodeWith(5);
    private Node<Integer> _4 = NodeTest.nodeWith(4);
    private Node<Integer> _3 = NodeTest.nodeWith(3);
    
    @Test
    public void smallAdds() {
        toTest.add(_3.getItem());
        checkRootValue(_3);
        
        toTest.add(_4.getItem());
        Node<Integer> root = checkRootValue(_3);
        checkRightValueOf(root, _4);
        
        toTest.add(_5.getItem());
        root = checkRootValue(_4);
        checkRightValueOf(root, _5);
        checkLeftValueOf(root, _3);
        
        toTest.add(_6.getItem());
        root = checkRootValue(_4);
        checkRightValueOf(root, _5);
        checkRightValueOf(root.getRight(), _6);
        
        toTest.add(_7.getItem());
        root = checkRootValue(_4);
        checkRightValueOf(root, _6);
        checkRightValueOf(root.getRight(), _7);
        checkLeftValueOf(root.getRight(), _5);
        checkNoChildren(root.getRight().getRight());
        checkNoChildren(root.getRight().getLeft());
        
        toTest.add(_8.getItem());
        root = checkRootValue(_6);
        checkLeftValueOf(root, _4);
        checkRightValueOf(root, _7);
        checkRightValueOf(root.getRight(), _8);
    }
    
    private void checkNoChildren(Node<Integer> node) {
        assertNull(node.getRight());
        assertNull(node.getLeft());
        assertEquals(0, node.getNumberOfChildren());
    }
    
    private void checkRightValueOf(Node<Integer> node, Node<Integer> valueOf) {
        Node<Integer> right = node.getRight();
        assertNotNull(right);
        assertEquals(valueOf.getItem(), right.getItem());
    }
    
    private void checkLeftValueOf(Node<Integer> node, Node<Integer> valueOf) {
        Node<Integer> left = node.getLeft();
        assertNotNull(left);
        assertEquals(valueOf.getItem(), left.getItem());
    }
    
    private Node<Integer> checkRootValue(Node<Integer> expectedRoot) {
        Node<Integer> root = toTest.getRoot();
        assertEquals(expectedRoot.getItem(), root.getItem());
        return root;
    }
    
    @Test
    public void checkConsistency() {
        _3.setRight(_4);
        _4.setRight(_5);
        toTest.checkConsistency(_5, true);
        check4IsNewRootAndBalanced();
    }
    
    @Test
    public void leftLeft() {
        _5.setLeft(_4);
        _4.setLeft(_3);
        toTest.leftLeft(_4);
        check4IsNewRootAndBalanced();
    }
    
    @Test
    public void rightRight() {
        _3.setRight(_4);
        _4.setRight(_5);
        toTest.rightRight(_4);
        check4IsNewRootAndBalanced();
    }
    
    @Test
    public void leftRight() {
        _5.setLeft(_3);
        _3.setRight(_4);
        toTest.leftRight(_4);
        check4IsNewRootAndBalanced();
    }
    
    @Test
    public void rightLeft() {
        _3.setRight(_5);
        _5.setLeft(_4);
        toTest.rightLeft(_4);
        check4IsNewRootAndBalanced();
    }

    private void check4IsNewRootAndBalanced() {
        assertEquals(_5, _4.getRight());
        assertEquals(_3, _4.getLeft());
        assertNull(_4.getParent());
        assertEquals(_4, _3.getParent());
        assertEquals(_4, _5.getParent());
        assertNull(_5.getRight());
        assertNull(_5.getLeft());
        assertNull(_3.getRight());
        assertNull(_3.getLeft());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotAddNull() {
        toTest.add(null);
    }
    
    @Test
    public void emptySize() {
        assertEquals(0, toTest.size());
    }
    
    @Test(expected=NullPointerException.class)
    public void emptyGet() {
        toTest.elementAtPercentile(0);
    }

    @Test
    public void elementAt() {
        int total = 1000;
        populate(total);
        assertEquals(new Integer(900), toTest.elementAtPercentile(90));
    }
    
    @Test
    public void elementAtWithRoundingUp(){
        populate(16);
        assertEquals(new Integer(15), toTest.elementAtPercentile(90));
    }
    
    @Test
    public void dupes() {
        toTest.add(10);
        toTest.add(10);
        Node<Integer> root = toTest.getRoot();
        assertEquals(2, toTest.size());
        assertEquals(1, root.getNumberOfChildren());
        // add another
        toTest.add(10);
        assertEquals(3, toTest.size());
        root = toTest.getRoot();
        assertEquals(2, root.getNumberOfChildren());
    }
    
    @Test
    public void remove() {
        int total = 10;
        populate(total);
        for (int i = total ; i > 0 ; --i) {
            assertTrue("" + i, toTest.remove(i - 1));
            assertEquals(i - 1, toTest.size());
        }
    }

    private void populate(int total) {
        for (int i = 0 ; i < total ; i++) {
            toTest.add(i);
        }
        assertEquals(total, toTest.size());
    }
    
}
