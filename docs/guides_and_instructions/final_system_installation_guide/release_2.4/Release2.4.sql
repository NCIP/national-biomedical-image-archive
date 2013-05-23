/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

CREATE OR REPLACE VIEW SUBMITTED_IMAGES 
AS(
	SELECT tdp.project, tdp.dp_site_name, 
		trunc(gi.submission_date, 'DDD') as submission_date, 
		tdp.trial_dp_pk_id as trial_dp_pk_id, 
		gi.patient_pk_id as patient_pk_id,
		gi.study_pk_id as study_pk_id, 
		gi.general_series_pk_id as series_pk_id, 
		gi.visibility as visibility,
		count(gi.image_pk_id) as number_of_images
	FROM trial_data_provenance tdp, general_image gi
	WHERE gi.trial_dp_pk_id = tdp.trial_dp_pk_id 
	GROUP BY tdp.project, tdp.dp_site_name, trunc(gi.submission_date, 'DDD'),
		tdp.trial_dp_pk_id, gi.patient_pk_id, gi.study_pk_id, gi.general_series_pk_id, 
		gi.visibility );
