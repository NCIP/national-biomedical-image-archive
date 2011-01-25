/**
 * DomainModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class DomainModel  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection exposedUMLAssociationCollection;
    private gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection exposedUMLClassCollection;
    private gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection umlGeneralizationCollection;
    private java.lang.String projectDescription;  // attribute
    private java.lang.String projectLongName;  // attribute
    private java.lang.String projectShortName;  // attribute
    private java.lang.String projectVersion;  // attribute

    public DomainModel() {
    }

    public DomainModel(
           gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection exposedUMLAssociationCollection,
           gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection exposedUMLClassCollection,
           java.lang.String projectDescription,
           java.lang.String projectLongName,
           java.lang.String projectShortName,
           java.lang.String projectVersion,
           gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection umlGeneralizationCollection) {
           this.exposedUMLAssociationCollection = exposedUMLAssociationCollection;
           this.exposedUMLClassCollection = exposedUMLClassCollection;
           this.umlGeneralizationCollection = umlGeneralizationCollection;
           this.projectDescription = projectDescription;
           this.projectLongName = projectLongName;
           this.projectShortName = projectShortName;
           this.projectVersion = projectVersion;
    }


    /**
     * Gets the exposedUMLAssociationCollection value for this DomainModel.
     * 
     * @return exposedUMLAssociationCollection
     */
    public gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection getExposedUMLAssociationCollection() {
        return exposedUMLAssociationCollection;
    }


    /**
     * Sets the exposedUMLAssociationCollection value for this DomainModel.
     * 
     * @param exposedUMLAssociationCollection
     */
    public void setExposedUMLAssociationCollection(gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection exposedUMLAssociationCollection) {
        this.exposedUMLAssociationCollection = exposedUMLAssociationCollection;
    }


    /**
     * Gets the exposedUMLClassCollection value for this DomainModel.
     * 
     * @return exposedUMLClassCollection
     */
    public gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection getExposedUMLClassCollection() {
        return exposedUMLClassCollection;
    }


    /**
     * Sets the exposedUMLClassCollection value for this DomainModel.
     * 
     * @param exposedUMLClassCollection
     */
    public void setExposedUMLClassCollection(gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection exposedUMLClassCollection) {
        this.exposedUMLClassCollection = exposedUMLClassCollection;
    }


    /**
     * Gets the umlGeneralizationCollection value for this DomainModel.
     * 
     * @return umlGeneralizationCollection
     */
    public gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection getUmlGeneralizationCollection() {
        return umlGeneralizationCollection;
    }


    /**
     * Sets the umlGeneralizationCollection value for this DomainModel.
     * 
     * @param umlGeneralizationCollection
     */
    public void setUmlGeneralizationCollection(gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection umlGeneralizationCollection) {
        this.umlGeneralizationCollection = umlGeneralizationCollection;
    }


    /**
     * Gets the projectDescription value for this DomainModel.
     * 
     * @return projectDescription
     */
    public java.lang.String getProjectDescription() {
        return projectDescription;
    }


    /**
     * Sets the projectDescription value for this DomainModel.
     * 
     * @param projectDescription
     */
    public void setProjectDescription(java.lang.String projectDescription) {
        this.projectDescription = projectDescription;
    }


    /**
     * Gets the projectLongName value for this DomainModel.
     * 
     * @return projectLongName
     */
    public java.lang.String getProjectLongName() {
        return projectLongName;
    }


    /**
     * Sets the projectLongName value for this DomainModel.
     * 
     * @param projectLongName
     */
    public void setProjectLongName(java.lang.String projectLongName) {
        this.projectLongName = projectLongName;
    }


    /**
     * Gets the projectShortName value for this DomainModel.
     * 
     * @return projectShortName
     */
    public java.lang.String getProjectShortName() {
        return projectShortName;
    }


    /**
     * Sets the projectShortName value for this DomainModel.
     * 
     * @param projectShortName
     */
    public void setProjectShortName(java.lang.String projectShortName) {
        this.projectShortName = projectShortName;
    }


    /**
     * Gets the projectVersion value for this DomainModel.
     * 
     * @return projectVersion
     */
    public java.lang.String getProjectVersion() {
        return projectVersion;
    }


    /**
     * Sets the projectVersion value for this DomainModel.
     * 
     * @param projectVersion
     */
    public void setProjectVersion(java.lang.String projectVersion) {
        this.projectVersion = projectVersion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DomainModel)) return false;
        DomainModel other = (DomainModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exposedUMLAssociationCollection==null && other.getExposedUMLAssociationCollection()==null) || 
             (this.exposedUMLAssociationCollection!=null &&
              this.exposedUMLAssociationCollection.equals(other.getExposedUMLAssociationCollection()))) &&
            ((this.exposedUMLClassCollection==null && other.getExposedUMLClassCollection()==null) || 
             (this.exposedUMLClassCollection!=null &&
              this.exposedUMLClassCollection.equals(other.getExposedUMLClassCollection()))) &&
            ((this.umlGeneralizationCollection==null && other.getUmlGeneralizationCollection()==null) || 
             (this.umlGeneralizationCollection!=null &&
              this.umlGeneralizationCollection.equals(other.getUmlGeneralizationCollection()))) &&
            ((this.projectDescription==null && other.getProjectDescription()==null) || 
             (this.projectDescription!=null &&
              this.projectDescription.equals(other.getProjectDescription()))) &&
            ((this.projectLongName==null && other.getProjectLongName()==null) || 
             (this.projectLongName!=null &&
              this.projectLongName.equals(other.getProjectLongName()))) &&
            ((this.projectShortName==null && other.getProjectShortName()==null) || 
             (this.projectShortName!=null &&
              this.projectShortName.equals(other.getProjectShortName()))) &&
            ((this.projectVersion==null && other.getProjectVersion()==null) || 
             (this.projectVersion!=null &&
              this.projectVersion.equals(other.getProjectVersion())));
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
        if (getExposedUMLAssociationCollection() != null) {
            _hashCode += getExposedUMLAssociationCollection().hashCode();
        }
        if (getExposedUMLClassCollection() != null) {
            _hashCode += getExposedUMLClassCollection().hashCode();
        }
        if (getUmlGeneralizationCollection() != null) {
            _hashCode += getUmlGeneralizationCollection().hashCode();
        }
        if (getProjectDescription() != null) {
            _hashCode += getProjectDescription().hashCode();
        }
        if (getProjectLongName() != null) {
            _hashCode += getProjectLongName().hashCode();
        }
        if (getProjectShortName() != null) {
            _hashCode += getProjectShortName().hashCode();
        }
        if (getProjectVersion() != null) {
            _hashCode += getProjectVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DomainModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("projectDescription");
        attrField.setXmlName(new javax.xml.namespace.QName("", "projectDescription"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("projectLongName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "projectLongName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("projectShortName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "projectShortName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("projectVersion");
        attrField.setXmlName(new javax.xml.namespace.QName("", "projectVersion"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exposedUMLAssociationCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "exposedUMLAssociationCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>exposedUMLAssociationCollection"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exposedUMLClassCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "exposedUMLClassCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>exposedUMLClassCollection"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("umlGeneralizationCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "umlGeneralizationCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>umlGeneralizationCollection"));
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
