/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

/*
select series_instance_uid
from general_series
group by series_instance_uid
having count(patient_id)>1

reveals the dupes.  just picking to save the patient with the higher number.

112427012=SD VC-316M
133234688=SD VC-475M
27066368=WRAMC VC-107M
103481344=SD VC-127M
46628865=WRAMC VC-267M

this will show pk ids:

select patient_pk_id, patient_id, series_instance_uid
from general_series
where series_instance_uid in (
'1.3.6.1.4.1.9328.50.12.478612',
'1.3.6.1.4.1.9328.50.14.5094',
'1.3.6.1.4.1.9328.50.45.168043460065405170743472371120220696212',
'1.3.6.1.4.1.9328.50.6.176007',
'1.3.6.1.4.1.9328.50.6.176324',
'1.3.6.1.4.1.9328.50.6.286865',
'1.3.6.1.4.1.9328.50.6.287289',
'1.3.6.1.4.1.9328.50.6.61220',
'1.3.6.1.4.1.9328.50.6.61841',
'1.3.6.1.4.1.9328.50.99.222364',
'1.3.6.1.4.1.9328.50.99.222371',
'1.3.6.1.4.1.9328.50.99.86496',
'1.3.6.1.4.1.9328.50.99.86923'
)
*/

/* -----------------------------------------------------------------*/
create temporary table study_temp as select study_pk_id from study where patient_pk_id=112427012;

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from
curation_data
where patient_pk_id=112427012;

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, image_temp i
where g.image_pk_id = i.image_pk_id;

delete gi
from general_image gi, series_temp s
where gi.general_series_pk_id = s.general_series_pk_id;

delete a
from annotation a, series_temp s 
where a.general_series_pk_id = s.general_series_pk_id;

delete gs
from general_series gs, study_temp s
where gs.study_pk_id = s.study_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p1
from patient p1 where p1.patient_pk_id = 112427012;



drop table study_temp;
drop table series_temp;
drop table image_temp;
/* -----------------------------------------------------------------*/

/* -----------------------------------------------------------------*/
create temporary table study_temp as select study_pk_id from study where patient_pk_id=133234688;

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from
curation_data
where patient_pk_id=133234688;

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, image_temp i
where g.image_pk_id = i.image_pk_id;

delete gi
from general_image gi, series_temp s
where gi.general_series_pk_id = s.general_series_pk_id;

delete a
from annotation a, series_temp s 
where a.general_series_pk_id = s.general_series_pk_id;

delete gs
from general_series gs, study_temp s
where gs.study_pk_id = s.study_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p1
from patient p1 where p1.patient_pk_id = 133234688;



drop table study_temp;
drop table series_temp;
drop table image_temp;
/* -----------------------------------------------------------------*/

/* -----------------------------------------------------------------*/
create temporary table study_temp as select study_pk_id from study where patient_pk_id=27066368;

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from
curation_data
where patient_pk_id=27066368;

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, image_temp i
where g.image_pk_id = i.image_pk_id;

delete gi
from general_image gi, series_temp s
where gi.general_series_pk_id = s.general_series_pk_id;

delete a
from annotation a, series_temp s 
where a.general_series_pk_id = s.general_series_pk_id;

delete gs
from general_series gs, study_temp s
where gs.study_pk_id = s.study_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p1
from patient p1 where p1.patient_pk_id = 27066368;



drop table study_temp;
drop table series_temp;
drop table image_temp;
/* -----------------------------------------------------------------*/

/* -----------------------------------------------------------------*/
create temporary table study_temp as select study_pk_id from study where patient_pk_id=103481344;

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from
curation_data
where patient_pk_id=103481344;

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, image_temp i
where g.image_pk_id = i.image_pk_id;

delete gi
from general_image gi, series_temp s
where gi.general_series_pk_id = s.general_series_pk_id;

delete a
from annotation a, series_temp s 
where a.general_series_pk_id = s.general_series_pk_id;

delete gs
from general_series gs, study_temp s
where gs.study_pk_id = s.study_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p1
from patient p1 where p1.patient_pk_id = 103481344;



drop table study_temp;
drop table series_temp;
drop table image_temp;
/* -----------------------------------------------------------------*/

/* -----------------------------------------------------------------*/
create temporary table study_temp as select study_pk_id from study where patient_pk_id=46628865;

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from
curation_data
where patient_pk_id=46628865;

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, image_temp i
where g.image_pk_id = i.image_pk_id;

delete gi
from general_image gi, series_temp s
where gi.general_series_pk_id = s.general_series_pk_id;

delete a
from annotation a, series_temp s 
where a.general_series_pk_id = s.general_series_pk_id;

delete gs
from general_series gs, study_temp s
where gs.study_pk_id = s.study_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p1
from patient p1 where p1.patient_pk_id = 46628865;



drop table study_temp;
drop table series_temp;
drop table image_temp;
/* -----------------------------------------------------------------*/