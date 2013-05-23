/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create temporary table patient_temp as 
select p.patient_pk_id
from patient p, trial_data_provenance tdp
where p.trial_dp_pk_id = tdp.trial_dp_pk_id and
      tdp.trial_dp_pk_id = 1329496064;
      
create temporary table study_temp as select study_pk_id from study where patient_pk_id in (select * from patient_temp);

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete from group9_dicom_tags 
where image_pk_id in (select image_pk_id from general_image where trial_dp_pk_id=1329496064);

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
from patient p1, patient_temp p2
where p1.patient_pk_id = p2.patient_pk_id;

drop table study_temp;
drop table series_temp;
drop table image_temp;
drop table patient_temp;