/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- MySQL dump 10.10
--
-- Host: localhost    Database: nciadev
-- ------------------------------------------------------
-- Server version	5.0.27-standard-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary table structure for view `all_logins`
--

DROP TABLE IF EXISTS `all_logins`;
/*!50001 DROP VIEW IF EXISTS `all_logins`*/;
/*!50001 CREATE TABLE `all_logins` (
  `LOGIN_TIMESTAMP` datetime,
  `IP_ADDRESS` varchar(15)
) */;


--
-- Table structure for table `annotation`
--

DROP TABLE IF EXISTS `annotation`;
CREATE TABLE `annotation` (
  `file_path` varchar(300) character set latin1 collate latin1_bin default NULL,
  `file_size` bigint(20) default NULL,
  `annotation_type` varchar(30) character set latin1 collate latin1_bin default NULL,
  `series_instance_uid` varchar(300) character set latin1 collate latin1_bin default NULL,
  `general_series_pk_id` bigint(20) default NULL,
  `annotations_pk_id` bigint(20) NOT NULL,
  `submission_date` datetime default NULL,
  PRIMARY KEY  (`annotations_pk_id`),
  UNIQUE KEY `PK_ANNOTATIONS_PK_ID` (`annotations_pk_id`),
  KEY `fk_ann_gs_pk_id` (`general_series_pk_id`),
  KEY `annotation_file_size` (`file_size`),
  KEY `annotations_submitted_date_idx` (`submission_date`),
  CONSTRAINT `fk_ann_gs_pk_id` FOREIGN KEY (`general_series_pk_id`) REFERENCES `general_series` (`general_series_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `build_control`
--

DROP TABLE IF EXISTS `build_control`;
CREATE TABLE `build_control` (
  `revision_number` int(11) NOT NULL,
  `build_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `clinical_trial`
--

DROP TABLE IF EXISTS `clinical_trial`;
CREATE TABLE `clinical_trial` (
  `trial_pk_id` bigint(20) NOT NULL,
  `trial_sponsor_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_protocol_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_protocol_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_coordinating_center` varchar(64) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`trial_pk_id`),
  UNIQUE KEY `PK_TRIAL_PK_ID` (`trial_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `clinical_trial_protocol`
--

DROP TABLE IF EXISTS `clinical_trial_protocol`;
CREATE TABLE `clinical_trial_protocol` (
  `id` bigint(20) NOT NULL,
  `protocol_id` varchar(255) character set latin1 collate latin1_bin NOT NULL,
  `protocol_name` varchar(1000) character set latin1 collate latin1_bin NOT NULL,
  `clinical_trial_sponsor_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `PK_C_TRIAL_PROTOCOL` (`id`),
  KEY `fk_clin_trial_sponsor_id` (`clinical_trial_sponsor_id`),
  CONSTRAINT `fk_clin_trial_sponsor_id` FOREIGN KEY (`clinical_trial_sponsor_id`) REFERENCES `clinical_trial_sponsor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `clinical_trial_sponsor`
--

DROP TABLE IF EXISTS `clinical_trial_sponsor`;
CREATE TABLE `clinical_trial_sponsor` (
  `id` bigint(20) NOT NULL,
  `coordinating_center` varchar(255) character set latin1 collate latin1_bin NOT NULL,
  `sponsor_name` varchar(255) character set latin1 collate latin1_bin NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `PK_C_TRIAL_SPONSOR` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `clinical_trial_subject`
--

DROP TABLE IF EXISTS `clinical_trial_subject`;
CREATE TABLE `clinical_trial_subject` (
  `id` bigint(20) NOT NULL,
  `trial_subject_id` varchar(255) character set latin1 collate latin1_bin NOT NULL,
  `trial_subject_reading_id` varchar(255) character set latin1 collate latin1_bin NOT NULL,
  `clinical_trial_protocol_id` bigint(20) NOT NULL,
  `patient_id` bigint(20) NOT NULL,
  `trial_site_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `PK_CLINICAL_TRIAL_SUBJECT` (`id`),
  KEY `fk_clinical_trial_protocol_id` (`clinical_trial_protocol_id`),
  KEY `fk_patient_id` (`patient_id`),
  KEY `fk_trial_site` (`trial_site_id`),
  CONSTRAINT `fk_clinical_trial_protocol_id` FOREIGN KEY (`clinical_trial_protocol_id`) REFERENCES `clinical_trial_protocol` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_trial_site` FOREIGN KEY (`trial_site_id`) REFERENCES `trial_site` (`trial_site_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `csm_application`
--

DROP TABLE IF EXISTS `csm_application`;
CREATE TABLE `csm_application` (
  `APPLICATION_ID` bigint(20) NOT NULL auto_increment,
  `APPLICATION_NAME` varchar(255) NOT NULL,
  `APPLICATION_DESCRIPTION` varchar(200) NOT NULL,
  `DECLARATIVE_FLAG` tinyint(1) NOT NULL default '0',
  `ACTIVE_FLAG` tinyint(1) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  `DATABASE_URL` varchar(100) default NULL,
  `DATABASE_USER_NAME` varchar(100) default NULL,
  `DATABASE_PASSWORD` varchar(100) default NULL,
  `DATABASE_DIALECT` varchar(100) default NULL,
  `DATABASE_DRIVER` varchar(100) default NULL,
  PRIMARY KEY  (`APPLICATION_ID`),
  UNIQUE KEY `UQ_APPLICATION_NAME` (`APPLICATION_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_group`
--

DROP TABLE IF EXISTS `csm_group`;
CREATE TABLE `csm_group` (
  `GROUP_ID` bigint(20) NOT NULL auto_increment,
  `GROUP_NAME` varchar(255) NOT NULL,
  `GROUP_DESC` varchar(200) default NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  `APPLICATION_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`GROUP_ID`),
  UNIQUE KEY `UQ_GROUP_GROUP_NAME` (`APPLICATION_ID`,`GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_GROUP` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_pg_pe`
--

DROP TABLE IF EXISTS `csm_pg_pe`;
CREATE TABLE `csm_pg_pe` (
  `PG_PE_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`PG_PE_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_ELEMENT_PROTECTION_GROUP_ID` (`PROTECTION_ELEMENT_ID`,`PROTECTION_GROUP_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PROTECTION_ELEMENT_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_GROUP_PROTECTION_ELEMENT` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_privilege`
--

DROP TABLE IF EXISTS `csm_privilege`;
CREATE TABLE `csm_privilege` (
  `PRIVILEGE_ID` bigint(20) NOT NULL auto_increment,
  `PRIVILEGE_NAME` varchar(100) NOT NULL,
  `PRIVILEGE_DESCRIPTION` varchar(200) default NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`PRIVILEGE_ID`),
  UNIQUE KEY `UQ_PRIVILEGE_NAME` (`PRIVILEGE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_protection_element`
--

DROP TABLE IF EXISTS `csm_protection_element`;
CREATE TABLE `csm_protection_element` (
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_ELEMENT_NAME` varchar(100) NOT NULL,
  `PROTECTION_ELEMENT_DESCRIPTION` varchar(200) default NULL,
  `OBJECT_ID` varchar(100) NOT NULL,
  `ATTRIBUTE` varchar(100) default NULL,
  `PROTECTION_ELEMENT_TYPE` varchar(100) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_PE_PE_NAME_ATTRIBUTE_APP_ID` (`OBJECT_ID`,`ATTRIBUTE`,`APPLICATION_ID`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_PE_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_protection_group`
--

DROP TABLE IF EXISTS `csm_protection_group`;
CREATE TABLE `csm_protection_group` (
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_GROUP_NAME` varchar(100) NOT NULL,
  `PROTECTION_GROUP_DESCRIPTION` varchar(200) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `LARGE_ELEMENT_COUNT_FLAG` tinyint(1) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  `PARENT_PROTECTION_GROUP_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PROTECTION_GROUP_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_GROUP_NAME` (`APPLICATION_ID`,`PROTECTION_GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  KEY `idx_PARENT_PROTECTION_GROUP_ID` (`PARENT_PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PG_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_GROUP` FOREIGN KEY (`PARENT_PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_role`
--

DROP TABLE IF EXISTS `csm_role`;
CREATE TABLE `csm_role` (
  `ROLE_ID` bigint(20) NOT NULL auto_increment,
  `ROLE_NAME` varchar(100) NOT NULL,
  `ROLE_DESCRIPTION` varchar(200) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `ACTIVE_FLAG` tinyint(1) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`ROLE_ID`),
  UNIQUE KEY `UQ_ROLE_ROLE_NAME` (`APPLICATION_ID`,`ROLE_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_ROLE` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_role_privilege`
--

DROP TABLE IF EXISTS `csm_role_privilege`;
CREATE TABLE `csm_role_privilege` (
  `ROLE_PRIVILEGE_ID` bigint(20) NOT NULL auto_increment,
  `ROLE_ID` bigint(20) NOT NULL,
  `PRIVILEGE_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`ROLE_PRIVILEGE_ID`),
  UNIQUE KEY `UQ_ROLE_PRIVILEGE_ROLE_ID` (`PRIVILEGE_ID`,`ROLE_ID`),
  KEY `idx_PRIVILEGE_ID` (`PRIVILEGE_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `FK_PRIVILEGE_ROLE` FOREIGN KEY (`PRIVILEGE_ID`) REFERENCES `csm_privilege` (`PRIVILEGE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_user`
--

DROP TABLE IF EXISTS `csm_user`;
CREATE TABLE `csm_user` (
  `USER_ID` bigint(20) NOT NULL auto_increment,
  `LOGIN_NAME` varchar(100) NOT NULL,
  `FIRST_NAME` varchar(100) NOT NULL,
  `LAST_NAME` varchar(100) NOT NULL,
  `ORGANIZATION` varchar(100) default NULL,
  `DEPARTMENT` varchar(100) default NULL,
  `TITLE` varchar(100) default NULL,
  `PHONE_NUMBER` varchar(15) default NULL,
  `PASSWORD` varchar(100) default NULL,
  `EMAIL_ID` varchar(100) default NULL,
  `START_DATE` date default NULL,
  `END_DATE` date default NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  `MIDDLE_NAME` varchar(100) default NULL,
  `FAX` varchar(20) default NULL,
  `ADDRESS` varchar(200) default NULL,
  `CITY` varchar(100) default NULL,
  `STATE` varchar(100) default NULL,
  `COUNTRY` varchar(100) default NULL,
  `POSTAL_CODE` varchar(10) default NULL,
  PRIMARY KEY  (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_user_group`
--

DROP TABLE IF EXISTS `csm_user_group`;
CREATE TABLE `csm_user_group` (
  `USER_GROUP_ID` bigint(20) NOT NULL auto_increment,
  `USER_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `FK_UG_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_user_group_role_pg`
--

DROP TABLE IF EXISTS `csm_user_group_role_pg`;
CREATE TABLE `csm_user_group_role_pg` (
  `USER_GROUP_ROLE_PG_ID` bigint(20) NOT NULL auto_increment,
  `USER_ID` bigint(20) default NULL,
  `GROUP_ID` bigint(20) default NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`USER_GROUP_ROLE_PG_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_GROUPS` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `csm_user_pe`
--

DROP TABLE IF EXISTS `csm_user_pe`;
CREATE TABLE `csm_user_pe` (
  `USER_PROTECTION_ELEMENT_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '1950-01-01',
  PRIMARY KEY  (`USER_PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_USER_PROTECTION_ELEMENT_PROTECTION_ELEMENT_ID` (`USER_ID`,`PROTECTION_ELEMENT_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  CONSTRAINT `FK_PE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_ELEMENT_USER` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ct_image`
--

DROP TABLE IF EXISTS `ct_image`;
CREATE TABLE `ct_image` (
  `kvp` decimal(22,6) default NULL,
  `scan_options` varchar(16) character set latin1 collate latin1_bin default NULL,
  `data_collection_diameter` decimal(22,6) default NULL,
  `reconstruction_diameter` decimal(22,6) default NULL,
  `gantry_detector_tilt` decimal(22,6) default NULL,
  `exposure_time` decimal(22,6) default NULL,
  `x_ray_tube_current` decimal(22,6) default NULL,
  `exposure` decimal(22,6) default NULL,
  `exposure_in_microas` decimal(22,6) default NULL,
  `convolution_kernel` varchar(16) character set latin1 collate latin1_bin default NULL,
  `revolution_time` decimal(22,6) default NULL,
  `single_collimation_width` decimal(22,6) default NULL,
  `total_collimation_width` decimal(22,6) default NULL,
  `table_speed` decimal(22,6) default NULL,
  `table_feed_per_rotation` decimal(22,6) default NULL,
  `ct_pitch_factor` decimal(22,6) default NULL,
  `anatomic_region_seq` varchar(500) character set latin1 collate latin1_bin default NULL,
  `image_pk_id` bigint(20) NOT NULL,
  `ct_image_pk_id` bigint(20) NOT NULL,
  `visibility` varchar(5) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`ct_image_pk_id`),
  UNIQUE KEY `PK_CT_IMAGE_PK_ID` (`ct_image_pk_id`),
  KEY `convolution_kernel_idx` (`convolution_kernel`),
  KEY `ct_image_image_pk_id_indx` (`image_pk_id`),
  KEY `kvp_idx` (`kvp`),
  KEY `visibility_ct_image` (`visibility`),
  CONSTRAINT `fk_image_pk_id` FOREIGN KEY (`image_pk_id`) REFERENCES `general_image` (`image_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `curation_data`
--

DROP TABLE IF EXISTS `curation_data`;
CREATE TABLE `curation_data` (
  `curation_data_pk_id` bigint(20) NOT NULL,
  `cde_name` varchar(100) character set latin1 collate latin1_bin default NULL,
  `cde_value` varchar(4000) character set latin1 collate latin1_bin default NULL,
  `cde_public_id` varchar(50) character set latin1 collate latin1_bin default NULL,
  `cde_public_id_version` varchar(50) character set latin1 collate latin1_bin default NULL,
  `curation_timestamp` datetime default NULL,
  `user_id` bigint(20) default NULL,
  `trial_name` varchar(100) character set latin1 collate latin1_bin default NULL,
  `patient_pk_id` bigint(20) default NULL,
  `study_pk_id` bigint(20) default NULL,
  `general_series_pk_id` bigint(20) default NULL,
  `image_pk_id` bigint(20) default NULL,
  `annotations_pk_id` bigint(20) default NULL,
  `is_number` tinyint(1) NOT NULL,
  PRIMARY KEY  (`curation_data_pk_id`),
  UNIQUE KEY `PK_C_DATA_PK_ID` (`curation_data_pk_id`),
  KEY `curation_data_idx` (`cde_public_id`,`cde_value`(500),`general_series_pk_id`),
  KEY `genimg_curdta_id_idx` (`image_pk_id`),
  KEY `annotations_pk_id_cur_data_fk` (`annotations_pk_id`),
  KEY `patient_pk_id_cur_data_fk` (`patient_pk_id`),
  KEY `series_pk_id_cur_data_fk` (`general_series_pk_id`),
  KEY `study_pk_id_cur_data_fk` (`study_pk_id`),
  CONSTRAINT `annotations_pk_id_cur_data_fk` FOREIGN KEY (`annotations_pk_id`) REFERENCES `annotation` (`annotations_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `image_pk_id_cur_data_fk` FOREIGN KEY (`image_pk_id`) REFERENCES `general_image` (`image_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `patient_pk_id_cur_data_fk` FOREIGN KEY (`patient_pk_id`) REFERENCES `patient` (`patient_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `series_pk_id_cur_data_fk` FOREIGN KEY (`general_series_pk_id`) REFERENCES `general_series` (`general_series_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `study_pk_id_cur_data_fk` FOREIGN KEY (`study_pk_id`) REFERENCES `study` (`study_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `curation_status`
--

DROP TABLE IF EXISTS `curation_status`;
CREATE TABLE `curation_status` (
  `curation_pk_id` bigint(20) NOT NULL,
  `sop_instance_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `curation_status` varchar(20) character set latin1 collate latin1_bin default NULL,
  `visibility` varchar(15) character set latin1 collate latin1_bin default NULL,
  `curator_name` varchar(40) character set latin1 collate latin1_bin default NULL,
  `curation_timestamp` datetime default NULL,
  `comments` varchar(1000) character set latin1 collate latin1_bin default NULL,
  `file_load_timestamp` datetime default NULL,
  `db_update_status` bigint(20) default NULL,
  `db_update_timestamp` datetime default NULL,
  `patient_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `study_instance_uid` varchar(500) character set latin1 collate latin1_bin default NULL,
  `series_instance_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`curation_pk_id`),
  UNIQUE KEY `PK_CURATION_PK_ID` (`curation_pk_id`),
  KEY `curstatus_sop_instance_uid` (`sop_instance_uid`),
  KEY `db_update_status_idx` (`db_update_status`),
  KEY `db_update_timestamp_idx` (`db_update_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `dicom_tag_info`
--

DROP TABLE IF EXISTS `dicom_tag_info`;
CREATE TABLE `dicom_tag_info` (
  `dicom_tag_info_pk_id` bigint(20) NOT NULL,
  `group_num` varchar(4) character set latin1 collate latin1_bin default NULL,
  `tag_num` varchar(4) character set latin1 collate latin1_bin default NULL,
  `tag_name` varchar(100) character set latin1 collate latin1_bin default NULL,
  `table_name` varchar(30) character set latin1 collate latin1_bin default NULL,
  `column_name` varchar(30) character set latin1 collate latin1_bin default NULL,
  `cde_public_id` varchar(30) character set latin1 collate latin1_bin default NULL,
  `cde_public_id_version` varchar(20) character set latin1 collate latin1_bin default NULL,
  `description` varchar(4000) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`dicom_tag_info_pk_id`),
  UNIQUE KEY `PK_D_TAG_INFO_PK_ID` (`dicom_tag_info_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `doc_location`
--

DROP TABLE IF EXISTS `doc_location`;
CREATE TABLE `doc_location` (
  `doc_location_pk_id` bigint(20) NOT NULL,
  `doc_directory` varchar(2000) character set latin1 collate latin1_bin default NULL,
  `doc_name` varchar(500) character set latin1 collate latin1_bin default NULL,
  `doc_author` varchar(100) character set latin1 collate latin1_bin default NULL,
  `doc_desc` varchar(2000) character set latin1 collate latin1_bin default NULL,
  `doc_type` varchar(100) character set latin1 collate latin1_bin default NULL,
  `title` varchar(100) character set latin1 collate latin1_bin default NULL,
  `study_instance_uid` varchar(500) character set latin1 collate latin1_bin default NULL,
  `study_pk_id` bigint(20) default NULL,
  `query_xml_text` varchar(4000) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`doc_location_pk_id`),
  KEY `fk_study_pk_id` (`study_pk_id`),
  CONSTRAINT `fk_study_pk_id` FOREIGN KEY (`study_pk_id`) REFERENCES `study` (`study_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `download_history`
--

DROP TABLE IF EXISTS `download_history`;
CREATE TABLE `download_history` (
  `download_history_pk_id` bigint(20) NOT NULL,
  `user_id` bigint(20) default NULL,
  `timestamp` datetime default NULL,
  `total_file_size` decimal(22,2) default NULL,
  `download_file_name` varchar(300) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`download_history_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `general_equipment`
--

DROP TABLE IF EXISTS `general_equipment`;
CREATE TABLE `general_equipment` (
  `general_equipment_pk_id` bigint(20) NOT NULL,
  `manufacturer` varchar(64) character set latin1 collate latin1_bin default NULL,
  `institution_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `manufacturer_model_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `software_versions` varchar(64) character set latin1 collate latin1_bin default NULL,
  `institution_address` varchar(1024) character set latin1 collate latin1_bin default NULL,
  `station_name` varchar(16) character set latin1 collate latin1_bin default NULL,
  `device_serial_number` varchar(64) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`general_equipment_pk_id`),
  UNIQUE KEY `PK_G_EQUIPMENT_ID` (`general_equipment_pk_id`),
  KEY `idx_manufacturer_model_name` (`manufacturer_model_name`),
  KEY `idx_software_versions` (`software_versions`),
  KEY `idx_manufacturer` (`manufacturer`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `general_image`
--

DROP TABLE IF EXISTS `general_image`;
CREATE TABLE `general_image` (
  `image_pk_id` bigint(20) NOT NULL,
  `instance_number` bigint(20) default NULL,
  `content_date` date default NULL,
  `content_time` varchar(16) character set latin1 collate latin1_bin default NULL,
  `image_type` varchar(16) character set latin1 collate latin1_bin default NULL,
  `acquisition_date` date default NULL,
  `acquisition_time` varchar(16) character set latin1 collate latin1_bin default NULL,
  `acquisition_number` decimal(22,6) default NULL,
  `lossy_image_compression` varchar(16) character set latin1 collate latin1_bin default NULL,
  `pixel_spacing` decimal(22,6) default NULL,
  `image_orientation_patient` varchar(200) character set latin1 collate latin1_bin default NULL,
  `image_position_patient` varchar(200) character set latin1 collate latin1_bin default NULL,
  `slice_thickness` decimal(22,6) default NULL,
  `slice_location` decimal(22,6) default NULL,
  `i_rows` decimal(22,6) default NULL,
  `i_columns` decimal(22,6) default NULL,
  `contrast_bolus_agent` varchar(64) character set latin1 collate latin1_bin default NULL,
  `contrast_bolus_route` varchar(64) character set latin1 collate latin1_bin default NULL,
  `sop_class_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `sop_instance_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `general_series_pk_id` bigint(20) default NULL,
  `patient_position` varchar(16) character set latin1 collate latin1_bin default NULL,
  `source_to_detector_distance` decimal(22,6) default NULL,
  `source_subject_distance` decimal(22,6) default NULL,
  `focal_spot_size` decimal(22,6) default NULL,
  `storage_media_file_set_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `mirc_doc_uri` varchar(2000) character set latin1 collate latin1_bin default NULL,
  `dicom_file_uri` varchar(2000) character set latin1 collate latin1_bin default NULL,
  `acquisition_datetime` varchar(50) character set latin1 collate latin1_bin default NULL,
  `image_comments` varchar(4000) character set latin1 collate latin1_bin default NULL,
  `image_receiving_timestamp` datetime default NULL,
  `curation_status` varchar(20) character set latin1 collate latin1_bin default NULL,
  `curation_timestamp` datetime default NULL,
  `visibility` varchar(5) character set latin1 collate latin1_bin default NULL,
  `annotation` varchar(20) character set latin1 collate latin1_bin default NULL,
  `submission_date` date default NULL,
  `dicom_size` decimal(22,6) default NULL,
  `image_laterality` varchar(16) character set latin1 collate latin1_bin default NULL,
  `trial_dp_pk_id` bigint(20) default NULL,
  `patient_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `study_instance_uid` varchar(500) character set latin1 collate latin1_bin default NULL,
  `series_instance_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `patient_pk_id` bigint(20) default NULL,
  `study_pk_id` bigint(20) default NULL,
  `project` varchar(200) character set latin1 collate latin1_bin default NULL,
  `version` bigint(20) default '0',
  `acquisition_matrix` decimal(22,6) default '0.000000',
  `dx_data_collection_diameter` decimal(22,6) default '0.000000',
  PRIMARY KEY  (`image_pk_id`),
  UNIQUE KEY `PK_IMAGE_PK_ID` (`image_pk_id`),
  UNIQUE KEY `Image_Visibility_Submitted_IDX` (`image_pk_id`,`visibility`(1),`submission_date`),
  KEY `acquisition_matrix_idx` (`acquisition_matrix`),
  KEY `contrast_bolus_route_idx` (`contrast_bolus_route`),
  KEY `curation_t_indx` (`curation_timestamp`),
  KEY `dx_data_collection_diameter` (`dx_data_collection_diameter`),
  KEY `general_image_search` (`general_series_pk_id`,`image_pk_id`,`slice_thickness`,`contrast_bolus_agent`,`visibility`,`curation_timestamp`),
  KEY `gi_gs_ds_indx` (`general_series_pk_id`,`dicom_size`,`visibility`),
  KEY `gi_ppkid_indx` (`patient_pk_id`),
  KEY `gi_spkid_indx` (`study_pk_id`),
  KEY `gi_tdpkid_indx` (`trial_dp_pk_id`),
  KEY `image_fk_series_pk_id` (`general_series_pk_id`),
  KEY `image_sop_instance_uid` (`sop_instance_uid`),
  KEY `slice_thickness_idx` (`slice_thickness`),
  KEY `SubmittedDateIndex` (`submission_date`),
  KEY `image_visibility_ind` (`visibility`(1)),
  KEY `idx_visibility_submitted_date` (`visibility`(1),`submission_date`),
  CONSTRAINT `FK_general_image_patient` FOREIGN KEY (`patient_pk_id`) REFERENCES `patient` (`patient_pk_id`),
  CONSTRAINT `FK_general_image_study` FOREIGN KEY (`study_pk_id`) REFERENCES `study` (`study_pk_id`),
  CONSTRAINT `FK_general_image_trialdata_prov` FOREIGN KEY (`trial_dp_pk_id`) REFERENCES `trial_data_provenance` (`trial_dp_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_g_series_pk_id` FOREIGN KEY (`general_series_pk_id`) REFERENCES `general_series` (`general_series_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `general_series`
--

DROP TABLE IF EXISTS `general_series`;
CREATE TABLE `general_series` (
  `general_series_pk_id` bigint(20) NOT NULL,
  `modality` varchar(16) character set latin1 collate latin1_bin default NULL,
  `series_instance_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `series_laterality` varchar(16) character set latin1 collate latin1_bin default NULL,
  `series_date` date default NULL,
  `protocol_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `series_desc` varchar(64) character set latin1 collate latin1_bin default NULL,
  `body_part_examined` varchar(16) character set latin1 collate latin1_bin default NULL,
  `study_pk_id` bigint(20) default NULL,
  `general_equipment_pk_id` bigint(20) default NULL,
  `trial_protocol_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_protocol_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_site_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `study_date` date default NULL,
  `study_desc` varchar(64) character set latin1 collate latin1_bin default NULL,
  `admitting_diagnoses_desc` varchar(64) character set latin1 collate latin1_bin default NULL,
  `patient_age` varchar(4) character set latin1 collate latin1_bin default NULL,
  `patient_sex` varchar(16) character set latin1 collate latin1_bin default NULL,
  `patient_weight` decimal(22,6) default NULL,
  `age_group` varchar(10) character set latin1 collate latin1_bin default NULL,
  `patient_pk_id` bigint(20) default NULL,
  `series_number` decimal(22,6) default NULL,
  `sync_frame_of_ref_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `patient_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `frame_of_reference_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `visibility` varchar(5) character set latin1 collate latin1_bin default NULL,
  `security_group` varchar(50) character set latin1 collate latin1_bin default NULL,
  `annotations_flag` varchar(5) character set latin1 collate latin1_bin default NULL,
  `version` bigint(20) default '0',
  PRIMARY KEY  (`general_series_pk_id`),
  UNIQUE KEY `PK_G_SERIES_PK_ID` (`general_series_pk_id`),
  KEY `body_part_examined_idx` (`body_part_examined`),
  KEY `general_series_sec_grp_idx` (`security_group`),
  KEY `general_series_site_idx` (`trial_site_name`),
  KEY `modality_idx` (`modality`),
  KEY `series_date_idx` (`series_date`),
  KEY `series_desc_idx` (`series_desc`),
  KEY `series_visibility_ind` (`visibility`),
  KEY `fk_gs_study_pk_id` (`study_pk_id`),
  KEY `fk_g_equipment_pk_id` (`general_equipment_pk_id`),
  KEY `Series_instance_uid_idx` (`series_instance_uid`),
  CONSTRAINT `fk_gs_study_pk_id` FOREIGN KEY (`study_pk_id`) REFERENCES `study` (`study_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_g_equipment_pk_id` FOREIGN KEY (`general_equipment_pk_id`) REFERENCES `general_equipment` (`general_equipment_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `grid_node`
--

DROP TABLE IF EXISTS `grid_node`;
CREATE TABLE `grid_node` (
  `grid_node_pk_id` bigint(20) NOT NULL,
  `node_name` varchar(50) character set latin1 collate latin1_bin NOT NULL,
  `url` varchar(255) character set latin1 collate latin1_bin NOT NULL,
  `node_description` varchar(255) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`grid_node_pk_id`),
  UNIQUE KEY `PK_GRID_NODE_PK_ID` (`grid_node_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hibernate_unique_key`
--

DROP TABLE IF EXISTS `hibernate_unique_key`;
CREATE TABLE `hibernate_unique_key` (
  `next_hi` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Temporary table structure for view `image`
--

DROP TABLE IF EXISTS `image`;
/*!50001 DROP VIEW IF EXISTS `image`*/;
/*!50001 CREATE TABLE `image` (
  `IMAGE_PK_ID` bigint(20),
  `INSTANCE_NUMBER` bigint(20),
  `CONTENT_DATE` date,
  `CONTENT_TIME` varchar(16),
  `IMAGE_TYPE` varchar(16),
  `ACQUISITION_DATE` date,
  `ACQUISITION_TIME` varchar(16),
  `ACQUISITION_NUMBER` decimal(22,6),
  `LOSSY_IMAGE_COMPRESSION` varchar(16),
  `PIXEL_SPACING` decimal(22,6),
  `IMAGE_ORIENTATION_PATIENT` varchar(200),
  `IMAGE_POSITION_PATIENT` varchar(200),
  `SLICE_THICKNESS` decimal(22,6),
  `SLICE_LOCATION` decimal(22,6),
  `I_ROWS` decimal(22,6),
  `I_COLUMNS` decimal(22,6),
  `CONTRAST_BOLUS_AGENT` varchar(64),
  `CONTRAST_BOLUS_ROUTE` varchar(64),
  `SOP_CLASS_UID` varchar(64),
  `SOP_INSTANCE_UID` varchar(64),
  `GENERAL_SERIES_PK_ID` bigint(20),
  `PATIENT_POSITION` varchar(16),
  `SOURCE_TO_DETECTOR_DISTANCE` decimal(22,6),
  `SOURCE_SUBJECT_DISTANCE` decimal(22,6),
  `FOCAL_SPOT_SIZE` decimal(22,6),
  `STORAGE_MEDIA_FILE_SET_UID` varchar(64),
  `MIRC_DOC_URI` varchar(2000),
  `DICOM_FILE_URI` varchar(2000),
  `ACQUISITION_DATETIME` varchar(50),
  `IMAGE_COMMENTS` varchar(4000),
  `IMAGE_RECEIVING_TIMESTAMP` datetime,
  `CURATION_STATUS` varchar(20),
  `CURATION_TIMESTAMP` datetime,
  `VISIBILITY` varchar(5),
  `ANNOTATION` varchar(20),
  `SUBMISSION_DATE` date,
  `DICOM_SIZE` decimal(22,6),
  `IMAGE_LATERALITY` varchar(16),
  `TRIAL_DP_PK_ID` bigint(20),
  `PATIENT_ID` varchar(64),
  `STUDY_INSTANCE_UID` varchar(500),
  `SERIES_INSTANCE_UID` varchar(64),
  `PATIENT_PK_ID` bigint(20),
  `STUDY_PK_ID` bigint(20),
  `PROJECT` varchar(200),
  `VERSION` bigint(20),
  `ACQUISITION_MATRIX` decimal(22,6),
  `DX_DATA_COLLECTION_DIAMETER` decimal(22,6),
  `KVP` decimal(22,6),
  `SCAN_OPTIONS` varchar(16),
  `DATA_COLLECTION_DIAMETER` decimal(22,6),
  `RECONSTRUCTION_DIAMETER` decimal(22,6),
  `GANTRY_DETECTOR_TILT` decimal(22,6),
  `EXPOSURE_TIME` decimal(22,6),
  `X_RAY_TUBE_CURRENT` decimal(22,6),
  `EXPOSURE` decimal(22,6),
  `EXPOSURE_IN_MICROAS` decimal(22,6),
  `CONVOLUTION_KERNEL` varchar(16),
  `REVOLUTION_TIME` decimal(22,6),
  `SINGLE_COLLIMATION_WIDTH` decimal(22,6),
  `TOTAL_COLLIMATION_WIDTH` decimal(22,6),
  `TABLE_SPEED` decimal(22,6),
  `TABLE_FEED_PER_ROTATION` decimal(22,6),
  `CT_PITCH_FACTOR` decimal(22,6),
  `ANATOMIC_REGION_SEQ` varchar(500)
) */;

--
-- Table structure for table `image_markup`
--

DROP TABLE IF EXISTS `image_markup`;
CREATE TABLE `image_markup` (
  `image_markup_pk_id` bigint(20) NOT NULL,
  `series_instance_uid` varchar(64) character set latin1 collate latin1_bin default NULL,
  `general_series_pk_id` bigint(20) default NULL,
  `login_name` varchar(300) character set latin1 collate latin1_bin default NULL,
  `markup_content` longtext character set latin1 collate latin1_bin,
  `submission_date` datetime default NULL,
  PRIMARY KEY  (`image_markup_pk_id`),
  UNIQUE KEY `PK_IMAGE_M_PK_ID` (`image_markup_pk_id`),
  KEY `FK_image_markup_series` (`general_series_pk_id`),
  CONSTRAINT `FK_image_markup_series` FOREIGN KEY (`general_series_pk_id`) REFERENCES `general_series` (`general_series_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `login_history`
--

DROP TABLE IF EXISTS `login_history`;
CREATE TABLE `login_history` (
  `login_history_pk_id` bigint(20) NOT NULL,
  `login_timestamp` datetime default NULL,
  `ip_address` varchar(15) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`login_history_pk_id`),
  UNIQUE KEY `PK_LOGIN_HISTORY_PK_ID` (`login_history_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Temporary table structure for view `manufacturer`
--

DROP TABLE IF EXISTS `manufacturer`;
/*!50001 DROP VIEW IF EXISTS `manufacturer`*/;
/*!50001 CREATE TABLE `manufacturer` (
  `MANUFACTURER` varchar(64)
) */;


--
-- Temporary table structure for view `manufacturer_model_software`
--

DROP TABLE IF EXISTS `manufacturer_model_software`;
/*!50001 DROP VIEW IF EXISTS `manufacturer_model_software`*/;
/*!50001 CREATE TABLE `manufacturer_model_software` (
  `ID` bigint(20),
  `MANUFACTURER` varchar(64),
  `MODEL` varchar(64),
  `SOFTWARE` varchar(64)
) */;


--
-- Temporary table structure for view `number_month`
--

DROP TABLE IF EXISTS `number_month`;
/*!50001 DROP VIEW IF EXISTS `number_month`*/;
/*!50001 CREATE TABLE `number_month` (
  `PATIENT_ID` bigint(20),
  `NUMBER_MONTH` bigint(21)
) */;


--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient` (
  `patient_pk_id` bigint(20) NOT NULL,
  `patient_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `patient_name` varchar(250) character set latin1 collate latin1_bin default NULL,
  `patient_birth_date` date default NULL,
  `patient_sex` varchar(16) character set latin1 collate latin1_bin default NULL,
  `ethnic_group` varchar(16) character set latin1 collate latin1_bin default NULL,
  `trial_dp_pk_id` bigint(20) default NULL,
  `trial_subject_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_subject_reading_id` varchar(255) character set latin1 collate latin1_bin default NULL,
  `trial_site_pk_id` bigint(20) default NULL,
  `visibility` varchar(5) character set latin1 collate latin1_bin default NULL,
  `version` bigint(20) default '0',
  PRIMARY KEY  (`patient_pk_id`),
  UNIQUE KEY `PK_PATINET_PK_ID` (`patient_pk_id`),
  KEY `patient_visibility_ind` (`visibility`),
  KEY `fk_trial_dp_pk_id` (`trial_dp_pk_id`),
  KEY `fk_trial_site_pk_id` (`trial_site_pk_id`),
  KEY `Patient_Id_Idx` (`patient_id`),
  CONSTRAINT `fk_trial_dp_pk_id` FOREIGN KEY (`trial_dp_pk_id`) REFERENCES `trial_data_provenance` (`trial_dp_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_trial_site_pk_id` FOREIGN KEY (`trial_site_pk_id`) REFERENCES `trial_site` (`trial_site_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `qa_status_history`
--

DROP TABLE IF EXISTS `qa_status_history`;
CREATE TABLE `qa_status_history` (
  `qa_status_history_pk_id` bigint(20) NOT NULL,
  `general_image_pk_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `comment` varchar(4000) character set latin1 collate latin1_bin default NULL,
  `history_timestamp` datetime default NULL,
  `new_value` varchar(100) character set latin1 collate latin1_bin default NULL,
  `old_value` varchar(100) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`qa_status_history_pk_id`),
  UNIQUE KEY `PK_QA_STATUS_HISTORY_PK_ID` (`qa_status_history_pk_id`),
  KEY `genimg_qasts_id_idx` (`general_image_pk_id`),
  KEY `fk_qa_user_pk_id` (`user_id`),
  CONSTRAINT `fk_qa_image_pk_id` FOREIGN KEY (`general_image_pk_id`) REFERENCES `general_image` (`image_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Temporary table structure for view `qa_status_summary`
--

DROP TABLE IF EXISTS `qa_status_summary`;
/*!50001 DROP VIEW IF EXISTS `qa_status_summary`*/;
/*!50001 CREATE TABLE `qa_status_summary` (
  `PROJECT_NAME` varchar(200),
  `PATIENT_PK_ID` bigint(20),
  `PATIENT_ID` varchar(64),
  `STUDY_PK_ID` bigint(20),
  `STUDY_UID` varchar(500),
  `SERIES_PK_ID` bigint(20),
  `SERIES_GROUP` varchar(300),
  `SERIES_UID` varchar(64),
  `IMAGE_VISIBILITY` varchar(5),
  `DP_SITE_NAME` varchar(64)
) */;

--
-- Temporary table structure for view `qa_status_summary_tmp`
--

DROP TABLE IF EXISTS `qa_status_summary_tmp`;
/*!50001 DROP VIEW IF EXISTS `qa_status_summary_tmp`*/;
/*!50001 CREATE TABLE `qa_status_summary_tmp` (
  `sid` bigint(20),
  `visibility` varchar(5)
) */;

--
-- Temporary table structure for view `quarantine`
--

DROP TABLE IF EXISTS `quarantine`;
/*!50001 DROP VIEW IF EXISTS `quarantine`*/;
/*!50001 CREATE TABLE `quarantine` (
  `GS_SERIES_PK_ID` bigint(20),
  `SERIES_INSTANCE_UID` varchar(64),
  `STUDY_INSTANCE_UID` varchar(500),
  `PROJECT` varchar(200),
  `DP_SITE_NAME` varchar(64)
) */;

--
-- Table structure for table `query_history`
--

DROP TABLE IF EXISTS `query_history`;
CREATE TABLE `query_history` (
  `query_history_pk_id` bigint(20) NOT NULL,
  `user_id` bigint(20) default NULL,
  `query_execute_timestamp` datetime default NULL,
  `query_elapsed_time` bigint(20) default NULL,
  `saved_query_pk_id` bigint(20) default NULL,
  PRIMARY KEY  (`query_history_pk_id`),
  UNIQUE KEY `PK_QUERY_HISTORY_PK_ID` (`query_history_pk_id`),
  KEY `query_history_user_idx` (`user_id`),
  KEY `query_hist_save_quer_idx` (`saved_query_pk_id`),
  CONSTRAINT `FK_Q_H_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `fk_s_query_pk_id` FOREIGN KEY (`saved_query_pk_id`) REFERENCES `saved_query` (`saved_query_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `query_history_attribute`
--

DROP TABLE IF EXISTS `query_history_attribute`;
CREATE TABLE `query_history_attribute` (
  `query_history_attribute_pk_id` bigint(20) NOT NULL,
  `attribute_name` varchar(300) character set latin1 collate latin1_bin default NULL,
  `subattribute_name` varchar(300) character set latin1 collate latin1_bin default NULL,
  `attribute_value` varchar(300) character set latin1 collate latin1_bin default NULL,
  `query_history_pk_id` bigint(20) default NULL,
  `instance_number` bigint(20) default NULL,
  PRIMARY KEY  (`query_history_attribute_pk_id`),
  UNIQUE KEY `PK_Q_H_A_PK_ID` (`query_history_attribute_pk_id`),
  KEY `attri_name_idx` (`attribute_name`),
  KEY `attri_value_idx` (`attribute_value`),
  KEY `query_history_attr_parent` (`query_history_pk_id`),
  KEY `FK_Q_H_PK_ID` (`query_history_pk_id`),
  CONSTRAINT `FK_Q_H_PK_ID` FOREIGN KEY (`query_history_pk_id`) REFERENCES `query_history` (`query_history_pk_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `saved_query`
--

DROP TABLE IF EXISTS `saved_query`;
CREATE TABLE `saved_query` (
  `saved_query_pk_id` bigint(20) NOT NULL,
  `user_id` bigint(20) default NULL,
  `query_name` varchar(200) character set latin1 collate latin1_bin default NULL,
  `new_data_flag` varchar(5) character set latin1 collate latin1_bin default NULL,
  `active` varchar(5) character set latin1 collate latin1_bin default NULL,
  `query_execute_timestamp` datetime default NULL,
  PRIMARY KEY  (`saved_query_pk_id`),
  UNIQUE KEY `PK_SAVED_Q_PK_ID` (`saved_query_pk_id`),
  KEY `saved_query_user_idx` (`user_id`),
  CONSTRAINT `FK_SAVED_QUERY_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `saved_query_attribute`
--

DROP TABLE IF EXISTS `saved_query_attribute`;
CREATE TABLE `saved_query_attribute` (
  `saved_query_attribute_pk_id` bigint(20) NOT NULL,
  `attribute_name` varchar(300) character set latin1 collate latin1_bin default NULL,
  `subattribute_name` varchar(300) character set latin1 collate latin1_bin default NULL,
  `attribute_value` varchar(300) character set latin1 collate latin1_bin default NULL,
  `saved_query_pk_id` bigint(20) default NULL,
  `instance_number` bigint(20) default NULL,
  PRIMARY KEY  (`saved_query_attribute_pk_id`),
  UNIQUE KEY `PK_S_Q_A_PK_ID` (`saved_query_attribute_pk_id`),
  KEY `saved_query_attr_parent` (`saved_query_pk_id`),
  KEY `FK_SAVED_QUERY_PK_ID` (`saved_query_pk_id`),
  CONSTRAINT `FK_SAVED_QUERY_PK_ID` FOREIGN KEY (`saved_query_pk_id`) REFERENCES `saved_query` (`saved_query_pk_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Temporary table structure for view `saved_query_last_exec`
--

DROP TABLE IF EXISTS `saved_query_last_exec`;
/*!50001 DROP VIEW IF EXISTS `saved_query_last_exec`*/;
/*!50001 CREATE TABLE `saved_query_last_exec` (
  `SAVED_QUERY_PK_ID` bigint(20),
  `LAST_EXECUTE_DATE` datetime
) */;

--
-- Temporary table structure for view `software_versions`
--

DROP TABLE IF EXISTS `software_versions`;
/*!50001 DROP VIEW IF EXISTS `software_versions`*/;
/*!50001 CREATE TABLE `software_versions` (
  `SOFTWARE_VERSIONS` varchar(64)
) */;

--
-- Table structure for table `study`
--

DROP TABLE IF EXISTS `study`;
CREATE TABLE `study` (
  `study_pk_id` bigint(20) NOT NULL,
  `study_instance_uid` varchar(500) character set latin1 collate latin1_bin default NULL,
  `study_date` date default NULL,
  `study_time` varchar(16) character set latin1 collate latin1_bin default NULL,
  `study_desc` varchar(64) character set latin1 collate latin1_bin default NULL,
  `admitting_diagnoses_desc` varchar(64) character set latin1 collate latin1_bin default NULL,
  `admitting_diagnoses_code_seq` varchar(500) character set latin1 collate latin1_bin default NULL,
  `patient_pk_id` bigint(20) default NULL,
  `study_id` varchar(16) character set latin1 collate latin1_bin default NULL,
  `trial_time_point_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_time_point_desc` varchar(1024) character set latin1 collate latin1_bin default NULL,
  `patient_age` varchar(4) character set latin1 collate latin1_bin default NULL,
  `age_group` varchar(10) character set latin1 collate latin1_bin default NULL,
  `patient_size` decimal(22,6) default NULL,
  `patient_weight` decimal(22,6) default NULL,
  `occupation` varchar(16) character set latin1 collate latin1_bin default NULL,
  `additional_patient_history` varchar(4000) character set latin1 collate latin1_bin default NULL,
  `visibility` varchar(5) character set latin1 collate latin1_bin default NULL,
  `version` bigint(20) default '0',
  PRIMARY KEY  (`study_pk_id`),
  UNIQUE KEY `PK_STUDY_PK_ID` (`study_pk_id`),
  KEY `study_date_idx` (`study_date`),
  KEY `study_desc_idx` (`study_desc`),
  KEY `study_visibility_ind` (`visibility`),
  KEY `fk_patient_pk_id` (`patient_pk_id`),
  KEY `STUDY_INSTANCE_UID_idx` (`study_instance_uid`),
  CONSTRAINT `fk_patient_pk_id` FOREIGN KEY (`patient_pk_id`) REFERENCES `patient` (`patient_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Temporary table structure for view `study_series_number`
--

DROP TABLE IF EXISTS `study_series_number`;
/*!50001 DROP VIEW IF EXISTS `study_series_number`*/;
/*!50001 CREATE TABLE `study_series_number` (
  `PATIENT_PK_ID` bigint(20),
  `PATIENT_ID` varchar(64),
  `PROJECT` varchar(200),
  `STUDY_NUMBER` bigint(21),
  `SERIES_NUMBER` bigint(21)
) */;

--
-- Temporary table structure for view `submission_count_by_month`
--

DROP TABLE IF EXISTS `submission_count_by_month`;
/*!50001 DROP VIEW IF EXISTS `submission_count_by_month`*/;
/*!50001 CREATE TABLE `submission_count_by_month` (
  `PROJECT` varchar(200),
  `DP_SITE_NAME` varchar(64),
  `MONTH` varbinary(2),
  `YEAR` varbinary(4),
  `COUNT` bigint(21)
) */;


--
-- Table structure for table `trial_data_provenance`
--

DROP TABLE IF EXISTS `trial_data_provenance`;
CREATE TABLE `trial_data_provenance` (
  `trial_dp_pk_id` bigint(20) NOT NULL,
  `dp_site_name` varchar(40) character set latin1 collate latin1_bin default NULL,
  `dp_site_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `project` varchar(50) character set latin1 collate latin1_bin default NULL,
  PRIMARY KEY  (`trial_dp_pk_id`),
  UNIQUE KEY `PK_TRIAL_DP_PK_ID` (`trial_dp_pk_id`),
  KEY `siteNameIndex` (`dp_site_name`),
  KEY `projectIndex` (`project`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `trial_site`
--

DROP TABLE IF EXISTS `trial_site`;
CREATE TABLE `trial_site` (
  `trial_site_pk_id` bigint(20) NOT NULL,
  `trial_site_id` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_site_name` varchar(64) character set latin1 collate latin1_bin default NULL,
  `trial_pk_id` bigint(20) default NULL,
  PRIMARY KEY  (`trial_site_pk_id`),
  UNIQUE KEY `PK_TRIAL_SITE_PK_ID` (`trial_site_pk_id`),
  KEY `fk_trial_pk_id` (`trial_pk_id`),
  CONSTRAINT `fk_trial_pk_id` FOREIGN KEY (`trial_pk_id`) REFERENCES `clinical_trial` (`trial_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Final view structure for view `all_logins`
--

/*!50001 DROP TABLE IF EXISTS `all_logins`*/;
/*!50001 DROP VIEW IF EXISTS `all_logins`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `all_logins` AS select `login_history`.`login_timestamp` AS `LOGIN_TIMESTAMP`,`login_history`.`ip_address` AS `IP_ADDRESS` from `login_history` order by `login_history`.`login_timestamp` desc */;


--
-- Final view structure for view `image`
--

/*!50001 DROP TABLE IF EXISTS `image`*/;
/*!50001 DROP VIEW IF EXISTS `image`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `image` AS select `gi`.`image_pk_id` AS `IMAGE_PK_ID`,`gi`.`instance_number` AS `INSTANCE_NUMBER`,`gi`.`content_date` AS `CONTENT_DATE`,`gi`.`content_time` AS `CONTENT_TIME`,`gi`.`image_type` AS `IMAGE_TYPE`,`gi`.`acquisition_date` AS `ACQUISITION_DATE`,`gi`.`acquisition_time` AS `ACQUISITION_TIME`,`gi`.`acquisition_number` AS `ACQUISITION_NUMBER`,`gi`.`lossy_image_compression` AS `LOSSY_IMAGE_COMPRESSION`,`gi`.`pixel_spacing` AS `PIXEL_SPACING`,`gi`.`image_orientation_patient` AS `IMAGE_ORIENTATION_PATIENT`,`gi`.`image_position_patient` AS `IMAGE_POSITION_PATIENT`,`gi`.`slice_thickness` AS `SLICE_THICKNESS`,`gi`.`slice_location` AS `SLICE_LOCATION`,`gi`.`i_rows` AS `I_ROWS`,`gi`.`i_columns` AS `I_COLUMNS`,`gi`.`contrast_bolus_agent` AS `CONTRAST_BOLUS_AGENT`,`gi`.`contrast_bolus_route` AS `CONTRAST_BOLUS_ROUTE`,`gi`.`sop_class_uid` AS `SOP_CLASS_UID`,`gi`.`sop_instance_uid` AS `SOP_INSTANCE_UID`,`gi`.`general_series_pk_id` AS `GENERAL_SERIES_PK_ID`,`gi`.`patient_position` AS `PATIENT_POSITION`,`gi`.`source_to_detector_distance` AS `SOURCE_TO_DETECTOR_DISTANCE`,`gi`.`source_subject_distance` AS `SOURCE_SUBJECT_DISTANCE`,`gi`.`focal_spot_size` AS `FOCAL_SPOT_SIZE`,`gi`.`storage_media_file_set_uid` AS `STORAGE_MEDIA_FILE_SET_UID`,`gi`.`mirc_doc_uri` AS `MIRC_DOC_URI`,`gi`.`dicom_file_uri` AS `DICOM_FILE_URI`,`gi`.`acquisition_datetime` AS `ACQUISITION_DATETIME`,`gi`.`image_comments` AS `IMAGE_COMMENTS`,`gi`.`image_receiving_timestamp` AS `IMAGE_RECEIVING_TIMESTAMP`,`gi`.`curation_status` AS `CURATION_STATUS`,`gi`.`curation_timestamp` AS `CURATION_TIMESTAMP`,`gi`.`visibility` AS `VISIBILITY`,`gi`.`annotation` AS `ANNOTATION`,`gi`.`submission_date` AS `SUBMISSION_DATE`,`gi`.`dicom_size` AS `DICOM_SIZE`,`gi`.`image_laterality` AS `IMAGE_LATERALITY`,`gi`.`trial_dp_pk_id` AS `TRIAL_DP_PK_ID`,`gi`.`patient_id` AS `PATIENT_ID`,`gi`.`study_instance_uid` AS `STUDY_INSTANCE_UID`,`gi`.`series_instance_uid` AS `SERIES_INSTANCE_UID`,`gi`.`patient_pk_id` AS `PATIENT_PK_ID`,`gi`.`study_pk_id` AS `STUDY_PK_ID`,`gi`.`project` AS `PROJECT`,`gi`.`version` AS `VERSION`,`gi`.`acquisition_matrix` AS `ACQUISITION_MATRIX`,`gi`.`dx_data_collection_diameter` AS `DX_DATA_COLLECTION_DIAMETER`,`cti`.`kvp` AS `KVP`,`cti`.`scan_options` AS `SCAN_OPTIONS`,`cti`.`data_collection_diameter` AS `DATA_COLLECTION_DIAMETER`,`cti`.`reconstruction_diameter` AS `RECONSTRUCTION_DIAMETER`,`cti`.`gantry_detector_tilt` AS `GANTRY_DETECTOR_TILT`,`cti`.`exposure_time` AS `EXPOSURE_TIME`,`cti`.`x_ray_tube_current` AS `X_RAY_TUBE_CURRENT`,`cti`.`exposure` AS `EXPOSURE`,`cti`.`exposure_in_microas` AS `EXPOSURE_IN_MICROAS`,`cti`.`convolution_kernel` AS `CONVOLUTION_KERNEL`,`cti`.`revolution_time` AS `REVOLUTION_TIME`,`cti`.`single_collimation_width` AS `SINGLE_COLLIMATION_WIDTH`,`cti`.`total_collimation_width` AS `TOTAL_COLLIMATION_WIDTH`,`cti`.`table_speed` AS `TABLE_SPEED`,`cti`.`table_feed_per_rotation` AS `TABLE_FEED_PER_ROTATION`,`cti`.`ct_pitch_factor` AS `CT_PITCH_FACTOR`,`cti`.`anatomic_region_seq` AS `ANATOMIC_REGION_SEQ` from (`general_image` `gi` join `ct_image` `cti`) where (`gi`.`image_pk_id` = `cti`.`image_pk_id`) */;



--
-- Final view structure for view `manufacturer`
--

/*!50001 DROP TABLE IF EXISTS `manufacturer`*/;
/*!50001 DROP VIEW IF EXISTS `manufacturer`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `manufacturer` AS select distinct `general_equipment`.`manufacturer` AS `MANUFACTURER` from `general_equipment` where `general_equipment`.`general_equipment_pk_id` in (select `general_series`.`general_equipment_pk_id` AS `GENERAL_EQUIPMENT_PK_ID` from `general_series` where (`general_series`.`visibility` = _latin1'1')) */;


--
-- Final view structure for view `manufacturer_model_software`
--

/*!50001 DROP TABLE IF EXISTS `manufacturer_model_software`*/;
/*!50001 DROP VIEW IF EXISTS `manufacturer_model_software`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `manufacturer_model_software` AS select distinct `general_equipment`.`general_equipment_pk_id` AS `ID`,`general_equipment`.`manufacturer` AS `MANUFACTURER`,`general_equipment`.`manufacturer_model_name` AS `MODEL`,`general_equipment`.`software_versions` AS `SOFTWARE` from `general_equipment` where `general_equipment`.`general_equipment_pk_id` in (select `general_series`.`general_equipment_pk_id` AS `GENERAL_EQUIPMENT_PK_ID` from `general_series` where (`general_series`.`visibility` = _latin1'1')) */;


--
-- Final view structure for view `number_month`
--

/*!50001 DROP TABLE IF EXISTS `number_month`*/;
/*!50001 DROP VIEW IF EXISTS `number_month`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `number_month` AS (select `study`.`patient_pk_id` AS `PATIENT_ID`,round(timestampdiff(MONTH,min(`study`.`study_date`),max(`study`.`study_date`)),0) AS `NUMBER_MONTH` from `study` where (`study`.`visibility` = _latin1'1') group by `study`.`patient_pk_id`) */;


--
-- Final view structure for view `qa_status_summary`
--

/*!50001 DROP TABLE IF EXISTS `qa_status_summary`*/;
/*!50001 DROP VIEW IF EXISTS `qa_status_summary`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `qa_status_summary` AS select `tdp`.`project` AS `PROJECT_NAME`,`p`.`patient_pk_id` AS `PATIENT_PK_ID`,`p`.`patient_id` AS `PATIENT_ID`,`s`.`study_pk_id` AS `STUDY_PK_ID`,`s`.`study_instance_uid` AS `STUDY_UID`,`gs`.`general_series_pk_id` AS `SERIES_PK_ID`,`gs`.`security_group` AS `SERIES_GROUP`,`gs`.`series_instance_uid` AS `SERIES_UID`,`qt`.`visibility` AS `IMAGE_VISIBILITY`,`tdp`.`dp_site_name` AS `DP_SITE_NAME` from ((((`trial_data_provenance` `tdp` join `patient` `p`) join `study` `s`) join `general_series` `gs`) join `qa_status_summary_tmp` `qt`) where ((`tdp`.`trial_dp_pk_id` = `p`.`trial_dp_pk_id`) and (`p`.`patient_pk_id` = `s`.`patient_pk_id`) and (`s`.`study_pk_id` = `gs`.`study_pk_id`) and (`qt`.`sid` = `gs`.`general_series_pk_id`)) */;

--
-- Final view structure for view `qa_status_summary_tmp`
--

/*!50001 DROP TABLE IF EXISTS `qa_status_summary_tmp`*/;
/*!50001 DROP VIEW IF EXISTS `qa_status_summary_tmp`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `qa_status_summary_tmp` AS select distinct `general_image`.`general_series_pk_id` AS `sid`,`general_image`.`visibility` AS `visibility` from `general_image` */;

--
-- Final view structure for view `quarantine`
--

/*!50001 DROP TABLE IF EXISTS `quarantine`*/;
/*!50001 DROP VIEW IF EXISTS `quarantine`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `quarantine` AS (select `gs`.`general_series_pk_id` AS `GS_SERIES_PK_ID`,`gs`.`series_instance_uid` AS `SERIES_INSTANCE_UID`,`s`.`study_instance_uid` AS `STUDY_INSTANCE_UID`,`tdp`.`project` AS `PROJECT`,`tdp`.`dp_site_name` AS `DP_SITE_NAME` from (((`patient` `p` join `study` `s`) join `general_series` `gs`) join `trial_data_provenance` `tdp`) where ((`p`.`trial_dp_pk_id` = `tdp`.`trial_dp_pk_id`) and (`s`.`patient_pk_id` = `p`.`patient_pk_id`) and (`s`.`study_pk_id` = `gs`.`study_pk_id`))) */;


/*!50001 DROP TABLE IF EXISTS `saved_query_last_exec`*/;
/*!50001 DROP VIEW IF EXISTS `saved_query_last_exec`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `saved_query_last_exec` AS select `qh`.`saved_query_pk_id` AS `SAVED_QUERY_PK_ID`,max(`qh`.`query_execute_timestamp`) AS `LAST_EXECUTE_DATE` from (`saved_query` `sq` join `query_history` `qh`) where (`qh`.`saved_query_pk_id` = `sq`.`saved_query_pk_id`) group by `qh`.`saved_query_pk_id` */;
--
-- Final view structure for view `software_versions`
--

/*!50001 DROP TABLE IF EXISTS `software_versions`*/;
/*!50001 DROP VIEW IF EXISTS `software_versions`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `software_versions` AS select distinct `general_equipment`.`software_versions` AS `SOFTWARE_VERSIONS` from `general_equipment` where `general_equipment`.`general_equipment_pk_id` in (select `general_series`.`general_equipment_pk_id` AS `GENERAL_EQUIPMENT_PK_ID` from `general_series` where ((`general_series`.`visibility` = _latin1'1') and (`general_equipment`.`software_versions` is not null))) */;

--
-- Final view structure for view `study_series_number`
--

/*!50001 DROP TABLE IF EXISTS `study_series_number`*/;
/*!50001 DROP VIEW IF EXISTS `study_series_number`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `study_series_number` AS select `g`.`patient_pk_id` AS `PATIENT_PK_ID`,`p`.`patient_id` AS `PATIENT_ID`,`dp`.`project` AS `PROJECT`,count(distinct `g`.`study_pk_id`) AS `STUDY_NUMBER`,count(distinct `g`.`general_series_pk_id`) AS `SERIES_NUMBER` from ((`general_series` `g` join `patient` `p`) join `trial_data_provenance` `dp`) where ((`g`.`visibility` = _latin1'1') and (`g`.`patient_pk_id` = `p`.`patient_pk_id`) and (`p`.`trial_dp_pk_id` = `dp`.`trial_dp_pk_id`)) group by `g`.`patient_pk_id`,`p`.`patient_id`,`dp`.`project` */;

--
-- Final view structure for view `submission_count_by_month`
--

/*!50001 DROP TABLE IF EXISTS `submission_count_by_month`*/;
/*!50001 DROP VIEW IF EXISTS `submission_count_by_month`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `submission_count_by_month` AS select `tdp`.`project` AS `PROJECT`,`tdp`.`dp_site_name` AS `DP_SITE_NAME`,date_format(`gi`.`submission_date`,_utf8'%m') AS `MONTH`,date_format(`gi`.`submission_date`,_utf8'%Y') AS `YEAR`,count(0) AS `COUNT` from (`general_image` `gi` join `trial_data_provenance` `tdp`) where (`gi`.`trial_dp_pk_id` = `tdp`.`trial_dp_pk_id`) group by `tdp`.`project`,`tdp`.`dp_site_name`,date_format(`gi`.`submission_date`,_utf8'%m'),date_format(`gi`.`submission_date`,_utf8'%Y') order by `tdp`.`project`,`tdp`.`dp_site_name`,date_format(`gi`.`submission_date`,_utf8'%Y'),date_format(`gi`.`submission_date`,_utf8'%m') */;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-08-05 17:41:31

-- Views for ncia core service

CREATE  OR REPLACE VIEW dicom_series AS
SELECT GENERAL_SERIES_PK_ID AS GENERAL_SERIES_PK_ID, BODY_PART_EXAMINED AS BODY_PART_EXAMINED, FRAME_OF_REFERENCE_UID AS FRAME_OF_REFERENCE_UID, SERIES_LATERALITY AS SERIES_LATERALITY, MODALITY AS MODALITY, 
PROTOCOL_NAME AS PROTOCOL_NAME, SERIES_DATE AS SERIES_DATE, SERIES_DESC AS SERIES_DESC, SERIES_INSTANCE_UID AS SERIES_INSTANCE_UID, SERIES_NUMBER AS SERIES_NUMBER, SYNC_FRAME_OF_REF_UID AS SYNC_FRAME_OF_REF_UID,
STUDY_PK_ID AS STUDY_PK_ID, GENERAL_EQUIPMENT_PK_ID AS GENERAL_EQUIPMENT_PK_ID
FROM general_series WHERE  VISIBILITY='1' and SECURITY_GROUP IS NULL;


CREATE  OR REPLACE VIEW dicom_study AS
SELECT STUDY_PK_ID AS STUDY_PK_ID, STUDY_INSTANCE_UID AS STUDY_INSTANCE_UID, ADDITIONAL_PATIENT_HISTORY AS ADDITIONAL_PATIENT_HISTORY, STUDY_DATE AS STUDY_DATE, STUDY_DESC AS STUDY_DESC, ADMITTING_DIAGNOSES_DESC AS ADMITTING_DIAGNOSES_DESC,
ADMITTING_DIAGNOSES_CODE_SEQ AS ADMITTING_DIAGNOSES_CODE_SEQ , OCCUPATION AS OCCUPATION, PATIENT_AGE AS PATIENT_AGE,  PATIENT_SIZE AS PATIENT_SIZE, PATIENT_WEIGHT AS PATIENT_WEIGHT, STUDY_ID AS STUDY_ID, 
STUDY_TIME AS STUDY_TIME, TRIAL_TIME_POINT_ID AS TRIAL_TIME_POINT_ID, TRIAL_TIME_POINT_DESC AS TRIAL_TIME_POINT_DESC, PATIENT_PK_ID AS PATIENT_PK_ID
FROM study WHERE VISIBILITY='1';

CREATE  OR REPLACE VIEW dicom_patient AS
SELECT PATIENT_PK_ID AS PATIENT_PK_ID, PATIENT_ID AS PATIENT_ID, ETHNIC_GROUP AS ETHNIC_GROUP, PATIENT_BIRTH_DATE AS PATIENT_BIRTH_DATE, PATIENT_SEX AS PATIENT_SEX, PATIENT_NAME AS PATIENT_NAME, TRIAL_DP_PK_ID AS TRIAL_DP_PK_ID, TRIAL_SITE_PK_ID AS TRIAL_SITE_PK_ID
FROM patient WHERE VISIBILITY='1';



CREATE OR REPLACE VIEW dicom_image AS SELECT `gi`.`image_pk_id` AS `IMAGE_PK_ID`,`gi`.`instance_number` AS 
`INSTANCE_NUMBER`,`gi`.`content_date` AS `CONTENT_DATE`,`gi`.`content_time` AS `CONTENT_TIME`,`gi`.`image_type` 
AS `IMAGE_TYPE`,`gi`.`acquisition_date` AS `ACQUISITION_DATE`,`gi`.`acquisition_time` AS `ACQUISITION_TIME`,
`gi`.`acquisition_number` AS `ACQUISITION_NUMBER`,`gi`.`lossy_image_compression` AS `LOSSY_IMAGE_COMPRESSION`,
`gi`.`pixel_spacing` AS `PIXEL_SPACING`,`gi`.`image_orientation_patient` AS `IMAGE_ORIENTATION_PATIENT`,
`gi`.`image_position_patient` AS `IMAGE_POSITION_PATIENT`,`gi`.`slice_thickness` AS `SLICE_THICKNESS`,
`gi`.`slice_location` AS `SLICE_LOCATION`,`gi`.`i_rows` AS `I_ROWS`,`gi`.`i_columns` AS `I_COLUMNS`,
`gi`.`contrast_bolus_agent` AS `CONTRAST_BOLUS_AGENT`,`gi`.`contrast_bolus_route` AS `CONTRAST_BOLUS_ROUTE`,
`gi`.`sop_class_uid` AS `SOP_CLASS_UID`,`gi`.`sop_instance_uid` AS `SOP_INSTANCE_UID`,`gi`.`general_series_pk_id` 
AS `GENERAL_SERIES_PK_ID`,`gi`.`patient_position` AS `PATIENT_POSITION`,`gi`.`source_to_detector_distance` AS 
`SOURCE_TO_DETECTOR_DISTANCE`,`gi`.`source_subject_distance` AS `SOURCE_SUBJECT_DISTANCE`,`gi`.`focal_spot_size` 
AS `FOCAL_SPOT_SIZE`,`gi`.`storage_media_file_set_uid` AS `STORAGE_MEDIA_FILE_SET_UID`,`gi`.`mirc_doc_uri` AS 
`MIRC_DOC_URI`,`gi`.`dicom_file_uri` AS `DICOM_FILE_URI`,`gi`.`acquisition_datetime` AS `ACQUISITION_DATETIME`,
`gi`.`image_comments` AS `IMAGE_COMMENTS`,`gi`.`image_receiving_timestamp` AS `IMAGE_RECEIVING_TIMESTAMP`,
`gi`.`curation_status` AS `CURATION_STATUS`,`gi`.`curation_timestamp` AS `CURATION_TIMESTAMP`,`gi`.`visibility` 
AS `VISIBILITY`,`gi`.`annotation` AS `ANNOTATION`,`gi`.`submission_date` AS `SUBMISSION_DATE`,`gi`.`dicom_size` 
AS `DICOM_SIZE`,`gi`.`image_laterality` AS `IMAGE_LATERALITY`,`gi`.`trial_dp_pk_id` AS `TRIAL_DP_PK_ID`,
`gi`.`patient_id` AS `PATIENT_ID`,`gi`.`study_instance_uid` AS `STUDY_INSTANCE_UID`,`gi`.`series_instance_uid` 
AS `SERIES_INSTANCE_UID`,`gi`.`patient_pk_id` AS `PATIENT_PK_ID`,`gi`.`study_pk_id` AS `STUDY_PK_ID`,
`gi`.`project` AS `PROJECT`,`gi`.`version` AS `VERSION`,`gi`.`acquisition_matrix` AS `ACQUISITION_MATRIX`,
`gi`.`dx_data_collection_diameter` AS `DX_DATA_COLLECTION_DIAMETER`,`cti`.`kvp` AS `KVP`,`cti`.`scan_options` 
AS `SCAN_OPTIONS`,`cti`.`data_collection_diameter` AS `DATA_COLLECTION_DIAMETER`,`cti`.`reconstruction_diameter` 
AS `RECONSTRUCTION_DIAMETER`,`cti`.`gantry_detector_tilt` AS `GANTRY_DETECTOR_TILT`,`cti`.`exposure_time` AS 
`EXPOSURE_TIME`,`cti`.`x_ray_tube_current` AS `X_RAY_TUBE_CURRENT`,`cti`.`exposure` AS `EXPOSURE`,
`cti`.`exposure_in_microas` AS `EXPOSURE_IN_MICROAS`,`cti`.`convolution_kernel` AS `CONVOLUTION_KERNEL`,
`cti`.`revolution_time` AS `REVOLUTION_TIME`,`cti`.`single_collimation_width` AS `SINGLE_COLLIMATION_WIDTH`,
`cti`.`total_collimation_width` AS `TOTAL_COLLIMATION_WIDTH`,`cti`.`table_speed` AS `TABLE_SPEED`,
`cti`.`table_feed_per_rotation` AS `TABLE_FEED_PER_ROTATION`,`cti`.`ct_pitch_factor` AS `CT_PITCH_FACTOR`,
`cti`.`anatomic_region_seq` AS `ANATOMIC_REGION_SEQ` 
FROM (`general_image` `gi` JOIN `ct_image` `cti`) 
WHERE (`gi`.`image_pk_id` = `cti`.`image_pk_id`) and gi.visibility='1';



-- csm 4.0 upgrade

CREATE TABLE csm_filter_clause ( 
	FILTER_CLAUSE_ID BIGINT AUTO_INCREMENT  NOT NULL,
	CLASS_NAME VARCHAR(100) NOT NULL,
	FILTER_CHAIN VARCHAR(2000) NOT NULL,
	TARGET_CLASS_NAME VARCHAR (100) NOT NULL,
	TARGET_CLASS_ATTRIBUTE_NAME VARCHAR (100) NOT NULL,
	TARGET_CLASS_ATTRIBUTE_TYPE VARCHAR (100) NOT NULL,
	TARGET_CLASS_ALIAS VARCHAR (100),
	TARGET_CLASS_ATTRIBUTE_ALIAS VARCHAR (100),
	GENERATED_SQL VARCHAR (4000) NOT NULL,
	APPLICATION_ID BIGINT NOT NULL,
	UPDATE_DATE DATE NOT NULL,
	PRIMARY KEY(FILTER_CLAUSE_ID)	
)ENGINE=InnoDB 
;
 
ALTER TABLE csm_protection_element ADD ( ATTRIBUTE_VALUE VARCHAR(100) );

ALTER TABLE csm_protection_element DROP INDEX  UQ_PE_PE_NAME_ATTRIBUTE_APP_ID;
 
ALTER TABLE csm_protection_element ADD CONSTRAINT UQ_PE_PE_NAME_ATTRIBUTE_VALUE_APP_ID UNIQUE (OBJECT_ID, ATTRIBUTE, ATTRIBUTE_VALUE, APPLICATION_ID);

-- alter general_image
ALTER TABLE general_image add index series_instance_uid_idx (series_instance_uid);
