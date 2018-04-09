cd %CLASSES_HOME%
%JAVA_HOME%\bin\java -Djava.security.policy=sos.policy -Djava.rmi.server.codebase=http://localhost:2001/ Keyboard.clientBusinessObjects.Client SOSProperties.txt
cd %SOS_HOME%

