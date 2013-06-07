package com.google.code.jstructure.avltree;

class Node<T> {
    
    private Node<T>   left;
    private Node<T>   right;
    private Node<T>   parent;
    private final T   item;
    
    Node(Node<T> left, Node<T> right, T item, Node<T> parent) {
        super();
        this.left   = left;
        this.right  = right;
        this.item   = item;
        this.parent = parent;
    }
    
    int balance() {
        return getRightBalance() - getLeftBalance();
    }
    
    int getRightBalance() {
        return right == null ? 0 : 1 + Math.max(right.getRightBalance(), right.getLeftBalance());
    }
    
    int getLeftBalance() {
        return left == null ? 0 : 1 + Math.max(left.getRightBalance(), left.getLeftBalance());
    }

    Node<T> getLeft() {
        return left;
    }

    Node<T> setLeft(Node<T> left) {
        checkNotSelf(left);
        this.left = left;
        if (left != null) {
            left.setParent(this);
        }
        return this;
    }

    Node<T> getRight() {
        return right;
    }

    Node<T> setRight(Node<T> right) {
        checkNotSelf(right);
        this.right = right;
        if (right != null) {
            right.setParent(this);
        }
        return this;
    }

    Node<T> getParent() {
        return parent;
    }

    void setParent(Node<T> parent) {
        checkNotSelf(parent);
        this.parent = parent;
    }
    
    private void checkNotSelf(Node<T> node) {
        if (node == this) throw new IllegalArgumentException();
    }

    T getItem() {
        return item;
    }
    
    int getNumberOfChildren() {
        return nodesIncludingMe() - 1; // don't count me
    }

    private int nodesIncludingMe() {
        int leftChildren = left == null ? 0 : left.nodesIncludingMe();
        int rightChildren = right == null ? 0 : right.nodesIncludingMe();
        return leftChildren + rightChildren + 1; // + 1 is for me
    }
    
    @Override
    public String toString() {
        return String.format("(%s) %s (%s)", left == null ? "" : left.getItem(), item, right == null ? "" : right.getItem());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.equals(other.item))
            return false;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        return true;
    }

    Node<T> swapWithParent() {
        Node<T> parent      = this.getParent();
        boolean isRight     = parent.isRight(this);
        Node<T> grandParent = parent.getParent();
        parent.replace(this, null);
        if (grandParent != null) {
            grandParent.replace(parent, this);
        }
        Node<T> orphan;
        if (isRight) {
            orphan = getRight();
            setRight(parent);
        } else {
            orphan = getLeft();
            setLeft(parent);
        }
        //return orphan;
        throw new UnsupportedOperationException();
    }
    
    boolean isRight(Node<T> child) {
        return right == child;
    }
    
    boolean isLeft(Node<T> child) {
        return left == child;
    }
    
    Node<T> replace(Node<T> toReplace, Node<T> with) {
        Node<T> replaced = changePositions(toReplace, with);
        if (with != null) {
            with.setLeft(replaced.getLeft());
            with.setRight(replaced.getRight());
            with.setParent(this);
        }
        return replaced;
    }
    
    Node<T> changePositions(Node<T> toReplace, Node<T> with) {
        Node<T> replaced = this.getLeft();
        if (replaced != toReplace) {
            replaced = this.getRight();
            if (replaced != toReplace) {
                return null;
            }
            this.right = with;
        } else {
            this.left = with;
        }
        return replaced;
    }
}