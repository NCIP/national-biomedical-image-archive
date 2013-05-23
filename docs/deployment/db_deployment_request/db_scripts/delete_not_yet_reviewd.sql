/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- create a temp table contains only series instance uid that it has visibility=0 (Not yet reviewed) images (the series's visibility might be NULL, and 1)
create temporary table image_temp 
as select image_pk_id 
   from general_image 
   where visibility=0 and project != 'RIDER Pilot'; --  50937

-- create a temp table contains only series instance uid that it has visibility=0 (Not yet reviewed) images (the series's visibility might be NULL, and 1)
create temporary table series_temp 
as select distinct general_series_pk_id 
   from general_image 
   where visibility=0 and project != 'RIDER Pilot'; -- 435 

-- create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);
create temporary table study_temp 
as select distinct study_pk_id 
   from general_series 
   where general_series_pk_id in (select * from series_temp); -- 237

-- create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);
create temporary table patient_temp 
as select distinct patient_pk_id 
   from study 
   where study_pk_id in (select * from study_temp); -- 111

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, general_image i
where g.image_pk_id = i.image_pk_id 
and visibility=0 
and project != 'RIDER Pilot';

delete gi
from general_image gi, image_temp it
where gi.image_pk_id = it.image_pk_id;

delete a
from annotation a, series_temp s 
where a.general_series_pk_id = s.general_series_pk_id;

delete gs
from general_series gs, series_temp st
where gs.general_series_pk_id = st.general_series_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p
from patient p, patient_temp p2 where p.patient_pk_id = p2.patient_pk_id ;


drop table patient_temp;
drop table study_temp;
drop table series_temp;
drop table image_temp;


