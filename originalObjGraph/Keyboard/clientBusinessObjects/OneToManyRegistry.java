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

public class OneToManyRegistry extends RegistryMerger implements Serializable, BidirectionalToManyRegistry
{
  private Map toOnesKeys;
  private Map toOnes;
  private ToManyRegistry toManys;

  public OneToManyRegistry( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
    toOnes = (Map) new HashMap();
    toOnesKeys = (Map) new HashMap();
    toManys = new ToManyRegistry( _associationsCoordinator );
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    return super.isConsistent( anAssociationsCoordinator, toOnes ) && toManys.isConsistent( anAssociationsCoordinator );
  }

  public void printString( int depth )
  {
    String indent = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    String indent3 = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 2 );
    System.out.println( indent + "<<<Printing OneToManyRegistryr>>>");
    System.out.println( indent2 + "<<<Printing To-One side>>>");
    Map.Entry toOneEntry = null;
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      System.out.println( indent3 + toOneEntry.getKey() + " ===>> " + toOneEntry.getValue() );
    }
    toManys.printString( depth + 1 );
  }

  public void merge( OneToManyRegistry that )
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
    toManys.merge( that.toManys );
  }

  public Object getToOne( RoleKey oneKey )
  {
    if( MiddleWareFramework.getSingleton().isIncludingRemovals() )
    {
      return toOnes.get( oneKey );
    }

    if( toOnesKeys.containsKey( oneKey )
    && ! ((RoleKey)toOnesKeys.get( oneKey )).wasRemoved() )
    {
      return toOnes.get( oneKey );
    }

    return null;
  }

  public void associate( RoleKey manyKey, RoleKey oneKey )
  {
    if( ((StubInterface)(manyKey.getManipulator())).isStub()
    && ((StubInterface)oneKey.getManipulator()).isStub() )
      throw new InternalError( "Only a programming bug, will allow a stub to be associated with a stub" );

    if( ((StubInterface)(manyKey.getManipulator())).isStub() )
    {
      if( toOnesKeys.containsKey( oneKey ) )
      {
        ((RoleKey)toOnesKeys.get( oneKey )).restore();
      }
      else
      {
        toOnesKeys.put( oneKey, oneKey );
      }
      toOnes.put( oneKey, manyKey.getManipulator() );
      getAssociationsCoordinator().addToWorkingSet( (StubInterface) oneKey.getManipulator() );
      return;
    }

    if( ((StubInterface)oneKey.getManipulator()).isStub() )
    {
      toManys.put( manyKey, oneKey.getManipulator() );
      getAssociationsCoordinator().addToWorkingSet( (StubInterface) manyKey.getManipulator() );
      return;
    }

    Object previousManyManipulator = null;
    RoleKey previousManyKey = null;
    previousManyManipulator = getToOne( oneKey );
    if( previousManyManipulator != null )
    {
      previousManyKey = new RoleKey( previousManyManipulator, manyKey.getRoleName() );
      disassociate( previousManyKey, oneKey );
    }
    getAssociationsCoordinator().addToWorkingSet( (Visitable) oneKey.getManipulator() );
    if( toOnesKeys.containsKey( oneKey ) )
    {
      ((RoleKey)toOnesKeys.get( oneKey )).restore();
    }
    else
    {
      toOnesKeys.put( oneKey, oneKey );
    }
    toOnes.put( oneKey, manyKey.getManipulator() );
    getAssociationsCoordinator().addToWorkingSet( (Visitable) manyKey.getManipulator() );
    toManys.put( manyKey, oneKey.getManipulator() );
  }

  public Collection getToMany( RoleKey manyKey )
  {
    return toManys.get( manyKey );
  }

  public void disassociate( RoleKey manyKey, RoleKey oneKey )
  {
    if( toOnesKeys.containsKey( oneKey ) )
    {
      ((RoleKey)toOnesKeys.get( oneKey )).remove();
    }
    toManys.remove( manyKey, oneKey.getManipulator() );
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  toOnes.equals( ((OneToManyRegistry) that).toOnes )
            || toManys.equals( ((OneToManyRegistry) that).toManys );
  }

  public int hashCode()
  {
    return toOnes.hashCode() ^ toManys.hashCode();
  }



}