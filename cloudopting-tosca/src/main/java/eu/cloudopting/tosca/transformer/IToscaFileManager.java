package eu.cloudopting.tosca.transformer;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xml.dtm.ref.DTMNodeList;

public interface IToscaFileManager {

	public abstract void setToscaFile(String xmlFile);

	public abstract DTMNodeList getNodeByType(String type);

	/**
	 * The TOSCA file could contain more than one root since there could be
	 * elements that do not have children or parents (at the moment the standard
	 * do not force a single root We know that we start working from the VMhost
	 * type so we consider that the root
	 */
	public abstract void getRootNode();

	public abstract String getTemplateForNode(String id);

	public abstract ArrayList<String> getOrderedContainers();

	public abstract HashMap getPropertiesForNode(String id);

}