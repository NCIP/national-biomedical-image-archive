/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

CREATE TABLE saved_query_attribute (
  saved_query_attribute_pk_id NUMBER(24,0) NOT NULL,
  attribute_name VARCHAR2(300 CHAR),
  subattribute_name VARCHAR2(300 CHAR),
  attribute_value VARCHAR2(300 CHAR),
  saved_query_pk_id NUMBER(24,0),
  instance_number NUMBER(24,0)
)
;



ALTER TABLE saved_query_attribute
ADD CONSTRAINT PRIMARY_13 PRIMARY KEY
(
  saved_query_attribute_pk_id
)
ENABLE
;

CREATE INDEX saved_query_attr_parent ON saved_query_attribute
(
  saved_query_pk_id
) 
;





CREATE TABLE study (
  study_pk_id NUMBER(24,0) NOT NULL,
  study_instance_uid VARCHAR2(500 CHAR),
  study_date DATE NOT NULL,
  study_time VARCHAR2(16 CHAR),
  study_desc VARCHAR2(64 CHAR),
  admitting_diagnoses_desc VARCHAR2(64 CHAR),
  admitting_diagnoses_code_seq VARCHAR2(500 CHAR),
  patient_pk_id NUMBER(24,0),
  study_id VARCHAR2(16 CHAR),
  trial_time_point_id VARCHAR2(64 CHAR),
  trial_time_point_desc VARCHAR2(1024 CHAR),
  patient_age VARCHAR2(4 CHAR),
  age_group VARCHAR2(10 CHAR),
  patient_size FLOAT,
  patient_weight FLOAT,
  occupation VARCHAR2(16 CHAR),
  additional_patient_history VARCHAR2(4000 CHAR)
)
;



ALTER TABLE study
ADD CONSTRAINT PRIMARY_14 PRIMARY KEY
(
  study_pk_id
)
ENABLE
;

CREATE UNIQUE INDEX study_instance_uid ON study
(
  study_instance_uid
) 
;

CREATE INDEX study_desc_idx ON study
(
  study_desc
) 
;

CREATE INDEX fk_patient_pk_id ON study
(
  patient_pk_id
) 
;





CREATE TABLE submission_history (
  submission_history_pk_id NUMBER(24,0) NOT NULL,
  patient_id VARCHAR2(64 CHAR),
  study_instance_uid VARCHAR2(500 CHAR),
  series_instance_uid VARCHAR2(64 CHAR),
  sop_instance_uid VARCHAR2(64 CHAR),
  submission_timestamp DATE,
  project VARCHAR2(200 CHAR),
  site VARCHAR2(40 CHAR),
  operation_type NUMBER(5,0) NOT NULL
)
;



CREATE INDEX submission_history_pk_id ON submission_history
(
  submission_history_pk_id
) 
;

CREATE INDEX project_side_idx ON submission_history
(
  project,
  site
) 
;

CREATE INDEX series_instance_uid_idx ON submission_history
(
  series_instance_uid
) 
;





CREATE TABLE trial_data_provenance (
  trial_dp_pk_id NUMBER(24,0) NOT NULL,
  dp_site_name VARCHAR2(40 CHAR),
  dp_site_id VARCHAR2(64 CHAR),
  project VARCHAR2(50 CHAR)
)
;



ALTER TABLE trial_data_provenance
ADD CONSTRAINT PRIMARY_15 PRIMARY KEY
(
  trial_dp_pk_id
)
ENABLE
;

CREATE INDEX siteNameIndex ON trial_data_provenance
(
  dp_site_name
) 
;

CREATE INDEX projectIndex ON trial_data_provenance
(
  project
) 
;





CREATE TABLE trial_site (
  trial_site_pk_id NUMBER(24,0) NOT NULL,
  trial_site_id VARCHAR2(64 CHAR),
  trial_site_name VARCHAR2(64 CHAR),
  trial_pk_id NUMBER(24,0)
)
;



ALTER TABLE trial_site
ADD CONSTRAINT PRIMARY_16 PRIMARY KEY
(
  trial_site_pk_id
)
ENABLE
;

CREATE INDEX fk_trial_pk_id ON trial_site
(
  trial_pk_id
) 
;





CREATE TABLE annotation (
  file_path VARCHAR2(300 CHAR),
  file_size NUMBER(24,0),
  annotation_type VARCHAR2(30 CHAR),
  series_instance_uid VARCHAR2(300 CHAR),
  general_series_pk_id NUMBER(24,0),
  annotations_pk_id NUMBER(24,0) NOT NULL,
  submission_date DATE
)
;



ALTER TABLE annotation
ADD CONSTRAINT PRIMARY_45 PRIMARY KEY
(
  annotations_pk_id
)
ENABLE
;

CREATE INDEX fk_ann_gs_pk_id ON annotation
(
  general_series_pk_id
) 
;

CREATE INDEX annotation_file_size ON annotation
(
  file_size
) 
;

CREATE INDEX annotations_submitted_date_idx ON annotation
(
  submission_date
) 
;





CREATE TABLE clinical_trial_sponsor (
  id NUMBER(24,0) NOT NULL,
  coordinating_center VARCHAR2(255 CHAR) NOT NULL,
  sponsor_name VARCHAR2(255 CHAR) NOT NULL
)
;



ALTER TABLE clinical_trial_sponsor
ADD CONSTRAINT PRIMARY_46 PRIMARY KEY
(
  id
)
ENABLE
;





CREATE TABLE clinical_trial (
  trial_pk_id NUMBER(24,0) NOT NULL,
  trial_sponsor_name VARCHAR2(64 CHAR),
  trial_protocol_id VARCHAR2(64 CHAR),
  trial_protocol_name VARCHAR2(64 CHAR),
  trial_coordinating_center VARCHAR2(64 CHAR)
)
;



ALTER TABLE clinical_trial
ADD CONSTRAINT PRIMARY_17 PRIMARY KEY
(
  trial_pk_id
)
ENABLE
;





CREATE TABLE clinical_trial_protocol (
  id NUMBER(24,0) NOT NULL,
  protocol_id VARCHAR2(255 CHAR) NOT NULL,
  protocol_name VARCHAR2(1000 CHAR) NOT NULL,
  clinical_trial_sponsor_id NUMBER(24,0) NOT NULL
)
;



ALTER TABLE clinical_trial_protocol
ADD CONSTRAINT PRIMARY_18 PRIMARY KEY
(
  id
)
ENABLE
;

CREATE INDEX fk_clin_trial_sponsor_id ON clinical_trial_protocol
(
  clinical_trial_sponsor_id
) 
;





CREATE TABLE clinical_trial_subject (
  id NUMBER(24,0) NOT NULL,
  trial_subject_id VARCHAR2(255 CHAR) NOT NULL,
  trial_subject_reading_id VARCHAR2(255 CHAR) NOT NULL,
  clinical_trial_protocol_id NUMBER(24,0) NOT NULL,
  patient_id NUMBER(24,0) NOT NULL,
  trial_site_id NUMBER(24,0) NOT NULL
)
;



ALTER TABLE clinical_trial_subject
ADD CONSTRAINT PRIMARY_19 PRIMARY KEY
(
  id
)
ENABLE
;

CREATE INDEX fk_clinical_trial_protocol_id ON clinical_trial_subject
(
  clinical_trial_protocol_id
) 
;

CREATE INDEX fk_patient_id ON clinical_trial_subject
(
  patient_id
) 
;

CREATE INDEX fk_trial_site ON clinical_trial_subject
(
  trial_site_id
) 
;





CREATE TABLE collection_descriptions (
  collection_descriptions_pk_id NUMBER(24,0) NOT NULL,
  collection_name VARCHAR2(64 CHAR),
  user_name VARCHAR2(20 CHAR),
  description CLOB,
  collection_descriptions_timest DATE
)
;


COMMENT ON COLUMN collection_descriptions.collection_descriptions_timest IS 'ORIGINAL NAME:collection_descriptions_timestamp'
;


ALTER TABLE collection_descriptions
ADD CONSTRAINT PRIMARY_20 PRIMARY KEY
(
  collection_descriptions_pk_id
)
ENABLE
;





CREATE TABLE csm_application (
  APPLICATION_ID NUMBER(24,0) NOT NULL,
  APPLICATION_NAME VARCHAR2(255 CHAR) NOT NULL,
  APPLICATION_DESCRIPTION VARCHAR2(200 CHAR) NOT NULL,
  DECLARATIVE_FLAG NUMBER(3,0) DEFAULT '0' NOT NULL,
  ACTIVE_FLAG NUMBER(3,0) DEFAULT '0' NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL,
  DATABASE_URL VARCHAR2(100 CHAR),
  DATABASE_USER_NAME VARCHAR2(100 CHAR),
  DATABASE_PASSWORD VARCHAR2(100 CHAR),
  DATABASE_DIALECT VARCHAR2(100 CHAR),
  DATABASE_DRIVER VARCHAR2(100 CHAR)
)
;



ALTER TABLE csm_application
ADD CONSTRAINT PRIMARY_21 PRIMARY KEY
(
  APPLICATION_ID
)
ENABLE
;

CREATE UNIQUE INDEX UQ_APPLICATION_NAME ON csm_application
(
  APPLICATION_NAME
) 
;





CREATE TABLE csm_filter_clause (
  FILTER_CLAUSE_ID NUMBER(24,0) NOT NULL,
  CLASS_NAME VARCHAR2(100 CHAR) NOT NULL,
  FILTER_CHAIN VARCHAR2(2000 CHAR) NOT NULL,
  TARGET_CLASS_NAME VARCHAR2(100 CHAR) NOT NULL,
  TARGET_CLASS_ATTRIBUTE_NAME VARCHAR2(100 CHAR) NOT NULL,
  TARGET_CLASS_ATTRIBUTE_TYPE VARCHAR2(100 CHAR) NOT NULL,
  TARGET_CLASS_ALIAS VARCHAR2(100 CHAR),
  TARGET_CLASS_ATTRIBUTE_ALIAS VARCHAR2(100 CHAR),
  GENERATED_SQL VARCHAR2(4000 CHAR) NOT NULL,
  APPLICATION_ID NUMBER(24,0) NOT NULL,
  UPDATE_DATE DATE NOT NULL
)
;



ALTER TABLE csm_filter_clause
ADD CONSTRAINT PRIMARY_23 PRIMARY KEY
(
  FILTER_CLAUSE_ID
)
ENABLE
;





CREATE TABLE csm_group (
  GROUP_ID NUMBER(24,0) NOT NULL,
  GROUP_NAME VARCHAR2(255 CHAR) NOT NULL,
  GROUP_DESC VARCHAR2(200 CHAR),
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL,
  APPLICATION_ID NUMBER(24,0) NOT NULL
)
;




ALTER TABLE csm_group
ADD CONSTRAINT PRIMARY_24 PRIMARY KEY
(
  GROUP_ID
)
ENABLE
;

CREATE INDEX UQ_GROUP_GROUP_NAME ON csm_group
(
  APPLICATION_ID,
  GROUP_NAME
) 
;

CREATE INDEX idx_APPLICATION_ID_1 ON csm_group
(
  APPLICATION_ID
) 
;





CREATE TABLE csm_pg_pe (
  PG_PE_ID NUMBER(24,0) NOT NULL,
  PROTECTION_GROUP_ID NUMBER(24,0) NOT NULL,
  PROTECTION_ELEMENT_ID NUMBER(24,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL
)
;



ALTER TABLE csm_pg_pe
ADD CONSTRAINT PRIMARY_22 PRIMARY KEY
(
  PG_PE_ID
)
ENABLE
;

CREATE INDEX UQ_PROTECTION_GROUP_PROTECTI_1 ON csm_pg_pe
(
  PROTECTION_ELEMENT_ID,
  PROTECTION_GROUP_ID
) 
;

CREATE INDEX idx_PROTECTION_ELEMENT_ID_1 ON csm_pg_pe
(
  PROTECTION_ELEMENT_ID
) 
;

CREATE INDEX idx_PROTECTION_GROUP_ID_1 ON csm_pg_pe
(
  PROTECTION_GROUP_ID
) 
;





CREATE TABLE csm_privilege (
  PRIVILEGE_ID NUMBER(24,0) NOT NULL,
  PRIVILEGE_NAME VARCHAR2(100 CHAR) NOT NULL,
  PRIVILEGE_DESCRIPTION VARCHAR2(200 CHAR),
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL
)
;



ALTER TABLE csm_privilege
ADD CONSTRAINT PRIMARY_25 PRIMARY KEY
(
  PRIVILEGE_ID
)
ENABLE
;

CREATE UNIQUE INDEX UQ_PRIVILEGE_NAME ON csm_privilege
(
  PRIVILEGE_NAME
) 
;





CREATE TABLE csm_protection_element (
  PROTECTION_ELEMENT_ID NUMBER(24,0) NOT NULL,
  PROTECTION_ELEMENT_NAME VARCHAR2(100 CHAR) NOT NULL,
  PROTECTION_ELEMENT_DESCRIPTION VARCHAR2(200 CHAR),
  OBJECT_ID VARCHAR2(100 CHAR) NOT NULL,
  ATTRIBUTE VARCHAR2(100 CHAR),
  PROTECTION_ELEMENT_TYPE VARCHAR2(100 CHAR),
  APPLICATION_ID NUMBER(24,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL,
  ATTRIBUTE_VALUE VARCHAR2(100 CHAR)
)
;



ALTER TABLE csm_protection_element
ADD CONSTRAINT PRIMARY_26 PRIMARY KEY
(
  PROTECTION_ELEMENT_ID
)
ENABLE
;

CREATE INDEX UQ_PE_PE_NAME_ATTRIBUTE_VALUE_ ON csm_protection_element
(
  OBJECT_ID,
  ATTRIBUTE,
  ATTRIBUTE_VALUE,
  APPLICATION_ID
) 
;

CREATE INDEX idx_APPLICATION_ID_2 ON csm_protection_element
(
  APPLICATION_ID
) 
;





CREATE TABLE csm_protection_group (
  PROTECTION_GROUP_ID NUMBER(24,0) NOT NULL,
  PROTECTION_GROUP_NAME VARCHAR2(100 CHAR) NOT NULL,
  PROTECTION_GROUP_DESCRIPTION VARCHAR2(200 CHAR),
  APPLICATION_ID NUMBER(24,0) NOT NULL,
  LARGE_ELEMENT_COUNT_FLAG NUMBER(3,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL,
  PARENT_PROTECTION_GROUP_ID NUMBER(24,0)
)
;



ALTER TABLE csm_protection_group
ADD CONSTRAINT PRIMARY_27 PRIMARY KEY
(
  PROTECTION_GROUP_ID
)
ENABLE
;

CREATE INDEX UQ_PROTECTION_GROUP_PROTECTION ON csm_protection_group
(
  APPLICATION_ID,
  PROTECTION_GROUP_NAME
) 
;

CREATE INDEX idx_APPLICATION_ID_3 ON csm_protection_group
(
  APPLICATION_ID
) 
;

CREATE INDEX idx_PARENT_PROTECTION_GROUP_ID ON csm_protection_group
(
  PARENT_PROTECTION_GROUP_ID
) 
;





CREATE TABLE csm_role (
  ROLE_ID NUMBER(24,0) NOT NULL,
  ROLE_NAME VARCHAR2(100 CHAR) NOT NULL,
  ROLE_DESCRIPTION VARCHAR2(200 CHAR),
  APPLICATION_ID NUMBER(24,0) NOT NULL,
  ACTIVE_FLAG NUMBER(3,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL
)
;



ALTER TABLE csm_role
ADD CONSTRAINT PRIMARY_28 PRIMARY KEY
(
  ROLE_ID
)
ENABLE
;

CREATE INDEX UQ_ROLE_ROLE_NAME ON csm_role
(
  APPLICATION_ID,
  ROLE_NAME
) 
;

CREATE INDEX idx_APPLICATION_ID ON csm_role
(
  APPLICATION_ID
) 
;





CREATE TABLE csm_role_privilege (
  ROLE_PRIVILEGE_ID NUMBER(24,0) NOT NULL,
  ROLE_ID NUMBER(24,0) NOT NULL,
  PRIVILEGE_ID NUMBER(24,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL
)
;



ALTER TABLE csm_role_privilege
ADD CONSTRAINT PRIMARY_29 PRIMARY KEY
(
  ROLE_PRIVILEGE_ID
)
ENABLE
;

CREATE INDEX UQ_ROLE_PRIVILEGE_ROLE_ID ON csm_role_privilege
(
  PRIVILEGE_ID,
  ROLE_ID
) 
;

CREATE INDEX idx_PRIVILEGE_ID ON csm_role_privilege
(
  PRIVILEGE_ID
) 
;

CREATE INDEX idx_ROLE_ID_1 ON csm_role_privilege
(
  ROLE_ID
) 
;





CREATE TABLE csm_user (
  USER_ID NUMBER(24,0) NOT NULL,
  LOGIN_NAME VARCHAR2(100 CHAR) NOT NULL,
  FIRST_NAME VARCHAR2(100 CHAR) NOT NULL,
  LAST_NAME VARCHAR2(100 CHAR) NOT NULL,
  ORGANIZATION VARCHAR2(100 CHAR),
  DEPARTMENT VARCHAR2(100 CHAR),
  TITLE VARCHAR2(100 CHAR),
  PHONE_NUMBER VARCHAR2(15 CHAR),
  PASSWORD VARCHAR2(100 CHAR),
  EMAIL_ID VARCHAR2(100 CHAR),
  START_DATE DATE,
  END_DATE DATE,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL,
  MIDDLE_NAME VARCHAR2(100 CHAR),
  FAX VARCHAR2(20 CHAR),
  ADDRESS VARCHAR2(200 CHAR),
  CITY VARCHAR2(100 CHAR),
  STATE VARCHAR2(100 CHAR),
  COUNTRY VARCHAR2(100 CHAR),
  POSTAL_CODE VARCHAR2(10 CHAR)
)
;



ALTER TABLE csm_user
ADD CONSTRAINT PRIMARY_30 PRIMARY KEY
(
  USER_ID
)
ENABLE
;





CREATE TABLE csm_user_group (
  USER_GROUP_ID NUMBER(24,0) NOT NULL,
  USER_ID NUMBER(24,0) NOT NULL,
  GROUP_ID NUMBER(24,0) NOT NULL
)
;



ALTER TABLE csm_user_group
ADD CONSTRAINT PRIMARY_31 PRIMARY KEY
(
  USER_GROUP_ID
)
ENABLE
;

CREATE INDEX idx_USER_ID_2 ON csm_user_group
(
  USER_ID
) 
;

CREATE INDEX idx_GROUP_ID_1 ON csm_user_group
(
  GROUP_ID
) 
;





CREATE TABLE csm_user_group_role_pg (
  USER_GROUP_ROLE_PG_ID NUMBER(24,0) NOT NULL,
  USER_ID NUMBER(24,0),
  GROUP_ID NUMBER(24,0),
  ROLE_ID NUMBER(24,0) NOT NULL,
  PROTECTION_GROUP_ID NUMBER(24,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL
)
;



ALTER TABLE csm_user_group_role_pg
ADD CONSTRAINT PRIMARY_32 PRIMARY KEY
(
  USER_GROUP_ROLE_PG_ID
)
ENABLE
;

CREATE INDEX idx_GROUP_ID ON csm_user_group_role_pg
(
  GROUP_ID
) 
;

CREATE INDEX idx_ROLE_ID ON csm_user_group_role_pg
(
  ROLE_ID
) 
;

CREATE INDEX idx_PROTECTION_GROUP_ID ON csm_user_group_role_pg
(
  PROTECTION_GROUP_ID
) 
;

CREATE INDEX idx_USER_ID_1 ON csm_user_group_role_pg
(
  USER_ID
) 
;





CREATE TABLE csm_user_pe (
  USER_PROTECTION_ELEMENT_ID NUMBER(24,0) NOT NULL,
  PROTECTION_ELEMENT_ID NUMBER(24,0) NOT NULL,
  USER_ID NUMBER(24,0) NOT NULL,
  UPDATE_DATE DATE DEFAULT '01-jan-1950' NOT NULL
)
;



ALTER TABLE csm_user_pe
ADD CONSTRAINT PRIMARY_33 PRIMARY KEY
(
  USER_PROTECTION_ELEMENT_ID
)
ENABLE
;

CREATE INDEX UQ_USER_PROTECTION_ELEMENT_PRO ON csm_user_pe
(
  USER_ID,
  PROTECTION_ELEMENT_ID
) 
;

CREATE INDEX idx_USER_ID ON csm_user_pe
(
  USER_ID
) 
;

CREATE INDEX idx_PROTECTION_ELEMENT_ID ON csm_user_pe
(
  PROTECTION_ELEMENT_ID
) 
;





CREATE TABLE ct_image (
  kvp FLOAT,
  scan_options VARCHAR2(16 CHAR),
  data_collection_diameter FLOAT,
  reconstruction_diameter FLOAT,
  gantry_detector_tilt FLOAT,
  exposure_time FLOAT,
  x_ray_tube_current FLOAT,
  exposure FLOAT,
  exposure_in_microas FLOAT,
  convolution_kernel VARCHAR2(16 CHAR),
  revolution_time FLOAT,
  single_collimation_width FLOAT,
  total_collimation_width FLOAT,
  table_speed FLOAT,
  table_feed_per_rotation FLOAT,
  ct_pitch_factor FLOAT,
  anatomic_region_seq VARCHAR2(500 CHAR),
  image_pk_id NUMBER(24,0) NOT NULL,
  ct_image_pk_id NUMBER(24,0) NOT NULL,
  general_series_pk_id NUMBER(24,0)
)
;



ALTER TABLE ct_image
ADD CONSTRAINT PRIMARY_34 PRIMARY KEY
(
  ct_image_pk_id
)
ENABLE
;

CREATE INDEX convolution_kernel_idx ON ct_image
(
  convolution_kernel
) 
;

CREATE INDEX ct_image_image_pk_id_indx ON ct_image
(
  image_pk_id
) 
;

CREATE INDEX kvp_idx ON ct_image
(
  kvp
) 
;

CREATE INDEX general_series_pk_id ON ct_image
(
  general_series_pk_id
) 
;




CREATE TABLE custom_series_list (
  CUSTOM_SERIES_LIST_PK_ID NUMBER(24,0) NOT NULL,
  NAME VARCHAR2(200 CHAR),
  COMMENT_ VARCHAR2(200 CHAR),
  HYPERLINK VARCHAR2(200 CHAR),
  CUSTOM_SERIES_LIST_TIMESTAMP DATE,
  USER_NAME VARCHAR2(20 CHAR)
)
;


COMMENT ON COLUMN custom_series_list.COMMENT_ IS 'ORIGINAL NAME:COMMENT'
;


ALTER TABLE custom_series_list
ADD CONSTRAINT PRIMARY_37 PRIMARY KEY
(
  CUSTOM_SERIES_LIST_PK_ID
)
ENABLE
;





CREATE TABLE custom_series_list_attribute (
  CUSTOM_SERIES_LIST_ATTRIBUTE_P NUMBER(24,0) NOT NULL,
  SERIES_INSTANCE_UID VARCHAR2(200 CHAR),
  CUSTOM_SERIES_LIST_PK_ID NUMBER(24,0) NOT NULL
)
;


COMMENT ON COLUMN custom_series_list_attribute.CUSTOM_SERIES_LIST_ATTRIBUTE_P IS 'ORIGINAL NAME:CUSTOM_SERIES_LIST_ATTRIBUTE_PK_ID'
;


ALTER TABLE custom_series_list_attribute
ADD CONSTRAINT PRIMARY_38 PRIMARY KEY
(
  CUSTOM_SERIES_LIST_ATTRIBUTE_P
)
ENABLE
;

CREATE INDEX series_instance_uid_idx_2 ON custom_series_list_attribute
(
  SERIES_INSTANCE_UID
) 
;

CREATE INDEX FK_CUSTOM_SERIES_LIST_PK_ID ON custom_series_list_attribute
(
  CUSTOM_SERIES_LIST_PK_ID
) 
;




CREATE TABLE deletion_audit_trail (
  deletion_pk_id NUMBER(24,0) NOT NULL,
  data_id VARCHAR2(64 CHAR),
  data_type VARCHAR2(50 CHAR),
  total_image NUMBER(24,0),
  user_name VARCHAR2(50 CHAR),
  time_stamp DATE
)
;



ALTER TABLE deletion_audit_trail
ADD CONSTRAINT PRIMARY_41 PRIMARY KEY
(
  deletion_pk_id
)
ENABLE
;





CREATE TABLE download_data_history (
  download_data_history_pk_id NUMBER(24,0) NOT NULL,
  series_instance_uid VARCHAR2(64 CHAR),
  login_name VARCHAR2(100 CHAR),
  download_timestamp DATE,
  download_type VARCHAR2(20 CHAR),
  size_ FLOAT
)
;


COMMENT ON COLUMN download_data_history.size_ IS 'ORIGINAL NAME:size'
;


ALTER TABLE download_data_history
ADD CONSTRAINT PRIMARY_42 PRIMARY KEY
(
  download_data_history_pk_id
)
ENABLE
;





CREATE TABLE download_history (
  download_history_pk_id NUMBER(24,0) NOT NULL,
  user_id NUMBER(24,0),
  timestamp DATE,
  total_file_size FLOAT,
  download_file_name VARCHAR2(300 CHAR)
)
;



ALTER TABLE download_history
ADD CONSTRAINT PRIMARY_43 PRIMARY KEY
(
  download_history_pk_id
)
ENABLE
;





CREATE TABLE general_equipment (
  general_equipment_pk_id NUMBER(24,0) NOT NULL,
  manufacturer VARCHAR2(64 CHAR),
  institution_name VARCHAR2(64 CHAR),
  manufacturer_model_name VARCHAR2(64 CHAR),
  software_versions VARCHAR2(64 CHAR),
  institution_address VARCHAR2(1024 CHAR),
  station_name VARCHAR2(16 CHAR),
  device_serial_number VARCHAR2(64 CHAR)
)
;



ALTER TABLE general_equipment
ADD CONSTRAINT PRIMARY_44 PRIMARY KEY
(
  general_equipment_pk_id
)
ENABLE
;

CREATE INDEX idx_manufacturer_model_name ON general_equipment
(
  manufacturer_model_name
) 
;

CREATE INDEX idx_software_versions ON general_equipment
(
  software_versions
) 
;

CREATE INDEX idx_manufacturer ON general_equipment
(
  manufacturer
) 
;





CREATE TABLE general_image (
  image_pk_id NUMBER(24,0) NOT NULL,
  instance_number NUMBER(24,0),
  content_date DATE,
  content_time VARCHAR2(16 CHAR),
  image_type VARCHAR2(16 CHAR),
  acquisition_date DATE,
  acquisition_time VARCHAR2(16 CHAR),
  acquisition_number FLOAT,
  lossy_image_compression VARCHAR2(16 CHAR),
  pixel_spacing FLOAT,
  image_orientation_patient VARCHAR2(200 CHAR),
  image_position_patient VARCHAR2(200 CHAR),
  slice_thickness FLOAT,
  slice_location FLOAT,
  i_rows FLOAT,
  i_columns FLOAT,
  contrast_bolus_agent VARCHAR2(64 CHAR),
  contrast_bolus_route VARCHAR2(64 CHAR),
  sop_class_uid VARCHAR2(64 CHAR),
  sop_instance_uid VARCHAR2(64 CHAR),
  general_series_pk_id NUMBER(24,0),
  patient_position VARCHAR2(16 CHAR),
  source_to_detector_distance FLOAT,
  source_subject_distance FLOAT,
  focal_spot_size FLOAT,
  storage_media_file_set_uid VARCHAR2(64 CHAR),
  dicom_file_uri VARCHAR2(2000 CHAR),
  acquisition_datetime VARCHAR2(50 CHAR),
  image_comments VARCHAR2(4000 CHAR),
  image_receiving_timestamp DATE,
  curation_timestamp DATE,
  annotation VARCHAR2(20 CHAR),
  submission_date DATE,
  dicom_size FLOAT,
  image_laterality VARCHAR2(16 CHAR),
  trial_dp_pk_id NUMBER(24,0),
  patient_id VARCHAR2(64 CHAR),
  study_instance_uid VARCHAR2(500 CHAR),
  series_instance_uid VARCHAR2(64 CHAR),
  patient_pk_id NUMBER(24,0),
  study_pk_id NUMBER(24,0),
  project VARCHAR2(200 CHAR),
  acquisition_matrix FLOAT DEFAULT '0.000000',
  dx_data_collection_diameter FLOAT DEFAULT '0.000000',
  md5_digest VARCHAR2(50 CHAR)
)
;



ALTER TABLE general_image
ADD CONSTRAINT PRIMARY_6 PRIMARY KEY
(
  image_pk_id
)
ENABLE
;

CREATE INDEX Image_Visibility_Submitted_IDX ON general_image
(
  image_pk_id,
  submission_date
) 
;

CREATE UNIQUE INDEX sop_instance_uid ON general_image
(
  sop_instance_uid
) 
;

CREATE INDEX acquisition_matrix_idx ON general_image
(
  acquisition_matrix
) 
;

CREATE INDEX contrast_bolus_route_idx ON general_image
(
  contrast_bolus_route
) 
;

CREATE INDEX curation_t_indx ON general_image
(
  curation_timestamp
) 
;

CREATE INDEX dx_data_collection_diameter ON general_image
(
  dx_data_collection_diameter
) 
;

CREATE INDEX general_image_search ON general_image
(
  general_series_pk_id,
  image_pk_id,
  slice_thickness,
  contrast_bolus_agent,
  curation_timestamp
) 
;

CREATE INDEX gi_gs_ds_indx ON general_image
(
  general_series_pk_id,
  dicom_size
) 
;

CREATE INDEX gi_ppkid_indx ON general_image
(
  patient_pk_id
) 
;

CREATE INDEX gi_spkid_indx ON general_image
(
  study_pk_id
) 
;

CREATE INDEX gi_tdpkid_indx ON general_image
(
  trial_dp_pk_id
) 
;

CREATE INDEX image_fk_series_pk_id ON general_image
(
  general_series_pk_id
) 
;

CREATE INDEX slice_thickness_idx ON general_image
(
  slice_thickness
) 
;

CREATE INDEX SubmittedDateIndex ON general_image
(
  submission_date
) 
;

CREATE INDEX series_instance_uid_idx_1 ON general_image
(
  series_instance_uid
) 
;

CREATE INDEX PATIENT_ID_IDX_1 ON general_image
(
  patient_id
) 
;

CREATE INDEX STUDY_INSTANCE_UID_IDX_46 ON general_image
(
  study_instance_uid
) 
;





CREATE TABLE general_series (
  general_series_pk_id NUMBER(24,0) NOT NULL,
  modality VARCHAR2(16 CHAR) NOT NULL,
  series_instance_uid VARCHAR2(64 CHAR),
  series_laterality VARCHAR2(16 CHAR),
  series_date DATE,
  protocol_name VARCHAR2(64 CHAR),
  series_desc VARCHAR2(64 CHAR),
  body_part_examined VARCHAR2(16 CHAR),
  study_pk_id NUMBER(24,0),
  general_equipment_pk_id NUMBER(24,0),
  trial_protocol_id VARCHAR2(64 CHAR),
  trial_protocol_name VARCHAR2(64 CHAR),
  trial_site_name VARCHAR2(64 CHAR),
  study_date DATE,
  study_desc VARCHAR2(64 CHAR),
  admitting_diagnoses_desc VARCHAR2(64 CHAR),
  patient_age VARCHAR2(4 CHAR),
  patient_sex VARCHAR2(16 CHAR),
  patient_weight FLOAT,
  age_group VARCHAR2(10 CHAR),
  patient_pk_id NUMBER(24,0),
  series_number FLOAT,
  sync_frame_of_ref_uid VARCHAR2(64 CHAR),
  patient_id VARCHAR2(64 CHAR),
  frame_of_reference_uid VARCHAR2(64 CHAR),
  visibility VARCHAR2(5 CHAR),
  security_group VARCHAR2(50 CHAR),
  annotations_flag VARCHAR2(5 CHAR),
  project VARCHAR2(200 CHAR),
  site VARCHAR2(40 CHAR),
  study_instance_uid VARCHAR2(500 CHAR),
  max_submission_timestamp DATE
)
;



ALTER TABLE general_series
ADD CONSTRAINT PRIMARY_11 PRIMARY KEY
(
  general_series_pk_id
)
ENABLE
;

CREATE UNIQUE INDEX SERIES_INSTANCE_UID_IDX_46 ON general_series
(
  series_instance_uid
) 
;

CREATE INDEX body_part_examined_idx ON general_series
(
  body_part_examined
) 
;

CREATE INDEX general_series_sec_grp_idx ON general_series
(
  security_group
) 
;

CREATE INDEX general_series_site_idx ON general_series
(
  trial_site_name
) 
;

CREATE INDEX modality_idx ON general_series
(
  modality
) 
;

CREATE INDEX series_desc_idx ON general_series
(
  series_desc
) 
;

CREATE INDEX series_visibility_ind ON general_series
(
  visibility
) 
;

CREATE INDEX fk_g_equipment_pk_id ON general_series
(
  general_equipment_pk_id
) 
;

CREATE INDEX patient_pk_id_idx ON general_series
(
  patient_pk_id
) 
;

CREATE INDEX fk_gs_study_pk_id ON general_series
(
  study_pk_id
) 
;






CREATE TABLE hibernate_unique_key (
  next_hi NUMBER(24,0) NOT NULL
)
;







CREATE TABLE image_markup (
  image_markup_pk_id NUMBER(24,0) NOT NULL,
  series_instance_uid VARCHAR2(64 CHAR),
  general_series_pk_id NUMBER(24,0),
  login_name VARCHAR2(300 CHAR),
  markup_content CLOB,
  submission_date DATE
)
;



ALTER TABLE image_markup
ADD CONSTRAINT PRIMARY_3 PRIMARY KEY
(
  image_markup_pk_id
)
ENABLE
;

CREATE INDEX FK_image_markup_series ON image_markup
(
  general_series_pk_id
) 
;





CREATE TABLE login_history (
  login_history_pk_id NUMBER(24,0) NOT NULL,
  login_timestamp DATE,
  ip_address VARCHAR2(15 CHAR)
)
;



ALTER TABLE login_history
ADD CONSTRAINT PRIMARY_4 PRIMARY KEY
(
  login_history_pk_id
)
ENABLE
;





CREATE TABLE patient (
  patient_pk_id NUMBER(24,0) NOT NULL,
  patient_id VARCHAR2(64 CHAR),
  patient_name VARCHAR2(250 CHAR),
  patient_birth_date DATE,
  patient_sex VARCHAR2(16 CHAR),
  ethnic_group VARCHAR2(16 CHAR),
  trial_dp_pk_id NUMBER(24,0),
  trial_subject_id VARCHAR2(64 CHAR),
  trial_subject_reading_id VARCHAR2(255 CHAR),
  trial_site_pk_id NUMBER(24,0)
)
;



ALTER TABLE patient
ADD CONSTRAINT PRIMARY_5 PRIMARY KEY
(
  patient_pk_id
)
ENABLE
;

CREATE INDEX fk_trial_dp_pk_id ON patient
(
  trial_dp_pk_id
) 
;

CREATE INDEX fk_trial_site_pk_id ON patient
(
  trial_site_pk_id
) 
;

CREATE INDEX Patient_Id_Idx ON patient
(
  patient_id
) 
;




CREATE TABLE qc_status_history (
  qc_status_history_pk_id NUMBER(24,0) NOT NULL,
  series_instance_uid VARCHAR2(64 CHAR) NOT NULL,
  user_id VARCHAR2(100 CHAR),
  comment_ VARCHAR2(4000 CHAR),
  history_timestamp DATE,
  new_value VARCHAR2(100 CHAR),
  old_value VARCHAR2(100 CHAR)
)
;


COMMENT ON COLUMN qc_status_history.comment_ IS 'ORIGINAL NAME:comment'
;


ALTER TABLE qc_status_history
ADD CONSTRAINT PRIMARY_8 PRIMARY KEY
(
  qc_status_history_pk_id
)
ENABLE
;

CREATE INDEX genseries_qcsts_id_idx ON qc_status_history
(
  series_instance_uid
) 
;





CREATE TABLE query_history (
  query_history_pk_id NUMBER(24,0) NOT NULL,
  user_id NUMBER(24,0),
  query_execute_timestamp DATE,
  query_elapsed_time NUMBER(24,0),
  saved_query_pk_id NUMBER(24,0)
)
;



ALTER TABLE query_history
ADD CONSTRAINT PRIMARY_9 PRIMARY KEY
(
  query_history_pk_id
)
ENABLE
;

CREATE INDEX query_history_user_idx ON query_history
(
  user_id
) 
;

CREATE INDEX query_hist_save_quer_idx ON query_history
(
  saved_query_pk_id
) 
;





CREATE TABLE query_history_attribute (
  query_history_attribute_pk_id NUMBER(24,0) NOT NULL,
  attribute_name VARCHAR2(300 CHAR),
  subattribute_name VARCHAR2(300 CHAR),
  attribute_value VARCHAR2(300 CHAR),
  query_history_pk_id NUMBER(24,0),
  instance_number NUMBER(24,0)
)
;



ALTER TABLE query_history_attribute
ADD CONSTRAINT PRIMARY_10 PRIMARY KEY
(
  query_history_attribute_pk_id
)
ENABLE
;

CREATE INDEX attri_name_idx ON query_history_attribute
(
  attribute_name
) 
;

CREATE INDEX attri_value_idx ON query_history_attribute
(
  attribute_value
) 
;

CREATE INDEX query_history_attr_parent ON query_history_attribute
(
  query_history_pk_id
) 
;





CREATE TABLE saved_query (
  saved_query_pk_id NUMBER(24,0) NOT NULL,
  user_id NUMBER(24,0),
  query_name VARCHAR2(200 CHAR),
  new_data_flag VARCHAR2(5 CHAR),
  active VARCHAR2(5 CHAR),
  query_execute_timestamp DATE
)
;



ALTER TABLE saved_query
ADD CONSTRAINT PRIMARY_12 PRIMARY KEY
(
  saved_query_pk_id
)
ENABLE
;

CREATE INDEX saved_query_user_idx ON saved_query
(
  user_id
) 
;

CREATE SEQUENCE CSM_APPLICATI_APPLICATION__SEQ
increment by 1
start with 3
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;

CREATE SEQUENCE CSM_GROUP_GROUP_ID_SEQ
increment by 1
start with 2
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;



CREATE SEQUENCE CSM_PG_PE_PG_PE_ID_SEQ
increment by 1
start with 1
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;



CREATE SEQUENCE CSM_PRIVILEGE_PRIVILEGE_ID_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;


CREATE SEQUENCE CSM_PROTECTIO_PROTECTION_E_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;



CREATE SEQUENCE CSM_PROTECTIO_PROTECTION_G_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;


CREATE SEQUENCE CSM_ROLE_ROLE_ID_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;


CREATE SEQUENCE CSM_ROLE_PRIV_ROLE_PRIVILE_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;

CREATE SEQUENCE CSM_USER_USER_ID_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;


CREATE SEQUENCE CSM_USER_GROU_USER_GROUP_I_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;

CREATE SEQUENCE CSM_USER_GROU_USER_GROUP_R_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;


CREATE SEQUENCE CSM_USER_PE_USER_PROTECTIO_SEQ
increment by 1
start with 10
NOMAXVALUE
minvalue 1
nocycle
nocache
noorder
;

CREATE OR REPLACE TRIGGER SET_CSM_PG_PE_PG_PE_ID
BEFORE INSERT
ON CSM_PG_PE
FOR EACH ROW
BEGIN
  SELECT CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL
  INTO :NEW.PG_PE_ID
  FROM DUAL;
END;
;


CREATE OR REPLACE TRIGGER SET_CSM_PG_PE_UPDATE_DATE
BEFORE INSERT
ON CSM_PG_PE
FOR EACH ROW
BEGIN
  SELECT SYSDATE
  INTO :NEW.UPDATE_DATE
  FROM DUAL;
END;
;


CREATE OR REPLACE TRIGGER SET_CSM_ROLE_PRIV_ROLE_PRIVILE
BEFORE INSERT
ON CSM_ROLE_PRIVILEGE
FOR EACH ROW
BEGIN
  SELECT CSM_ROLE_PRIV_ROLE_PRIVILE_SEQ.NEXTVAL
  INTO :NEW.ROLE_PRIVILEGE_ID
  FROM DUAL;
END;
;


CREATE OR REPLACE TRIGGER SET_CSM_ROLE_PRIV_UPDATE_DATE
BEFORE INSERT
ON CSM_ROLE_PRIVILEGE
FOR EACH ROW
BEGIN
  SELECT SYSDATE
  INTO :NEW.UPDATE_DATE
  FROM DUAL;
END;
;

CREATE OR REPLACE TRIGGER SET_CSM_USER_GROU_USER_GROUP_I
BEFORE INSERT
ON CSM_USER_GROUP
FOR EACH ROW
BEGIN
  SELECT CSM_USER_GROU_USER_GROUP_I_SEQ.NEXTVAL
  INTO :NEW.USER_GROUP_ID
  FROM DUAL;
END;
;


CREATE OR REPLACE TRIGGER SET_CSM_USER_PE_USER_PROTECTIO
BEFORE INSERT
ON CSM_USER_PE
FOR EACH ROW
BEGIN
  SELECT CSM_USER_PE_USER_PROTECTIO_SEQ.NEXTVAL
  INTO :NEW.USER_PROTECTION_ELEMENT_ID
  FROM DUAL;
END;
;


CREATE OR REPLACE TRIGGER SET_CSM_USER_PE_UPDATE_DATE
BEFORE INSERT
ON CSM_USER_PE
FOR EACH ROW
BEGIN
  SELECT SYSDATE
  INTO :NEW.UPDATE_DATE
  FROM DUAL;
END;
;