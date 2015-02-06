#!/usr/bin/env groovy

import java.util.regex.Matcher
import java.util.regex.Pattern

String propertyBlockFile = args[0]
String fileLocation = args[1]
String propertyPattern="\\s*([\\w\\d\\.\\-\\_]+)=(.*)"
def passedMap= [:]
def readMap= [:]
println "Entering  updatePropertiesService.groovy"

// read passedPropertiesBlock into map
File debugLog = new File("updatePropertiesService.debug")
debugLog.write("")
String passedPropertiesBlock= new File(propertyBlockFile).text

System.setOut(System.out)
debugLog.append("Reading property block from ${propertyBlockFile}\n")
passedPropertiesBlock.eachLine
{ passedLine ->
	//debugLog.append("processing line - ${passedLine}\n")
	passedPropMatcher =java.util.regex.Pattern.compile(propertyPattern).matcher(passedLine)
	passedPropMatcher.find()
	if (passedPropMatcher.matches())
	{
		String passedPropName = passedPropMatcher.group(1).trim()
		String passedPropVal = passedPropMatcher.group(2).trim()
		
		debugLog.append("\t${passedPropName} = ${passedPropVal}\n")
		passedMap.put(passedPropName,passedPropVal)
	}
}

String fileContents = new File(fileLocation).text

debugLog.append("Reading properties from ${fileLocation}\n")

propsMatcher=java.util.regex.Pattern.compile(/(.*<attribute name=.Properties.>)(.*?)(<\/attribute>.*)/, Pattern.DOTALL).matcher(fileContents)
propsMatcher.find()
String replacePropertyBlock = propsMatcher.group(1)
readPropertiesBlock = propsMatcher.group(2)

// read readPropertiesBlock into map
readPropertiesBlock.eachLine
{ readLine ->
	//debugLog.append("processing line - ${readLine}\n")
	rline= readLine.trim()
	readPropMatcher =java.util.regex.Pattern.compile(propertyPattern).matcher(rline)
	if (readPropMatcher.find())
	{
		String readPropName = readPropMatcher.group(1).trim()
		String readPropVal = readPropMatcher.group(2).trim()
	
		debugLog.append("\t${readPropName} = ${readPropVal}\n")
		readMap.put(readPropName,readPropVal)
	}
}

debugLog.append("Adding these properties\n")
readMap.each
{ readProp ->
	if (! passedMap.containsKey(readProp.key))
	{
		debugLog.append("${readProp.key}=${readProp.value}\n")
		replacePropertyBlock = "${replacePropertyBlock}${readProp.key}=${readProp.value}\n"
	}	
}
passedMap.each
{ passedProp ->
	debugLog.append("${passedProp.key}=${passedProp.value}\n")
	replacePropertyBlock ="${replacePropertyBlock}${passedProp.key}=${passedProp.value}\n"
}

replacePropertyBlock=replacePropertyBlock + propsMatcher.group(3)

File outFile = new File(fileLocation)
outFile.write(replacePropertyBlock)

