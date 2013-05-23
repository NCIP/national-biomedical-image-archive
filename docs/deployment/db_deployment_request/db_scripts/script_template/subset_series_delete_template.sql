/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

      
-- make sure this deletion won't leave a study that does NOT have any series

create temporary table series_temp as select general_series_pk_id from general_series where series_instance_uid=***SEIRES_INSTANCE_UID***;

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
where gs.series_instance_uid=***SEIRES_INSTANCE_UID***; 


drop table series_temp;
drop table image_temp;
