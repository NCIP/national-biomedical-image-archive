/**
 * CaGridEnumerationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.enumeration.stubs.service;

public class CaGridEnumerationServiceLocator extends org.apache.axis.client.Service implements gov.nih.nci.cagrid.enumeration.stubs.service.CaGridEnumerationService {

    public CaGridEnumerationServiceLocator() {
    }


    public CaGridEnumerationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CaGridEnumerationServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CaGridEnumerationPortTypePort
    private java.lang.String CaGridEnumerationPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getCaGridEnumerationPortTypePortAddress() {
        return CaGridEnumerationPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CaGridEnumerationPortTypePortWSDDServiceName = "CaGridEnumerationPortTypePort";

    public java.lang.String getCaGridEnumerationPortTypePortWSDDServiceName() {
        return CaGridEnumerationPortTypePortWSDDServiceName;
    }

    public void setCaGridEnumerationPortTypePortWSDDServiceName(java.lang.String name) {
        CaGridEnumerationPortTypePortWSDDServiceName = name;
    }

    public gov.nih.nci.cagrid.enumeration.stubs.CaGridEnumerationPortType getCaGridEnumerationPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CaGridEnumerationPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCaGridEnumerationPortTypePort(endpoint);
    }

    public gov.nih.nci.cagrid.enumeration.stubs.CaGridEnumerationPortType getCaGridEnumerationPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gov.nih.nci.cagrid.enumeration.stubs.bindings.CaGridEnumerationPortTypeSOAPBindingStub _stub = new gov.nih.nci.cagrid.enumeration.stubs.bindings.CaGridEnumerationPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getCaGridEnumerationPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCaGridEnumerationPortTypePortEndpointAddress(java.lang.String address) {
        CaGridEnumerationPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (gov.nih.nci.cagrid.enumeration.stubs.CaGridEnumerationPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                gov.nih.nci.cagrid.enumeration.stubs.bindings.CaGridEnumerationPortTypeSOAPBindingStub _stub = new gov.nih.nci.cagrid.enumeration.stubs.bindings.CaGridEnumerationPortTypeSOAPBindingStub(new java.net.URL(CaGridEnumerationPortTypePort_address), this);
                _stub.setPortName(getCaGridEnumerationPortTypePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CaGridEnumerationPortTypePort".equals(inputPortName)) {
            return getCaGridEnumerationPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/CaGridEnumeration/service", "CaGridEnumerationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/CaGridEnumeration/service", "CaGridEnumerationPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("CaGridEnumerationPortTypePort".equals(portName)) {
            setCaGridEnumerationPortTypePortEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
