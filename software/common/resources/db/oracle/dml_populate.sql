/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

insert  into csm_application(APPLICATION_ID,APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,UPDATE_DATE,DATABASE_URL,DATABASE_USER_NAME,DATABASE_PASSWORD,DATABASE_DIALECT,DATABASE_DRIVER) values (1,'csmupt','CSM UPT Super Admin Application',0,0,to_date('2010-10-11','YYYY-MM-DD'),NULL,NULL,NULL,NULL,NULL)
;
insert  into csm_application(APPLICATION_ID,APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,UPDATE_DATE,DATABASE_URL,DATABASE_USER_NAME,DATABASE_PASSWORD,DATABASE_DIALECT,DATABASE_DRIVER) values (2,'NCIA','Application Description',0,0,to_date('2010-10-11','YYYY-MM-DD'),NULL,NULL,NULL,NULL,NULL)
;
insert  into csm_group(GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID) values (1,'PUBLIC-GRID','Public Access for Grid users',to_date('2010-10-11','YYYY-MM-DD'),2)
;
insert  into csm_pg_pe(PG_PE_ID,PROTECTION_GROUP_ID,PROTECTION_ELEMENT_ID,UPDATE_DATE) values (1,2,3,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (1,'CREATE','This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (2,'ACCESS','This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (3,'READ','This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (4,'WRITE','This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (5,'UPDATE','This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (6,'DELETE','This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_privilege(PRIVILEGE_ID,PRIVILEGE_NAME,PRIVILEGE_DESCRIPTION,UPDATE_DATE) values (7,'EXECUTE','This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc',to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_protection_element(PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,ATTRIBUTE,PROTECTION_ELEMENT_TYPE,APPLICATION_ID,UPDATE_DATE,ATTRIBUTE_VALUE) values (1,'csmupt','CSM UPT Super Admin Application Protection Element','csmupt',NULL,NULL,1,to_date('2010-10-11','YYYY-MM-DD'),NULL)
;
insert  into csm_protection_element(PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,ATTRIBUTE,PROTECTION_ELEMENT_TYPE,APPLICATION_ID,UPDATE_DATE,ATTRIBUTE_VALUE) values(2,'NCIA','NCIA Admin Application Protection Element','NCIA',NULL,NULL,1,to_date('2010-10-11','YYYY-MM-DD'),NULL)
;
insert  into csm_protection_element(PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,ATTRIBUTE,PROTECTION_ELEMENT_TYPE,APPLICATION_ID,UPDATE_DATE,ATTRIBUTE_VALUE) values(3,'NCIA.Common_Element','','NCIA.Common_Element',NULL,NULL,2,to_date('2010-10-11','YYYY-MM-DD'),NULL)
;
insert  into csm_protection_group(PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID) values (1,'NCIA.PUBLIC','Public',2,0,to_date('2010-10-11','YYYY-MM-DD'),NULL)
;
insert  into csm_protection_group(PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID) values(2,'NCIA.Common_PG','',2,0,to_date('2010-10-11','YYYY-MM-DD'),NULL)
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (1,'NCIA.READ','public role',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (2,'NCIA.ADMIN','UPT access role',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (3,'NCIA.CURATE','Add or modify curation data',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (4,'NCIA.MANAGE_VISIBILITY_STATUS','Manage Visibility Status',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (5,'NCIA.VIEW_SUBMISSION_REPORT','View submission report',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (6,'NCIA.SUPER_CURATOR','Super Admin for approving deletion',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (7,'NCIA.DELETE_ADMIN','Super Admin for deletion',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role(ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) values (8,'NCIA.MANAGE_COLLECTION_DESCRIPTION','Manage collection description',2,0,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_role_privilege(ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID,UPDATE_DATE) values (1,1,3,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_user(USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE,MIDDLE_NAME,FAX,ADDRESS,CITY,STATE,COUNTRY,POSTAL_CODE) values (1,'nciadevtest','User','Test',NULL,NULL,NULL,NULL,'zJPWCwDeSgG8j2uyHEABIQ==','',NULL,NULL,to_date('2010-10-11','YYYY-MM-DD'),NULL,NULL,NULL,NULL,NULL,NULL,NULL)
;
insert  into csm_user_group_role_pg(USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE) values (1,NULL,1,1,1,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_user_pe(USER_PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_ID,USER_ID,UPDATE_DATE) values (1,1,1,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into csm_user_pe(USER_PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_ID,USER_ID,UPDATE_DATE) values (2,2,1,to_date('2010-10-11','YYYY-MM-DD'))
;
insert  into hibernate_unique_key(next_hi) values (1)
;
