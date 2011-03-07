Release Notes
=============
 
  #Product:#	NBIA
  #Version:#	5.1
  #Date:#	?????, 2011

Contents
--------

   1. Introduction
   2. Release History
   3. Features Addressed in This Release
   4. Defects Addressed in This Release   
   5. Known Issues/Defects
   6. Bug Reports, Feature Requests, and Support
   7. Documentation and Files
   8. NCI CBIIT Web Pages

Introduction
---------------------------

The National Biomedical Imaging Archive (NBIA) provides access 
to in vivo images. The goal of the NBIA is to provide 
the cancer research community,industry, and academia with 
access to an imaging data archive that will assist in the 
development and validation of analytical software tools 
supporting: lesion detection and classification, accelerated 
diagnostic imaging decision throughput, and quantitative 
imaging assessment of drug response. The repository aims to 
provide access to imaging resources that will improve the use 
of imaging in today’s cancer research and practice by: 
increasing the efficiency and reproducibility of imaging 
supporting cancer detection, improving the accuracy of imaging 
supporting cancer diagnosis, leveraging imaging to provide an 
objective assessment of therapeutic response, and ultimately 
enabling the development of imaging resources that will lead 
to confident clinical decisions in patient care.

* https://imaging.nci.nih.gov/

Release History
------------------------
    * NBIA v5.1         -- ??????   2011
    * NBIA v5.0.1       -- February 2011
    * NBIA v5.0         -- December 2010
    * NBIA v4.5         -- September2010
    * NBIA v4.4.1       -- June     2010
    * NBIA v4.4         -- March    2010
    * NBIA v4.3         -- October  2009
    * NBIA v4.2.2       -- September2009
    * NBIA v4.2.1       -- June     2009
    * NBIA v4.2         -- May      2009
    * NCIA v4.1         -- December 2008
    * NCIA v4.0         -- October  2008
    * NCIA v3.7         -- August   2008
    * NCIA v3.5         -- April    2008
    * NCIA v3.0         -- February 2008
    * NCIA v2.4         -- November 2007


Features Addressed in This Release
----------------------------------
[NBIA-258] Support searching for patients by ultrasound specific criteria in the webapp 
[NBIA-320] Please add a true "select all" feature to the QC tool 
[NBIA-275] Add modality info to web search UI 
[NBIA-276] Improvements to viewing multiple pages of search results 

Defects from 5.0.x Addressed in This Release
--------------------------------------------
[NBIA-339] Long-running online deletion will appear to fail because of 5 minute Apache proxy timeout (at CBIIT) 
[NBIA-337] In the view series page, highlighting of the series after adding to basket can be inconsistent 
[NBIA-333] Remote Search between Oracle and MySQL based NBIA node returns incorrect results for "Available on NBIA" date range query 
[NBIA-324] CQL that targets TrialDataProvenance will not return desired result set 
[NBIA-326] getNumberOfStudyTimePointForPatient returns cryptic error when Patient ID is not found. 
[NBIA-224] Remove storage service port number from the config file installer property files 
[NBIA-286] FTP downloads do not record the download the same way the HTTP and Download Mgr downloads do 
[NBIA-315] Invalid page linked to Help icon on Online Deletion page 


Known Issues/Defects
------------------------

[NBIA-227] IE8 will not be supported for this release.  
[NBIA-310] NBIA Download Manager remote series have completed downloading, but progress bar shows 99%.
[NBIA-316] NBIA Download Manager cannot download annotations when NBIA server is running on Windows.
[NBIA-317] NBIA Download Manager stops up on some Windows XP clients when download directory contains non-8.3 names.
[NBIA-321] Performance issue with deleting a large amount series with online deletion

 
See the Jira tracker for the latest use cases (implemented 
and deferred), existing open defects, community requests, 
resolutions and feature requests. The following issues are 
highlighted. 


https://tracker.nci.nih.gov/browse/NBIA


Bug Reports, Feature Requests, And Support
------------------------------------------

Send email to ncicb@pop.nci.nih.gov to request technical support or 
report a bug or request a new feature.

Existing requests and resolution may also be viewed at the NBIA Jira site.


Documentation And Files
-----------------------

Links to all documentation and files can be found at: 

https://gforge.nci.nih.gov/frs/?group_id=312


NCICB Web Pages
---------------

    * The NCI Center for Bioinformatics, http://ncicb.nci.nih.gov/
    * NCICB Application Support, http://ncicb.nci.nih.gov/NCICB/support
    * NCICB Download Center, http://ncicb.nci.nih.gov/download/
