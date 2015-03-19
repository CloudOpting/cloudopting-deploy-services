package eu.cloudopting.tosca.transformer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component("toscaFileManager")
public class ToscaFileManager implements IToscaFileManager {

	@Value("")
	private String xmlFile;
	private DefaultDirectedGraph<String, DefaultEdge> g;
	private DocumentImpl document = null;
	private XPathImpl xpath;

	private static AtomicReference<ToscaFileManager> INSTANCE = new AtomicReference<ToscaFileManager>();

	public ToscaFileManager() {
		final ToscaFileManager previous = INSTANCE.getAndSet(this);
		if (previous != null)
			throw new IllegalStateException("Second singleton " + this
					+ " created after " + previous);
	}

	public static ToscaFileManager getInstance() {
		return INSTANCE.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cloudopting.tosca.transformer.IToscaFileManager#setToscaFile(java.
	 * lang.String)
	 */
	@Override
	public void setToscaFile(String xmlFile) {

		this.xmlFile = xmlFile;
		this.g = new DefaultDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);

//		System.out.println(xmlFile);

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
		// this.document = nul;
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
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate",
					this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the RelationshipTemplate
		DTMNodeList relations = null;
		try {
			relations = (DTMNodeList) this.xpath.evaluate(
					"//RelationshipTemplate", this.document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now we create the Graph structure so we know the correct traversal
		// ordering
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < nodes.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
//			System.out.println(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
			this.g.addVertex(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
		}

		for (int i = 0; i < relations.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			NodeList nl = relations.item(i).getChildNodes();
//			System.out.println(nl.item(0).getNodeValue());
//			System.out.println(nl.item(1).getNodeValue());

//			System.out.println("relation s:"+ nl.item(1).getAttributes().getNamedItem("ref").getNodeValue());
//			System.out.println("relation t:"+ nl.item(3).getAttributes().getNamedItem("ref").getNodeValue());
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
		// this.g.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cloudopting.tosca.transformer.IToscaFileManager#getNodeByType(java
	 * .lang.String)
	 */
	@Override
	public DTMNodeList getNodeByType(String type) {
		if (this.xmlFile == null)
			return null;
		// Get the node by the type
		DTMNodeList nodes = null;
		System.out.println("//NodeTemplate[@type='" + type + "']");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate[@type='"
					+ type + "']", this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cloudopting.tosca.transformer.IToscaFileManager#getRootNode()
	 */
	@Override
	public void getRootNode() {
		getNodeByType("VMhost");
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cloudopting.tosca.transformer.IToscaFileManager#getTemplateForNode
	 * (java.lang.String)
	 */
	@Override
	public String getTemplateForNode(String id,String templateType) {
		if (this.xmlFile == null)
			return null;
		//
		DTMNodeList nodes = null;
		System.out.println("//NodeTemplate[@id='" + id + "']");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate[@id='"
					+ id + "']", this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		// We need to get the type
		String type = nodes.item(0).getAttributes().getNamedItem("type")
				.getNodeValue();
		DTMNodeList nodesTI = null;
		System.out.println("//NodeTypeImplementation[@nodeType='" + type
				+ "']/ImplementationArtifacts/ImplementationArtifact[@artifactType='"+templateType+"']");
		try {
			nodesTI = (DTMNodeList) this.xpath
					.evaluate(
							"//NodeTypeImplementation[@nodeType='"
									+ type
									+ "']/ImplementationArtifacts/ImplementationArtifact[@artifactType='"+templateType+"']",
							this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String template = nodesTI.item(0).getAttributes()
				.getNamedItem("artifactRef").getNodeValue();
		System.out.println("The template is:" + template);
		return template;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cloudopting.tosca.transformer.IToscaFileManager#getOrderedContainers()
	 */
	@Override
	public ArrayList<String> getOrderedContainers() {
		if (this.xmlFile == null)
			return null;
		// TODO here I return a fixed list will have to check the TOSCA instance
		// file to get the ORDERED list of DockerContainers
		// ORDERED means that reading the TOSCA file we need to determine the
		// dependencies (probably using a graph) and presenting the list with
		// the first to go in first position
		ArrayList<String> dockerNodesList = new ArrayList<String>();
		dockerNodesList.add("ClearoApacheDC");
		dockerNodesList.add("ClearoMySQLDC");
		return dockerNodesList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cloudopting.tosca.transformer.IToscaFileManager#getPropertiesForNode
	 * (java.lang.String)
	 */
	@Override
	public HashMap getPropertiesForNode(String id) {
		if (this.xmlFile == null)
			return null;
		DTMNodeList nodes = null;
		System.out.println("//NodeTemplate[@id='" + id + "']/Properties/*");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate[@id='"
					+ id + "']/Properties/*", this.document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> myHash = new HashMap<String, String>();
		NodeList props = nodes.item(0).getChildNodes();
		for (int i = 0; i < props.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());

//			System.out.println("property val:" + props.item(i).getTextContent());
			String[] keys = props.item(i).getNodeName().split(":");
			if (keys.length > 1) {
				String key = keys[1];
				System.out.println("property:" + key);
				myHash.put(key, props.item(i).getTextContent());
			}
		}

		return myHash;

	}

	public ArrayList<String> getChildrenOfNode(String node) {

		if (this.xmlFile == null)
			return null;
		//
		Set edges = this.g.outgoingEdgesOf(node);
		System.out.println("Children of:" + node + " are:" + edges.toString());
		Iterator<DefaultEdge> iterator = edges.iterator();
		ArrayList<String> children = new ArrayList<String>();
		while(iterator.hasNext()){
			String target = g.getEdgeTarget(iterator.next());
//			System.out.println(target);
			children.add(target);
		}
		
		
		return children;

	}

	public String getNodeType(String id) {
		if (this.xmlFile == null)
			return null;
		//
		DTMNodeList nodes = null;
		System.out.println("//NodeTemplate[@id='" + id + "']");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//NodeTemplate[@id='"
					+ id + "']", this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		// We need to get the type
		String type = nodes.item(0).getAttributes().getNamedItem("type")
				.getNodeValue();
		return type;
	}
	
	public String getServiceName(){
		if (this.xmlFile == null)
			return null;
		//
		DTMNodeList nodes = null;
		System.out.println("//ServiceTemplate/@id");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ServiceTemplate/@id", this.document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		// We need to get the type
		String serviceName = nodes.item(0).getNodeValue();
		return serviceName;
	}
}
