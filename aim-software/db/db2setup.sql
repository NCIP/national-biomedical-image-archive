/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

CREATE DATABASE @database.name@ USING CODESET UTF-8 TERRITORY EN ;

CONNECT TO @database.name@ USER @database.system.user@;

create table XMLDS.XMLTABLE (docid varchar(64) NOT NULL, xmlcolumn xml,   metadata xml, primary key (docid) );

disconnect
