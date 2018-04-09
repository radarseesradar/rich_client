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

public class ClientSideIDFactory
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
        next = MiddleWareFramework.getSingleton().getTVSessionBeanObject().allocateIDs( allocationSize );
        remaining = allocationSize;
      }
      remaining--;
    }
    catch( java.rmi.RemoteException e )
    {
      throw new KBAllocateIDsException( e.getMessage() );
    }
    return ++next;
  }

}