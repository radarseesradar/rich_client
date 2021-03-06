/* Generated by Together */

package smartClient.framework;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class TestVisitorUniStorageClamps extends TestVisitorUniRetrievalClamps 
{
  public TestVisitorUniStorageClamps( String name)
  {
    super( name );
  }

  public static void oneTimeSetUp()
  {
  	try
    {
		SmartClientFramework.getSingleton().setPropertiesUsing( "C:\\sos3\\UnidirectionalStorageProperties.txt" );
        SmartClientFramework.getSingleton().establishCommunications();
        tvObject = SmartClientFramework.getSingleton().getSession();
        tvObject.setPropertiesUsing( "C:\\sos3\\UnidirectionalStorageProperties.txt" );
    }
    catch( java.rmi.RemoteException e )
    {
    	throw new InternalError( "RMI exception on junit onetime setup for TestVisitorUniStorageClamps" );
    }
  }

}
