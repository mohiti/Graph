package com.graph.test.impl;

import com.graph.test.interfaces.GraphBuilder;

import java.util.ArrayList;
import java.util.List;


public class DirectedGraphBuilder<T extends Comparable<T>> implements GraphBuilder<T> {

    private List<Node<T>> nodes = new ArrayList<>();

    @Override
    public GraphBuilder<T> addNode(T label) {
        Node<T> node = new Node(label);
        nodes.add(node);

        return this;

    }

    /**
     * Add Edge to Graph.
     *
     * @param from The label of the start node
     * @param to   The label of the target node
     * @param weight The weight of the edge
     * @return the GraphBuilder this
     */
    @Override
    public GraphBuilder<T> addEdge(T from, T to, double weight) throws GraphBuildException {

        Node<T> fromNode = new Node<>(from);

        int fromNodeIndex = nodeIsExist(fromNode);

        if (fromNodeIndex == -1)
            throw new GraphBuildException("'from' node is not exist");


        Node<T> toNode = new Node<>(to);
        int toNodeIndex = nodeIsExist(toNode);

        if (toNodeIndex == -1)
            throw new GraphBuildException("'to' node is not exist");

        Edge<T> newEdge = new Edge<>(toNode, weight);

        nodes.get(fromNodeIndex).getEdges().add(newEdge);
        return this;
    }

    /**
     * Check if a node exist in Graphbuilder.
     *
     * @param testNode The node that we want to check it
     * @return the integer index of node. return -1 if node don't exist
     */
    public int nodeIsExist(Node<T> testNode) {
        int result = -1; // -1 means node is not exist

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).compareTo(testNode) == 0) {
                result = i;
                break;
            }

        }
        return result;
    }

    /**
     * Build method that build graph instance.
     *
     * @return the directed graph instance
     */
    @Override
    public DirectedGraph<T> buildGraph() {

        return new DirectedGraph<T>(this.nodes);
    }


}
