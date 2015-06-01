#!/bin/sh
nohup java -Xmx1024m -cp xml-apis-1.0.b2.jar -Djava.ext.dirs="../jai_imageio-1_1/lib/:$JAVA_HOME/jre/lib/ext" -jar Runner.jar &  
