/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.criteria;

/**
 * $Log: not supported by cvs2svn $
 */

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;

/**
 * @author Rona Zhou - NCICB/SAIC
 *
 */
public class AnnotationOptionCriteria extends SingleValuePersistentCriteria
		implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String AnnotationOnly = "Return Only Annotated Series";

	public final static String NoAnnotation = "Return Only Series That Do Not Have Annotations";

	public final static String NoCondition = "Return All Series";

	private String annotationOptionValue;

	/**
	 */
	public AnnotationOptionCriteria() {
		super();
	}

	/**
	 */
	public AnnotationOptionCriteria(String annotationOptionValue) {
		super();
		this.annotationOptionValue = annotationOptionValue;
	}

	/**
	 * @return Returns the contrastAgentValue.
	 */
	public String getAnnotationOptionValue() {
		return annotationOptionValue;
	}

	/**
	 * 
	 */
	public void setAnnotationOptionValue(String annotationOptionValue) {
		if (annotationOptionValue != null) {
			if (annotationOptionValue
					.equals(AnnotationOptionCriteria.AnnotationOnly)) {
				this.annotationOptionValue = AnnotationOptionCriteria.AnnotationOnly;
			} else if (annotationOptionValue
					.equals(AnnotationOptionCriteria.NoAnnotation)) {
				this.annotationOptionValue = AnnotationOptionCriteria.NoAnnotation;
			} else {
				this.annotationOptionValue = AnnotationOptionCriteria.NoCondition;
			}
		}
	}

	public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
		setAnnotationOptionValue(attr.getAttributeValue());
	}

	public String getSingleValue() {
		return annotationOptionValue;
	}

	public static final class AnnotationOnlyOption extends
			AnnotationOptionCriteria {
		public AnnotationOnlyOption() {
			super(AnnotationOnly);
		}
	}

	public static final class NoAnnotationOption extends
			AnnotationOptionCriteria {
		public NoAnnotationOption() {
			super(NoAnnotation);
		}
	}

	public static final class NoConditionOption extends
			AnnotationOptionCriteria {
		public NoConditionOption() {
			super(NoCondition);
		}
	}
}
