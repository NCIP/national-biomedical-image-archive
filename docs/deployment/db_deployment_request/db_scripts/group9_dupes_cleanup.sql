/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create table group9Temp as 
  select min(group9_dicom_tags_pk_id)     
  from group9_dicom_tags  
  group by image_pk_id   
  having count(*) > 1;    
    
delete g1
from group9_dicom_tags g1, group9Temp g2 
where g1.group9_dicom_tags_pk_id = g2.group9_dicom_tags_pk_id;
    
drop table group9Temp;
