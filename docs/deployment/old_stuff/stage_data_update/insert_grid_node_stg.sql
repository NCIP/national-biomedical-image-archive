/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

Insert into grid_node
   (GRID_NODE_PK_ID, NODE_NAME, URL, NODE_DESCRIPTION)
 Values
   (1, 'QA', 'http://137.187.182.164:59080/wsrf/services/cagrid/NCIAQueryService', 'QA');
COMMIT;