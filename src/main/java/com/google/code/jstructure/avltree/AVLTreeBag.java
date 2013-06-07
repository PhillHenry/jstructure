package com.google.code.jstructure.avltree;

public class AVLTreeBag<T extends Comparable<T>> {
    
    private Node<T> root;
    
    private int size;

    public boolean remove(T item) {
        Node<T> node    = root;
        Node<T> parent  = null;
        while (node != null) {
            if (item.compareTo(node.getItem()) > 0) {
                parent = node;
                node = node.getRight();
            } else if (item.compareTo(node.getItem()) < 0)  { // if items are equal, arbitrarily use left value
                parent = node;
                node = node.getLeft();
            } else {
                break;
            }
        }
        System.out.println("node = " + node + ", parent = " + parent);
        if (node == null)
            return false;
//        if (node == root) {
//            throw new UnsupportedOperationException("todo handle root");
//        } else {
            if (node.getNumberOfChildren() == 0) {
                if (parent != null) {
                    parent.replace(node, null);
                } else {
                    root = null;
                }
            } else if (node.getNumberOfChildren() == 1) {
                Node<T> with;
                if (node.getLeft() != null) {
                    with = node.getLeft();
                } else {
                    with = node.getRight();
                }
                if (parent != null) {
                    parent.changePositions(node, with);
                } else {
                    root = with;
                }
                with.setParent(parent);
                checkConsistency(with, false);
            } else {
                Node<T> predecessor = largestIn(node.getLeft());
                Node<T> predecessorParent = predecessor.getParent();
                Node<T> oldLeft = predecessor.getLeft();
                if (predecessorParent != node) {
                    predecessorParent.setRight(oldLeft);
                    predecessor.setLeft(node.getLeft());
                } else {
                    predecessor.setRight(node.getRight());
                }
                if (parent != null) {
                    parent.changePositions(node, predecessor);
                } else {
                    root = predecessor;
                }
                predecessor.setParent(parent);
                checkConsistency(oldLeft == null ? predecessorParent : oldLeft, false);
            }
//        }
        size--;
        return true;
    }
    
    private Node<T> largestIn(Node<T> subtreeRoot) {
        Node<T> right = subtreeRoot.getRight();
        if (right != null) {
            return largestIn(right);
        } 
        return subtreeRoot;
    }

    public void add(T item) {
        if (item == null) 
            throw new IllegalArgumentException();
        internalAdd(item);
        size++;
    }
    
    private void internalAdd(T item) {
        if (root == null) {
            root = new Node<T>(null, null, item, null);
            return;
        }
        Node<T> node    = root;
        Node<T> parent  = null;
        boolean isRight = false;
        while (node != null) {
            parent = node;
            if (item.compareTo(node.getItem()) > 0) {
                node = node.getRight();
                isRight = true;
            } else  { // if items are equal, arbitrarily use left value
                node = node.getLeft();
                isRight = false;
            }
        }
        
        Node<T> newNode = new Node<T>(null, null, item, parent);
        if (isRight) {
            parent.setRight(newNode);
        } else {
            parent.setLeft(newNode);
        }
        
        checkConsistency(newNode, isRight);
    }

    void checkConsistency(Node<T> node, boolean isRight) {
        Node<T> grandParent = getGrandParent(node);
        if (grandParent == null) return;
        
        Node<T> parent = node.getParent();
        if (grandParent.balance() > 1) {
            if (isRight) {
                rightRight(parent);
            } else {
                rightLeft(parent);
            }
        } else if (grandParent.balance() < -1) {
            if (isRight) {
                leftRight(parent);
            } else {
                leftLeft(parent);
            }
        } else {
            checkConsistency(parent, parent.isRight(node));
        }
    }
    
    void rightRight(Node<T> node) {
        Node<T> oldParent = swapPlacesWithParent(node);
        Node<T> oldLeft = node.getLeft();
        node.setLeft(oldParent);
        oldParent.setRight(oldLeft);
    }

    void rightLeft(Node<T> node) {
        Node<T> oldParent = swapPlacesWithParent(node);
        Node<T> oldRight = node.getRight();
        node.setRight(oldParent);
        oldParent.setLeft(oldRight);
        rightRight(node);
    }

    void leftLeft(Node<T> node) {
        Node<T> oldParent = swapPlacesWithParent(node);
        Node<T> oldRight = node.getRight();
        node.setRight(oldParent);
        oldParent.setLeft(oldRight);
    }

    void leftRight(Node<T> node) {
        Node<T> oldParent = swapPlacesWithParent(node);
        Node<T> oldLeft = node.getLeft();
        node.setLeft(oldParent);
        oldParent.setRight(oldLeft);
        leftLeft(node);
    }

    private Node<T> swapPlacesWithParent(Node<T> node) {
        Node<T> oldParent       = node.getParent();
        Node<T> oldGrandParent  = oldParent.getParent();
        node.setParent(oldGrandParent);
        if (oldGrandParent == null) {
            root = node;
        } else {
            oldParent.replace(node, null);
            oldGrandParent.changePositions(oldParent, node);
        }
        oldParent.setParent(node);
        return oldParent;
    }

    private Node<T> getGrandParent(Node<T> node) {
        Node<T> parent = node.getParent();
        if (parent == null) return null;
        return parent.getParent();
    }

    public int size() {
        return size;
    }

    public T elementAtPercentile(int percentile) {
        if (percentile > 100 || percentile < 0) throw new IllegalArgumentException();
        
        Node<T> node = root;
        
        int index = size * percentile / 100;
        
        int numChildrenToLeft = 0;
        while (node.getNumberOfChildren() != 0) {
            int numToLeft = numberOfChildrenToLeft(node);
            if (numChildrenToLeft + 1 == index) { // + 1 because we include self
                break;
            }
            Node<T> child = null;
            int totalToLeft = numChildrenToLeft + numToLeft;
            if (totalToLeft < index) {
                child = node.getRight();
                numChildrenToLeft += numToLeft;
            } else { 
                child = node.getLeft();
            }
            numChildrenToLeft++; // include this node
            if (child == null) {
                break;
            }
            node = child;
        }
        
        return node.getItem();
    }
    
    private int numberOfChildrenToLeft(Node<T> node) {
        Node<T> leftChild = node.getLeft();
        return leftChild == null ? 0 : leftChild.getNumberOfChildren();
    }

    Node<T> getRoot() {
        return root;
    }


}
