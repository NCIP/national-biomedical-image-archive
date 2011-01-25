/**
 * UMLAssociation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class UMLAssociation  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge targetUMLAssociationEdge;
    private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge sourceUMLAssociationEdge;
    private boolean bidirectional;  // attribute

    public UMLAssociation() {
    }

    public UMLAssociation(
           boolean bidirectional,
           gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge sourceUMLAssociationEdge,
           gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge targetUMLAssociationEdge) {
           this.targetUMLAssociationEdge = targetUMLAssociationEdge;
           this.sourceUMLAssociationEdge = sourceUMLAssociationEdge;
           this.bidirectional = bidirectional;
    }


    /**
     * Gets the targetUMLAssociationEdge value for this UMLAssociation.
     * 
     * @return targetUMLAssociationEdge
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge getTargetUMLAssociationEdge() {
        return targetUMLAssociationEdge;
    }


    /**
     * Sets the targetUMLAssociationEdge value for this UMLAssociation.
     * 
     * @param targetUMLAssociationEdge
     */
    public void setTargetUMLAssociationEdge(gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge targetUMLAssociationEdge) {
        this.targetUMLAssociationEdge = targetUMLAssociationEdge;
    }


    /**
     * Gets the sourceUMLAssociationEdge value for this UMLAssociation.
     * 
     * @return sourceUMLAssociationEdge
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge getSourceUMLAssociationEdge() {
        return sourceUMLAssociationEdge;
    }


    /**
     * Sets the sourceUMLAssociationEdge value for this UMLAssociation.
     * 
     * @param sourceUMLAssociationEdge
     */
    public void setSourceUMLAssociationEdge(gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge sourceUMLAssociationEdge) {
        this.sourceUMLAssociationEdge = sourceUMLAssociationEdge;
    }


    /**
     * Gets the bidirectional value for this UMLAssociation.
     * 
     * @return bidirectional
     */
    public boolean isBidirectional() {
        return bidirectional;
    }


    /**
     * Sets the bidirectional value for this UMLAssociation.
     * 
     * @param bidirectional
     */
    public void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UMLAssociation)) return false;
        UMLAssociation other = (UMLAssociation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.targetUMLAssociationEdge==null && other.getTargetUMLAssociationEdge()==null) || 
             (this.targetUMLAssociationEdge!=null &&
              this.targetUMLAssociationEdge.equals(other.getTargetUMLAssociationEdge()))) &&
            ((this.sourceUMLAssociationEdge==null && other.getSourceUMLAssociationEdge()==null) || 
             (this.sourceUMLAssociationEdge!=null &&
              this.sourceUMLAssociationEdge.equals(other.getSourceUMLAssociationEdge()))) &&
            this.bidirectional == other.isBidirectional();
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
        if (getTargetUMLAssociationEdge() != null) {
            _hashCode += getTargetUMLAssociationEdge().hashCode();
        }
        if (getSourceUMLAssociationEdge() != null) {
            _hashCode += getSourceUMLAssociationEdge().hashCode();
        }
        _hashCode += (isBidirectional() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UMLAssociation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociation"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("bidirectional");
        attrField.setXmlName(new javax.xml.namespace.QName("", "bidirectional"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetUMLAssociationEdge");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "targetUMLAssociationEdge"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">UMLAssociation>targetUMLAssociationEdge"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceUMLAssociationEdge");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "sourceUMLAssociationEdge"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">UMLAssociation>sourceUMLAssociationEdge"));
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
