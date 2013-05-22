/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform.aim;

import gov.nih.nci.nbia.beans.searchform.aim.Quantification.TYPE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;

public class AimMock {

	public static Set<String> createMockCodeMeaningNames() {
		Set<String> set = new HashSet<String>();

		set.add("Sphericity");
		set.add("Internal Structure Calcification");
		set.add("Margin");
		set.add("Lobulation");
		set.add("Spiculation");
		set.add("Subtlety");
		set.add("Internal Structure");
		set.add("Likelihood of Malignancy");
    	return set;
	}

	public static Set<String> createMockCodeValuePairs() {
		Set<String> set = new HashSet<String>();

		set.add("CIP-2181751");
		set.add("CIP-2181739");
		set.add("CIP-2181727");
    	return set;
	}


	public static Set<Quantification> createMockQuantifications() {
		Set<Quantification> set = new HashSet<Quantification>();

		List<SelectItem> possibleValues = new ArrayList<SelectItem>();
		possibleValues.add(new SelectItem("1"));
    	possibleValues.add(new SelectItem("2"));
    	possibleValues.add(new SelectItem("3"));
    	possibleValues.add(new SelectItem("4"));
    	possibleValues.add(new SelectItem("5"));

		Quantification q1 = new Quantification();
		q1.setName("Sphericity Scale");
		q1.setType(TYPE.SCALE);
		q1.setPossibleValues(possibleValues);

		Quantification q2 = new Quantification();
		q2.setName("Internal Structure Calcification Scale");
		q2.setType(TYPE.SCALE);
		q2.setPossibleValues(possibleValues);

		Quantification q3 = new Quantification();
		q3.setName("Margin Scale");
		q3.setType(TYPE.SCALE);
		q3.setPossibleValues(possibleValues);

		Quantification q4 = new Quantification();
		q4.setName("Lobulation Scale");
		q4.setType(TYPE.SCALE);
		q4.setPossibleValues(possibleValues);

		Quantification q5 = new Quantification();
		q5.setName("Spiculation Scale");
		q5.setType(TYPE.SCALE);
		q5.setPossibleValues(possibleValues);

		Quantification q6 = new Quantification();
		q6.setName("Subtlety Scale");
		q6.setType(TYPE.SCALE);
		q6.setPossibleValues(possibleValues);

		Quantification q7 = new Quantification();
		q7.setName("Internal Structure Scale");
		q7.setType(TYPE.SCALE);
		q7.setPossibleValues(possibleValues);

		Quantification q8 = new Quantification();
		q8.setName("Likelihood of Malignancy Scale");
		q8.setType(TYPE.SCALE);
		q8.setPossibleValues(possibleValues);

		set.add(q1);
		set.add(q2);
		set.add(q3);
		set.add(q4);
		set.add(q5);
		set.add(q6);
		set.add(q7);
		set.add(q8);

		return set;
	}
}
