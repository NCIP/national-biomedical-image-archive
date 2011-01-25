/**
 * EnumerationQueryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.data.enumeration;

public class EnumerationQueryResponse  implements java.io.Serializable {
    private gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer enumerationResponseContainer;

    public EnumerationQueryResponse() {
    }

    public EnumerationQueryResponse(
           gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer enumerationResponseContainer) {
           this.enumerationResponseContainer = enumerationResponseContainer;
    }


    /**
     * Gets the enumerationResponseContainer value for this EnumerationQueryResponse.
     * 
     * @return enumerationResponseContainer
     */
    public gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer getEnumerationResponseContainer() {
        return enumerationResponseContainer;
    }


    /**
     * Sets the enumerationResponseContainer value for this EnumerationQueryResponse.
     * 
     * @param enumerationResponseContainer
     */
    public void setEnumerationResponseContainer(gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer enumerationResponseContainer) {
        this.enumerationResponseContainer = enumerationResponseContainer;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EnumerationQueryResponse)) return false;
        EnumerationQueryResponse other = (EnumerationQueryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.enumerationResponseContainer==null && other.getEnumerationResponseContainer()==null) || 
             (this.enumerationResponseContainer!=null &&
              this.enumerationResponseContainer.equals(other.getEnumerationResponseContainer())));
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
        if (getEnumerationResponseContainer() != null) {
            _hashCode += getEnumerationResponseContainer().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnumerationQueryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationQueryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enumerationResponseContainer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/EnumerationResponseContainer", "EnumerationResponseContainer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.enumeration/EnumerationResponseContainer", "EnumerationResponseContainer"));
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
