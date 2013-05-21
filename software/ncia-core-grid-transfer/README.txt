Introduce Generated Service Skeleton:
======================================
This is an Introduce generated service.  

All that is needed for this service at this point is to populate the 
service side implemntation in the <service package>.service.<service name>Impl.java

Prerequisits:
=======================================
Java 1.5 and JAVA_HOME env defined
Ant 1.6.5 and ANT_HOME env defined
Globus 4.0.3 installed and GLOBUS_LOCATION env defined
(optional)Tomcat 5.0.28 installed and "CATALINA_HOME" env defined with globus deployed to it

To Build:
=======================================
"ant all" will build 
"ant deployGlobus" will deploy to "GLOBUS_LOCATION"
"ant deployTomcat" will deploy to "CATALINA_HOME"

===============================
We kept the name "ncia-core-grid-transfer" for the project since this is the
embodiment of NCIACoreService....whose name cannot change without screwing up
existing clients like caIntegrator2.

Beware that while this is Introduce generated, we tweaked a few things to fit
in our unit testing and static analysis processes.  If you regenerate, be
careful not to overwrite these tweaks if you care about such stuff.

-----------------
-----------------
For successfull build using Ant 1.8.x, change the build.xml's name="stubGenerationPostProcessor"> to name="stubGenerationPostProcessor" depends="defineClasspaths"
