package com.graph.test.impl;


import com.graph.test.interfaces.GraphParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DirectedGraphParser<T  extends Comparable<T> > implements GraphParser<T> {

    /**
     * Parse the xml file and return directed graph.
     *
     * @param inputStream the Xml file as inputStream
     * @return The Directed graph
     */
    @Override
    public DirectedGraph<T> parse(InputStream inputStream) throws ParserException, IOException {
        List<Node<T>> nodes = new ArrayList<>();
        List<Edge<T>> edges = new ArrayList<>();
        Node<T> node = null;
        Edge<T> edge = null;
        Node<T> nodeTo = null;
        Attribute graphAttr = null;

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            if(inputStream == null)
                throw new IOException("Input Xml file is null");

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
            while(xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();

                    //Get the 'node' element
                    if(startElement.getName().getLocalPart().equals("node")){

                        node = new Node();
                        edges = new ArrayList<>();
                        //Get the 'label' attribute from Node element
                        graphAttr = startElement.getAttributeByName(new QName("label"));
                        if(graphAttr != null){
                            node.setLabel((T)(graphAttr.getValue()));
                        } else throw new ParserException("Label is null in node");
                    }
                    //Get the 'edge' element
                    else if(startElement.getName().getLocalPart().equals("edge")){
                        edge = new Edge<>();
                        nodeTo = new Node();
                        //Get the 'to' attribute from Edge element
                        graphAttr = startElement.getAttributeByName(new QName("to"));
                        if(graphAttr != null){
                            nodeTo.setLabel((T)(graphAttr.getValue()));
                        }else throw new ParserException("Edage is not correct. 'to' is not defined");

                        edge.setTo(nodeTo);
                        //Get the 'weight' attribute from Edge element
                        graphAttr = startElement.getAttributeByName(new QName("weight"));
                        if(graphAttr != null){
                            edge.setWeight( Double.parseDouble(graphAttr.getValue()));
                        }else  throw new ParserException("Weight of Edage is not correct. " +
                                "'Weight' is not defined");

                      edges.add(edge);
                    }
                }
                //if Node end element is reached, add node object to list
                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();
                    if(endElement.getName().getLocalPart().equals("node")){
                        node.setEdges(edges);
                        nodes.add(node);

                    }
                }
            }
            DirectedGraph<T> graph = new DirectedGraph<>(nodes);

            return graph;

        }catch (ParserException px)
        {
            throw px;
        }catch (IOException ioException)
        {
            throw ioException;
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        return null;
    }


}
