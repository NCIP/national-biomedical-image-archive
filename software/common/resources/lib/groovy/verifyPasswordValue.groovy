#!/usr/bin/env groovy

import java.util.regex.Matcher
import java.util.regex.Pattern

def failCharList = ["&","<",">","\"","'"]
println "Checking all properties values who's name contains password to ensure they do not have invalid characters"
println ""
println "Invalid password characters are \$ and " + failCharList.toString()
//System.getProperties().each

properties.each
{ sysprop ->
	//println "Checking ${sysprop.key}"
	if( sysprop.key ==~ /.*database.*password.*/)
	{
		println "${sysprop.key} contains 'password'"
		// issues making $ work in array
		if (sysprop.value ==~/.*\$.*/)
		{
			println "Invalid character in password '\$' found in property '${sysprop.key}'"
			System.exit(1)
		}
		for ( failChar in failCharList )
		{
			if (sysprop.value ==~/.*${failChar}.*/)
			{
				println "Invalid character in password '${failChar}' found in property '${sysprop.key}'"
				System.exit(1)
			}
		}
		println "Password for '${sysprop.key}' does not contain any invalid characters"
		println ""
	}
}
