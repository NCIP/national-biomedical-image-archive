/**
 * QueryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.data;

public class QueryResponse  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cqlresultset.CQLQueryResults CQLQueryResultCollection;

    public QueryResponse() {
    }

    public QueryResponse(
           gov.nih.nci.cagrid.cqlresultset.CQLQueryResults CQLQueryResultCollection) {
           this.CQLQueryResultCollection = CQLQueryResultCollection;
    }


    /**
     * Gets the CQLQueryResultCollection value for this QueryResponse.
     * 
     * @return CQLQueryResultCollection
     */
    public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults getCQLQueryResultCollection() {
        return CQLQueryResultCollection;
    }


    /**
     * Sets the CQLQueryResultCollection value for this QueryResponse.
     * 
     * @param CQLQueryResultCollection
     */
    public void setCQLQueryResultCollection(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults CQLQueryResultCollection) {
        this.CQLQueryResultCollection = CQLQueryResultCollection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryResponse)) return false;
        QueryResponse other = (QueryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CQLQueryResultCollection==null && other.getCQLQueryResultCollection()==null) || 
             (this.CQLQueryResultCollection!=null &&
              this.CQLQueryResultCollection.equals(other.getCQLQueryResultCollection())));
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
        if (getCQLQueryResultCollection() != null) {
            _hashCode += getCQLQueryResultCollection().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gov.nih.nci.cagrid.data/DataService", ">QueryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CQLQueryResultCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLQueryResultCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLQueryResults"));
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
