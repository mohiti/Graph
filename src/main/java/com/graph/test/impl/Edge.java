package com.graph.test.impl;

import com.graph.test.interfaces.Graph;

import java.util.LinkedList;

public class Edge<T extends Comparable<T>> {

    private Node<T> to;

    private double weight;



    public Edge( Node<T> to, double weight) {

        this.to = to;
        this.weight = weight;

    }
    public Edge()
    {

    }

    public Node<T> getTo() {
        return to;
    }

    public void setTo(Node<T> to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


}
