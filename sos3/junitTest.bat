CD %CLASSES_HOME%
%JAVA_HOME%\bin\java -Djava.security.policy=sos.policy -Djava.rmi.server.codebase=http://localhost:2001/ junit.swingui.TestRunner "%1"
cd %SOS_HOME%
