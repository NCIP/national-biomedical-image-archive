/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

      
-- make sure this deletion won't leave a study that does NOT have any series

create temporary table series_temp as select general_series_pk_id 
from general_series 
where series_instance_uid='1.3.6.1.4.1.9328.50.45.283996776356102921066898929519313269921';

create temporary table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


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
from general_series gs
where gs.series_instance_uid='1.3.6.1.4.1.9328.50.45.283996776356102921066898929519313269921'; 


drop table series_temp;
drop table image_temp;


create table patient_temp as   
  select p.patient_pk_id 
  from study s, patient p 
  where s.patient_pk_id = p.patient_pk_id and p.trial_dp_pk_id in (1329496064,1334640643);

update patient
set visibility = '1'
where trial_dp_pk_id in (1329496064,1334640643);

update study
set visibility = '1'
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);

update general_series
set visibility = '1'
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);
  
update general_image
set visibility = '1', curation_timestamp=CURRENT_TIMESTAMP()
where visibility != '1' and patient_pk_id in (
  select patient_pk_id 
  from patient_temp);

drop table patient_temp;
