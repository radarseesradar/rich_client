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

public class BidirectionalOnlyMode extends AssociationMode
{
  public boolean isBidirectionalOnlyMode()
  {
    return true;
  }

  public void recordComand( UnidirectionalAssociation delegator, Command aCommand )
  {
    BidirectionalAssociation aBiAssoc = (BidirectionalAssociation) delegator;
    RoleKey directRoleKey = new RoleKey( aBiAssoc.getFrom(), aBiAssoc.getToRole() );
    Clamp directClamp = directRoleKey.toClamp();
    RoleKey inverseRoleKey = null;
    Clamp inverseClamp = null;
    if( ClampProvider.getSingleton().getStorageClamps().containsKey( directClamp ) )
    {
      directClamp = (Clamp) ClampProvider.getSingleton().getStorageClamps().get( directClamp );
      if( directClamp.getInverseClamp() == null )
      {
        inverseRoleKey = new RoleKey( aCommand.getArg().toTransient(), aBiAssoc.getFromRole() );
        inverseClamp = inverseRoleKey.toClamp();
        if( ClampProvider.getSingleton().getStorageClamps().containsKey( inverseClamp ) )
        {
          inverseClamp = (Clamp) ClampProvider.getSingleton().getStorageClamps().get( inverseClamp );
          directClamp.setInverseClamp( inverseClamp );
          inverseClamp.setInverseClamp( directClamp );
        }
      }
      directClamp.getChangeList().add( (CommandInterface) aCommand );
    }
    else
    {
      delegator.getChangeList().add( aCommand );
    }
  }

  public void save( Visitable delegator ) throws Exception
  {
    synchronized( delegator.getAssociationsCoordinator() )
    {
      byte [] byteArray;
      boolean savedPreservingStubsFlag = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPreservingStubs();
      boolean savedIncludingRemovalsFlag = MiddleWareFramework.getSingleton().getIncludingRemovals();
      MiddleWareFramework.getSingleton().setIncludingRemovals( true );
      MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().setPreservingStubs( true );
      try
      {
        Client client = MiddleWareFramework.getSingleton().getClient();
        TVSessionBeanObject tvObject = MiddleWareFramework.getSingleton().getTVSessionBeanObject();
        VisitorCRUDPacketBuilder aCRUDPacketBuilder = new VisitorCRUDPacketBuilder();
        delegator.accept( aCRUDPacketBuilder );

        CRUDPacket aCRUDPacket = aCRUDPacketBuilder.getCRUDPacket();
        aCRUDPacket.normalize();

        byteArray = (new Serializer()).serialize( aCRUDPacket );
        client.begin();
        tvObject.execute( byteArray );
        client.commit();

        aCRUDPacket.postStorageCleanup();
        VisitorPostStorage aVisitorPostStorage = new VisitorPostStorage();
        delegator.accept( aVisitorPostStorage );
      }
      finally
      {
        MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().setPreservingStubs( savedPreservingStubsFlag );
        MiddleWareFramework.getSingleton().setIncludingRemovals( savedIncludingRemovalsFlag );
      }
    }
  }
}