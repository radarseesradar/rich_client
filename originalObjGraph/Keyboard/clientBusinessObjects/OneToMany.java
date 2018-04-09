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

public class OneToMany extends BidirectionalAssociation implements ToMany, Serializable
{
  public OneToMany( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole,  _toRoleTypeName, _fromRole );
  }

  protected BidirectionalToManyRegistry getRegistry()
  {
    return (BidirectionalToManyRegistry) getOneToManyRegistry();
  }

  public int getAssociationType()
  {
    return AssociationTypes.ONE_TO_MANY;
  }

  public void add( Object toObject )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      BidirectionalToManyRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey inverseKey = new RoleKey( toObject, getFromRole() );
      aRegistry.associate( toKey, inverseKey );
      recordAdd( toObject );
    }
  }

  public void remove( Object toObject )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      BidirectionalToManyRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey fromKey = new RoleKey( toObject, getFromRole() );
      aRegistry.disassociate( toKey, fromKey );
      recordRemove( toObject );
    }
  }

  protected Collection getAll( RoleKey toKey )
  {
    BidirectionalToManyRegistry aRegistry = getRegistry();
    return (Collection) aRegistry.getToMany( toKey );
  }

  public Collection getAll()
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      BidirectionalToManyRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      return Collections.unmodifiableCollection(unstubAll( toKey ));
    }
  }

  public boolean addAll( Collection aCollection )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      for( Iterator anIterator = aCollection.iterator(); anIterator.hasNext(); )
      {
        add( anIterator.next() );
      }
      return true;
    }
  }

}