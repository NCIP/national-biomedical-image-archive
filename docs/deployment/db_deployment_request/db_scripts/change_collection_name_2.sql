/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

update trial_data_provenance set project='RIDER Lung CT' where trial_dp_pk_id=1335656449;
update general_image set project='RIDER Lung CT' where trial_dp_pk_id=1335656449;

update trial_data_provenance set project='RIDER Lung PET CT' where trial_dp_pk_id=1331298304;
update general_image set project='RIDER Lung PET CT' where trial_dp_pk_id=1331298304;

update trial_data_provenance set project='RIDER Neuro MRI' where trial_dp_pk_id=1333952515;
update general_image set project='RIDER Neuro MRI' where trial_dp_pk_id=1333952515;

update trial_data_provenance set project='RIDER Breast MRI' where trial_dp_pk_id=1337557007;
update general_image set project='RIDER Breast MRI' where trial_dp_pk_id=1337557007;

update submission_history set project='RIDER Lung CT' where project='RIDER' and site='MSKCC CTP';

update submission_history set project='RIDER Lung PET CT' where project='RIDER' and site='WashU-IRL';

update submission_history set project='RIDER Neuro MRI' where project='RIDER' and site='DUKE';

update submission_history set project='RIDER Breast MRI' where project='RIDER' and site='UMich';


