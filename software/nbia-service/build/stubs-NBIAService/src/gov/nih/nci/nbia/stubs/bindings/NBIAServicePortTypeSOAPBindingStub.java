/**
 * NBIAServicePortTypeSOAPBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.nbia.stubs.bindings;

public class NBIAServicePortTypeSOAPBindingStub extends org.apache.axis.client.Stub implements gov.nih.nci.nbia.stubs.NBIAServicePortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMultipleResourceProperties");
        oper.addParameter(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetMultipleResourceProperties"), new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourceProperties"), org.oasis.wsrf.properties.GetMultipleResourceProperties_Element.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourcePropertiesResponse"));
        oper.setReturnClass(org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetMultipleResourcePropertiesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFault"),
                      "org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFault"),
                      "org.oasis.wsrf.properties.ResourceUnknownFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetResourceProperty");
        oper.addParameter(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetResourceProperty"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"), javax.xml.namespace.QName.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetResourcePropertyResponse"));
        oper.setReturnClass(org.oasis.wsrf.properties.GetResourcePropertyResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetResourcePropertyResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFault"),
                      "org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFault"),
                      "org.oasis.wsrf.properties.ResourceUnknownFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("QueryResourceProperties");
        oper.addParameter(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryResourceProperties"), new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourceProperties"), org.oasis.wsrf.properties.QueryResourceProperties_Element.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourcePropertiesResponse"));
        oper.setReturnClass(org.oasis.wsrf.properties.QueryResourcePropertiesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryResourcePropertiesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnknownQueryExpressionDialectFault"),
                      "org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnknownQueryExpressionDialectFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryEvaluationErrorFault"),
                      "org.oasis.wsrf.properties.QueryEvaluationErrorFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryEvaluationErrorFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidQueryExpressionFault"),
                      "org.oasis.wsrf.properties.InvalidQueryExpressionFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidQueryExpressionFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFault"),
                      "org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFault"),
                      "org.oasis.wsrf.properties.ResourceUnknownFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiceSecurityMetadata");
        oper.addParameter(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", "GetServiceSecurityMetadataRequest"), new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataRequest"), gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataResponse"));
        oper.setReturnClass(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", "GetServiceSecurityMetadataResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("query");
        oper.addParameter(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", "QueryRequest"), new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">QueryRequest"), gov.nih.nci.cagrid.data.QueryRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">QueryResponse"));
        oper.setReturnClass(gov.nih.nci.cagrid.data.QueryResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", "QueryResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryException"),
                      "gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingException"),
                      "gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("executeQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", "ExecuteQueryRequest"), new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", ">ExecuteQueryRequest"), org.cagrid.dataservice.ExecuteQueryRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", ">ExecuteQueryResponse"));
        oper.setReturnClass(org.cagrid.dataservice.ExecuteQueryResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", "ExecuteQueryResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryException"),
                      "gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingException"),
                      "gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("enumerationQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", "EnumerationQueryRequest"), new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationQueryRequest"), gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationQueryResponse"));
        oper.setReturnClass(gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", "EnumerationQueryResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryException"),
                      "gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingException"),
                      "gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("executeEnumerationQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", "ExecuteEnumerationQueryRequest"), new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", ">ExecuteEnumerationQueryRequest"), org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", ">ExecuteEnumerationQueryResponse"));
        oper.setReturnClass(org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", "ExecuteEnumerationQueryResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryException"),
                      "gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingException"),
                      "gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("transferQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", "TransferQueryRequest"), new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", ">TransferQueryRequest"), gov.nih.nci.cagrid.data.transfer.TransferQueryRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", ">TransferQueryResponse"));
        oper.setReturnClass(gov.nih.nci.cagrid.data.transfer.TransferQueryResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", "TransferQueryResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryException"),
                      "gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingException"),
                      "gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("executeTransferQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", "ExecuteTransferQueryRequest"), new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", ">ExecuteTransferQueryRequest"), org.cagrid.dataservice.transfer.ExecuteTransferQueryRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", ">ExecuteTransferQueryResponse"));
        oper.setReturnClass(org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", "ExecuteTransferQueryResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryException"),
                      "gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingException"),
                      "gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType",
                      new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType"), 
                      true
                     ));
        _operations[9] = oper;

    }

    public NBIAServicePortTypeSOAPBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public NBIAServicePortTypeSOAPBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public NBIAServicePortTypeSOAPBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        addBindings0();
        addBindings1();
        addBindings2();
    }

    private void addBindings0() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidQueryExpressionFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InvalidQueryExpressionFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "SubscribeCreationFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.SubscribeCreationFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">SetTerminationTimeResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.SetTerminationTimeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Association");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.Association.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", ">ServiceMetadata>hostingResearchCenter");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", ">ExecuteEnumerationQueryRequest");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTimeChangeRejectedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.TerminationTimeChangeRejectedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "FilterType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.FilterType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataResponse");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "NonNegativeDurationType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.Duration.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">NotificationProducerRP");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.NotificationProducerRP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourcePropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", ">ExecuteTransferQueryRequest");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.transfer.ExecuteTransferQueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", ">EnumerationDataServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.enumeration.EnumerationDataServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "DeleteType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.DeleteType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">ResumeSubscription");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResumeSubscription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">>EnumerationQueryRequest>cqlQuery");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequestCqlQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "InvalidTopicExpressionFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.InvalidTopicExpressionFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "ContextProperty");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.ContextProperty.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", ">ServiceSecurityMetadata>operations");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadataOperations.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService/types", ">TransferDataServiceReference");
            cachedSerQNames.add(qName);
            cls = transfer.dataservice.cagrid.org.Cql2TransferDataService.types.TransferDataServiceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "GSISecureConversation");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.GSISecureConversation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", ">Service>pointOfContactCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.ServicePointOfContactCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "ServiceSecurityMetadata");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://transfer.cagrid.org/TransferService/Context/types", ">TransferServiceContextReference");
            cachedSerQNames.add(qName);
            cls = org.cagrid.transfer.context.stubs.types.TransferServiceContextReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Group");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.Group.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", ">ResearchCenter>pointOfContactCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.ResearchCenterPointOfContactCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "ReferencePropertiesType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.ReferencePropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "RelationshipTypeValues");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.RelationshipTypeValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "GSITransport");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.GSITransport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidSetResourcePropertiesRequestContentFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InvalidSetResourcePropertiesRequestContentFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", ">TransferQueryRequest");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.transfer.TransferQueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">SubscriptionManagerRP");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.SubscriptionManagerRP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", ">TransferDataServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.transfer.TransferDataServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Relationship");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.Relationship.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">GetCurrentMessage");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.GetCurrentMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/EnumerationResponseContainer", "EnumerationResponseContainer");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">ResourcePropertyValueChangeNotificationType>NewValue");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationTypeNewValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "Fault");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.Fault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "FaultSubcodeValues");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.FaultSubcodeValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">GetStatusResponse");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.GetStatusResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "AttributedQName");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.AttributedQName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService/types", ">DataServiceReference");
            cachedSerQNames.add(qName);
            cls = data.cagrid.nci.nih.gov.DataService.types.DataServiceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">GetCurrentMessageResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.GetCurrentMessageResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InsertType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InsertType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">Release");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.Release.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "RelationshipTypeValues");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.RelationshipTypeValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "ReplyAfterType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.ReplyAfterType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "SemanticMetadata");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.SemanticMetadata.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicNotSupportedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.TopicNotSupportedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", ">TransferDataServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.transfer.TransferDataServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">QueryRequest");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.QueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">ResumeSubscriptionResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResumeSubscriptionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", ">ServiceContext>operationCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.ServiceContextOperationCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "InputParameter");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.InputParameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", ">BaseFaultType>Description");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.faults.BaseFaultTypeDescription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ReferenceParametersType");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.ReferenceParametersType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", ">DataServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.DataServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "ResumeFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResumeFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">Pull");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.Pull.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ReferencePropertiesType");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.ReferencePropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "AttributedQName");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.AttributedQName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "NoCurrentMessageOnTopicFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.NoCurrentMessageOnTopicFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "protectionLevelType");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.ProtectionLevelType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "None");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.None.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/CaGridEnumeration/types", ">CaGridEnumerationReference");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.enumeration.stubs.types.CaGridEnumerationReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLGeneralization");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "QueryProcessingExceptionType");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", ">>ExecuteQueryRequest>query");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.ExecuteQueryRequestQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "SetResourcePropertyRequestFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.SetResourcePropertyRequestFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">UMLAssociation>sourceUMLAssociationEdge");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLResult");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.CQLResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService/types", ">EnumerationDataServiceReference");
            cachedSerQNames.add(qName);
            cls = enumeration.dataservice.cagrid.org.Cql2EnumerationDataService.types.EnumerationDataServiceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Predicate");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.Predicate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "LanguageSpecificStringType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.LanguageSpecificStringType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.cql/QueryLanguageSupport", ">QueryLanguageSupport>CQL2Support");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.cql.support.QueryLanguageSupportCQL2Support.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings1() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", ">ServiceMetadata>serviceDescription");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://transfer.cagrid.org/Transfer", "DataDescriptor");
            cachedSerQNames.add(qName);
            cls = org.cagrid.transfer.descriptor.DataDescriptor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "ResearchCenter");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.ResearchCenter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "ResourceUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.ResourceUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "ExpirationType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.ExpirationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "TBDIdentifier");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.TBDIdentifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "Output");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.Output.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "CaDSRRegistration");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.CaDSRRegistration.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">SetResourcePropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.SetResourcePropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://transfer.cagrid.org/Transfer", "DataStorageDescriptor");
            cachedSerQNames.add(qName);
            cls = org.cagrid.transfer.descriptor.DataStorageDescriptor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnableToModifyResourcePropertyFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.UnableToModifyResourcePropertyFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourceUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "UMLAttribute");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.UMLAttribute.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Object");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.Object.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "Relationship");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.Relationship.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">PauseSubscription");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.PauseSubscription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociationEdge");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "PositiveDurationType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.Duration.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService/types", ">TransferDataServiceReference");
            cachedSerQNames.add(qName);
            cls = transfer.data.cagrid.nci.nih.gov.TransferDataService.types.TransferDataServiceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">EnumerateResponse");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">DataServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.DataServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryEvaluationErrorFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryEvaluationErrorFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", ">>ExecuteTransferQueryRequest>cqlQuery");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.transfer.ExecuteTransferQueryRequestCqlQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", ">TransferQueryResponse");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.transfer.TransferQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourcePropertyValueChangeNotificationType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClass");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLClass.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataServiceExceptions", "MalformedQueryExceptionType");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "PauseFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.PauseFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">PullResponse");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.PullResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://nbia.nci.nih.gov/NBIAService", ">NBIAServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.nbia.stubs.NBIAServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "UMLClass");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.UMLClass.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "EndpointReferenceType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.EndpointReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationQueryRequest");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "Operation");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.Operation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationDataServiceResourceProperties");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.enumeration.EnumerationDataServiceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "ServiceContext");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.ServiceContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLAttributeResult");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.CQLAttributeResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "UnableToSetTerminationTimeFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.UnableToSetTerminationTimeFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", ">ExecuteEnumerationQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "AttributedURI");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.AttributedURI.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", ">ExecuteQueryRequest");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.ExecuteQueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">SetResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.SetResourceProperties_Element.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UpdateType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.UpdateType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">Destroy");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.Destroy.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "ResourceUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResourceUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClassReference");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "EndpointReferenceType");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.EndpointReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">EnumerationEnd");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerationEnd.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ReplyAfterType");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.ReplyAfterType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">TerminationNotification");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.TerminationNotification.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnknownQueryExpressionDialectFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLQueryResults");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.CQLQueryResults.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLObjectResult");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.CQLObjectResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>exposedUMLClassCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://transfer.cagrid.org/Transfer", "Status");
            cachedSerQNames.add(qName);
            cls = org.cagrid.transfer.descriptor.Status.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", ">BaseFaultType>ErrorCode");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.faults.BaseFaultTypeErrorCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">GetStatus");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.GetStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "ItemListType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.ItemListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService/types", ">EnumerationDataServiceReference");
            cachedSerQNames.add(qName);
            cls = enumeration.data.cagrid.nci.nih.gov.EnumerationDataService.types.EnumerationDataServiceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">PauseSubscriptionResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.PauseSubscriptionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLIdentifierResult");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.CQLIdentifierResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "GSISecureMessage");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.GSISecureMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">SubscribeResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.SubscribeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "EnumerationEndCodeType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerationEndCodeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">DestroyResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.DestroyResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourcePropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryResourcePropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">Enumerate");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.Enumerate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "TargetAttribute");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.TargetAttribute.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice/DataService", ">ExecuteQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.ExecuteQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "FaultSubcodeValues");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.FaultSubcodeValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "ResourceNotDestroyedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", ">ServiceContext>contextPropertyCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.ServiceContextContextPropertyCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", ">Operation>inputParameterCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.OperationInputParameterCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.cql/QueryLanguageSupport", "Cql2SupportType");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.cql.support.Cql2SupportType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataRequest");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "AttributedURI");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.AttributedURI.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

    }
    private void addBindings2() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://nbia.nci.nih.gov/NBIAService/CQLResultTypes", "PermissibleCQLObjectResults");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.nbia.cqlresulttypes.PermissibleCQLObjectResults.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "Address");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.Address.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", ">CQLQuery");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.CQLQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "EnumerationContextType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerationContextType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">Renew");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.Renew.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>umlGeneralizationCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ServiceNameType");
            cachedSerQNames.add(qName);
            cls = org.globus.addressing.ServiceNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://nbia.nci.nih.gov/NBIAService/types", ">NBIAServiceReference");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.nbia.stubs.types.NBIAServiceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "CommunicationMechanism");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.CommunicationMechanism.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.cql/QueryLanguageSupport", "QueryLanguageSupport");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.cql.support.QueryLanguageSupport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", "OpenEnumerationEndCodeType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.OpenEnumerationEndCodeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.ServiceMetadata.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.transfer/Cql2TransferDataService", ">ExecuteTransferQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">Subscribe");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.Subscribe.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Attribute");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.Attribute.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "QueryModifier");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.QueryModifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">ServiceSecurityResourceProperties");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.introduce.security.stubs.ServiceSecurityResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicPathDialectUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.TopicPathDialectUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://transfer.cagrid.org/Transfer", "DataTransferDescriptor");
            cachedSerQNames.add(qName);
            cls = org.cagrid.transfer.descriptor.DataTransferDescriptor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">>QueryRequest>cqlQuery");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.QueryRequestCqlQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "NotificationMessageHolderType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.NotificationMessageHolderType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "Enumeration");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.Enumeration.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "ResearchCenterDescription");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetResourcePropertyResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.GetResourcePropertyResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "Service");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.Service.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", ">ValueDomain>enumerationCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.ValueDomainEnumerationCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicExpressionType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.TopicExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">SetTerminationTime");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.SetTerminationTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "LogicalOperator");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlquery.LogicalOperator.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", "BaseFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.faults.BaseFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLCountResult");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.cqlresultset.CQLCountResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>exposedUMLAssociationCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">Notify");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.Notify.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryExpressionType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.transfer/TransferDataService", ">>TransferQueryRequest>cqlQuery");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.transfer.TransferQueryRequestCqlQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "ServiceNameType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.ServiceNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", ">UMLClass>umlAttributeCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.DomainModel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", "Operation");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.Operation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "PointOfContact");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.PointOfContact.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">QueryResponse");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.QueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService", ">>ExecuteEnumerationQueryRequest>cqlQuery");
            cachedSerQNames.add(qName);
            cls = org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryRequestCqlQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/09/enumeration", ">RenewResponse");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.ws._2004._09.enumeration.RenewResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">ScheduledResourceTerminationRP");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.ScheduledResourceTerminationRP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.GetMultipleResourceProperties_Element.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationQueryResponse");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "CommunicationStyle");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.CommunicationStyle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryResourceProperties_Element.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">ResourcePropertyValueChangeNotificationType>OldValue");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationTypeOldValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociation");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", ">Operation>faultCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.OperationFaultCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service", ">Service>serviceContextCollection");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.service.ServiceServiceContextCollection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">UMLAssociation>targetUMLAssociationEdge");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common", "ValueDomain");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.common.ValueDomain.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties/GetMultipleResourceProperties");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetMultipleResourceProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getMultipleResourcePropertiesRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse.class);
            }
        }
    }

    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties/GetResourceProperty");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetResourceProperty"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getResourcePropertyRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.oasis.wsrf.properties.GetResourcePropertyResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.oasis.wsrf.properties.GetResourcePropertyResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.oasis.wsrf.properties.GetResourcePropertyResponse.class);
            }
        }
    }

    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties/QueryResourceProperties");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "QueryResourceProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryResourcePropertiesRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.oasis.wsrf.properties.QueryResourcePropertiesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.oasis.wsrf.properties.QueryResourcePropertiesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.oasis.wsrf.properties.QueryResourcePropertiesResponse.class);
            }
        }
    }

    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity/GetServiceSecurityMetadataRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getServiceSecurityMetadata"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse.class);
            }
        }
    }

    public gov.nih.nci.cagrid.data.QueryResponse query(gov.nih.nci.cagrid.data.QueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://data.cagrid.nci.nih.gov/DataService/QueryRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "query"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.nih.nci.cagrid.data.QueryResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.nih.nci.cagrid.data.QueryResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.nih.nci.cagrid.data.QueryResponse.class);
            }
        }
    }

    public org.cagrid.dataservice.ExecuteQueryResponse executeQuery(org.cagrid.dataservice.ExecuteQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://org.cagrid.dataservice/DataService/ExecuteQueryRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "executeQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.cagrid.dataservice.ExecuteQueryResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.cagrid.dataservice.ExecuteQueryResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.cagrid.dataservice.ExecuteQueryResponse.class);
            }
        }
    }

    public gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse enumerationQuery(gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService/EnumerationQueryRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "enumerationQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.nih.nci.cagrid.data.enumeration.EnumerationQueryResponse.class);
            }
        }
    }

    public org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse executeEnumerationQuery(org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://org.cagrid.dataservice.enumeration/Cql2EnumerationDataService/EnumerationQueryRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "executeEnumerationQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.cagrid.dataservice.enumeration.ExecuteEnumerationQueryResponse.class);
            }
        }
    }

    public gov.nih.nci.cagrid.data.transfer.TransferQueryResponse transferQuery(gov.nih.nci.cagrid.data.transfer.TransferQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://gov.nih.nci.cagrid.data.transfer/TransferDataService/TransferQueryRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "transferQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.nih.nci.cagrid.data.transfer.TransferQueryResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.nih.nci.cagrid.data.transfer.TransferQueryResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.nih.nci.cagrid.data.transfer.TransferQueryResponse.class);
            }
        }
    }

    public org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse executeTransferQuery(org.cagrid.dataservice.transfer.ExecuteTransferQueryRequest parameters) throws java.rmi.RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://org.cagrid.dataservice.transfer/Cql2TransferDataService/ExecuteTransferQueryRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "executeTransferQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.cagrid.dataservice.transfer.ExecuteTransferQueryResponse.class);
            }
        }
    }

}
