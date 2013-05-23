/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- move this case back to idri per data owner request
update patient set trial_dp_pk_id=8 where  patient_id='1.3.6.1.4.1.9328.50.3.0496';

update general_image set trial_dp_pk_id=8, project='IDRI' where  patient_id='1.3.6.1.4.1.9328.50.3.0496';