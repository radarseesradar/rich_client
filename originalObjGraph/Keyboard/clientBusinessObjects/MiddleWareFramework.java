package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import Keyboard.*;
import secant.extreme.*;
import javax.rmi.*;

public class MiddleWareFramework
{
  private static MiddleWareFramework singleton = null;

  private TVSessionBeanObject tvSessionBeanObject = null;
  private Client client = null;
  private CRUDRecorder globalCRUDRecorder;
  private AssociationsCoordinator globalAssociationsCoordinator;
  private boolean disableMarkModify = false;
  private AssociationMode associationMode = null;
  private boolean includingRemovals = false;

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

  public AssociationMode getAssociationMode()
  {
    return associationMode;
  }

  private MiddleWareFramework()
  {
    globalCRUDRecorder = new CRUDRecorder();
    globalAssociationsCoordinator = new AssociationsCoordinator();
    associationMode = new BidirectionalOnlyMode();
  }

  public boolean isClient()
  {
    return !isServer();
  }

  public boolean isServer()
  {
    secant.portable.persistence.PersistenceService ps =
        secant.portable.persistence.PersistenceService.instance;
    if (ps != null)
    {
        return true;
    }
    return false;
  }

  public static MiddleWareFramework getSingleton ()
  {
    if (singleton == null)
      singleton = new MiddleWareFramework();
    return singleton;
  }

  public AssociationsCoordinator getGlobalAssociationsCoordinator()
  {
    return globalAssociationsCoordinator;
  }

  public void setClient( Client _client )
  {
    client = _client;
  }

  public Client getClient( )
  {
    return client;
  }

  public void setTVSessionBeanObject( TVSessionBeanObject sessionBean )
  {
    tvSessionBeanObject = sessionBean;
  }

  public TVSessionBeanObject getTVSessionBeanObject()
  {
    return tvSessionBeanObject;
  }

  public String getPersistentPackageName()
  {
    return "Keyboard";
  }

  public String getTransientPackageName()
  {
    return "Keyboard.clientBusinessObjects";
  }

  public CRUDRecorder getGlobalCRUDRecorder()
  {
    return globalCRUDRecorder;
  }

  public Class derivePersistentType( Class transientType )
  {
    try
    {
      Visitable attributesObj = (Visitable) transientType.newInstance();
      String othersPackageName = MiddleWareFramework.getSingleton().getPersistentPackageName();
      if( othersPackageName == null )
        return Class.forName( attributesObj.otherClassName() );
      return Class.forName( othersPackageName + "." + attributesObj.otherClassName() );
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
    getGlobalCRUDRecorder().clear();
    getGlobalAssociationsCoordinator().clear();
    ClampProvider.getSingleton().clearStorageClampsCommandLists();
  }

  public void establishCommunications()
  {
    try
    {
      TVSessionBeanObject tvObject = null;
      TVSessionBeanHome tvHome = null;
      Client aClient = null;
      aClient = new Client("//kbch");
      aClient.connectUser("secuser", "secuser");
      javax.naming.Context homes = new javax.naming.InitialContext();

      Object h = homes.lookup("TCService" + "/TVSessionBean");
      tvHome = (TVSessionBeanHome) PortableRemoteObject.narrow(h,TVSessionBeanHome.class);
      tvObject = tvHome.create();
      setTVSessionBeanObject( tvObject );
      setClient( aClient );

    }
    catch (Exception e)
    {
    e.printStackTrace();
    }
  }

}