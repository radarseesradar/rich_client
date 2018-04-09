package smartClient.framework;


import java.util.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class OneToManyRepository extends RepositoryMerger implements Serializable, BidirectionalToManyRepository
{
  /**
   * @associates RoleKey 
   */
  private Map toOnesKeys;

  /**
   * @associates Object 
   */
  private Map toOnes;
  private ToManyRepository toManys;

  public OneToManyRepository( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
    toOnes = (Map) new HashMap();
    toOnesKeys = (Map) new HashMap();
    toManys = new ToManyRepository( _associationsCoordinator );
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    return super.isConsistent( anAssociationsCoordinator, toOnes ) && toManys.isConsistent( anAssociationsCoordinator );
  }

  public void printString( int depth )
  {
    String indent = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    String indent3 = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 2 );
    System.out.println( indent + "<<<Printing OneToManyRepositoryr>>>");
    System.out.println( indent2 + "<<<Printing To-One side>>>");
    Map.Entry toOneEntry = null;
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      System.out.println( indent3 + toOneEntry.getKey() + " ===>> " + toOneEntry.getValue() );
    }
    toManys.printString( depth + 1 );
  }

  public void merge( OneToManyRepository that )
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
    if( SmartClientFramework.getSingleton().isIncludingRemovals() )
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
    getAssociationsCoordinator().addToWorkingSet( (Persistable) oneKey.getManipulator() );
    if( toOnesKeys.containsKey( oneKey ) )
    {
      ((RoleKey)toOnesKeys.get( oneKey )).restore();
    }
    else
    {
      toOnesKeys.put( oneKey, oneKey );
    }
    toOnes.put( oneKey, manyKey.getManipulator() );
    getAssociationsCoordinator().addToWorkingSet( (Persistable) manyKey.getManipulator() );
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
    return  toOnes.equals( ((OneToManyRepository) that).toOnes )
            || toManys.equals( ((OneToManyRepository) that).toManys );
  }

  public int hashCode()
  {
    return toOnes.hashCode() ^ toManys.hashCode();
  }



}