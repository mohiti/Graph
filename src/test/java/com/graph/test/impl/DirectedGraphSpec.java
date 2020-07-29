package com.graph.test.impl;

import com.graph.test.interfaces.Graph;
import com.graph.test.interfaces.GraphBuilder;
import com.graph.test.interfaces.GraphParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("<= Directed Graph Specification =>")
public class DirectedGraphSpec<T extends Comparable<T>> {

    private DirectedGraph directGraph;
    private DirectedGraphParser parser;
    private DirectedGraphPrinter directedGraphPrinter;

    private InputStream file;
    static InputStream errorFile;
    static ClassLoader classloader;


    @BeforeAll
    static void prepareXmlFile() throws FileNotFoundException {
        classloader = Thread.currentThread().getContextClassLoader();
    }

    @BeforeEach
    void init() throws Exception {
        directGraph = new DirectedGraph();
        parser = new DirectedGraphParser();
        directedGraphPrinter = new DirectedGraphPrinter();

        file = classloader.getResourceAsStream("samples/smallGraph.xml");
        directGraph = parser.parse(file);
    }


    @Test
    @DisplayName("Directed Graph is empty when no node is added to it")
    public void directGraphEmptyWhenNoNodeAdded() {
        directGraph = new DirectedGraph();
        Collection<T> nodes = directGraph.getLabels();
        assertTrue(nodes.isEmpty(), () -> "Graph should be empty");
    }

    @Test
    @DisplayName("GraphParser import count of nodes correctly ")
    public void graphParserImportCountOfNodesIsCorrect() throws IOException, GraphParser.ParserException {
        assertTrue(directGraph.getLabels().size() == 10, () -> "Xml file don't import correct. Number of node is not true");
    }

    @Test
    @DisplayName("GraphParser import graph nodes correctly ")
    public void graphParserImportNodesCorrectly() throws IOException, GraphParser.ParserException {
        assertTrue(directGraph.getLabels().containsAll(Arrays.asList("(77|77)", "(29|47)", "(91|61)",
                "(19|54)", "(60|48)", "(73|62)", "(15|53)", "(84|75)", "(41|20)", "(95|44)")), () -> "Xml file don't import correct. nodes are not true");
    }

    @Test
    @DisplayName("GraphParser import count of edges correctly ")
    public void graphParserImportCountOfEdgesCorrectly() throws IOException, GraphParser.ParserException {
        assertTrue(directGraph.getNumberOfEdge() == 44, () -> "Xml file don't import correct. " +
                " Number of edges is not correct. Number of edge is " + directGraph.getNumberOfEdge());
    }

    @Test
    @DisplayName("GraphParser import weight of selected edges correctly ")
    public void graphParserImportWeightOfSelectedEdgesCorrectly() throws IOException, GraphParser.ParserException {


        assertTrue(directGraph.getWeight("(41|20)", "(29|47)") == 268.36502928384897, () -> "Xml file don't import correct. " +
                " Weight of that edge is incorrect. (41|20) -> (29|47). ");
        assertTrue(directGraph.getWeight("(95|44)", "(77|77)") == 39.546657567035446, () -> "Xml file don't import correct. " +
                " Weight of that edge is incorrect. (95|44) -> (77|77). ");
        assertTrue(directGraph.getWeight("(91|61)", "(91|61)") == 0.0, () -> "Xml file don't import correct. " +
                " Weight of that edge is incorrect. (91|61) -> (91|61). ");
        assertTrue(directGraph.getWeight("(60|48)", "(29|47)") == 422.56236972213134, () -> "Xml file don't import correct. " +
                " Weight of that edge is incorrect. (60|48) -> (29|47). ");
        assertTrue(directGraph.getWeight("(15|53)", "(41|20)") == 47.09963034331464, () -> "Xml file don't import correct. " +
                " Weight of that edge is incorrect. (15|53) -> (41|20). ");


    }

    @Test
    @DisplayName("Throws exception when edge is not exist")
    void throwsExceptionWhenEdgeNotExist() throws IOException, GraphParser.ParserException {

        IllegalArgumentException throwException = assertThrows(IllegalArgumentException.class
                , () -> directGraph.getEdge("(29|47)", "(29|48)"));
        assertEquals("Node (29|48) does not exist", throwException.getMessage());
    }

    @Test
    @DisplayName("Node is not exist")
    void nodeNotExist() throws IOException, GraphParser.ParserException {

        Node node = new Node("(29|48)");

        assertTrue(directGraph.nodeIsExist(node) == -1, () -> "Xml file don't import correct. Node is exist!");
    }

    @Test
    @DisplayName("Throws ParserException when Xml file element is not correct")
    void throwsParserExceptionWhenXmlFileIsNotCorrect() throws IOException {
        errorFile = classloader.getResourceAsStream("samples/smallGraphWithError.xml");
        GraphParser.ParserException throwException = assertThrows(GraphParser.ParserException.class
                , () -> parser.parse(errorFile));
        assertEquals("Edage is not correct. 'to' is not defined", throwException.getMessage());
    }

    @Test
    @DisplayName("Throws ParserException when Label is null in Xml file element")
    void throwsParserExceptionWhenLabelIsNullInNodeInXmlFile() {
        errorFile = classloader.getResourceAsStream("samples/smallGraphWithError2.xml");
        GraphParser.ParserException throwException = assertThrows(GraphParser.ParserException.class
                , () -> parser.parse(errorFile));
        assertEquals("Label is null in node", throwException.getMessage());
    }

    @Test
    @DisplayName("Throws Exception when file is not exist")
    void throwsExceptionWhenFileIsNotInExist() {
        errorFile = classloader.getResourceAsStream("samples/smallGraphWithError2222.xml");
        IOException throwException = assertThrows(IOException.class
                , () -> parser.parse(errorFile));
        assertEquals("Input Xml file is null", throwException.getMessage());
    }

    @Test
    @DisplayName("Throws GraphBuildException when from node is not exist")
    void throwsGraphBuildExceptionWhenFromNodeIsNotExist() {
        GraphBuilder<Integer> graphBuilder = new DirectedGraphBuilder();

        for (int i = 0; i < 10; i++) {
            graphBuilder.addNode(i);
        }

        GraphBuilder.GraphBuildException throwException = assertThrows(GraphBuilder.GraphBuildException.class
                , () -> graphBuilder.addEdge(15, 4, 12));
        assertEquals("'from' node is not exist", throwException.getMessage());
    }

    @Test
    @DisplayName("Throws GraphBuildException when to node is not exist")
    void throwsGraphBuildExceptionWhenToNodeIsNotExist() {
        GraphBuilder<Integer> graphBuilder = new DirectedGraphBuilder();

        for (int i = 0; i < 10; i++) {
            graphBuilder.addNode(i);
        }

        GraphBuilder.GraphBuildException throwException = assertThrows(GraphBuilder.GraphBuildException.class
                , () -> graphBuilder.addEdge(3, 15, 12));
        assertEquals("'to' node is not exist", throwException.getMessage());
    }

    @Test
    @DisplayName("GraphBuilder build graph correctly ")
    public void graphBuilderBuildGraphCorrectly() throws IOException, GraphParser.ParserException {

        DirectedGraphBuilder<Integer> graphBuilder = new DirectedGraphBuilder<>();

        for (int i = 0; i < 5; i++) {
            graphBuilder.addNode(i);
        }
        graphBuilder.addEdge(0, 1, 10);
        graphBuilder.addEdge(1, 2, 20);
        graphBuilder.addEdge(1, 3, 24);
        graphBuilder.addEdge(2, 3, 32);
        graphBuilder.addEdge(2, 4, 44);
        graphBuilder.addEdge(3, 4, 12);
        DirectedGraph<Integer> graph = graphBuilder.buildGraph();
        assertTrue(graph.getLabels().containsAll(Arrays.asList(0, 1, 2, 3, 4)), () -> "Graph not build correctly. ");
        assertTrue(graph.getNumberOfEdge() == 6, () -> "Graph not build correctly. Number of edges is not correct.");

        TestGraphEdge<Integer> testGraphEdge = (Integer x, Integer y, double z) -> {
            for (Node node : graph.getNodes()
            ) {
                if (node.getEdges() != null)
                    for (int i = 0; i < node.getEdges().size(); i++) {
                        if (((Edge) node.getEdges().get(i)).getTo().getLabel().equals(y)
                                && node.getLabel().equals(x) && ((Edge) node.getEdges().get(i)).getWeight() == z) {
                            return true;
                        }

                    }
            }
            return false;
        };
        assertTrue(testGraphEdge.testEdge(0, 1, 10), () -> "Graph not build correctly. Edge 0 -> 1 is not correct.");
        assertTrue(testGraphEdge.testEdge(1, 2, 20), () -> "Graph not build correctly. Edge 1 -> 2 is not correct.");
        assertTrue(testGraphEdge.testEdge(1, 3, 24), () -> "Graph not build correctly. Edge 1 -> 3 is not correct.");
        assertTrue(testGraphEdge.testEdge(2, 3, 32), () -> "Graph not build correctly. Edge 2 -> 3 is not correct.");
        assertTrue(testGraphEdge.testEdge(2, 4, 44), () -> "Graph not build correctly. Edge 2 -> 4 is not correct.");
        assertTrue(testGraphEdge.testEdge(3, 4, 12), () -> "Graph not build correctly. Edge 3 -> 4 is not correct.");


    }

    @FunctionalInterface
    interface TestGraphEdge<T> {
        boolean testEdge(T from, T to, double weight);
    }


    @Test
    @DisplayName("GraphPrinter print graph correctly ")
    public void graphPrinterPrintGraphCorrectly() throws IOException, GraphParser.ParserException {

        DirectedGraphBuilder<Integer> graphBuilder = new DirectedGraphBuilder<>();

        for (int i = 0; i < 5; i++) {
            graphBuilder.addNode(i);
        }
        graphBuilder.addEdge(0, 1, 10);
        graphBuilder.addEdge(1, 2, 20);
        graphBuilder.addEdge(1, 3, 24);
        graphBuilder.addEdge(2, 3, 32);
        graphBuilder.addEdge(2, 4, 44);
        graphBuilder.addEdge(3, 4, 12);

        Graph<Integer> graph = graphBuilder.buildGraph();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        // After this all System.out.println() statements will come to outContent stream.
        System.setOut(new PrintStream(outContent));

        directedGraphPrinter.print(graph, System.out);

        String expectedOutput = "digraph testGraph {\t\n" +
                "node [shape = circle];\t\n" +
                "\"0\" -> \"1\" [label = 10.0];\t\n" +
                "\"1\" -> \"2\" [label = 20.0];\t\n" +
                "\"1\" -> \"3\" [label = 24.0];\t\n" +
                "\"2\" -> \"3\" [label = 32.0];\t\n" +
                "\"2\" -> \"4\" [label = 44.0];\t\n" +
                "\"3\" -> \"4\" [label = 12.0];\t\n" +
                "}";
        assertEquals(expectedOutput, outContent.toString());


    }

    @Test
    @DisplayName("GraphPrinter print graph correctly ")
    public void graphPrinterPrintGraphByFormatterCorrectly() throws IOException, GraphParser.ParserException {

        DirectedGraphBuilder<Integer> graphBuilder = new DirectedGraphBuilder<>();

        for (int i = 0; i < 5; i++) {
            graphBuilder.addNode(i);
        }
        graphBuilder.addEdge(0, 1, 10);
        graphBuilder.addEdge(1, 2, 20);
        graphBuilder.addEdge(1, 3, 24);
        graphBuilder.addEdge(2, 3, 32);
        graphBuilder.addEdge(2, 4, 44);
        graphBuilder.addEdge(3, 4, 12);


        Graph<Integer> graph = graphBuilder.buildGraph();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        // After this all System.out.println() statements will come to outContent stream.
        System.setOut(new PrintStream(outContent));

        Function<Integer, String> formatter = x -> x.toString();
        directedGraphPrinter.print(graph, System.out, formatter);

        String expectedOutput = "digraph testGraph {\t\n" +
                "node [shape = circle];\t\n" +
                "\"0\" -> \"1\" [label = 10.0];\t\n" +
                "\"1\" -> \"2\" [label = 20.0];\t\n" +
                "\"1\" -> \"3\" [label = 24.0];\t\n" +
                "\"2\" -> \"3\" [label = 32.0];\t\n" +
                "\"2\" -> \"4\" [label = 44.0];\t\n" +
                "\"3\" -> \"4\" [label = 12.0];\t\n" +
                "}";
        assertEquals(expectedOutput, outContent.toString());


    }

}
