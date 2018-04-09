cd %CLASSES_HOME%
rem start /min %JAVA_HOME%\bin\rmiregistry 5000
rem start %JAVA_HOME%\bin\java examples.classServer.ClassFileServer 2001 %CLASSES_HOME%
start %JAVA_HOME%\bin\java -Djava.security.policy=sos.policy -Djava.rmi.server.codebase=http://localhost:2001/ smartClient.framework.Registrar SOSProperties.txt
cd %SOS_HOME%
