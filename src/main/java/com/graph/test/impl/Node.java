package com.graph.test.impl;

import java.util.ArrayList;
import java.util.List;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    private T label;
    List<Edge<T>> edges = new ArrayList<Edge<T>>();

    public Node(T label) {
        this.label = label;
    }

    public Node() {

    }

    public T getLabel() {
        return label;
    }

    public void setLabel(T label) {
        this.label = label;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge<T>> edges) {
        this.edges = edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> that = (Node<?>) o;
        return label == that.label;
    }

    @Override
    public int compareTo(Node<T> o) {
        return getLabel().compareTo(o.getLabel());
    }


}
