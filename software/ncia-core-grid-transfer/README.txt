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

