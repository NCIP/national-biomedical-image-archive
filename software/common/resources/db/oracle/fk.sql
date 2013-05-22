/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/


ALTER TABLE annotation
ADD CONSTRAINT fk_ann_gs_pk_id FOREIGN KEY
(
  general_series_pk_id
)
REFERENCES general_series
(
  general_series_pk_id
)
ENABLE
;


ALTER TABLE clinical_trial_protocol
ADD CONSTRAINT fk_clin_trial_sponsor_id FOREIGN KEY
(
  clinical_trial_sponsor_id
)
REFERENCES clinical_trial_sponsor
(
  id
)
ENABLE
;


ALTER TABLE clinical_trial_subject
ADD CONSTRAINT fk_clinical_trial_protocol_id FOREIGN KEY
(
  clinical_trial_protocol_id
)
REFERENCES clinical_trial_protocol
(
  id
)
ENABLE
;


ALTER TABLE clinical_trial_subject
ADD CONSTRAINT fk_patient_id FOREIGN KEY
(
  patient_id
)
REFERENCES patient
(
  patient_pk_id
)
ENABLE
;


ALTER TABLE clinical_trial_subject
ADD CONSTRAINT fk_trial_site FOREIGN KEY
(
  trial_site_id
)
REFERENCES trial_site
(
  trial_site_pk_id
)
ENABLE
;


ALTER TABLE csm_group
ADD CONSTRAINT FK_APPLICATION_GROUP FOREIGN KEY
(
  APPLICATION_ID
)
REFERENCES csm_application
(
  APPLICATION_ID
)
ENABLE
;


ALTER TABLE csm_pg_pe
ADD CONSTRAINT FK_PROTECTION_ELEMENT_PROTECTI FOREIGN KEY
(
  PROTECTION_ELEMENT_ID
)
REFERENCES csm_protection_element
(
  PROTECTION_ELEMENT_ID
)
ENABLE
;


ALTER TABLE csm_pg_pe
ADD CONSTRAINT FK_PROTECTION_GROUP_PROTECTION FOREIGN KEY
(
  PROTECTION_GROUP_ID
)
REFERENCES csm_protection_group
(
  PROTECTION_GROUP_ID
)
ENABLE
;


ALTER TABLE csm_protection_element
ADD CONSTRAINT FK_PE_APPLICATION FOREIGN KEY
(
  APPLICATION_ID
)
REFERENCES csm_application
(
  APPLICATION_ID
)
ENABLE
;


ALTER TABLE csm_protection_group
ADD CONSTRAINT FK_PG_APPLICATION FOREIGN KEY
(
  APPLICATION_ID
)
REFERENCES csm_application
(
  APPLICATION_ID
)
ENABLE
;


ALTER TABLE csm_protection_group
ADD CONSTRAINT FK_PROTECTION_GROUP FOREIGN KEY
(
  PARENT_PROTECTION_GROUP_ID
)
REFERENCES csm_protection_group
(
  PROTECTION_GROUP_ID
)
ENABLE
;


ALTER TABLE csm_role
ADD CONSTRAINT FK_APPLICATION_ROLE FOREIGN KEY
(
  APPLICATION_ID
)
REFERENCES csm_application
(
  APPLICATION_ID
)
ENABLE
;


ALTER TABLE csm_role_privilege
ADD CONSTRAINT FK_PRIVILEGE_ROLE FOREIGN KEY
(
  PRIVILEGE_ID
)
REFERENCES csm_privilege
(
  PRIVILEGE_ID
)
ENABLE
;


ALTER TABLE csm_role_privilege
ADD CONSTRAINT FK_ROLE FOREIGN KEY
(
  ROLE_ID
)
REFERENCES csm_role
(
  ROLE_ID
)
ENABLE
;


ALTER TABLE csm_user_group
ADD CONSTRAINT FK_UG_GROUP FOREIGN KEY
(
  GROUP_ID
)
REFERENCES csm_group
(
  GROUP_ID
)
ENABLE
;


ALTER TABLE csm_user_group
ADD CONSTRAINT FK_USER_GROUP FOREIGN KEY
(
  USER_ID
)
REFERENCES csm_user
(
  USER_ID
)
ENABLE
;


ALTER TABLE csm_user_group_role_pg
ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTION_ FOREIGN KEY
(
  GROUP_ID
)
REFERENCES csm_group
(
  GROUP_ID
)
ENABLE
;


ALTER TABLE csm_user_group_role_pg
ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTIO_1 FOREIGN KEY
(
  PROTECTION_GROUP_ID
)
REFERENCES csm_protection_group
(
  PROTECTION_GROUP_ID
)
ENABLE
;


ALTER TABLE csm_user_group_role_pg
ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTIO_2 FOREIGN KEY
(
  ROLE_ID
)
REFERENCES csm_role
(
  ROLE_ID
)
ENABLE
;


ALTER TABLE csm_user_group_role_pg
ADD CONSTRAINT FK_USER_GROUP_ROLE_PROTECTIO_3 FOREIGN KEY
(
  USER_ID
)
REFERENCES csm_user
(
  USER_ID
)
ENABLE
;


ALTER TABLE csm_user_pe
ADD CONSTRAINT FK_PE_USER FOREIGN KEY
(
  USER_ID
)
REFERENCES csm_user
(
  USER_ID
)
ENABLE
;


ALTER TABLE csm_user_pe
ADD CONSTRAINT FK_PROTECTION_ELEMENT_USER FOREIGN KEY
(
  PROTECTION_ELEMENT_ID
)
REFERENCES csm_protection_element
(
  PROTECTION_ELEMENT_ID
)
ENABLE
;


ALTER TABLE ct_image
ADD CONSTRAINT ct_image_ibfk_1 FOREIGN KEY
(
  general_series_pk_id
)
REFERENCES general_series
(
  general_series_pk_id
)
ENABLE
;


ALTER TABLE ct_image
ADD CONSTRAINT fk_image_pk_id FOREIGN KEY
(
  image_pk_id
)
REFERENCES general_image
(
  image_pk_id
)
ENABLE
;


ALTER TABLE custom_series_list_attribute
ADD CONSTRAINT FK_CUSTOM_SERIES_LIST_PK_ID FOREIGN KEY
(
  CUSTOM_SERIES_LIST_PK_ID
)
REFERENCES custom_series_list
(
  CUSTOM_SERIES_LIST_PK_ID
)
ENABLE
;


ALTER TABLE general_image
ADD CONSTRAINT FK_general_image_patient FOREIGN KEY
(
  patient_pk_id
)
REFERENCES patient
(
  patient_pk_id
)
ENABLE
;


ALTER TABLE general_image
ADD CONSTRAINT FK_general_image_study FOREIGN KEY
(
  study_pk_id
)
REFERENCES study
(
  study_pk_id
)
ENABLE
;


ALTER TABLE general_image
ADD CONSTRAINT FK_general_image_trialdata_pro FOREIGN KEY
(
  trial_dp_pk_id
)
REFERENCES trial_data_provenance
(
  trial_dp_pk_id
)
ENABLE
;


ALTER TABLE general_image
ADD CONSTRAINT fk_g_series_pk_id FOREIGN KEY
(
  general_series_pk_id
)
REFERENCES general_series
(
  general_series_pk_id
)
ENABLE
;


ALTER TABLE general_series
ADD CONSTRAINT fk_gs_study_pk_id FOREIGN KEY
(
  study_pk_id
)
REFERENCES study
(
  study_pk_id
)
ENABLE
;


ALTER TABLE general_series
ADD CONSTRAINT fk_g_equipment_pk_id FOREIGN KEY
(
  general_equipment_pk_id
)
REFERENCES general_equipment
(
  general_equipment_pk_id
)
ENABLE
;



ALTER TABLE image_markup
ADD CONSTRAINT FK_image_markup_series FOREIGN KEY
(
  general_series_pk_id
)
REFERENCES general_series
(
  general_series_pk_id
)
ENABLE
;


ALTER TABLE patient
ADD CONSTRAINT fk_trial_dp_pk_id FOREIGN KEY
(
  trial_dp_pk_id
)
REFERENCES trial_data_provenance
(
  trial_dp_pk_id
)
ENABLE
;


ALTER TABLE patient
ADD CONSTRAINT fk_trial_site_pk_id FOREIGN KEY
(
  trial_site_pk_id
)
REFERENCES trial_site
(
  trial_site_pk_id
)
ENABLE
;


ALTER TABLE query_history
ADD CONSTRAINT FK_Q_H_USER_ID FOREIGN KEY
(
  user_id
)
REFERENCES csm_user
(
  USER_ID
)
ENABLE
;


ALTER TABLE query_history
ADD CONSTRAINT fk_s_query_pk_id FOREIGN KEY
(
  saved_query_pk_id
)
REFERENCES saved_query
(
  saved_query_pk_id
)
ENABLE
;


ALTER TABLE query_history_attribute
ADD CONSTRAINT FK_Q_H_PK_ID FOREIGN KEY
(
  query_history_pk_id
)
REFERENCES query_history
(
  query_history_pk_id
)
ENABLE
;


ALTER TABLE saved_query
ADD CONSTRAINT FK_SAVED_QUERY_USER_ID FOREIGN KEY
(
  user_id
)
REFERENCES csm_user
(
  USER_ID
)
ENABLE
;


ALTER TABLE saved_query_attribute
ADD CONSTRAINT FK_SAVED_QUERY_PK_ID FOREIGN KEY
(
  saved_query_pk_id
)
REFERENCES saved_query
(
  saved_query_pk_id
)
ENABLE
;


ALTER TABLE study
ADD CONSTRAINT fk_patient_pk_id FOREIGN KEY
(
  patient_pk_id
)
REFERENCES patient
(
  patient_pk_id
)
ENABLE
;


ALTER TABLE trial_site
ADD CONSTRAINT fk_trial_pk_id FOREIGN KEY
(
  trial_pk_id
)
REFERENCES clinical_trial
(
  trial_pk_id
)
ENABLE
;


CREATE VIEW dicom_image AS 
SELECT gi.image_pk_id AS IMAGE_PK_ID,
       gi.instance_number AS INSTANCE_NUMBER,gi.content_date AS CONTENT_DATE,gi.content_time AS CONTENT_TIME,
       gi.image_type AS IMAGE_TYPE,gi.acquisition_date AS ACQUISITION_DATE,gi.acquisition_time AS ACQUISITION_TIME,gi.acquisition_number AS ACQUISITION_NUMBER,
       gi.lossy_image_compression AS LOSSY_IMAGE_COMPRESSION,gi.pixel_spacing AS PIXEL_SPACING,gi.image_orientation_patient AS IMAGE_ORIENTATION_PATIENT,
       gi.image_position_patient AS IMAGE_POSITION_PATIENT,gi.slice_thickness AS SLICE_THICKNESS,gi.slice_location AS SLICE_LOCATION,gi.i_rows AS I_ROWS,
       gi.i_columns AS I_COLUMNS,gi.contrast_bolus_agent AS CONTRAST_BOLUS_AGENT,gi.contrast_bolus_route AS CONTRAST_BOLUS_ROUTE,gi.sop_class_uid AS SOP_CLASS_UID,
       gi.sop_instance_uid AS SOP_INSTANCE_UID,gi.general_series_pk_id AS GENERAL_SERIES_PK_ID,gi.patient_position AS PATIENT_POSITION,
       gi.source_to_detector_distance AS SOURCE_TO_DETECTOR_DISTANCE,gi.source_subject_distance AS SOURCE_SUBJECT_DISTANCE,gi.focal_spot_size AS FOCAL_SPOT_SIZE,
       gi.storage_media_file_set_uid AS STORAGE_MEDIA_FILE_SET_UID,gi.dicom_file_uri AS DICOM_FILE_URI,gi.acquisition_datetime AS ACQUISITION_DATETIME,
       gi.image_comments AS IMAGE_COMMENTS,gi.image_receiving_timestamp AS IMAGE_RECEIVING_TIMESTAMP,gi.curation_timestamp AS CURATION_TIMESTAMP,gi.annotation AS ANNOTATION,
       gi.submission_date AS SUBMISSION_DATE,gi.dicom_size AS DICOM_SIZE,gi.image_laterality AS IMAGE_LATERALITY,gi.trial_dp_pk_id AS TRIAL_DP_PK_ID,gi.patient_id AS PATIENT_ID,
       gi.study_instance_uid AS STUDY_INSTANCE_UID,gi.series_instance_uid AS SERIES_INSTANCE_UID,
       gi.patient_pk_id AS PATIENT_PK_ID,gi.study_pk_id AS STUDY_PK_ID,gi.project AS PROJECT,
       gi.acquisition_matrix AS ACQUISITION_MATRIX,gi.dx_data_collection_diameter AS DX_DATA_COLLECTION_DIAMETER,
       cti.kvp AS KVP,cti.scan_options AS SCAN_OPTIONS,cti.data_collection_diameter AS DATA_COLLECTION_DIAMETER,
       cti.reconstruction_diameter AS RECONSTRUCTION_DIAMETER,cti.gantry_detector_tilt AS GANTRY_DETECTOR_TILT,
       cti.exposure_time AS EXPOSURE_TIME,cti.x_ray_tube_current AS X_RAY_TUBE_CURRENT,
       cti.exposure AS EXPOSURE,cti.exposure_in_microas AS EXPOSURE_IN_MICROAS,cti.convolution_kernel AS CONVOLUTION_KERNEL,
       cti.revolution_time AS REVOLUTION_TIME,
       cti.single_collimation_width AS SINGLE_COLLIMATION_WIDTH,cti.total_collimation_width AS TOTAL_COLLIMATION_WIDTH,
       cti.table_speed AS TABLE_SPEED,
       cti.table_feed_per_rotation AS TABLE_FEED_PER_ROTATION,cti.ct_pitch_factor AS CT_PITCH_FACTOR,
       cti.anatomic_region_seq AS ANATOMIC_REGION_SEQ 
FROM general_image gi, ct_image cti
WHERE gi.image_pk_id = cti.image_pk_id
;

CREATE VIEW dicom_series AS 
SELECT
  general_series.general_series_pk_id    AS GENERAL_SERIES_PK_ID,
  general_series.body_part_examined      AS BODY_PART_EXAMINED,
  general_series.frame_of_reference_uid  AS FRAME_OF_REFERENCE_UID,
  general_series.series_laterality       AS SERIES_LATERALITY,
  general_series.modality                AS MODALITY,
  general_series.protocol_name           AS PROTOCOL_NAME,
  general_series.series_date             AS SERIES_DATE,
  general_series.series_desc             AS SERIES_DESC,
  general_series.series_instance_uid     AS SERIES_INSTANCE_UID,
  general_series.series_number           AS SERIES_NUMBER,
  general_series.sync_frame_of_ref_uid   AS SYNC_FRAME_OF_REF_UID,
  general_series.study_pk_id             AS STUDY_PK_ID,
  general_series.general_equipment_pk_id AS GENERAL_EQUIPMENT_PK_ID
FROM general_series
WHERE (general_series.visibility = '1')
       AND general_series.security_group is null
;

CREATE VIEW dicom_study AS 
SELECT
  study.study_pk_id                  AS STUDY_PK_ID,
  study.study_instance_uid           AS STUDY_INSTANCE_UID,
  study.additional_patient_history   AS ADDITIONAL_PATIENT_HISTORY,
  study.study_date                   AS STUDY_DATE,
  study.study_desc                   AS STUDY_DESC,
  study.admitting_diagnoses_desc     AS ADMITTING_DIAGNOSES_DESC,
  study.admitting_diagnoses_code_seq AS ADMITTING_DIAGNOSES_CODE_SEQ,
  study.occupation                   AS OCCUPATION,
  study.patient_age                  AS PATIENT_AGE,
  study.patient_size                 AS PATIENT_SIZE,
  study.patient_weight               AS PATIENT_WEIGHT,
  study.study_id                     AS STUDY_ID,
  study.study_time                   AS STUDY_TIME,
  study.trial_time_point_id          AS TRIAL_TIME_POINT_ID,
  study.trial_time_point_desc        AS TRIAL_TIME_POINT_DESC,
  study.patient_pk_id                AS PATIENT_PK_ID
FROM study
;

CREATE VIEW manufacturer AS 
SELECT DISTINCT general_equipment.manufacturer AS MANUFACTURER
FROM general_equipment
WHERE general_equipment.general_equipment_pk_id IN(SELECT general_series.general_equipment_pk_id AS GENERAL_EQUIPMENT_PK_ID
                                                   FROM general_series
                                                   WHERE general_series.visibility = '1')
;

CREATE VIEW manufacturer_model_software AS 
SELECT DISTINCT
  general_equipment.general_equipment_pk_id AS ID,
  general_equipment.manufacturer            AS MANUFACTURER,
  general_equipment.manufacturer_model_name AS MODEL,
  general_equipment.software_versions       AS SOFTWARE
FROM general_equipment
WHERE general_equipment.general_equipment_pk_id IN(SELECT general_series.general_equipment_pk_id     AS GENERAL_EQUIPMENT_PK_ID
                                                   FROM general_series
                                                   WHERE general_series.visibility = '1')
;


CREATE VIEW number_month AS 
SELECT PATIENT_PK_ID as PATIENT_ID, Round(months_between(max(study_date), min(study_date))) as NUMBER_MONTH
       FROM study
       GROUP BY patient_pk_ID
;

CREATE VIEW saved_query_last_exec AS 
SELECT
  qh.saved_query_pk_id AS SAVED_QUERY_PK_ID,
  MAX(qh.query_execute_timestamp) AS LAST_EXECUTE_DATE
FROM saved_query sq, query_history qh
WHERE qh.saved_query_pk_id = sq.saved_query_pk_id
GROUP BY qh.saved_query_pk_id
;

CREATE VIEW software_versions AS 
SELECT DISTINCT
  general_equipment.software_versions AS SOFTWARE_VERSIONS
FROM general_equipment
WHERE general_equipment.general_equipment_pk_id IN(SELECT general_series.general_equipment_pk_id AS GENERAL_EQUIPMENT_PK_ID
                                                   FROM general_series
                                                   WHERE (general_series.visibility = '1') AND (general_equipment.software_versions IS NOT NULL))
;

CREATE VIEW study_series_number AS 
SELECT
  g.patient_pk_id AS PATIENT_PK_ID,
  p.patient_id    AS PATIENT_ID,
  dp.project      AS PROJECT,
  COUNT(DISTINCT g.study_pk_id) AS STUDY_NUMBER,
  COUNT(DISTINCT g.general_series_pk_id) AS SERIES_NUMBER
FROM general_series g, patient p, trial_data_provenance dp
WHERE g.visibility = '1' AND 
      g.patient_pk_id = p.patient_pk_id AND 
      p.trial_dp_pk_id = dp.trial_dp_pk_id
GROUP BY g.patient_pk_id,p.patient_id,dp.project
;
