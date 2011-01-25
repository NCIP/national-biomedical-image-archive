/**
 * PermissibleCQLObjectResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.nbia.cqlresulttypes;

public class PermissibleCQLObjectResults  implements java.io.Serializable {
    private gov.nih.nci.ncia.domain.ClinicalTrialSite clinicalTrialSite;
    private gov.nih.nci.ncia.domain.Image image;
    private gov.nih.nci.ncia.domain.Patient patient;
    private gov.nih.nci.ncia.domain.Equipment equipment;
    private gov.nih.nci.ncia.domain.ClinicalTrialSponsor clinicalTrialSponsor;
    private gov.nih.nci.ncia.domain.Study study;
    private gov.nih.nci.ncia.domain.ClinicalTrialProtocol clinicalTrialProtocol;
    private gov.nih.nci.ncia.domain.CurationData curationData;
    private gov.nih.nci.ncia.domain.Series series;
    private gov.nih.nci.ncia.domain.TrialDataProvenance trialDataProvenance;
    private gov.nih.nci.ncia.domain.ClinicalTrialSubject clinicalTrialSubject;
    private gov.nih.nci.ncia.domain.Annotation annotation;

    public PermissibleCQLObjectResults() {
    }

    public PermissibleCQLObjectResults(
           gov.nih.nci.ncia.domain.Annotation annotation,
           gov.nih.nci.ncia.domain.ClinicalTrialProtocol clinicalTrialProtocol,
           gov.nih.nci.ncia.domain.ClinicalTrialSite clinicalTrialSite,
           gov.nih.nci.ncia.domain.ClinicalTrialSponsor clinicalTrialSponsor,
           gov.nih.nci.ncia.domain.ClinicalTrialSubject clinicalTrialSubject,
           gov.nih.nci.ncia.domain.CurationData curationData,
           gov.nih.nci.ncia.domain.Equipment equipment,
           gov.nih.nci.ncia.domain.Image image,
           gov.nih.nci.ncia.domain.Patient patient,
           gov.nih.nci.ncia.domain.Series series,
           gov.nih.nci.ncia.domain.Study study,
           gov.nih.nci.ncia.domain.TrialDataProvenance trialDataProvenance) {
           this.clinicalTrialSite = clinicalTrialSite;
           this.image = image;
           this.patient = patient;
           this.equipment = equipment;
           this.clinicalTrialSponsor = clinicalTrialSponsor;
           this.study = study;
           this.clinicalTrialProtocol = clinicalTrialProtocol;
           this.curationData = curationData;
           this.series = series;
           this.trialDataProvenance = trialDataProvenance;
           this.clinicalTrialSubject = clinicalTrialSubject;
           this.annotation = annotation;
    }


    /**
     * Gets the clinicalTrialSite value for this PermissibleCQLObjectResults.
     * 
     * @return clinicalTrialSite
     */
    public gov.nih.nci.ncia.domain.ClinicalTrialSite getClinicalTrialSite() {
        return clinicalTrialSite;
    }


    /**
     * Sets the clinicalTrialSite value for this PermissibleCQLObjectResults.
     * 
     * @param clinicalTrialSite
     */
    public void setClinicalTrialSite(gov.nih.nci.ncia.domain.ClinicalTrialSite clinicalTrialSite) {
        this.clinicalTrialSite = clinicalTrialSite;
    }


    /**
     * Gets the image value for this PermissibleCQLObjectResults.
     * 
     * @return image
     */
    public gov.nih.nci.ncia.domain.Image getImage() {
        return image;
    }


    /**
     * Sets the image value for this PermissibleCQLObjectResults.
     * 
     * @param image
     */
    public void setImage(gov.nih.nci.ncia.domain.Image image) {
        this.image = image;
    }


    /**
     * Gets the patient value for this PermissibleCQLObjectResults.
     * 
     * @return patient
     */
    public gov.nih.nci.ncia.domain.Patient getPatient() {
        return patient;
    }


    /**
     * Sets the patient value for this PermissibleCQLObjectResults.
     * 
     * @param patient
     */
    public void setPatient(gov.nih.nci.ncia.domain.Patient patient) {
        this.patient = patient;
    }


    /**
     * Gets the equipment value for this PermissibleCQLObjectResults.
     * 
     * @return equipment
     */
    public gov.nih.nci.ncia.domain.Equipment getEquipment() {
        return equipment;
    }


    /**
     * Sets the equipment value for this PermissibleCQLObjectResults.
     * 
     * @param equipment
     */
    public void setEquipment(gov.nih.nci.ncia.domain.Equipment equipment) {
        this.equipment = equipment;
    }


    /**
     * Gets the clinicalTrialSponsor value for this PermissibleCQLObjectResults.
     * 
     * @return clinicalTrialSponsor
     */
    public gov.nih.nci.ncia.domain.ClinicalTrialSponsor getClinicalTrialSponsor() {
        return clinicalTrialSponsor;
    }


    /**
     * Sets the clinicalTrialSponsor value for this PermissibleCQLObjectResults.
     * 
     * @param clinicalTrialSponsor
     */
    public void setClinicalTrialSponsor(gov.nih.nci.ncia.domain.ClinicalTrialSponsor clinicalTrialSponsor) {
        this.clinicalTrialSponsor = clinicalTrialSponsor;
    }


    /**
     * Gets the study value for this PermissibleCQLObjectResults.
     * 
     * @return study
     */
    public gov.nih.nci.ncia.domain.Study getStudy() {
        return study;
    }


    /**
     * Sets the study value for this PermissibleCQLObjectResults.
     * 
     * @param study
     */
    public void setStudy(gov.nih.nci.ncia.domain.Study study) {
        this.study = study;
    }


    /**
     * Gets the clinicalTrialProtocol value for this PermissibleCQLObjectResults.
     * 
     * @return clinicalTrialProtocol
     */
    public gov.nih.nci.ncia.domain.ClinicalTrialProtocol getClinicalTrialProtocol() {
        return clinicalTrialProtocol;
    }


    /**
     * Sets the clinicalTrialProtocol value for this PermissibleCQLObjectResults.
     * 
     * @param clinicalTrialProtocol
     */
    public void setClinicalTrialProtocol(gov.nih.nci.ncia.domain.ClinicalTrialProtocol clinicalTrialProtocol) {
        this.clinicalTrialProtocol = clinicalTrialProtocol;
    }


    /**
     * Gets the curationData value for this PermissibleCQLObjectResults.
     * 
     * @return curationData
     */
    public gov.nih.nci.ncia.domain.CurationData getCurationData() {
        return curationData;
    }


    /**
     * Sets the curationData value for this PermissibleCQLObjectResults.
     * 
     * @param curationData
     */
    public void setCurationData(gov.nih.nci.ncia.domain.CurationData curationData) {
        this.curationData = curationData;
    }


    /**
     * Gets the series value for this PermissibleCQLObjectResults.
     * 
     * @return series
     */
    public gov.nih.nci.ncia.domain.Series getSeries() {
        return series;
    }


    /**
     * Sets the series value for this PermissibleCQLObjectResults.
     * 
     * @param series
     */
    public void setSeries(gov.nih.nci.ncia.domain.Series series) {
        this.series = series;
    }


    /**
     * Gets the trialDataProvenance value for this PermissibleCQLObjectResults.
     * 
     * @return trialDataProvenance
     */
    public gov.nih.nci.ncia.domain.TrialDataProvenance getTrialDataProvenance() {
        return trialDataProvenance;
    }


    /**
     * Sets the trialDataProvenance value for this PermissibleCQLObjectResults.
     * 
     * @param trialDataProvenance
     */
    public void setTrialDataProvenance(gov.nih.nci.ncia.domain.TrialDataProvenance trialDataProvenance) {
        this.trialDataProvenance = trialDataProvenance;
    }


    /**
     * Gets the clinicalTrialSubject value for this PermissibleCQLObjectResults.
     * 
     * @return clinicalTrialSubject
     */
    public gov.nih.nci.ncia.domain.ClinicalTrialSubject getClinicalTrialSubject() {
        return clinicalTrialSubject;
    }


    /**
     * Sets the clinicalTrialSubject value for this PermissibleCQLObjectResults.
     * 
     * @param clinicalTrialSubject
     */
    public void setClinicalTrialSubject(gov.nih.nci.ncia.domain.ClinicalTrialSubject clinicalTrialSubject) {
        this.clinicalTrialSubject = clinicalTrialSubject;
    }


    /**
     * Gets the annotation value for this PermissibleCQLObjectResults.
     * 
     * @return annotation
     */
    public gov.nih.nci.ncia.domain.Annotation getAnnotation() {
        return annotation;
    }


    /**
     * Sets the annotation value for this PermissibleCQLObjectResults.
     * 
     * @param annotation
     */
    public void setAnnotation(gov.nih.nci.ncia.domain.Annotation annotation) {
        this.annotation = annotation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PermissibleCQLObjectResults)) return false;
        PermissibleCQLObjectResults other = (PermissibleCQLObjectResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.clinicalTrialSite==null && other.getClinicalTrialSite()==null) || 
             (this.clinicalTrialSite!=null &&
              this.clinicalTrialSite.equals(other.getClinicalTrialSite()))) &&
            ((this.image==null && other.getImage()==null) || 
             (this.image!=null &&
              this.image.equals(other.getImage()))) &&
            ((this.patient==null && other.getPatient()==null) || 
             (this.patient!=null &&
              this.patient.equals(other.getPatient()))) &&
            ((this.equipment==null && other.getEquipment()==null) || 
             (this.equipment!=null &&
              this.equipment.equals(other.getEquipment()))) &&
            ((this.clinicalTrialSponsor==null && other.getClinicalTrialSponsor()==null) || 
             (this.clinicalTrialSponsor!=null &&
              this.clinicalTrialSponsor.equals(other.getClinicalTrialSponsor()))) &&
            ((this.study==null && other.getStudy()==null) || 
             (this.study!=null &&
              this.study.equals(other.getStudy()))) &&
            ((this.clinicalTrialProtocol==null && other.getClinicalTrialProtocol()==null) || 
             (this.clinicalTrialProtocol!=null &&
              this.clinicalTrialProtocol.equals(other.getClinicalTrialProtocol()))) &&
            ((this.curationData==null && other.getCurationData()==null) || 
             (this.curationData!=null &&
              this.curationData.equals(other.getCurationData()))) &&
            ((this.series==null && other.getSeries()==null) || 
             (this.series!=null &&
              this.series.equals(other.getSeries()))) &&
            ((this.trialDataProvenance==null && other.getTrialDataProvenance()==null) || 
             (this.trialDataProvenance!=null &&
              this.trialDataProvenance.equals(other.getTrialDataProvenance()))) &&
            ((this.clinicalTrialSubject==null && other.getClinicalTrialSubject()==null) || 
             (this.clinicalTrialSubject!=null &&
              this.clinicalTrialSubject.equals(other.getClinicalTrialSubject()))) &&
            ((this.annotation==null && other.getAnnotation()==null) || 
             (this.annotation!=null &&
              this.annotation.equals(other.getAnnotation())));
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
        if (getClinicalTrialSite() != null) {
            _hashCode += getClinicalTrialSite().hashCode();
        }
        if (getImage() != null) {
            _hashCode += getImage().hashCode();
        }
        if (getPatient() != null) {
            _hashCode += getPatient().hashCode();
        }
        if (getEquipment() != null) {
            _hashCode += getEquipment().hashCode();
        }
        if (getClinicalTrialSponsor() != null) {
            _hashCode += getClinicalTrialSponsor().hashCode();
        }
        if (getStudy() != null) {
            _hashCode += getStudy().hashCode();
        }
        if (getClinicalTrialProtocol() != null) {
            _hashCode += getClinicalTrialProtocol().hashCode();
        }
        if (getCurationData() != null) {
            _hashCode += getCurationData().hashCode();
        }
        if (getSeries() != null) {
            _hashCode += getSeries().hashCode();
        }
        if (getTrialDataProvenance() != null) {
            _hashCode += getTrialDataProvenance().hashCode();
        }
        if (getClinicalTrialSubject() != null) {
            _hashCode += getClinicalTrialSubject().hashCode();
        }
        if (getAnnotation() != null) {
            _hashCode += getAnnotation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PermissibleCQLObjectResults.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://nbia.nci.nih.gov/NBIAService/CQLResultTypes", "PermissibleCQLObjectResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clinicalTrialSite");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialSite"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialSite"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Image"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Image"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("patient");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Patient"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Patient"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipment");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Equipment"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Equipment"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clinicalTrialSponsor");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialSponsor"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialSponsor"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("study");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Study"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Study"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clinicalTrialProtocol");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialProtocol"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialProtocol"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("curationData");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "CurationData"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "CurationData"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("series");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Series"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Series"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trialDataProvenance");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "TrialDataProvenance"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "TrialDataProvenance"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clinicalTrialSubject");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialSubject"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "ClinicalTrialSubject"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotation");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Annotation"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://nbia/1.0/gov.nih.nci.ncia.domain", "Annotation"));
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
