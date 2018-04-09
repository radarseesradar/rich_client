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

public class UnidirectionalToMany extends UnidirectionalAssociation implements ToMany, Serializable
{
  public UnidirectionalToMany( Object _from, String _toRole, String _toRoleTypeName )
  {
    super( _from, _toRole, _toRoleTypeName );
  }

  protected UnidirectionalToManyRegistry getRegistry()
  {
    return (UnidirectionalToManyRegistry) getUnidirectionalToManyRegistry();
  }

  public int getAssociationType()
  {
    return AssociationTypes.TO_MANY;
  }

  public void add( Object toObject )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      UnidirectionalToManyRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      aRegistry.put( toKey, toObject );
      recordAdd( toObject );
    }
  }

  public void remove( Object toObject )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      UnidirectionalToManyRegistry aRegistry = getRegistry();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      aRegistry.remove( toKey, toObject );
      recordRemove( toObject );
    }
  }

  protected Collection getAll( RoleKey toKey )
  {
    UnidirectionalToManyRegistry aRegistry = getRegistry();
    return (Collection) aRegistry.get( toKey );
  }

  public Collection getAll()
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      UnidirectionalToManyRegistry aRegistry = getRegistry();
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