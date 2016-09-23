/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

delete from annotation where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 927072258);

delete from general_series where patient_pk_id = 927072258;  
  
delete from study where patient_pk_id = 927072258; 

delete from patient where patient_pk_id = 927072258;
