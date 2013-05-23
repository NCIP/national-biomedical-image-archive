/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

CREATE SEQUENCE NCIASTG.CSM_USER_GROU_USER_GROUP_R_SEQ
  START WITH 1425
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


GRANT SELECT ON  NCIASTG.CSM_USER_GROU_USER_GROUP_R_SEQ TO NCIAREPORT;

