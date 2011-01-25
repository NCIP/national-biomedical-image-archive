/**
 * Group.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cqlquery;


/**
 * Binary joint
 */
public class Group  implements java.io.Serializable {
    private gov.nih.nci.cagrid.cqlquery.Association[] association;
    private gov.nih.nci.cagrid.cqlquery.Attribute[] attribute;
    private gov.nih.nci.cagrid.cqlquery.Group[] group;
    private gov.nih.nci.cagrid.cqlquery.LogicalOperator logicRelation;  // attribute

    public Group() {
    }

    public Group(
           gov.nih.nci.cagrid.cqlquery.Association[] association,
           gov.nih.nci.cagrid.cqlquery.Attribute[] attribute,
           gov.nih.nci.cagrid.cqlquery.Group[] group,
           gov.nih.nci.cagrid.cqlquery.LogicalOperator logicRelation) {
           this.association = association;
           this.attribute = attribute;
           this.group = group;
           this.logicRelation = logicRelation;
    }


    /**
     * Gets the association value for this Group.
     * 
     * @return association
     */
    public gov.nih.nci.cagrid.cqlquery.Association[] getAssociation() {
        return association;
    }


    /**
     * Sets the association value for this Group.
     * 
     * @param association
     */
    public void setAssociation(gov.nih.nci.cagrid.cqlquery.Association[] association) {
        this.association = association;
    }

    public gov.nih.nci.cagrid.cqlquery.Association getAssociation(int i) {
        return this.association[i];
    }

    public void setAssociation(int i, gov.nih.nci.cagrid.cqlquery.Association _value) {
        this.association[i] = _value;
    }


    /**
     * Gets the attribute value for this Group.
     * 
     * @return attribute
     */
    public gov.nih.nci.cagrid.cqlquery.Attribute[] getAttribute() {
        return attribute;
    }


    /**
     * Sets the attribute value for this Group.
     * 
     * @param attribute
     */
    public void setAttribute(gov.nih.nci.cagrid.cqlquery.Attribute[] attribute) {
        this.attribute = attribute;
    }

    public gov.nih.nci.cagrid.cqlquery.Attribute getAttribute(int i) {
        return this.attribute[i];
    }

    public void setAttribute(int i, gov.nih.nci.cagrid.cqlquery.Attribute _value) {
        this.attribute[i] = _value;
    }


    /**
     * Gets the group value for this Group.
     * 
     * @return group
     */
    public gov.nih.nci.cagrid.cqlquery.Group[] getGroup() {
        return group;
    }


    /**
     * Sets the group value for this Group.
     * 
     * @param group
     */
    public void setGroup(gov.nih.nci.cagrid.cqlquery.Group[] group) {
        this.group = group;
    }

    public gov.nih.nci.cagrid.cqlquery.Group getGroup(int i) {
        return this.group[i];
    }

    public void setGroup(int i, gov.nih.nci.cagrid.cqlquery.Group _value) {
        this.group[i] = _value;
    }


    /**
     * Gets the logicRelation value for this Group.
     * 
     * @return logicRelation
     */
    public gov.nih.nci.cagrid.cqlquery.LogicalOperator getLogicRelation() {
        return logicRelation;
    }


    /**
     * Sets the logicRelation value for this Group.
     * 
     * @param logicRelation
     */
    public void setLogicRelation(gov.nih.nci.cagrid.cqlquery.LogicalOperator logicRelation) {
        this.logicRelation = logicRelation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Group)) return false;
        Group other = (Group) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.association==null && other.getAssociation()==null) || 
             (this.association!=null &&
              java.util.Arrays.equals(this.association, other.getAssociation()))) &&
            ((this.attribute==null && other.getAttribute()==null) || 
             (this.attribute!=null &&
              java.util.Arrays.equals(this.attribute, other.getAttribute()))) &&
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              java.util.Arrays.equals(this.group, other.getGroup()))) &&
            ((this.logicRelation==null && other.getLogicRelation()==null) || 
             (this.logicRelation!=null &&
              this.logicRelation.equals(other.getLogicRelation())));
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
        if (getAssociation() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAssociation());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAssociation(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
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
        if (getGroup() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGroup());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGroup(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLogicRelation() != null) {
            _hashCode += getLogicRelation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Group.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Group"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("logicRelation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "logicRelation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "LogicalOperator"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("association");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Association"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Association"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Attribute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Attribute"));
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
