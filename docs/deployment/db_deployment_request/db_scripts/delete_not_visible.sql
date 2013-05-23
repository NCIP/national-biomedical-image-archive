/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create temporary table image_temp
select image_pk_id
from general_image
where visibility='2' and project!='RIDER Pilot'; -- 21145
 

create temporary table series_temp
as select distinct(gs.general_series_pk_id)
   from general_series gs, general_image gi
   where gs.general_series_pk_id=gi.general_series_pk_id 
         and gs.visibility='0'
	 and gi.visibility='2' and gi.project!='RIDER Pilot'; -- 335

create temporary table study_temp
select distinct(s.study_pk_id), s.visibility
   from study s, general_image gi
   where s.study_pk_id=gi.study_pk_id 
         and s.visibility = '0'
	 and gi.visibility='2' and gi.project!='RIDER Pilot'; -- 132

create temporary table patient_temp
select distinct(p.patient_pk_id), p.visibility
   from patient p, general_image gi
   where p.patient_pk_id=gi.patient_pk_id 
         and p.visibility = '0'
	 and gi.visibility='2' and gi.project!='RIDER Pilot'; -- 48

delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, general_image i
where g.image_pk_id = i.image_pk_id 
and visibility='2' 
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
from patient p, patient_temp p2 
where p.patient_pk_id = p2.patient_pk_id ;



drop table patient_temp;
drop table study_temp;
drop table series_temp;
drop table image_temp;



