/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

spool c:\temp\acrin_collection_name_change.log

update general_image gi set gi.project='CT Colonography' where gi.TRIAL_DP_PK_ID = 3;

update trial_data_provenance tdp set tdp.project='CT Colonography' where tdp.trial_dp_pk_id=3;

commit;

spool off;