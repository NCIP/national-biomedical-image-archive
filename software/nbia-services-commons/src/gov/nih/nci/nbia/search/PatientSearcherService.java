/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import java.util.List;

import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.concurrent.ExecutorService;

/**
 * This object provides an interface for searching multiple nodes for patients
 * that match a given query.  This interface returns a completion service that
 * can be used to track these asynchronous searches.
 * 
 * <P>p.s. This app does asynchronous searches, but theoretically the executor
 * service underneath could be synchronous.
 */
public interface PatientSearcherService {

	/**
	 * Kick off a patient search against the specified nodes.
	 */
	public PatientSearchCompletionService searchForPatients(List<NBIANode> nodesToSearch,
                                                            DICOMQuery dicomQuery);
	
	/**
	 * Set the executor service to execute the search requests with.
	 * This MUST be called before invoking {@link #searchForPatients}
	 */
	public void setExecutorService(ExecutorService executorService);
}
