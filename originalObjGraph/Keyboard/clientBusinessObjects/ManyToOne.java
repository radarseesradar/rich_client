package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;

public class ManyToOne extends BidirectionalAssociation implements ToOne, Serializable
{
  public ManyToOne( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole,  _toRoleTypeName, _fromRole );
  }

  public int getAssociationType()
  {
    return AssociationTypes.MANY_TO_ONE;
  }

  public Object get()
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      Object result = null;
      OneToManyRegistry aRegistry = getOneToManyRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      result = aRegistry.getToOne( toKey );
      if( result != null &&  ! ((Visitable)getFrom()).getAssociationsCoordinator().isPreservingStubs() )
        result = ((StubInterface) result).yourself();
      return result;
    }
  }

  public void set( Object toOne )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      OneToManyRegistry aRegistry = getOneToManyRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey inverseKey = new RoleKey( toOne, getFromRole() );
      aRegistry.associate( inverseKey, toKey );
      recordSet( toOne );
    }
  }

}