CREATE DATABASE @database.name@ USING CODESET UTF-8 TERRITORY EN ;

CONNECT TO @database.name@ USER @database.system.user@;

create table XMLDS.XMLTABLE (docid varchar(64) NOT NULL, xmlcolumn xml,   metadata xml, primary key (docid) );

disconnect
