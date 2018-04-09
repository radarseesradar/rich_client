package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.util.*;
import java.io.*;

public class UnidirectionalToOne extends UnidirectionalAssociation implements ToOne, Serializable
{
  public UnidirectionalToOne( Object _from, String _toRole, String _toRoleTypeName )
  {
    super( _from, _toRole, _toRoleTypeName );
  }

  protected UnidirectionalToOneRegistry getRegistry()
  {
    return (UnidirectionalToOneRegistry) getUnidirectionalToOneRegistry();
  }

  public Object get()
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      Object result = null;
      UnidirectionalToOneRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      result = aRegistry.get( toKey );
      if( result != null &&  ! ((Visitable)this.getFrom()).getAssociationsCoordinator().isPreservingStubs() )
        result = ((StubInterface) result).yourself();
      return result;
    }
  }

  public int getAssociationType()
  {
    return AssociationTypes.TO_ONE;
  }

  public void set( Object toOne )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      Visitable previousToOneIfAny = (Visitable) get();
      if( previousToOneIfAny != null && MiddleWareFramework.getSingleton().isClient() )
        previousToOneIfAny.setModified( true );
      UnidirectionalToOneRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      aRegistry.put( toKey, toOne );
      recordSet( toOne );
    }
  }
}