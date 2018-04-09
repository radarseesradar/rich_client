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

public class OneToOneRegistry extends RegistryMerger implements Serializable
{
  private Map toOnes;
  private Map toOnesKeys;

  public OneToOneRegistry( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
    toOnes = (Map) new HashMap();
    toOnesKeys = (Map) new HashMap();
  }


  public void printString( int depth )
  {
    String indent = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    System.out.println( indent + "<<<Printing OneToOneRegistryr>>>");
    Map.Entry toOneEntry = null;
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      System.out.println( indent2 + toOneEntry.getKey() + " ===>> " + toOneEntry.getValue() );
    }
  }

  public void merge( OneToOneRegistry that )
  {
    RoleKey nextRoll = null;
    merge( that.toOnes, toOnes );
    for( Iterator anIterator = that.toOnes.keySet().iterator(); anIterator.hasNext(); )
    {
      nextRoll = (RoleKey) anIterator.next();
      if( ! toOnesKeys.containsKey( nextRoll ) )
      {
        toOnesKeys.put( nextRoll, nextRoll );
      }
    }
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    return super.isConsistent( anAssociationsCoordinator, toOnes );
  }

  public Object getToOne( RoleKey toOneKey )
  {
    if( MiddleWareFramework.getSingleton().isIncludingRemovals() )
    {
      return toOnes.get( toOneKey );
    }

    if( toOnesKeys.containsKey( toOneKey )
    && ! ((RoleKey)toOnesKeys.get( toOneKey )).wasRemoved() )
    {
      return toOnes.get( toOneKey );
    }

    return null;
  }

  private void removePreviousInverseAssociations( RoleKey thisToOneKey, RoleKey thatToOneKey )
  {
    Object previousThisManipulator = null;
    Object previousThatManipulator = null;
    RoleKey previousThisToOneKey = null;
    RoleKey previousThatToOneKey = null;
    if( toOnes.containsKey( thisToOneKey ) )
    {
      previousThatToOneKey = new RoleKey( toOnes.get( thisToOneKey ), thatToOneKey.getRoleName() );
      if( toOnesKeys.containsKey( previousThatToOneKey ) )
      {
        ((RoleKey)toOnesKeys.get( previousThatToOneKey )).remove();
      }
      else
      {
        throw new InternalError( "<OneToOneRegistry.removePreviousInverseAssociations:10> toOnes and toOnesKeys should always be in sync" );
      }
    }
    if( toOnes.containsKey( thatToOneKey ) )
    {
      previousThisToOneKey = new RoleKey( toOnes.get( thatToOneKey ), thisToOneKey.getRoleName() );
      if( toOnesKeys.containsKey( previousThisToOneKey ) )
      {
        ((RoleKey)toOnesKeys.get( previousThisToOneKey )).remove();
      }
      else
      {
        throw new InternalError( "<OneToOneRegistry.removePreviousInverseAssociations:20> toOnes and toOnesKeys should always be in sync" );
      }
    }
  }

  public void associate( RoleKey thisToOneKey, RoleKey thatToOneKey )
  {
    removePreviousInverseAssociations( thisToOneKey, thatToOneKey );
    if( toOnesKeys.containsKey( thisToOneKey ) )
    {
      ((RoleKey)toOnesKeys.get( thisToOneKey )).restore();
    }
    else
    {
      toOnesKeys.put( thisToOneKey, thisToOneKey );
    }
    toOnes.put( thisToOneKey, thatToOneKey.getManipulator() );
    if( toOnesKeys.containsKey( thatToOneKey ) )
    {
      ((RoleKey)toOnesKeys.get( thatToOneKey )).restore();
    }
    else
    {
      toOnesKeys.put( thatToOneKey, thatToOneKey );
    }
    toOnes.put( thatToOneKey, thisToOneKey.getManipulator() );
    getAssociationsCoordinator().addToWorkingSet( (Visitable) thatToOneKey.getManipulator() );
    getAssociationsCoordinator().addToWorkingSet( (Visitable) thisToOneKey.getManipulator() );
  }

  public void disassociate( RoleKey thisToOneKey, RoleKey thatToOneKey )
  {
    if( toOnesKeys.containsKey( thisToOneKey ) )
    {
      ((RoleKey)toOnesKeys.get( thisToOneKey )).remove();
    }
    if( toOnesKeys.containsKey( thatToOneKey ) )
    {
      ((RoleKey)toOnesKeys.get( thatToOneKey )).remove();
    }
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  toOnes.equals( ((OneToOneRegistry) that).toOnes );
  }

  public int hashCode()
  {
    return toOnes.hashCode();
  }

}