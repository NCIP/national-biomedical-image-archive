/**
 * GetServiceSecurityMetadataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.introduce.security.stubs;

public class GetServiceSecurityMetadataResponse  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata serviceSecurityMetadata;

    public GetServiceSecurityMetadataResponse() {
    }

    public GetServiceSecurityMetadataResponse(
           gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata serviceSecurityMetadata) {
           this.serviceSecurityMetadata = serviceSecurityMetadata;
    }


    /**
     * Gets the serviceSecurityMetadata value for this GetServiceSecurityMetadataResponse.
     * 
     * @return serviceSecurityMetadata
     */
    public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() {
        return serviceSecurityMetadata;
    }


    /**
     * Sets the serviceSecurityMetadata value for this GetServiceSecurityMetadataResponse.
     * 
     * @param serviceSecurityMetadata
     */
    public void setServiceSecurityMetadata(gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata serviceSecurityMetadata) {
        this.serviceSecurityMetadata = serviceSecurityMetadata;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetServiceSecurityMetadataResponse)) return false;
        GetServiceSecurityMetadataResponse other = (GetServiceSecurityMetadataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.serviceSecurityMetadata==null && other.getServiceSecurityMetadata()==null) || 
             (this.serviceSecurityMetadata!=null &&
              this.serviceSecurityMetadata.equals(other.getServiceSecurityMetadata())));
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
        if (getServiceSecurityMetadata() != null) {
            _hashCode += getServiceSecurityMetadata().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetServiceSecurityMetadataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceSecurityMetadata");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "ServiceSecurityMetadata"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "ServiceSecurityMetadata"));
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
