/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- MAKE SURE YOU DONT LEAVE A DANGLING STUDY!!!

ccreate temporary table study_temp as 
  select study_pk_id 
  from general_series 
  where general_series_pk_id in (1341199072,1341199094,1341199112);
  
create temporary table series_temp as 
  select general_series_pk_id 
  from general_series 
  where general_series_pk_id in (1341199072,1341199094,1341199112);

create temporary table image_temp as 
  select image_pk_id 
  from general_image 
  where general_series_pk_id in (1341199072,1341199094,1341199112);


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
from general_image gi, series_temp st
where gi.general_series_pk_id = st.general_series_pk_id;

delete a
from annotation a, series_temp st
where a.general_series_pk_id = st.general_series_pk_id;

delete s
from general_series s, series_temp st
where s.general_series_pk_id in = st.general_series_pk_id;

delete s 
from study s, study_temp st
where s.study_pk_id=st.study_pk_id;

drop table image_temp;
drop table series_temp;
drop table study_temp;





create temporary table series_temp as 
select max(general_series_pk_id) as series_pk_id, series_instance_uid
from general_series
group by series_instance_uid
having count(series_instance_uid) > 1;

create temporary table image_temp as 
  select image_pk_id 
  from general_image 
  where general_series_pk_id in (select series_pk_id from series_temp);


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
from general_image gi, series_temp st
where gi.general_series_pk_id=st.series_pk_id;

delete a
from annotation a, series_temp st  
where a.general_series_pk_id =st.series_pk_id;

delete gs
from general_series gs, series_temp st
where gs.general_series_pk_id=st.series_pk_id ;

drop table series_temp;
drop table image_temp;
