/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: DicomConstants.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

/**
 *
 * @author NCIA Team
 *
 * This class defines all contants variables for the dicom attributes names.
 * For example, variable #13 is content time.
 */
public class DicomConstants {
    public static final String SPECIFIC_CHARACTER_SET = "1";
    public static final String IMAGE_TYPE = "2";
    public static final String SOP_CLASS_UID = "3";
    public static final String SOP_INSTANCE_UID = "4";
    public static final String STUDY_DATE = "6";
    public static final String SERIES_DATE = "7";
    public static final String ACQUISITION_DATE = "8";
    public static final String CONTENT_DATE = "9";
    public static final String ACQUISITION_DATETIME = "10";
    public static final String STUDY_TIME = "11";
    public static final String ACQUISITION_TIME = "12";
    public static final String CONTENT_TIME = "13";
    public static final String MODALITY = "14";
    public static final String MANUFACTURER = "15";
    public static final String INSTITUTION_NAME = "16";
    public static final String STUDY_DESCRIPTION = "18";
    public static final String SERIES_DESCRIPTION = "19";
    public static final String ADMITTING_DIAGNOSES_DESCRIPTION = "21";
    public static final String ADMITTING_DIAGNOSES_CODE_SEQUENCE = "22";
    public static final String MANUFACTURER_MODEL_NAME = "23";
    public static final String REFERENCED_SOP_INSTANCE_UID = "24";
    public static final String ANATOMIC_REGION_SEQUENCE = "25";
    public static final String PATIENT_NAME = "26";
    public static final String PATIENT_ID = "27";
    public static final String PATIENT_BIRTH_DATE = "28";
    public static final String PATIENT_SEX = "30";
    public static final String PATIENT_AGE = "32";
    public static final String PATIENT_SIZE = "33";
    public static final String PATIENT_WEIGHT = "35";
    public static final String ETHNIC_GROUP = "36";
    public static final String OCCUPATION = "37";
    public static final String ADDITIONAL_PATIENT_HISTORY = "38";
    public static final String CLINICAL_TRIAL_SPONSOR_NAME = "39";
    public static final String CLINICAL_TRIAL_PROTOCOL_ID = "41";
    public static final String CLINICAL_TRIAL_PROTOCOL_NAME = "43";
    public static final String CLINICAL_TRIAL_SITE_ID = "44";
    public static final String CLINICAL_TRIAL_SITE_NAME = "46";
    public static final String CLINICAL_TRIAL_SUBJECT_ID = "47";
    public static final String CLINICAL_TRIAL_SUBJECT_READING_ID = "48";
    public static final String CLINICAL_TRIAL_TIME_POINT_ID = "49";
    public static final String CLINICAL_TRIAL_TIME_POINT_DESCRIPTION = "50";
    public static final String CLINICAL_TRIAL_COORDINATING_CENTER_NAME = "51";
    public static final String CONTRAST_BOLUS_AGENT = "53";
    public static final String BODY_PART_EXAMINED = "54";
    public static final String SCAN_OPTIONS = "56";
    public static final String SLICE_THICKNESS = "58";
    public static final String KVP = "59";
    public static final String DATA_COLLECTION_DIAMETER = "60";
    public static final String SOFTWARE_VERSIONS = "61";
    public static final String PROTOCOL_NAME = "62";
    public static final String CONTRAST_BOLUS_ROUTE = "63";
    public static final String RECONSTRUCTION_DIAMETER = "64";
    public static final String SOURCE_TO_DETECTOR_DISTANCE = "65";
    public static final String SOURCE_SUBJECT_DISTANCE = "66";
    public static final String GANTRY_DETECTOR_TILT = "67";
    public static final String EXPOSURE_TIME = "68";
    public static final String X_RAY_TUBE_CURRENT = "69";
    public static final String EXPOSURE = "70";
    public static final String EXPOSURE_IN_MAS = "71";
    public static final String EXTREMITY_FILTER = "72";
    public static final String FOCAL_SPOT_SIZE = "73";
    public static final String CONVOLUTION_KERNEL = "74";
    public static final String PATIENT_POSITION = "75";
    public static final String REVOLUTION_TIME = "76";
    public static final String SINGLE_COLLIMATION_WIDTH = "77";
    public static final String TOTAL_COLLIMATION_WIDTH = "78";
    public static final String TABLE_SPEED = "79";
    public static final String TABLE_FEED_PER_ROTATION = "80";
    public static final String CT_PITCH_FACTOR = "81";
    public static final String STUDY_INSTANCE_UID = "82";
    public static final String SERIES_INSTANCE_UID = "83";
    public static final String STUDY_ID = "84";
    public static final String SERIES_ID = "85";
    public static final String ACQUISITION_NUMBER = "87";
    public static final String INSTANCE_NUMBER = "88";
    public static final String PATIENT_IMAGE_POSITION = "89";
    public static final String PATIENT_IMAGE_ORIENTATION = "90";
    public static final String FRAME_OF_REFERENCE_UID = "91";
    public static final String IMAGE_LATERALITY = "92";
    public static final String LATERALITY = "93";
    public static final String SYNCHRONIZATION_FRAME_OF_REF_UID = "94";
    public static final String POSITION_REFERENCE_INDICATOR = "95";
    public static final String SLICE_LOCATION = "96";
    public static final String IMAGE_COMMENTS = "97";
    public static final String ROWS = "98";
    public static final String COLUMNS = "99";
    public static final String PIXEL_SPACING = "100";
    public static final String LOSSY_IMAGE_COMPRESSION = "101";
    public static final String UID = "102";
    public static final String STORAGE_MEDIA_FILE_SET_UID = "103";
    public static final String REFERENCED_FRAME_OF_REFERENCE_UID = "104";
    public static final String RELATED_FRAME_OF_REFERENCE_UID = "105";
    public static final String PROJECT_NAME = "106";
    public static final String TRIAL_NAME = "107";
    public static final String SITE_NAME = "108";
    public static final String SITE_ID = "109";
    public static final String TRIAL_VISIBILITY = "110";
    public static final String ACQUISITION_MATRIX = "111";
    public static final String DX_DATA_COLLECTION_DIAMETER = "112";   
    public static final String US_COLOR_DATA_PRESENT = "113";
    public static final String US_NUM_FRAME = "114";
    public static final String SCANNING_SEQUENCE = "115";
    public static final String SEQUENCE_VARIANT = "116";
    public static final String REPETITION_TIME = "117";
    public static final String ECHO_TIME = "118";
    public static final String INVERSION_TIME = "119";
    public static final String SEQUENCE_NAME = "120";
    public static final String IMAGED_NUCLEUS = "121";
    public static final String MAGNETIC_FIELD_STRENGTH = "122";
    public static final String SAR = "123";
    public static final String DB_DT = "124";
    public static final String TRIGGER_TIME = "125";
    public static final String ANGIO_FLAG = "126";
}
