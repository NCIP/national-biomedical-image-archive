Introduce Generated Service Skeleton:
======================================
This is an Introduce generated service.  

All that is needed for this service at this point is to populate the 
service side implementation in the <service package>.service.<service name>Impl.java

Prerequisites:
=======================================
Java 1.5 and JAVA_HOME env defined
Ant 1.7.0 and ANT_HOME env defined
Globus 4.0.3 installed and GLOBUS_LOCATION env defined
(optional) Tomcat 5.5.27 installed and "CATALINA_HOME" env defined with Globus deployed to it
(optional) JBoss 4.0.5.GA installed and "JBOSS_HOME" env defined with Globus deployed to it


To Build:
=======================================
"ant all" will build

"ant deployGlobus" will deploy to "GLOBUS_LOCATION"
"ant undeployGlobus" will undeploy from "GLOBUS_LOCATION"

"ant deployTomcat" will deploy to "CATALINA_HOME"
"ant undeployTomcat" will undeploy from "CATALINA_HOME"

"ant deployJBoss" will deploy to "JBOSS_HOME"
"ant undeployJBoss" will undeploy from "JBOSS_HOME"