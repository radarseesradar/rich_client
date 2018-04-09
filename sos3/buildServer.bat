@echo OFF
cd %CLASSES_HOME%
copy %SOS_HOME%\SOSProperties.txt .
copy %SOS_HOME%\sos.policy .
%JAVA_HOME%\bin\rmic -d . smartClient.framework.Server
%JAVA_HOME%\bin\rmic -d . smartClient.framework.Session
cd %SOS_HOME%
