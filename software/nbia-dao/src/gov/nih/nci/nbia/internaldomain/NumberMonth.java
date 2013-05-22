/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;

public class NumberMonth implements Serializable {

	public NumberMonth() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumberMonth() {
		return numberMonth;
	}

	public void setNumberMonth(Integer numberMonth) {
		this.numberMonth = numberMonth;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof NumberMonth) {
			NumberMonth c = (NumberMonth) obj;
			Integer thisId = getId();
			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public int hashCode() {
		int h = 0;
		if (getId() != null) {
			h += getId().hashCode();
		}
		return h;
	}

	private static final long serialVersionUID = 0x499602d2L;
	private Integer id;
	private Integer numberMonth;
}