/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create temporary table image_temp as 
select image_pk_id 
from general_image 
where general_series_pk_id = 1329103012;


delete c
from ct_image c, image_temp i
where c.image_pk_id = i.image_pk_id;

delete q
from qa_status_history q, image_temp i
where q.general_image_pk_id = i.image_pk_id;

delete g
from group9_dicom_tags g, image_temp i
where g.image_pk_id = i.image_pk_id;

delete 
from general_image
where general_series_pk_id = 1329103012;

delete 
from annotation
where general_series_pk_id = 1329103012;

delete 
from general_series 
where general_series_pk_id = 1329103012; 


drop table image_temp;