/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- Create table
create table CLINICAL_TRIAL_SPONSOR
(
  ID                  NUMBER not null,
  COORDINATING_CENTER VARCHAR2(255) not null,
  SPONSOR_NAME        VARCHAR2(255) not null
)
tablespace NCIA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints
alter table CLINICAL_TRIAL_SPONSOR
  add constraint PK_CLINICAL_TRIAL_SPONSOR_ID primary key (ID)
  using index
  tablespace NCIA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

-- Create table
create table CLINICAL_TRIAL_PROTOCOL
(
  ID                        NUMBER not null,
  PROTOCOL_ID               VARCHAR2(255) not null,
  PROTOCOL_NAME             VARCHAR2(1000) not null,
  CLINICAL_TRIAL_SPONSOR_ID NUMBER not null
)
tablespace NCIA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

-- Create/Recreate primary, unique and foreign key constraints
alter table CLINICAL_TRIAL_PROTOCOL
  add constraint PK_CLINICAL_TRIAL_PROTOCOL_ID primary key (ID)
  using index
  tablespace NCIA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table CLINICAL_TRIAL_PROTOCOL
  add constraint FK_CLIN_TRIAL_SPONSOR_ID foreign key (CLINICAL_TRIAL_SPONSOR_ID)
  references CLINICAL_TRIAL_SPONSOR (ID);
-- Create table
create table CLINICAL_TRIAL_SUBJECT
(
  ID                         NUMBER not null,
  TRIAL_SUBJECT_ID           VARCHAR2(255) not null,
  TRIAL_SUBJECT_READING_ID   VARCHAR2(255) not null,
  CLINICAL_TRIAL_PROTOCOL_ID NUMBER not null,
  PATIENT_ID                 NUMBER not null,
  TRIAL_SITE_ID              NUMBER not null
)
tablespace NCIA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints
alter table CLINICAL_TRIAL_SUBJECT
  add constraint PK_CLINICAL_TRIAL_SUBJECT_ID primary key (ID)
  using index
  tablespace NCIA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table CLINICAL_TRIAL_SUBJECT
  add constraint FK_CLINICAL_TRIAL_PROTOCOL_ID foreign key (CLINICAL_TRIAL_PROTOCOL_ID)
  references CLINICAL_TRIAL_PROTOCOL (ID);
alter table CLINICAL_TRIAL_SUBJECT
  add constraint FK_PATIENT_ID foreign key (PATIENT_ID)
  references PATIENT (PATIENT_PK_ID);
alter table CLINICAL_TRIAL_SUBJECT
  add constraint FK_TRIAL_SITE foreign key (TRIAL_SITE_ID)
  references TRIAL_SITE (TRIAL_SITE_PK_ID);

-- Add/modify columns
alter table GENERAL_EQUIPMENT add INSTITUTION_ADDRESS varchar2(1024);
alter table GENERAL_EQUIPMENT add STATION_NAME varchar2(16);
alter table GENERAL_EQUIPMENT add DEVICE_SERIAL_NUMBER varchar2(64);

-- create view

create or replace view IMAGE as
  select gi.*,cti.kvp,cti.scan_options,cti.data_collection_diameter,cti.reconstruction_diameter,cti.gantry_detector_tilt,cti.exposure_time,
  cti.x_ray_tube_current,cti.exposure,cti.exposure_in_microas,cti.convolution_kernel,cti.revolution_time,cti.single_collimation_width,
  cti.total_collimation_width,cti.table_speed,cti.table_feed_per_rotation,cti.ct_pitch_factor,cti.anatomic_region_seq
    from general_image gi,ct_image cti
    where gi.image_pk_id = cti.image_pk_id


/* Scripts for Markup persistence */

CREATE TABLE IMAGE_MARKUP
(
  IMAGE_MARKUP_PK_ID    NUMBER                  NOT NULL,
  SERIES_INSTANCE_UID   VARCHAR2(64),
  GENERAL_SERIES_PK_ID  NUMBER,
  LOGIN_NAME            VARCHAR2(300),
  MARKUP_CONTENT        CLOB,
  SUBMISSION_DATE       DATE
)
TABLESPACE NCIA
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
  LOB (MARKUP_CONTENT) STORE AS 
      ( TABLESPACE  NCIA 
        ENABLE      STORAGE IN ROW
        CHUNK       8192
        PCTVERSION  10
        NOCACHE
        STORAGE    (
                    INITIAL          64K
                    MINEXTENTS       1
                    MAXEXTENTS       2147483645
                    PCTINCREASE      0
                    BUFFER_POOL      DEFAULT
                   )
      )
NOCACHE
NOPARALLEL;


CREATE UNIQUE INDEX PK_IMAGE_MARKUP ON IMAGE_MARKUP
(IMAGE_MARKUP_PK_ID)
LOGGING
TABLESPACE NCIA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


ALTER TABLE IMAGE_MARKUP ADD (
  CONSTRAINT PK_IMAGE_MARKUP PRIMARY KEY (IMAGE_MARKUP_PK_ID)
    USING INDEX 
    TABLESPACE NCIA
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

CREATE SEQUENCE SEQ_IMAGE_MARKUP INCREMENT BY 1 START WITH 2 MINVALUE 1 NOCYCLE NOCACHE NOORDER;

--REQUIRED FOR CSM 3.2 UPDATE TO ORACLE SCHEMA

ALTER TABLE CSM_APPLICATION MODIFY  DECLARATIVE_FLAG NUMBER(1) DEFAULT 0 NOT NULL ;
ALTER TABLE CSM_PROTECTION_ELEMENT DROP COLUMN PROTECTION_ELEMENT_TYPE_ID;
ALTER TABLE CSM_PROTECTION_ELEMENT ADD PROTECTION_ELEMENT_TYPE VARCHAR(100);
ALTER TABLE CSM_PG_PE MODIFY  UPDATE_DATE DATE NULL;
ALTER TABLE CSM_PROTECTION_ELEMENT ADD ( CONSTRAINT UQ_PE_OBJ_ID_ATTR_APP_ID UNIQUE (PROTECTION_ELEMENT_NAME, ATTRIBUTE, APPLICATION_ID));

ALTER TABLE CSM_APPLICATION MODIFY APPLICATION_NAME varchar2(255) ;
ALTER TABLE CSM_GROUP MODIFY GROUP_NAME varchar2(255) ;
ALTER TABLE CSM_APPLICATION ADD DATABASE_URL VARCHAR(100);
ALTER TABLE CSM_APPLICATION ADD DATABASE_USER_NAME VARCHAR(100);
ALTER TABLE CSM_APPLICATION ADD DATABASE_PASSWORD VARCHAR(100);
ALTER TABLE CSM_APPLICATION ADD DATABASE_DIALECT VARCHAR(100);
ALTER TABLE CSM_APPLICATION ADD DATABASE_DRIVER VARCHAR(100);

UPDATE csm_application SET csm_application.DECLARATIVE_FLAG='0';
UPDATE csm_pg_pe SET UPDATE_DATE=NULL;

ALTER TABLE CSM_USER_GROUP_ROLE_PG DROP CONSTRAINT FK_USER_GROUP_ROLE_PG_GROUP;

ALTER TABLE CSM_USER_GROUP_ROLE_PG ADD ( CONSTRAINT FK_USER_GROUP_ROLE_PG_GROUP FOREIGN KEY (GROUP_ID) REFERENCES CSM_GROUP (GROUP_ID) ON DELETE CASCADE );

update csm_application set application_name='csmupt' where application_id=2;

update csm_protection_element set object_id='csmupt', protection_element_name='csmupt' where protection_element_id=1;
commit;
