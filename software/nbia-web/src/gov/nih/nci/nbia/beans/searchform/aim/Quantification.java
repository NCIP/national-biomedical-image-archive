/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform.aim;

import java.util.List;

import javax.faces.model.SelectItem;

public class Quantification {
	enum TYPE {
		SCALE,
		NUMBER,
		QUANTILE
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	//make wrapper class for SelectItem prolly
	public List<SelectItem> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<SelectItem> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public boolean isScale() {
		return type == TYPE.SCALE;
	}

	public boolean isQuantile() {
		return type == TYPE.QUANTILE;
	}

	public boolean isNumber() {
		return type == TYPE.NUMBER;
	}


	//////////////////////////////////////////PRIVATE///////////////////////////////////

	private TYPE type;

	private String name;

	private String value;

	private List<SelectItem> possibleValues;
}
