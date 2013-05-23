/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

      
create temporary table study_temp as select study_pk_id from study where patient_pk_id in
(1316159488,                                                                                                                                                                             
1318486065,                                                                                                                                                                                
1329037328,                                                                                                                                                                              
1329037318,                                                                                                                                                                                
1329037327,                                                                                                                                                                               
1329037343,                                                                                                                                                                                
1329037345,                                                                                                                                                                                
1329037361,                                                                                                                                                                                
1329037364,                                                                                                                                                                               
1329037365,                                                                                                                                                                                
1329037368,                                                                                                                                                                                
1329037414,                                                                                                                                                                               
1334607944,                                                                                                                                                                              
1334607983,                                                                                                                                                                               
1334607984,                                                                                                                                                                                
1334607985,                                                                                                                                                                               
1334607986,                                                                                                                                                                              
1334607987,                                                                                                                                                                               
1334607988,                                                                                                                                                                                
1334607989,                                                                                                                                                                               
1334607990,                                                                                                                                                                                
1334607991,                                                                                                                                                                               
1334607992,                                                                                                                                                                               
1334607993,                                                                                                                                                                             
1334607994,                                                                                                                                                                           
1334607995,                                                                                                                                                                            
1334608113);

create temporary table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

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
from general_series gs, study_temp s
where gs.study_pk_id = s.study_pk_id; 

delete s1
from study s1, study_temp s2
where s1.study_pk_id = s2.study_pk_id;

delete p1
from patient p1 where p1.patient_pk_id in
(1316159488,                                                                                                                                                                             
1318486065,                                                                                                                                                                                
1329037328,                                                                                                                                                                              
1329037318,                                                                                                                                                                                
1329037327,                                                                                                                                                                               
1329037343,                                                                                                                                                                                
1329037345,                                                                                                                                                                                
1329037361,                                                                                                                                                                                
1329037364,                                                                                                                                                                               
1329037365,                                                                                                                                                                                
1329037368,                                                                                                                                                                                
1329037414,                                                                                                                                                                               
1334607944,                                                                                                                                                                              
1334607983,                                                                                                                                                                               
1334607984,                                                                                                                                                                                
1334607985,                                                                                                                                                                               
1334607986,                                                                                                                                                                              
1334607987,                                                                                                                                                                               
1334607988,                                                                                                                                                                                
1334607989,                                                                                                                                                                               
1334607990,                                                                                                                                                                                
1334607991,                                                                                                                                                                               
1334607992,                                                                                                                                                                               
1334607993,                                                                                                                                                                             
1334607994,                                                                                                                                                                           
1334607995,                                                                                                                                                                            
1334608113);



drop table study_temp;
drop table series_temp;
drop table image_temp;
