/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package org.nih.nci.cagrid.aim.service;

import gov.nih.nci.cagrid.data.InitializationException; 
import edu.osu.bmi.xml.service.delegates.transfer.SubmitByTransferDelegate; 

import edu.osu.bmi.xml.service.delegates.transfer.QueryByTransferDelegate; 

import gov.nih.nci.cagrid.data.InitializationException; 
import edu.osu.bmi.xml.service.delegates.SubmitDelegate; 

import gov.nih.nci.cagrid.common.FaultHelper; 
import gov.nih.nci.cagrid.data.QueryProcessingException; 
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor; 
import edu.osu.bmi.xml.db.XMLDBConnector; 
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType; 

import java.rmi.RemoteException; 
import java.util.Properties; 

import edu.osu.bmi.xml.service.delegates.DataServiceUtils; 

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class AIMDataServiceImpl extends AIMDataServiceImplBase {

	private Properties cqlQueryProcessorConfig = null;
	private LazyCQLQueryProcessor queryProcessorInstance = null;
	private XMLDBConnector connector = null;

	
	public AIMDataServiceImpl() throws RemoteException {
		super();
	}
	
  public void submit(java.lang.String[] xmls) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
	 if (cqlQueryProcessorConfig == null) { 
		 try { 
			 cqlQueryProcessorConfig = DataServiceUtils.getCqlQueryProcessorConfig(); 
		 } catch (QueryProcessingException e2) { 
			 FaultHelper helper = new FaultHelper( 
				 new QueryProcessingExceptionType()); 
			 helper.addFaultCause(e2); 
				 throw (QueryProcessingExceptionType) helper.getFault(); 
		 } 
	 } 
	 if (connector == null) { 
		 try { 
			 connector = XMLDBConnector.newInstance(cqlQueryProcessorConfig); 
		 } catch (InitializationException e2) { 
			 FaultHelper helper = new FaultHelper( 
				 new QueryProcessingExceptionType()); 
			 helper.addFaultCause(e2); 
			 throw (QueryProcessingExceptionType) helper.getFault(); 
		 } 
	 } 

	 SubmitDelegate.submit(connector, xmls); 
}

  public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference queryByTransfer(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
	 if (cqlQueryProcessorConfig == null) { 
		 try { 
			 cqlQueryProcessorConfig = DataServiceUtils.getCqlQueryProcessorConfig(); 
		 } catch (QueryProcessingException e2) { 
			 FaultHelper helper = new FaultHelper( 
				 new QueryProcessingExceptionType()); 
			 helper.addFaultCause(e2); 
				 throw (QueryProcessingExceptionType) helper.getFault(); 
		 } 
	 } 
	 if (queryProcessorInstance == null) { 
		 try { 
			 queryProcessorInstance = DataServiceUtils.getCqlQueryProcessorInstance(cqlQueryProcessorConfig); 
		 } catch (QueryProcessingException e2) { 
			 FaultHelper helper = new FaultHelper( 
				 new QueryProcessingExceptionType()); 
			 helper.addFaultCause(e2); 
			 throw (QueryProcessingExceptionType) helper.getFault(); 
		 } 
	 } 

	 return QueryByTransferDelegate.queryByTransfer( 
		 queryProcessorInstance, cqlQuery); 
}

  public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference submitByTransfer() throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
	 if (cqlQueryProcessorConfig == null) { 
		 try { 
			 cqlQueryProcessorConfig = DataServiceUtils.getCqlQueryProcessorConfig(); 
		 } catch (QueryProcessingException e2) { 
			 FaultHelper helper = new FaultHelper( 
				 new QueryProcessingExceptionType()); 
			 helper.addFaultCause(e2); 
				 throw (QueryProcessingExceptionType) helper.getFault(); 
		 } 
	 } 
	 if (connector == null) { 
		 try { 
			 connector = XMLDBConnector.newInstance(cqlQueryProcessorConfig); 
		 } catch (InitializationException e2) { 
			 FaultHelper helper = new FaultHelper( 
				 new QueryProcessingExceptionType()); 
			 helper.addFaultCause(e2); 
			 throw (QueryProcessingExceptionType) helper.getFault(); 
		 } 
	 } 

	 return SubmitByTransferDelegate.submitByTransfer( 
		 connector); 
}

}

