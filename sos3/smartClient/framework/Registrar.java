/* Generated by Together */

package smartClient.framework;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.Properties;
import java.net.*;
import smartClient.classServer.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class Registrar
{
	public static void main( String []args )
    {
    	try
        {
		    if (args.length == 0)
		    {
		      System.out.println("You should provide properties file :"
		                 + "c:\\jdk1.3.1\\bin\\java smartClient.framework.Registrar SOSProperties.txt");
		      System.exit(0);
		    }
		    SmartClientFramework.getSingleton().setPropertiesUsing( args[0] );
		    Properties properties = SmartClientFramework.getSingleton().getProperties();
            SmartClientFramework.getSingleton().setServer( true );
		    String serverName = (String)properties.get( "SOS_SERVER_NAME" );
		    Server theServer = new Server();
			String registryURL = (String) properties.get( "REGISTRY_URL" );
			String registryPort = (String) properties.get( "REGISTRY_PORT" );
			String httpFileServerPort = (String) properties.get( "HTTP_FILE_SERVER_PORT" );
			String serverClassesHome = (String) properties.get( "SERVER_CLASSES_HOME" );
            System.out.println( "Creating Registry." );
            LocateRegistry.createRegistry( Integer.parseInt( registryPort ) );
			System.out.println( "Creating HTTP class file server." );
	    	new ClassFileServer( Integer.parseInt( httpFileServerPort ), serverClassesHome );
            System.out.println( "Registering the server." );
		    Naming.rebind( registryURL + serverName, theServer );
			System.out.println( "Exiting the registrar." );
        }
        catch( RemoteException exception )
        {
        	exception.printStackTrace();
        	System.out.println( exception );
        }
        catch( MalformedURLException exception )
        {
        	exception.printStackTrace();
        	System.out.println( exception );
        }
		catch (IOException e)
        {
		    System.out.println("Unable to start ClassServer: " +
				       e.getMessage());
		    e.printStackTrace();
		}
    }
}
