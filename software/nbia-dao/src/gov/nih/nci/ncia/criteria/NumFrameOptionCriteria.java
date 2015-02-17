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
public class NumFrameOptionCriteria extends SingleValuePersistentCriteria
		implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String SingleFrameOnly = "Return Only Image With Single Frame ";

	public final static String MultiFrame = "Return Only Image With Multiple Frame";
	public final static String NoCondition = "Return All Images";
	
	private String numFrameOptionValue;

	/**
	 */
	public NumFrameOptionCriteria() {
		super();
	}

	/**
	 */
	public NumFrameOptionCriteria(String numFrameOptionValue) {
		super();
		this.numFrameOptionValue = numFrameOptionValue;
	}

	/**
	 * @return Returns the contrastAgentValue.
	 */
	public String getNumFrameOptionValue() {
		return numFrameOptionValue;
	}

	/**
	 * 
	 */
	public void setNumFrameOptionValue(String numFrameOptionValue) {
		if (numFrameOptionValue != null) {
			if (numFrameOptionValue
					.equals(NumFrameOptionCriteria.SingleFrameOnly)) {
				this.numFrameOptionValue = NumFrameOptionCriteria.SingleFrameOnly;
			} else if (numFrameOptionValue
					.equals(NumFrameOptionCriteria.MultiFrame)) {
				this.numFrameOptionValue = NumFrameOptionCriteria.MultiFrame;
			} else {
				this.numFrameOptionValue = NumFrameOptionCriteria.NoCondition;
			}
		}
	}

	public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
		setNumFrameOptionValue(attr.getAttributeValue());
	}

	public String getSingleValue() {
		return numFrameOptionValue;
	}

	public static final class SingleFrameOnlyOption extends
			NumFrameOptionCriteria {
		public SingleFrameOnlyOption() {
			super(SingleFrameOnly);
		}
	}

	public static final class MultiFrameOption extends
			NumFrameOptionCriteria {
		public MultiFrameOption() {
			super(MultiFrame);
		}
	}

	public static final class NoConditionOption extends
			NumFrameOptionCriteria {
		public NoConditionOption() {
			super(NoCondition);
		}
	}
}
