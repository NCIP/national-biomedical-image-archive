/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch.requests;

import java.util.concurrent.Callable;

import gov.nih.nci.nbia.remotesearch.RemoteNode;

/**
 * It turns out there is only one asynchronous remote request we need to deal
 * with... that of the patient search request.  Originally thought there would
 * be more....justifying the need for this superclass to accommodate any
 * commonality.
 */
public abstract class RemoteRequest<T> implements Callable<T> {

	/**
	 * Constructor.
	 * 
	 * @param remoteNode node that this request is headed for.	 
	 */
	public RemoteRequest(RemoteNode remoteNode) {
		this.remoteNode = remoteNode;
	}
	
	//////////////////////////////////PROTECTED////////////////////////////////////////
	
	protected RemoteNode remoteNode;
}
