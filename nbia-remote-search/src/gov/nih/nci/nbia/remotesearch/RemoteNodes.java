package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * This object is responsible for talking to the index server and capturing
 * which nodes are out there to be searched.
 */
public class RemoteNodes {

	public static RemoteNodes getInstance() {
		return instance;
	}

	/**
	 * The meat of this object.  This talks to the index server to
	 * find all the NCIACoreService instances with the version 1.3.
	 * Further, it will solicit available search terms for each endpoint
	 * and save them per node.
	 *
	 * <P>This impl iterates over the list from the index service in serial.
	 * Might get a performance boost to send out all available search term
	 * requests in parallel.
	 */
	public void discoverNodes() throws Exception {
		System.out.println("************** INVOKING DISCOVER NODES******************** at "+Util.currrentDateTimeString());
		List<RemoteNode> newRemoteNodes = new ArrayList<RemoteNode>();

		EndpointReferenceType[] endpoints = discoveryClient.discoverServicesByName("NCIACoreService");
		if(endpoints==null) {
			System.out.println("Discovery service returns null endpoint array which I guess means no matches.");
			return;
		}

		System.out.println("Number of NCIACoreService endpoints found in index server:"+endpoints.length);

		for(EndpointReferenceType endpoint : endpoints) {
			try {
				ServiceMetadata serviceMetadata = MetadataUtils.getServiceMetadata(endpoint);
				String version = serviceMetadata.getServiceDescription().getService().getVersion();
	    		String url = endpoint.getAddress().toString();

				if(!version.equals("1.3") ||
			       url.equals(NCIAConfig.getLocalGridURI())) {

					continue;
					//old, new or different service that we don't know about.  too stringent?
					//rather be conservative v. promiscuous i guess
				}
				else {
					System.out.println("Retrieving search terms from:"+endpoint.getAddress());
					long before = System.currentTimeMillis();
					AvailableSearchTerms availableSearchTerms = retrieveAvailableSearchTerms(endpoint);
					System.out.println("!!!!complete Retrieving search terms from:"+endpoint.getAddress());	
					UsAvailableSearchTerms usAvailableSearchTerms = retrieveUsAvailableSearchTerms(endpoint);
					//UsAvailableSearchTerms usAvailableSearchTerms = null;
					long after = System.currentTimeMillis();
					System.out.println("retrieveAvailableSearchTerms time lapse:"+(after-before));
					//DumpUtil.debug(availableSearchTerms);


					RemoteNode remoteNode = new RemoteNode(serviceMetadata,
							                               endpoint,
							                               availableSearchTerms,
							                               usAvailableSearchTerms);
					newRemoteNodes.add(remoteNode);
				}
			}
			catch(Throwable ex) {
				//	if any one fails, keep going and just don't add it to list
				//	log that node was foudn but couldnt get info
				ex.printStackTrace();
			}
		}
		synchronized(lock) {
			remoteNodes = new ArrayList<RemoteNode>(newRemoteNodes);
		}

		System.out.println("************** DISCOVERY COMPLETE******************* at "+Util.currrentDateTimeString());
	}

	/**
	 * Return the most recently discovered nodes.  This will be a 0 length collection
	 * if discoverNodes has never been invoked to completion.  This will never return null.
	 */
	public Collection<RemoteNode> getRemoteNodes() {
		synchronized(lock) {
			return remoteNodes;
		}
	}


	///////////////////////////////////////////PRIVATE/////////////////////////////////////

	/**
	 * singleton instance of this object.
	 */
	private static RemoteNodes instance = new RemoteNodes(NCIAConfig.getIndexServerURL());

	/**
	 * mutex lock for accessing remote nodes while they are being copied from working copy
	 */
	private Object lock = new Object();

	/**
	 * The saved copy of the last discovered nodes.
	 */
	private List<RemoteNode> remoteNodes = new ArrayList<RemoteNode>();

	/**
	 * The client used to talk to the index server.
	 */
	private DiscoveryClient discoveryClient;

	/**
	 * Consruct an instance that consults only the specified index server.
	 * The indexServerUrl can't be null.  If url is illegitimate, won't find out
	 * until we try to use it in discoverNodes.
	 */
	private RemoteNodes(String indexServerUrl)  {
		try {
			System.out.println("Using indexServerUrl:"+indexServerUrl);
			discoveryClient = new DiscoveryClient(indexServerUrl);
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * wrapper method to get the available search terms for a given endpoint
	 */
	private static AvailableSearchTerms retrieveAvailableSearchTerms(EndpointReferenceType endpointReferenceType) throws Exception {
		String serviceAddress = endpointReferenceType.getAddress().toString();		
		NCIACoreServiceClient nciaCoreServiceClient = new NCIACoreServiceClient(serviceAddress);
		return nciaCoreServiceClient.getAvailableSearchTerms();
		/*	
		try {
			return nciaCoreServiceClient.getAvailableSearchTermsNew();
		}
		catch (Exception e){
			return nciaCoreServiceClient.getAvailableSearchTerms();
		}
		*/
	}
	
	/**
	 * wrapper method to get the available search terms for a given endpoint
	 */
	private static UsAvailableSearchTerms retrieveUsAvailableSearchTerms(EndpointReferenceType endpointReferenceType) throws Exception {
		String serviceAddress = endpointReferenceType.getAddress().toString();		
		NCIACoreServiceClient nciaCoreServiceClient = new NCIACoreServiceClient(serviceAddress);
		//return nciaCoreServiceClient.getUsAvailableSearchTerms();
		UsAvailableSearchTerms usAvailableSearchTerms = null;
		try {
			usAvailableSearchTerms = nciaCoreServiceClient.getUsAvailableSearchTerms();
		}
		catch (Exception e){
			System.out.println("!!!!! no ultrasound data in system");
		}
		return usAvailableSearchTerms; 
	}
}
