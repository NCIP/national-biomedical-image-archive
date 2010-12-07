delete c
from ct_image c, general_image i
where c.image_pk_id = i.image_pk_id and 
      i.trial_dp_pk_id = 297402368;

delete gi
from general_image gi
where gi.trial_dp_pk_id = 297402368;

delete a
from annotation a, general_series s, patient p
where a.general_series_pk_id = s.general_series_pk_id and
      s.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 297402368;

delete a
from image_markup a, general_series s, patient p
where a.general_series_pk_id = s.general_series_pk_id and
      s.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 297402368;

delete gs
from general_series gs, patient p
where gs.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 297402368;

delete s1
from study s1, patient p
where s1.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 297402368;


delete 
from patient
where trial_dp_pk_id = 297402368;

delete 
from trial_data_provenance 
where trial_dp_pk_id = 297402368;






delete c
from ct_image c, general_image i
where c.image_pk_id = i.image_pk_id and 
      i.trial_dp_pk_id = 323649536;

delete gi
from general_image gi
where gi.trial_dp_pk_id = 323649536;

delete a
from annotation a, general_series s, patient p
where a.general_series_pk_id = s.general_series_pk_id and
      s.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 323649536;

delete a
from image_markup a, general_series s, patient p
where a.general_series_pk_id = s.general_series_pk_id and
      s.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 323649536;

delete gs
from general_series gs, patient p
where gs.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 323649536;

delete s1
from study s1, patient p
where s1.patient_pk_id = p.patient_pk_id and
      p.trial_dp_pk_id = 323649536;


delete 
from patient
where trial_dp_pk_id = 323649536;

delete 
from trial_data_provenance 
where trial_dp_pk_id = 323649536;