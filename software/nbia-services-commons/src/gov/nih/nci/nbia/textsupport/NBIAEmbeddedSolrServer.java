package gov.nih.nci.nbia.textsupport;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import gov.nih.nci.nbia.util.NCIAConfig;

public class NBIAEmbeddedSolrServer {
	   private static NBIAEmbeddedSolrServer instance = null;
	   private static SolrServer server;
	   private static String solrHome=NCIAConfig.getSolrHome();
	   static Logger log = Logger.getLogger(NBIAEmbeddedSolrServer.class);
	   protected NBIAEmbeddedSolrServer() {
	   }
	   public static NBIAEmbeddedSolrServer getInstance() {
	      if(instance == null) {
	         instance = new NBIAEmbeddedSolrServer();
	         instance.startServer();
	      }
	      return instance;
	   }
	   private void startServer()
	   {
		   log.warn("SolrHome is "+solrHome);
			server = new EmbeddedSolrServer(CoreContainer.createAndLoad(
					solrHome, new File(solrHome + "solr.xml")), null);
			log.warn("Embedded Solr Server started successfully");
	   }
	   private void stopServer()
	   {
		   server.shutdown();
	   }
	   protected void finalize() throws Throwable {
		     try {
		    	 stopServer();        // close open files
		     } finally {
		         super.finalize();
		     }
	   }
	   public SolrServer GetServer()
	   {
		   return server;
	   }
	   public String getSolrHome()
	   {
		   return solrHome;
	   }
}
