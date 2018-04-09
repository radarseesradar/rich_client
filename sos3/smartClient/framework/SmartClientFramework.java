package smartClient.framework;


import java.io.*;
import java.rmi.RMISecurityManager;
import java.util.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class SmartClientFramework
{
  private static SmartClientFramework singleton = null;

  private AssociationsCoordinator globalAssociationsCoordinator;
  private boolean disableMarkModify = false;
  private boolean includingRemovals = false;
  private Properties properties = null;
  private boolean server = false;
  private SessionInterface session;
  private PersistencePolicyInterface persistencePolicy;

  private void setSession( SessionInterface aRemoteSession )
  {
  	session = aRemoteSession;
  }

  public PersistencePolicyInterface getPersistencePolicy()
  {
  	String persistencePolicyClassname = null;
    Class persistencePolicyClass = null;
  	if( persistencePolicy == null )
    {
  		persistencePolicyClassname = (String) SmartClientFramework.getSingleton().getProperties().get( "PERSISTENCE_POLICY" );
        try
        {
       		persistencePolicyClass = Class.forName( persistencePolicyClassname );
      		setPersistencePolicy( (PersistencePolicyInterface) persistencePolicyClass.newInstance() );
        }
	    catch( InstantiationException e )
	    {
	    	e.printStackTrace();
	    	throw new InternalError( e.toString() );
	    }
	    catch( IllegalAccessException e )
	    {
	      // This would indicate a programming bug.
	      throw new InternalError( e.toString() );
	    }
	    catch( ClassNotFoundException e )
	    {
	      // This would indicate a programming bug.
	      throw new InternalError( e.toString() );
	    }
    }
  	return persistencePolicy;
  }

  private void setPersistencePolicy( PersistencePolicyInterface aPersistencePolicy )
  {
  	persistencePolicy = aPersistencePolicy;
  }

  public SessionInterface getSession()
  {
  	return session;
  }

  public void setSecurityManager()
  {
//    System.setSecurityManager( new RMISecurityManager() );
  }

  public void setPropertiesUsing( String filename )
  {
    FileInputStream fis = null;
  	try
    {
  	  properties = new Properties();
      fis = new FileInputStream( filename );
      properties.load( fis );
    }
    catch ( IOException ioe )
    {
      System.out.println("Error in Reading properties File :" + ioe);
    }
    finally
    {
      try
      {
        if( fis != null )
          fis.close();
      }
      catch( IOException e )
      {
      }
    }
	clear();
  }

  public Properties getProperties()
  {
  	return properties;
  }

  public void setIncludingRemovals( boolean trueOrFalse )
  {
    includingRemovals = trueOrFalse;
  }

  public boolean getIncludingRemovals()
  {
    return includingRemovals;
  }

  public boolean isIncludingRemovals()
  {
    return includingRemovals;
  }

  public void setDisableMarkModify( boolean trueOrFalse )
  {
    disableMarkModify = trueOrFalse;
  }

  public boolean getDisableMarkModify()
  {
    return disableMarkModify;
  }

  public boolean markModifyIsDisabled()
  {
    return disableMarkModify;
  }

  private SmartClientFramework()
  {
    globalAssociationsCoordinator = new AssociationsCoordinator();
    setSecurityManager();
  }

  public String whereAmI()
  {
  	if( isServer() )
    	return "on the server";
    else
    	return "on the client";
  }

  public boolean isClient()
  {
    return !isServer();
  }

  public boolean isServer()
  {
    return server;
  }

  public void setServer( boolean trueOrFalse )
  {
  	server = trueOrFalse;
  }

  public static SmartClientFramework getSingleton ()
  {
    if (singleton == null)
      singleton = new SmartClientFramework();
    return singleton;
  }

  public AssociationsCoordinator getGlobalAssociationsCoordinator()
  {
    return globalAssociationsCoordinator;
  }

  public Class derivePersistentType( Class transientType )
  {
  	return deriveOthersType( transientType );
  }

  protected Class deriveOthersType( Class thisType )
  {
    try
    {
      Persistable thisObj = (Persistable) thisType.newInstance();
      String othersPackageName = thisObj.otherPackageName();
      if( othersPackageName == null || othersPackageName.equals( "" ) )
        return Class.forName( thisObj.otherClassName() );
      return Class.forName( othersPackageName + "." + thisObj.otherClassName() );
    }
    catch( IllegalAccessException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
    catch( InstantiationException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }


  public void clear()
  {
    getGlobalAssociationsCoordinator().clear();
  	ClampProvider.getSingleton().clear();
  }

  public void establishCommunications()
  {
  	setSession( (new Client()).createSession() );
  }

  public void save( Persistable delegator ) throws Exception
  {
    synchronized( delegator.getAssociationsCoordinator() )
    {
      boolean savedPreservingStubsFlag = getGlobalAssociationsCoordinator().getPreservingStubs();
      boolean savedIncludingRemovalsFlag = getIncludingRemovals();
      setIncludingRemovals( true );
      getGlobalAssociationsCoordinator().setPreservingStubs( true );
      try
      {
        VisitorCRUDPacketBuilder aCRUDPacketBuilder = new VisitorCRUDPacketBuilder();
        delegator.accept( aCRUDPacketBuilder );

        CRUDPacket aCRUDPacket = aCRUDPacketBuilder.getCRUDPacket();
        aCRUDPacket.normalize();

        getSession().execute( aCRUDPacket );

        aCRUDPacket.postStorageCleanup();
        VisitorPostStorage aVisitorPostStorage = new VisitorPostStorage();
        delegator.accept( aVisitorPostStorage );
      }
      finally
      {
        getGlobalAssociationsCoordinator().setPreservingStubs( savedPreservingStubsFlag );
        setIncludingRemovals( savedIncludingRemovalsFlag );
      }
    }
  }

}