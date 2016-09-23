package gov.nih.nci.nbia.textsupport;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;

import gov.nih.nci.nbia.util.NCIAConfig;

public class NBIAEmbeddedSolrServer implements SolrServerInterface{
	   private static SolrServer server;
	   private static String solrHome=NCIAConfig.getSolrHome();
	   static Logger log = Logger.getLogger(NBIAEmbeddedSolrServer.class);
	   // Spring maintains this as a singleton so this only happens once
	   public NBIAEmbeddedSolrServer() {
	         startServer();
	   }
	   private void startServer()
	   {
		   try {
			log.warn("SolrHome is "+solrHome);
				server = new EmbeddedSolrServer(CoreContainer.createAndLoad(
						solrHome, new File(solrHome + "solr.xml")), null);
				log.info("Embedded Solr Server started successfully");
		} catch (Exception e) {
			log.error("Unable to start Solr");
			e.printStackTrace();
		}
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
