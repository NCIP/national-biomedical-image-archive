/**
 * QueryModifier.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cqlquery;


/**
 * Modifies the returned data from the query
 */
public class QueryModifier  implements java.io.Serializable {
    private java.lang.String[] attributeNames;
    private java.lang.String distinctAttribute;
    private boolean countOnly;  // attribute

    public QueryModifier() {
    }

    public QueryModifier(
           java.lang.String[] attributeNames,
           boolean countOnly,
           java.lang.String distinctAttribute) {
           this.attributeNames = attributeNames;
           this.distinctAttribute = distinctAttribute;
           this.countOnly = countOnly;
    }


    /**
     * Gets the attributeNames value for this QueryModifier.
     * 
     * @return attributeNames
     */
    public java.lang.String[] getAttributeNames() {
        return attributeNames;
    }


    /**
     * Sets the attributeNames value for this QueryModifier.
     * 
     * @param attributeNames
     */
    public void setAttributeNames(java.lang.String[] attributeNames) {
        this.attributeNames = attributeNames;
    }

    public java.lang.String getAttributeNames(int i) {
        return this.attributeNames[i];
    }

    public void setAttributeNames(int i, java.lang.String _value) {
        this.attributeNames[i] = _value;
    }


    /**
     * Gets the distinctAttribute value for this QueryModifier.
     * 
     * @return distinctAttribute
     */
    public java.lang.String getDistinctAttribute() {
        return distinctAttribute;
    }


    /**
     * Sets the distinctAttribute value for this QueryModifier.
     * 
     * @param distinctAttribute
     */
    public void setDistinctAttribute(java.lang.String distinctAttribute) {
        this.distinctAttribute = distinctAttribute;
    }


    /**
     * Gets the countOnly value for this QueryModifier.
     * 
     * @return countOnly
     */
    public boolean isCountOnly() {
        return countOnly;
    }


    /**
     * Sets the countOnly value for this QueryModifier.
     * 
     * @param countOnly
     */
    public void setCountOnly(boolean countOnly) {
        this.countOnly = countOnly;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryModifier)) return false;
        QueryModifier other = (QueryModifier) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributeNames==null && other.getAttributeNames()==null) || 
             (this.attributeNames!=null &&
              java.util.Arrays.equals(this.attributeNames, other.getAttributeNames()))) &&
            ((this.distinctAttribute==null && other.getDistinctAttribute()==null) || 
             (this.distinctAttribute!=null &&
              this.distinctAttribute.equals(other.getDistinctAttribute()))) &&
            this.countOnly == other.isCountOnly();
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
        if (getAttributeNames() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributeNames());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributeNames(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDistinctAttribute() != null) {
            _hashCode += getDistinctAttribute().hashCode();
        }
        _hashCode += (isCountOnly() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryModifier.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "QueryModifier"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("countOnly");
        attrField.setXmlName(new javax.xml.namespace.QName("", "countOnly"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributeNames");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "AttributeNames"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distinctAttribute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "DistinctAttribute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
