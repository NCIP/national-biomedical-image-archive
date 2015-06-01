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
 * @author Q. Pan - NCICB/SAIC
 *
 */
public class ColorModeOptionCriteria extends SingleValuePersistentCriteria
		implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String BMode = "Return Only Images With No Color";

	public final static String ColorMode = "Return Colored Images";
	public final static String NoCondition = "Return All Images";
	
	private String colorModeOptionValue;

	/**
	 */
	public ColorModeOptionCriteria() {
		super();
	}

	/**
	 */
	public ColorModeOptionCriteria(String colorModeOptionValue) {
		super();
		this.colorModeOptionValue = colorModeOptionValue;
	}

	/**
	 * @return Returns the contrastAgentValue.
	 */
	public String getColorModeOptionValue() {
		return colorModeOptionValue;
	}

	/**
	 * 
	 */
	public void setColorModeOptionValue(String colorModeOptionValue) {
		if (colorModeOptionValue != null) {
			if (colorModeOptionValue
					.equals(ColorModeOptionCriteria.BMode)) {
				this.colorModeOptionValue = ColorModeOptionCriteria.BMode;
			} else if (colorModeOptionValue
					.equals(ColorModeOptionCriteria.ColorMode)) {
				this.colorModeOptionValue = ColorModeOptionCriteria.ColorMode;
			} else {
				this.colorModeOptionValue = ColorModeOptionCriteria.NoCondition;
			}
		}
	}

	public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
		setColorModeOptionValue(attr.getAttributeValue());
	}

	public String getSingleValue() {
		return colorModeOptionValue;
	}

	public static final class BModeOption extends
			ColorModeOptionCriteria {
		public BModeOption() {
			super(BMode);
		}
	}

	public static final class ColorModeOption extends
			ColorModeOptionCriteria {
		public ColorModeOption() {
			super(ColorMode);
		}
	}

	public static final class NoConditionOption extends
			ColorModeOptionCriteria {
		public NoConditionOption() {
			super(NoCondition);
		}
	}
}
