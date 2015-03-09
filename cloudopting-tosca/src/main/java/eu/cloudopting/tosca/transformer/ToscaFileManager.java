package eu.cloudopting.tosca.transformer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xpath.XPathFactory;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.apache.xpath.jaxp.XPathImpl;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ToscaFileManager {

	private String xmlFile;
	private DefaultDirectedGraph<String, DefaultEdge> g;
	private DocumentImpl document = null;
	private XPathImpl xpath;

	public ToscaFileManager(String xmlFile) {

		this.xmlFile = xmlFile;
		this.g = new DefaultDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);

		System.out.println(xmlFile);

		// Here we do the stuff to parse the XML
		InputSource source = new InputSource(new StringReader(xmlFile));

		DocumentBuilderFactoryImpl dbf = new DocumentBuilderFactoryImpl();
		DocumentBuilderImpl db = null;
		try {
			db = (DocumentBuilderImpl) dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//		this.document = nul;
		try {
			this.document = (DocumentImpl) db.parse(source);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Now we can do all the XPath we need
		// At the moment we need to create the correct graph of the tree of
		// nodes we need to manage since they could be written casually in the
		// file

		XPathFactoryImpl xpathFactory = (XPathFactoryImpl) XPathFactoryImpl
				.newInstance();
		this.xpath = (XPathImpl) xpathFactory.newXPath();

		// Get the NodeTemplates
		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate", this.document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the RelationshipTemplate
		DTMNodeList relations = null;
		try {
			relations = (DTMNodeList) this.xpath.evaluate("//RelationshipTemplate",
					this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now we create the Graph structure so we know the correct traversal ordering
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < nodes.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			System.out.println(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
			this.g.addVertex(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
		}

		for (int i = 0; i < relations.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			NodeList nl = relations.item(i).getChildNodes();
			System.out.println(nl.item(0).getNodeValue());
			System.out.println(nl.item(1).getNodeValue());

			System.out.println("relation s:"
					+ nl.item(1).getAttributes().getNamedItem("ref")
							.getNodeValue());
			System.out.println("relation t:"
					+ nl.item(3).getAttributes().getNamedItem("ref")
							.getNodeValue());
			// System.out.println(relations.item(i).getFirstChild()
			// .getAttributes().getNamedItem("ref").getNodeValue());
			// System.out.println(relations.item(i).getAttributes()
			// .getNamedItem("id").getNodeValue());
			// this.g.addVertex(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
			this.g.addEdge(nl.item(1).getAttributes().getNamedItem("ref")
					.getNodeValue(),
					nl.item(3).getAttributes().getNamedItem("ref")
							.getNodeValue());
		}
		String v;
		TopologicalOrderIterator<String, DefaultEdge> orderIterator;

		orderIterator = new TopologicalOrderIterator<String, DefaultEdge>(
				this.g);
		System.out.println("\nOrdering:");
		while (orderIterator.hasNext()) {
			v = orderIterator.next();
			System.out.println(v);
		}
		String filePath = new String("ClearoExample.xml");
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.g.

	}
	
	public DTMNodeList getNodeByType(String type){
		// Get the node by the type
		DTMNodeList nodes = null;
		System.out.println("//NodeTemplate[@type='"+type+"']");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate[@type='"+type+"']",
					this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodes;
	}
	
	/**
	 * The TOSCA file could contain more than one root since there could be elements that do not have children or parents (at the moment the standard do not force a single root
	 * We know that we start working from the VMhost type so we consider that the root 
	 */
	public void getRootNode(){
	
		getNodeByType("VMhost");
		return;
	}

}
