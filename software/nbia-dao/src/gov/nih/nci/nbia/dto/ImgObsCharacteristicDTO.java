/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

public class ImgObsCharacteristicDTO {

	public ImgObsCharacteristicDTO(String codeValue,  
			                       String codeMeaningName,
			                       String codeSchemaDesignator) {
		super();
		this.codeValue = codeValue;
		this.codeMeaningName = codeMeaningName;
		this.codeSchemaDesignator = codeSchemaDesignator;
	}
	
	public String getCodeValue() {
		return codeValue;
	}

	public String getCodeMeaningName() {
		return codeMeaningName;
	}

	public String getCodeSchemaDesignator() {
		return codeSchemaDesignator;
	}

	private String codeValue;
	
	private String codeMeaningName;
	
	private String codeSchemaDesignator;	

}
