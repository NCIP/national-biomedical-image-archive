create table group9Temp as 
  select min(group9_dicom_tags_pk_id)     
  from group9_dicom_tags  
  group by image_pk_id   
  having count(*) > 1;    
    
delete g1
from group9_dicom_tags g1, group9Temp g2 
where g1.group9_dicom_tags_pk_id = g2.group9_dicom_tags_pk_id;
    
drop table group9Temp;
