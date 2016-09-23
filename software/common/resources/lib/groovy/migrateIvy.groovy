#!/usr/bin/env groovy
import java.util.regex.Matcher
import java.util.regex.Pattern

class migrateIvyRepos
{
	
	def ivyDepFile
	def migrateOutFile
	def mavenModuleList=[]
	def cagrid12ModuleList=[]
	def cagrid13ModuleList=[]
	def cbiitIvyModuleList=[]
	
	public migrateIvyRepos(mavenFileName, cagrid12FileName, cagrid13FileName, cbiitIvyFileName,ivyFileName,outFile)
	{
		this.ivyDepFile=ivyFileName
		this.migrateOutFile=outFile
		println "Loading Files"
		loadRepoFile(mavenFileName,mavenModuleList)
		loadRepoFile(cbiitIvyFileName,cbiitIvyModuleList)
		loadRepoFile(cagrid12FileName,cagrid12ModuleList)
		loadRepoFile(cagrid13FileName,cagrid13ModuleList)
	}
	public loadRepoFile(fileName, moduleList)
	{
		def file=new File(fileName)
		def i=0
		file.eachLine
		{ line -> 
			i++
			if(line ==~ /^.*\.jar$/)
			{
				//println "adding - " + line
				moduleList.add(line)
			}
		}
		println "Macthed "  + moduleList.size() + " out of " + i
	}
	public processDependencies()
	{
		def xml = new XmlParser().parse(ivyDepFile)
		println "Processing Dependencies"
		new File(migrateOutFile).withWriter
		{ out ->
			xml.dependencies.dependency.each
			{ dep ->
				def org=dep.@org
				def module=dep.@name
				def version=dep.@rev
				def conf=dep.@conf
				def majorVersion=version.split("\\.")[0].plus(".")
				println "\nOld Dependency - " + org + " " + module + " " + version + "(" + majorVersion + ")"
				out.writeLine "\nOld Dependency - " + org + " " + module + " " + version
				def foundMaven=searchRepo(module,version,majorVersion,conf,'maven', mavenModuleList,out)
				def foundCbiit=searchRepo(module,version,majorVersion,conf,'cbiit-ivy', cbiitIvyModuleList,out)
				def foundCagrid12=searchRepo(module,version,majorVersion,conf,'cagrid12', cagrid12ModuleList,out)
				def foundCagrid13=searchRepo(module,version,majorVersion,conf,'cagrid13', cagrid13ModuleList,out)
				//println "${module} FOUND? maven - ${foundMaven} cbiit - ${foundCbiit} cagrid12 - ${foundCagrid12}"
				if (!foundMaven && !foundCbiit && !foundCagrid12 && ! foundCagrid13)
				{
					println "\tNO MATCH FOUND ${org} ${module} ${version}"
					out.writeLine "\tNO MATCH FOUND ${org} ${module} ${version}"
				}
			}
		}
	}
	public searchRepo (module, version, majorVersion, conf, repoName, repoList, outFile)
	{
		def matchList = repoList.grep(~/^.*\/$module\/$version\/.*.jar$/)
			
		println "${module} ${version} ${majorVersion} ${conf} ${repoName} matches-" + matchList.size()
		//outFile.writeLine "\t${module} ${version} ${majorVersion} ${conf} ${repoName} matches-" + matchList.size()
		if (matchList.size() > 0)
		{
			println "\t${repoName} Exact Matches"
			outFile.writeLine "\t${repoName} Exact Matches"
			matchList.each
			{ newDep ->
				//out.writeLine newDep
				def matcher= newDep =~ /^\.\/(.*)\/([^\/]+)\/([^\/]+)\/([^\/]+.jar)$/
				if (matcher.matches())
				{
					//println "matches - " + matcher.groupCount()
					def newOrg=matcher.group(1).replaceAll("\\/","\\.")
					def newModule=matcher.group(2)
					def newVersion=matcher.group(3)
					def artifact=matcher.group(4)
					println "\t\t<dependency org=\"${newOrg}\" name=\"${newModule}\" rev=\"${newVersion}\" conf=\"${conf}\"/>\t${artifact}"
					outFile.writeLine "\t\t<dependency org=\"${newOrg}\" name=\"${newModule}\" rev=\"${newVersion}\" conf=\"${conf}\"/>\t${artifact}"
				}
			}
			return true
		}else
		{
			def m2List = repoList.grep(~/^.*\/$module\/$majorVersion.*\/.*.jar$/)
			if (m2List.size() > 0)
			{
				println "\t${repoName} Major Version Matches"
				outFile.writeLine "\t${repoName} Major Version Matches"
				m2List.each
				{ m2 ->
					//out.writeLine newDep
					def matcher2= m2 =~ /^\.\/(.*)\/([^\/]+)\/([^\/]+)\/([^\/]+.jar)$/
					if (matcher2.matches())
					{
						//println "matches - " + matcher2.groupCount()
						def newOrg=matcher2.group(1).replaceAll("\\/","\\.")
						def newModule=matcher2.group(2)
						def newVersion=matcher2.group(3)
						def artifact=matcher2.group(4)
						println "\t\t<dependency org=\"${newOrg}\" name=\"${newModule}\" rev=\"${newVersion}\" conf=\"${conf}\"/>\t${artifact}"
						outFile.writeLine "\t\t<dependency org=\"${newOrg}\" name=\"${newModule}\" rev=\"${newVersion}\" conf=\"${conf}\"/>\t${artifact}"
					}
				}
				return true
				
			} else
			{

				def m3List = repoList.grep(~/^.*\/$module.*\/$majorVersion.*\/.*.jar$/)
				if (m3List.size() > 0)
				{
					println "\t${repoName} Module Wildcard Matches"
					outFile.writeLine "\t${repoName} Module Wildcard Matches"
					m3List.each
					{ m3 ->
						//out.writeLine newDep
						def matcher3= m3 =~ /^\.\/(.*)\/([^\/]+)\/([^\/]+)\/([^\/]+.jar)$/
						if (matcher3.matches())
						{
							//println "matches - " + matcher2.groupCount()
							def newOrg=matcher3.group(1).replaceAll("\\/","\\.")
							def newModule=matcher3.group(2)
							def newVersion=matcher3.group(3)
							def artifact=matcher3.group(4)
							println "\t\t<dependency org=\"${newOrg}\" name=\"${newModule}\" rev=\"${newVersion}\" conf=\"${conf}\"/>\t${artifact}"
							outFile.writeLine "\t\t<dependency org=\"${newOrg}\" name=\"${newModule}\" rev=\"${newVersion}\" conf=\"${conf}\"/>\t${artifact}"
						}
					}
					return true
				}
				return false
			}
		}
	}
}
def  mivy= new migrateIvyRepos(args[0],args[1],args[2],args[3],args[4],args[5])
mivy.processDependencies()
