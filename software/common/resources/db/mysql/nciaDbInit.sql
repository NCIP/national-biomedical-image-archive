/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- USE `ncia`;

insert into csm_application(APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,UPDATE_DATE)
values ("csmupt","CSM UPT Super Admin Application",0,0,sysdate());

insert into csm_user (LOGIN_NAME,FIRST_NAME,LAST_NAME,EMAIL_ID,PASSWORD,UPDATE_DATE)
values ("nciadevtest","User","Test","", "zJPWCwDeSgG8j2uyHEABIQ==",sysdate());
 
insert into csm_protection_element(PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID,UPDATE_DATE)
values("csmupt","CSM UPT Super Admin Application Protection Element","csmupt",1,sysdate());

insert into csm_user_pe(PROTECTION_ELEMENT_ID,USER_ID,UPDATE_DATE)
values(1,1,sysdate());

INSERT INTO csm_application(APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,UPDATE_DATE)
VALUES ("NCIA","Application Description",0,0,sysdate());

insert into csm_protection_element(PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID,UPDATE_DATE)
values("NCIA","NCIA Admin Application Protection Element","NCIA",1,sysdate());

INSERT INTO csm_role ( ROLE_ID, ROLE_NAME, ROLE_DESCRIPTION, APPLICATION_ID, ACTIVE_FLAG, UPDATE_DATE ) 
VALUES (2, 'NCIA.ADMIN', 'UPT access role', 2, 0, sysdate());

INSERT INTO csm_role ( ROLE_ID, ROLE_NAME, ROLE_DESCRIPTION, APPLICATION_ID, ACTIVE_FLAG, UPDATE_DATE ) 
VALUES (3, 'NCIA.CURATE', 'Add or modify curation data', 2, 0, sysdate());

INSERT INTO csm_role ( ROLE_ID, ROLE_NAME, ROLE_DESCRIPTION, APPLICATION_ID, ACTIVE_FLAG, UPDATE_DATE ) 
VALUES ( 4, 'NCIA.MANAGE_VISIBILITY_STATUS', 'Manage Visibility Status', 2, 0, sysdate());

INSERT INTO csm_role ( ROLE_ID, ROLE_NAME, ROLE_DESCRIPTION, APPLICATION_ID, ACTIVE_FLAG, UPDATE_DATE ) 
VALUES (5, 'NCIA.VIEW_SUBMISSION_REPORT', 'View submission report', 2, 0, sysdate());

INSERT INTO csm_role ( ROLE_ID, ROLE_NAME, ROLE_DESCRIPTION, APPLICATION_ID, ACTIVE_FLAG, UPDATE_DATE ) 
VALUES (1, 'NCIA.READ', 'public role', 2, 0, sysdate());

INSERT INTO csm_protection_group( PROTECTION_GROUP_ID, PROTECTION_GROUP_NAME, PROTECTION_GROUP_DESCRIPTION, APPLICATION_ID, LARGE_ELEMENT_COUNT_FLAG, UPDATE_DATE) 
VALUES ( 1, 'NCIA.PUBLIC', 'Public', 2, 0, sysdate());

INSERT INTO csm_group (GROUP_NAME, GROUP_DESC, UPDATE_DATE, APPLICATION_ID)
VALUES ("PUBLIC-GRID", "Public Access for Grid users", sysdate(), 2);

INSERT INTO csm_user_group_role_pg (GROUP_ID, ROLE_ID, PROTECTION_GROUP_ID, UPDATE_DATE) 
VALUES (1, 1, 1, sysdate());

#
# The following entries are Common Set of Privileges
#

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("CREATE","This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection", sysdate());

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("ACCESS","This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself", sysdate());

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("READ","This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry", sysdate());

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("WRITE","This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity", sysdate());

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("UPDATE","This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc", sysdate());

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("DELETE","This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc", sysdate());

INSERT INTO csm_privilege (privilege_name, privilege_description, update_date)
VALUES("EXECUTE","This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc", sysdate());

insert into csm_user_pe(PROTECTION_ELEMENT_ID,USER_ID,UPDATE_DATE)
values(2,1,sysdate());

insert into csm_role_privilege(role_privilege_id, role_id, privilege_id, update_date) 
values(1,1,3,sysdate());

insert into hibernate_unique_key (next_hi) values (1);
# The following 2 insert statments are added for UAT to handle the new installation of NBIA 
# which database only has only one default user created as the ADMIN of NBIA 
INSERT INTO csm_user_group_role_pg (USER_ID, ROLE_ID, PROTECTION_GROUP_ID, UPDATE_DATE) 
VALUES (1, 2, 1, sysdate());

INSERT INTO csm_pg_pe (PROTECTION_GROUP_ID, PROTECTION_ELEMENT_ID, UPDATE_DATE) 
VALUES (1, 2, sysdate());

COMMIT;

