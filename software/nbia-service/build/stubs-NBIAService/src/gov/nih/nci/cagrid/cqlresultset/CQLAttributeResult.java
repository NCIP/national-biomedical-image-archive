/**
 * CQLAttributeResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cqlresultset;


/**
 * Result Attribute
 */
public class CQLAttributeResult  extends gov.nih.nci.cagrid.cqlresultset.CQLResult  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cqlresultset.TargetAttribute[] attribute;

    public CQLAttributeResult() {
    }

    public CQLAttributeResult(
           gov.nih.nci.cagrid.cqlresultset.TargetAttribute[] attribute) {
           this.attribute = attribute;
    }


    /**
     * Gets the attribute value for this CQLAttributeResult.
     * 
     * @return attribute
     */
    public gov.nih.nci.cagrid.cqlresultset.TargetAttribute[] getAttribute() {
        return attribute;
    }


    /**
     * Sets the attribute value for this CQLAttributeResult.
     * 
     * @param attribute
     */
    public void setAttribute(gov.nih.nci.cagrid.cqlresultset.TargetAttribute[] attribute) {
        this.attribute = attribute;
    }

    public gov.nih.nci.cagrid.cqlresultset.TargetAttribute getAttribute(int i) {
        return this.attribute[i];
    }

    public void setAttribute(int i, gov.nih.nci.cagrid.cqlresultset.TargetAttribute _value) {
        this.attribute[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CQLAttributeResult)) return false;
        CQLAttributeResult other = (CQLAttributeResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.attribute==null && other.getAttribute()==null) || 
             (this.attribute!=null &&
              java.util.Arrays.equals(this.attribute, other.getAttribute())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getAttribute() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttribute());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttribute(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CQLAttributeResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "CQLAttributeResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "Attribute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet", "TargetAttribute"));
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
