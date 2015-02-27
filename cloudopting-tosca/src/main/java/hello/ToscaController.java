package hello;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.cloudopting.tosca.transformer.ToscaFileManager;

@RestController
public class ToscaController {

//	private ToscaFileManager tfm = null;
	@RequestMapping("/tosca/")
	public String index() {
		DefaultDirectedGraph<String, DefaultEdge> g;

		g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

		g.addVertex("vm");
		g.addVertex("cont1");
		g.addVertex("cont2");
		g.addVertex("apa");
		g.addVertex("vh");
		g.addVertex("my");

		g.addEdge("vm", "cont1");
		g.addEdge("vm", "cont2");
		g.addEdge("cont1", "apa");
		g.addEdge("apa", "vh");
		g.addEdge("cont2", "my");

		String v;
		TopologicalOrderIterator<String, DefaultEdge> orderIterator;

		orderIterator = new TopologicalOrderIterator<String, DefaultEdge>(g);
		System.out.println("\nOrdering:");
		while (orderIterator.hasNext()) {
			v = orderIterator.next();
			System.out.println(v);
		}

		// TODO here we need to get back the xml from the DB given the service ID
		// Now as a workaround I read the file from the FS
		String filePath = new String("ClearoExample.xml");
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// With the retrieved XML we instantiate the ToscaFileManager that is the only one that know how to read it
		ToscaFileManager tfm = new ToscaFileManager(xml);
		
		// Now we need to start the work on the TOSCA file we start from the root that we know is the CloudVM to be created
		
		
		
		return "Tosca Controller";
	}

}
