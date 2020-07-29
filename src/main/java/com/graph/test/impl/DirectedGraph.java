package com.graph.test.impl;

import com.graph.test.interfaces.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class DirectedGraph<T extends Comparable<T>> implements Graph<T> {

    private List<Node<T>> nodes;

    public List<Node<T>> getNodes() {
        return nodes;
    }

    public DirectedGraph(List<Node<T>> nodes) {
        this.nodes = nodes;
    }

    public DirectedGraph() {
    }

    /**
     * @return A collection containing all labels of nodes that belong to this graph structure.
     */
    @Override
    public Collection<T> getLabels() {
        List<T> labels = new ArrayList<T>();

        if (nodes != null)
            for (Node node : nodes
            ) {
                labels.add((T) node.getLabel());
            }
        return labels;
    }

    /**
     * Calls visitor for each node
     *
     * @param visitor The callback method that accepts the edges
     */
    @Override
    public void forEachLabel(Consumer<? super T> visitor) {
        for (Node node : nodes
        ) {
            visitor.accept((T) node.getLabel());
        }
    }

    /**
     * Calls visitor for each node combination that is adjacent excluding self loops
     *
     * @param node    The start node
     * @param visitor The callback method that accepts the edges
     */
    @Override
    public void forEachNeighbor(T node, EdgeConsumer<? super T> visitor) {
        Node mainNode = new Node(node);
        int nodeID = nodeIsExist(mainNode);
        if (nodeID != -1) {
            mainNode = nodes.get(nodeID);
            for (int i = 0; i < mainNode.getEdges().size(); i++) {
                Edge<T> tEdge = (Edge<T>) mainNode.getEdges().get(i);
                if (mainNode.compareTo(tEdge.getTo()) != 0)
                    visitor.accept((T) mainNode.getLabel(), tEdge.getTo().getLabel(), tEdge.getWeight());

            }
        }


    }

    /**
     * Calls visitor for each node combination that is adjacent
     *
     * @param node    The start node
     * @param visitor The callback method that accepts the edges
     */
    public void forEachLebelVistAllEdge(T node, EdgeConsumer<? super T> visitor) {
        Node mainNode = new Node(node);
        int nodeID = nodeIsExist(mainNode);
        if (nodeID != -1) {
            mainNode = nodes.get(nodeID);
            for (int i = 0; i < mainNode.getEdges().size(); i++) {
                Edge<T> tEdge = (Edge<T>) mainNode.getEdges().get(i);

                visitor.accept((T) mainNode.getLabel(), tEdge.getTo().getLabel(), tEdge.getWeight());

            }
        }
    }

    /**
     * this method check If a node exist
     *
     * @param testNode The start node
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
     * Returns the weight of the edge from <b>from</b> to <b>to</b>.
     *
     * @param from The label of the start node
     * @param to   The label of the target node
     * @return The weight of the edge from <b>from</b> to <b>to</b>, if there is one,<br />
     * infinity, if there is none or<br /> 0 if <b>from</b> and <b>to</b> refer to the same node.
     */
    @Override
    public double getWeight(T from, T to) {

        Edge<T> selectedEdge = null;
        try {
            selectedEdge = getEdge(from, to);
            return selectedEdge.getWeight();
        } catch (IllegalArgumentException ex) {
            throw ex;
            //return -1;
        }

    }

    /**
     * Returns the Edge of from <b>from</b> to <b>to</b>.
     *
     * @param from The label of the start node
     * @param to   The label of the target node
     * @return The Edge of from <b>from</b> to <b>to</b>, if there is one,<br />
     */
    public Edge<T> getEdge(T from, T to) {

        Node<T> fromNode = new Node<>(from);
        Node<T> toNode = new Node<>(to);
        int fromNodeExist = nodeIsExist(fromNode);
        int toNodeExist = nodeIsExist(toNode);
        Edge<T> selectedEdge = null;

        if (fromNodeExist == -1) {
            throw new IllegalArgumentException("Node " + from.toString()
                    + " does not exist");
        } else if (toNodeExist == -1) {
            throw new IllegalArgumentException("Node " + to.toString()
                    + " does not exist");
        } else {

            for (Edge edge : nodes.get(fromNodeExist).getEdges()
            ) {
                if (edge.getTo().compareTo(toNode) == 0) {
                    selectedEdge = edge;
                    break;
                }
            }

            if (selectedEdge == null) {
                throw new IllegalArgumentException(
                        "Edge between " + from.toString() + " and " + to.toString()
                                + " does not exist");
            } else {
                return selectedEdge;
            }
        }

    }

    /**
     *
     * @return number of edge of graph
     */
    public int getNumberOfEdge() {
        int numberOfEdge = 0;
        if (nodes != null)
            for (Node node : nodes
            ) {
                if (node.getEdges() != null)
                    numberOfEdge += node.getEdges().size();
            }
        return numberOfEdge;
    }


}
