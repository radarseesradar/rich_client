@echo OFF
if "%JAVA_HOME%" == "" goto invalidJavaHome
if "%CLASSES_HOME%" == "" goto invalidClassesHome
if "%SOS_HOME%" == "" goto invalidSOSHome
%JAVA_HOME%\bin\javac -g -d %CLASSES_HOME% smartClient\framework\*.java
%JAVA_HOME%\bin\javac -g -d %CLASSES_HOME% model\test\client\*.java
%JAVA_HOME%\bin\javac -g -d %CLASSES_HOME% model\test\server\*.java
call buildServer
goto exit

:invalidJavaHome
    echo
    echo The environment variable JAVA_HOME must be set
    echo
    goto exit

:invalidClassesHome
    echo
    echo The environment variable CLASSES_HOME must be set
    echo
    goto exit

:invalidSOSHome
    echo
    echo The environment variable SOS_HOME must be set
    echo
    goto exit

:exit

