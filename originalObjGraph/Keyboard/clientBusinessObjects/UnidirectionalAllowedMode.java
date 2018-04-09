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
import java.io.*;

public class UnidirectionalAllowedMode extends AssociationMode
{
  public boolean isUnidirectionalAllowedMode()
  {
    return true;
  }

  public void recordComand( UnidirectionalAssociation selfDelegator, Command aCommand )
  {
    selfDelegator.getCRUDRecorder().addAssociationChange( aCommand );
  }

  public void save( Visitable delegator ) throws Exception
  {
    synchronized( delegator.getAssociationsCoordinator() )
    {
      byte [] byteArray;
      Client client = MiddleWareFramework.getSingleton().getClient();
      TVSessionBeanObject tvObject = MiddleWareFramework.getSingleton().getTVSessionBeanObject();
      CRUDRecorder aCRUDRecorder = MiddleWareFramework.getSingleton().getGlobalCRUDRecorder();
      CRUDPacket aCRUDPacket = aCRUDRecorder.createCRUDPacket();

      byteArray = (new Serializer()).serialize( aCRUDPacket );
      client.begin();
      tvObject.execute( byteArray );
      client.commit();

      aCRUDRecorder.postStorageCleanup();
      aCRUDRecorder.clear();
    }
  }
}