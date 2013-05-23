/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

delete 
from ct_image
where image_pk_id in (select image_pk_id from general_image where trial_dp_pk_id = 297402368);

delete 
from general_image gi
where gi.trial_dp_pk_id = 297402368;

delete 
from annotation a
where a.general_series_pk_id in 
  (select gs.general_series_pk_id 
   from general_series gs, patient p
   where gs.patient_pk_id = p.patient_pk_id and
         p.trial_dp_pk_id = 297402368);

delete 
from image_markup 
where general_series_pk_id in 
    (select s.general_series_pk_id
	   from general_series s, patient p
	   where s.patient_pk_id = p.patient_pk_id and
           p.trial_dp_pk_id = 297402368);

delete 
from general_series 
where patient_pk_id in 
    (select patient_pk_id
	   from patient p
     where p.trial_dp_pk_id = 297402368);

delete 
from study 
where patient_pk_id in
    (select patient_pk_id 
	   from patient
     where trial_dp_pk_id = 297402368);


delete 
from patient
where trial_dp_pk_id = 297402368;

delete 
from trial_data_provenance 
where trial_dp_pk_id = 297402368;





delete 
from ct_image
where image_pk_id in (select image_pk_id from general_image where trial_dp_pk_id = 323649536);

delete 
from general_image gi
where gi.trial_dp_pk_id = 323649536;

delete 
from annotation a
where a.general_series_pk_id in 
  (select gs.general_series_pk_id 
   from general_series gs, patient p
   where gs.patient_pk_id = p.patient_pk_id and
         p.trial_dp_pk_id = 323649536);

delete 
from image_markup 
where general_series_pk_id in 
    (select s.general_series_pk_id
	   from general_series s, patient p
	   where s.patient_pk_id = p.patient_pk_id and
           p.trial_dp_pk_id = 323649536);

delete 
from general_series 
where patient_pk_id in 
    (select patient_pk_id
	   from patient p
     where p.trial_dp_pk_id = 323649536);

delete 
from study 
where patient_pk_id in
    (select patient_pk_id 
	   from patient
     where trial_dp_pk_id = 323649536);


delete 
from patient
where trial_dp_pk_id = 323649536;

delete 
from trial_data_provenance 
where trial_dp_pk_id = 323649536;