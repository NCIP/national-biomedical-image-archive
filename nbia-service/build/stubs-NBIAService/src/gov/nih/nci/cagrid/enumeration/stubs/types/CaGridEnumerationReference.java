/**
 * CaGridEnumerationReference.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.enumeration.stubs.types;

public class CaGridEnumerationReference  implements java.io.Serializable {
    private org.apache.axis.message.addressing.EndpointReferenceType endpointReference;

    public CaGridEnumerationReference() {
    }

    public CaGridEnumerationReference(
           org.apache.axis.message.addressing.EndpointReferenceType endpointReference) {
           this.endpointReference = endpointReference;
    }


    /**
     * Gets the endpointReference value for this CaGridEnumerationReference.
     * 
     * @return endpointReference
     */
    public org.apache.axis.message.addressing.EndpointReferenceType getEndpointReference() {
        return endpointReference;
    }


    /**
     * Sets the endpointReference value for this CaGridEnumerationReference.
     * 
     * @param endpointReference
     */
    public void setEndpointReference(org.apache.axis.message.addressing.EndpointReferenceType endpointReference) {
        this.endpointReference = endpointReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CaGridEnumerationReference)) return false;
        CaGridEnumerationReference other = (CaGridEnumerationReference) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.endpointReference==null && other.getEndpointReference()==null) || 
             (this.endpointReference!=null &&
              this.endpointReference.equals(other.getEndpointReference())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getEndpointReference() != null) {
            _hashCode += getEndpointReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CaGridEnumerationReference.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/CaGridEnumeration/types", ">CaGridEnumerationReference"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endpointReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "EndpointReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "EndpointReferenceType"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
