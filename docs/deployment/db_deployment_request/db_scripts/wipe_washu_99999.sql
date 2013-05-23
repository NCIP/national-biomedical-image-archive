/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create table series_temp as
select distinct gs.general_series_pk_id
from patient p, general_series gs 
where p.trial_dp_pk_id = 1331298304 and
      p.patient_pk_id = gs.patient_pk_id and
      gs.series_number = 99999;


create table image_temp as 
select gi.image_pk_id 
from general_image gi, series_temp st
where gi.general_series_pk_id=st.general_series_pk_id;

delete ct.*
from ct_image ct, image_temp it
where ct.image_pk_id = it.image_pk_id;

delete qa.*
from qa_status_history qa, image_temp it
where qa.general_image_pk_id = it.image_pk_id;

delete gi.*
from general_image gi, image_temp it
where gi.image_pk_id = it.image_pk_id;

delete gs.*
from general_series gs, series_temp st
where gs.general_series_pk_id=st.general_series_pk_id;

drop table image_temp;

drop table series_temp;

/* FYI there's nothing in group9 table, but for some reason this table is cursed and it really
   gets stuck there. because no index? */
   
   
delete s.*
from study s, patient p
where study_instance_uid not in (select study_instance_uid from general_image)  and
      s.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 1331298304;   
      
delete p.*
from patient p
where patient_id not in (select patient_id from general_image)  and
      p.trial_dp_pk_id = 1331298304;     