export PATH=/usr/local/jboss-5.1.0.GA/server/ncicb-29080/DBUpdater:$PATH
export CLASSPATH=$CLASSPATH:${PWD}/nbiaDatabaseUpdator.jar
echo $CLASSPATH
echo $PATH
java -cp nbiaDatabaseUpdator.jar gov.nih.nci.nbia.util.DicomFileSizeCorrectorUtil 
echo starting another process
java -cp nbiaDatabaseUpdator.jar gov.nih.nci.nbia.util.MRUtil 
