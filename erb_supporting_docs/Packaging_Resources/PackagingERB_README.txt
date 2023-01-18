To package the tool:

1. Change paths in java code
	a. com.epa.utility/Constants: Change the constructor to call "setFilePathsDynamic()" instead of "setFilePathsStatic()"
	
2. Change paths in log4j
	b. Change the 5 commented paths. 1 for each of the following, debug, info, warn, error, fatal.

In eclipse, run the following gradle commands
	-clean
	-build
	-install
	-jlink
	-jpackage (an error may return, but jpackage should still work)
**After running jpackage, 
	-Navigate to MetroCERI/erb/app/build/jpackage/erb
	-Copy and paste the 4 below items into a clean directory
		1. app directory
		2. runtime directory
		3. erb.exe
		4. erb.ico
	-Copy the directory named "lib" from the same directory this file is located in, and paste it into the directory with the newly pasted items above.
	-Your directory should now contains 5 items.
	-Double click erb.exe to run the tool.
 