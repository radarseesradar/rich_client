package smartClient.framework;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */

public class ClientSideIDFactory implements IDFactoryInterface
{
  public static final int allocationSize = 100;
  private int remaining;
  private long next;

  public long nextId()
  {
    try
    {
      if( remaining == 0 )
      {
        next = SmartClientFramework.getSingleton().getSession().allocateIDs( allocationSize );
        remaining = allocationSize;
      }
      remaining--;
    }
    catch( java.rmi.RemoteException e )
    {
      throw new AllocateIDsException ( e.getMessage() );
    }
    return ++next;
  }

	public static void main( String []args )
    {
	    if (args.length == 0)
	    {
	    	System.out.println("You should provide properties file :"
	                 + "c:\\jdk1.3.1\\bin\\java Keyboard.clientBusinessObjects.ClientSideIDFactory SOSProperties.txt");
	    	System.exit(0);
	    }
	    SmartClientFramework.getSingleton().setPropertiesUsing( args[0] );
        SmartClientFramework.getSingleton().establishCommunications();
        ClientSideIDFactory aClientSideIDFactory = new ClientSideIDFactory();
        System.out.println( "Next client side ID is " + aClientSideIDFactory.nextId() + ".");
    }
}