/**
 * NBIAServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.nbia.stubs;

public interface NBIAServicePortType extends java.rmi.Remote {
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
    public gov.nih.nci.cagrid.data.QueryResponse query(gov.nih.nci.cagrid.data.QueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
    public org.cagrid.dataservice.ExecuteQueryResponse executeQuery(org.cagrid.dataservice.ExecuteQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
    public gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse enumerationQuery(gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
    public org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse executeEnumerationQuery(org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
    public gov.nih.nci.cagrid.data.transfer.TransferQueryResponse transferQuery(gov.nih.nci.cagrid.data.transfer.TransferQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
    public org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse executeTransferQuery(org.cagrid.dataservice.transfer.ExecuteTransferQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
}
