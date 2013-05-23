/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

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