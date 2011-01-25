/**
 * Object.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cqlquery;


/**
 * Object used as search criteria or target definition
 */
public class Object  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cqlquery.Attribute attribute;
    private gov.nih.nci.cagrid.cqlquery.Association association;
    private gov.nih.nci.cagrid.cqlquery.Group group;
    private java.lang.String name;  // attribute

    public Object() {
    }

    public Object(
           gov.nih.nci.cagrid.cqlquery.Association association,
           gov.nih.nci.cagrid.cqlquery.Attribute attribute,
           gov.nih.nci.cagrid.cqlquery.Group group,
           java.lang.String name) {
           this.attribute = attribute;
           this.association = association;
           this.group = group;
           this.name = name;
    }


    /**
     * Gets the attribute value for this Object.
     * 
     * @return attribute
     */
    public gov.nih.nci.cagrid.cqlquery.Attribute getAttribute() {
        return attribute;
    }


    /**
     * Sets the attribute value for this Object.
     * 
     * @param attribute
     */
    public void setAttribute(gov.nih.nci.cagrid.cqlquery.Attribute attribute) {
        this.attribute = attribute;
    }


    /**
     * Gets the association value for this Object.
     * 
     * @return association
     */
    public gov.nih.nci.cagrid.cqlquery.Association getAssociation() {
        return association;
    }


    /**
     * Sets the association value for this Object.
     * 
     * @param association
     */
    public void setAssociation(gov.nih.nci.cagrid.cqlquery.Association association) {
        this.association = association;
    }


    /**
     * Gets the group value for this Object.
     * 
     * @return group
     */
    public gov.nih.nci.cagrid.cqlquery.Group getGroup() {
        return group;
    }


    /**
     * Sets the group value for this Object.
     * 
     * @param group
     */
    public void setGroup(gov.nih.nci.cagrid.cqlquery.Group group) {
        this.group = group;
    }


    /**
     * Gets the name value for this Object.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Object.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Object)) return false;
        Object other = (Object) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attribute==null && other.getAttribute()==null) || 
             (this.attribute!=null &&
              this.attribute.equals(other.getAttribute()))) &&
            ((this.association==null && other.getAssociation()==null) || 
             (this.association!=null &&
              this.association.equals(other.getAssociation()))) &&
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              this.group.equals(other.getGroup()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
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
        if (getAttribute() != null) {
            _hashCode += getAttribute().hashCode();
        }
        if (getAssociation() != null) {
            _hashCode += getAssociation().hashCode();
        }
        if (getGroup() != null) {
            _hashCode += getGroup().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Object.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Object"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Attribute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Attribute"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("association");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Association"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Association"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Group"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Group"));
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
