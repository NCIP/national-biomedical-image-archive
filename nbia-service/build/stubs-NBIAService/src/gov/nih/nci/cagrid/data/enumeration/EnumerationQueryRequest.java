/**
 * EnumerationQueryRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.data.enumeration;

public class EnumerationQueryRequest  implements java.io.Serializable {
    private gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequestCqlQuery cqlQuery;

    public EnumerationQueryRequest() {
    }

    public EnumerationQueryRequest(
           gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequestCqlQuery cqlQuery) {
           this.cqlQuery = cqlQuery;
    }


    /**
     * Gets the cqlQuery value for this EnumerationQueryRequest.
     * 
     * @return cqlQuery
     */
    public gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequestCqlQuery getCqlQuery() {
        return cqlQuery;
    }


    /**
     * Sets the cqlQuery value for this EnumerationQueryRequest.
     * 
     * @param cqlQuery
     */
    public void setCqlQuery(gov.nih.nci.cagrid.data.enumeration.EnumerationQueryRequestCqlQuery cqlQuery) {
        this.cqlQuery = cqlQuery;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EnumerationQueryRequest)) return false;
        EnumerationQueryRequest other = (EnumerationQueryRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cqlQuery==null && other.getCqlQuery()==null) || 
             (this.cqlQuery!=null &&
              this.cqlQuery.equals(other.getCqlQuery())));
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
        if (getCqlQuery() != null) {
            _hashCode += getCqlQuery().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnumerationQueryRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">EnumerationQueryRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cqlQuery");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", "cqlQuery"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data.enumeration/EnumerationDataService", ">>EnumerationQueryRequest>cqlQuery"));
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
