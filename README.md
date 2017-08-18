# Invoke Dynamic sample. 
Assembles a class containing invokeDynamic instructions. The linked CallSites are mutable - they delegate to instance methods in a
target.  

## Build with gradle
set JAVA_HOME environment variable. The build forks the compiler at that location

## Run
run with -Dorg.slf4j.simpleLogger.logFile=System.out to log to standard output instead of std err

Eclipse note: 
Change project settings to ignore forbidden references.
Project properties-> Java Compiler -> Errors/Warnings: Forbidden reference (access rules) = Ignore
