#find all explicit jar filenames in build scripts
find . -name "*.xml" -exec grep "[^\*]\.jar" '{}' \; -print 