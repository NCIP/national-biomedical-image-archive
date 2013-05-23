/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

      
create temporary table image_temp as select image_pk_id from general_image where instance_number >=250 and series_instance_uid='1.3.6.1.4.1.9328.50.1.245630263591502535745452645381329674063';

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
from general_image gi, image_temp i
where gi.image_pk_id = i.image_pk_id;

drop table image_temp;
