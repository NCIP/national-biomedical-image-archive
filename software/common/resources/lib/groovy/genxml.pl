#!/usr/bin/perl

use Switch;
print "<properties>\n";
my $skipProp,$prop,$default;
while (my $line = <>)
{
	chomp($line);
	if ($line =~ /^([\d\w\\.\-\_]+)=(.*)/)
	{
		$prop=$1;
		if ($prop eq "jboss.java.opts")
		{
			print "\t<!-- java.opts is to complex for rules -->\n";
			next;
		}

		$default=$2;
		switch ($prop)
		{
			case /jboss/ {$skipProp="exclude.validate.jboss"}
			case /tomcat/ {$skipProp="exclude.validate.tomcat"}
			case /database/ {$skipProp="exclude.validate.database"}
			case /grid/ {$skipProp="exclude.validate.grid"}
			else {$skipProp=""}
		}


		#print "\t<!--\n";
		print "\t<property name=\"${prop}\" description=\"\" skip-property=\"${skipProp}\">\n";
		print "\t\t<rules>\n";
		print "\t\t\t<rule>\n";
		print "\t\t\t\t<name>${prop}-syntax</name>\n";
		print "\t\t\t\t<description>Checks ${prop} is the valid syntax.</description>\n";
		print "\t\t\t\t<conditions>\n";
		print "\t\t\t\t\t<or>\n";
		print "\t\t\t\t\t\t<and>\n";
		print "\t\t\t\t\t\t\t<condition>${prop} ==~ /.*\\S+.*/</condition>\n";
		if (!($prop =~ /grid.poc/ || $prop =~ /jar.signing.keystore.cert/))
		{
			print "\t\t\t\t\t\t\t<condition negate=\"true\">${prop} ==~ /.*\\s.*/</condition>\n";
		}
		print "\t\t\t\t\t\t\t<condition negate=\"true\">${prop} ==~ /.*\\x5c.*/</condition>\n";
		if (!($prop =~ /dir/ || $prop =~ /file/ || $prop=~ /url/ || $prop =~ /location/ || $prop =~ /path/ || $prop =~ /\.home$/))
		{
			print "\t\t\t\t\t\t\t<condition negate=\"true\">${prop} ==~ /.*\\/.*/</condition>\n";
		}
		print "\t\t\t\t\t\t</and>\n";
		print "\t\t\t\t\t\t<condition>${prop} == null </condition>\n";
		print "\t\t\t\t\t\t<condition>${prop} == \"\" </condition>\n";
		print "\t\t\t\t\t</or>\n";
		print "\t\t\t\t</conditions>\n";
		if ($prop =~ /dir/ || $prop =~ /file/ || $prop=~ /url/ || $prop =~ /location/ || $prop =~ /path/ || $prop =~ /\.home$/)
		{
			print "\t\t\t\t<fail-message>${prop} did not pass validation. ${prop} must be null or have a value and not contain spaces or  back slashes (\\). Sample valid value - \"${default}\".</fail-message>\n";
		}
		else
		{
			print "\t\t\t\t<fail-message>${prop} did not pass validation. ${prop} must be null or have a value and not contain spaces, forward slashes (\/) or back slashes (\\). Sample valid value - \"${default}\".</fail-message>\n";
		}
		print "\t\t\t</rule>\n";

		if($prop =~ /\.port$/ || $prop =~ /\.port\./)
		{
			print "\t\t\t<rule>\n";
			print "\t\t\t\t<name>${prop}-value</name>\n";
			print "\t\t\t\t<description>Checks ${prop} has proper value.</description>\n";
			print "\t\t\t\t<conditions>\n";
			print "\t\t\t\t\t<or>\n";
			print "\t\t\t\t\t\t<and>\n";
			print "\t\t\t\t\t\t\t<condition>$prop ==~ /\\d+/</condition>\n";
			print "\t\t\t\t\t\t\t<condition>$prop &gt;= 0</condition>\n";
			print "\t\t\t\t\t\t\t<condition>$prop &lt;= 65535</condition>\n";
			print "\t\t\t\t\t\t</and>\n";
			print "\t\t\t\t\t\t<condition>$prop == null </condition>\n";
			print "\t\t\t\t\t\t<condition>$prop == \"\" </condition>\n";
			print "\t\t\t\t\t</or>\n";
			print "\t\t\t\t</conditions>\n";
			print "\t\t\t\t<fail-message>${prop} did not pass validation. ${prop} must be empty or between 0 and 65535. Sample valid value - \"${default}\".</fail-message>\n";
			print "\t\t\t</rule>\n";
		}

		if($prop =~ /\.url$/ && $prop !~ /database/ && $prop ne "application.url" && $prop ne "ldap.url")
		{
			#warn "$prop -> url rule added\n";
			print "\t\t\t<rule>\n";
			print "\t\t\t\t<name>${prop}-value</name>\n";
			print "\t\t\t\t<description>Checks ${prop} has proper value.</description>\n";
			print "\t\t\t\t<conditions>\n";
			print "\t\t\t\t\t<condition>$prop ==~ /^http.*:\/\/.*\.nci\.nih\.gov.*/</condition>\n";
			print "\t\t\t\t</conditions>\n";
			print "\t\t\t\t<fail-message>${prop} did not pass validation. ${prop} must be http(s)://*.nci.nih.gov/*. Sample valid value - \"${default}\".</fail-message>\n";
			print "\t\t\t</rule>\n";
		}
		if($prop =~ /^exclude/)
		{
			print "\t\t\t<rule>\n";
			print "\t\t\t\t<name>${prop}-tf</name>\n";
			print "\t\t\t\t<description>Checks ${prop} has proper value.</description>\n";
			print "\t\t\t\t<conditions>\n";
			print "\t\t\t\t\t<or>\n";
			print "\t\t\t\t\t\t<condition>$prop == \"true\"</condition>\n";
			print "\t\t\t\t\t\t<condition>$prop == \"false\"</condition>\n";
			print "\t\t\t\t\t\t<condition>$prop == \"\"</condition>\n";
			print "\t\t\t\t\t\t<condition>$prop == null</condition>\n";
			print "\t\t\t\t\t</or>\n";
			print "\t\t\t\t</conditions>\n";
			print "\t\t\t\t<fail-message>${prop} did not pass validation. ${prop} must be true, false or empty. Sample valid value - \"${default}\".</fail-message>\n";
			print "\t\t\t</rule>\n";
		}

		print "\t\t</rules>\n";
		print "\t</property>\n";
		#print "\t-->\n";
	}
}
print "</properties>\n";
