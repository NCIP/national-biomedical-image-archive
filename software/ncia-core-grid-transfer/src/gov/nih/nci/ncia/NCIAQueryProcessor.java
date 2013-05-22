/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.ncia.service.NCIACoreServiceConfiguration;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;
import gov.nih.nci.ncia.domain.TrialDataProvenance;

import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils;
import java.util.List;

import javax.xml.namespace.QName;

import org.globus.wsrf.encoding.ObjectSerializer;

/**
 * @author lethai
 *
 */
public class NCIAQueryProcessor extends SDK4QueryProcessor {
	/*
	 * This method modifies the CQLQuery to apply the filtering
	 * @see gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor#processQuery(gov.nih.nci.cagrid.cqlquery.CQLQuery)
	 */
	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException,
	                                                              QueryProcessingException {
		try {
			String userDN = SecurityUtils.getCallerIdentity();

			System.out.println("userDN:"+userDN);
			if(userDN==null) {
				return processQuery(cqlQuery,
						            new PublicTrialDataProvenance());
			}
			else {
				return processQuery(cqlQuery,
						            new AuthenticatedTrialDataProvenance(userDN));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new QueryProcessingException(ex);
		}
	}

	///////////////////////////////////////////////////PRIVATE//////////////////////////////////////////////////////////

	/**
	 * Process a CQL query according to the authorization level of the
	 * specified user i.e. through a secure grid connection with an authenticated user
	 */
	private CQLQueryResults processQuery(CQLQuery cqlQuery,
			                             TrialDataProvenanceFilter trialDataProvenanceFilter) throws Exception {

        gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();
//		if (cqlTarget.getName().equalsIgnoreCase("gov.nih.nci.ncia.domain.TrialDataProvenance")) {
//			return super.processQuery(cqlQuery);
//		}

		List<TrialDataProvenance> authorizedTdpList = trialDataProvenanceFilter.getDataFilter();
		dumpList(authorizedTdpList);
  		if(authorizedTdpList.size() > 0){
  			if (cqlTarget.getName().equalsIgnoreCase("gov.nih.nci.ncia.domain.TrialDataProvenance") && cqlQuery.getTarget().getGroup() != null 
  					&& GridUtil.retrieveProjectAttribute(cqlQuery.getTarget().getGroup()) !=null) {
  				boolean found = GridUtil.isProjectFound(cqlQuery, authorizedTdpList);
  				
  				if(!found) {
  					return new CQLQueryResults();
  				}
  			}
  			Group tdpGroup = NCIAQueryFilter.convertTDPToCQL(authorizedTdpList);
  			cqlQuery = NCIAQueryFilter.applyFilter(cqlQuery, tdpGroup);

  			System.out.println(ObjectSerializer.toString(cqlQuery,
                                                         new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));

  		}
  		else {
  			return new CQLQueryResults();
  		}

		return super.processQuery(cqlQuery);
	}


	private static void dumpList(List<TrialDataProvenance> list) {
		System.out.println("Authorized TDP*************");
		for(TrialDataProvenance o : list) {
			System.out.println(o.getProject()+"//"+o.getSiteName());
		}
	}
	private static interface TrialDataProvenanceFilter {
		public List<TrialDataProvenance> getDataFilter() throws Exception;

	}

	private static class AuthenticatedTrialDataProvenance implements TrialDataProvenanceFilter {
		public AuthenticatedTrialDataProvenance(String theUsername) {
			this.username = theUsername;
		}

		public List<TrialDataProvenance> getDataFilter() throws Exception {

			return NCIAQueryFilter.getDataFilterByUserName(username);
		}

		private String username;
	}

	private static class PublicTrialDataProvenance implements TrialDataProvenanceFilter {

		public List<TrialDataProvenance> getDataFilter() throws Exception {
			String publicGroupName = NCIACoreServiceConfiguration.getConfiguration().getNciaPublicGroup();
			return NCIAQueryFilter.getDataFilterByGroupName(publicGroupName);
		}
	}
}
