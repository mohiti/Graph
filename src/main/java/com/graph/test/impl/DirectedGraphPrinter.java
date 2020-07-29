package com.graph.test.impl;

import com.graph.test.interfaces.Graph;
import com.graph.test.interfaces.GraphParser;
import com.graph.test.interfaces.GraphPrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class DirectedGraphPrinter implements GraphPrinter {

    /**
     * Print graph using DOT language format. they can use output in http://www.webgraphviz.com/ to visualize the graph
     *
     * @param graph The graph as a input
     * @param appendable   is An object to which graph char sequences and values can be appended
     * @param formatter  is a functional interface to convert T to String
     */
    @Override
    public <T> void print(Graph<T> graph, Appendable appendable, Function<? super T, String> formatter) throws IOException {
        appendable.append("digraph testGraph {\t\n");
        appendable.append("node [shape = circle];\t\n");
        if (graph != null) {
            Collection<T> tCollection = graph.getLabels();

            for (T label : tCollection
            ) {

                Graph.EdgeConsumer edgeConsumer = new Graph.EdgeConsumer() {
                    @Override
                    public void accept(Comparable from, Comparable to, double weight) {
                        try {
                            appendable.append("\"" + formatter.apply((T) from) + "\" -> \"" + formatter.apply((T) to) + "\" [label = " + weight + "];\t\n");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                };
                graph.forEachNeighbor(label, edgeConsumer);
            }
        }

        appendable.append("}");
    }

    /**
     * Print graph using DOT language format. they can use output in http://www.webgraphviz.com/ to visualize the graph
     *
     * @param graph The graph as a input
     * @param appendable   is An object to which graph char sequences and values can be appended
     */
    @Override
    public <T> void print(Graph<T> graph, Appendable appendable) throws IOException {

        appendable.append("digraph testGraph {\t\n");
        appendable.append("node [shape = circle];\t\n");
        if (graph != null) {
            Consumer<T> visitor = x -> {

                Graph.EdgeConsumer edgeConsumer = new Graph.EdgeConsumer() {
                    @Override
                    public void accept(Comparable from, Comparable to, double weight) {
                        try {
                            appendable.append("\"" + from + "\" -> \"" + to + "\" [label = " + weight + "];\t\n");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                };
                graph.forEachLebelVistAllEdge(x, edgeConsumer);
            };

            graph.forEachLabel(visitor);

        }
        appendable.append("}");

    }


}
