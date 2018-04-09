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
public class OneToOneRepository extends RepositoryMerger implements Serializable
{
  /**
   * @associates Object 
   */
  private Map toOnes;

  /**
   * @associates RoleKey 
   */
  private Map toOnesKeys;

  public OneToOneRepository( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
    toOnes = (Map) new HashMap();
    toOnesKeys = (Map) new HashMap();
  }


  public void printString( int depth )
  {
    String indent = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    System.out.println( indent + "<<<Printing OneToOneRepositoryr>>>");
    Map.Entry toOneEntry = null;
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      System.out.println( indent2 + toOneEntry.getKey() + " ===>> " + toOneEntry.getValue() );
    }
  }

  public void merge( OneToOneRepository that )
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
    if( SmartClientFramework.getSingleton().isIncludingRemovals() )
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
        throw new InternalError( "<OneToOneRepository.removePreviousInverseAssociations:10> toOnes and toOnesKeys should always be in sync" );
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
        throw new InternalError( "<OneToOneRepository.removePreviousInverseAssociations:20> toOnes and toOnesKeys should always be in sync" );
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
    getAssociationsCoordinator().addToWorkingSet( (Persistable) thatToOneKey.getManipulator() );
    getAssociationsCoordinator().addToWorkingSet( (Persistable) thisToOneKey.getManipulator() );
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
    return  toOnes.equals( ((OneToOneRepository) that).toOnes );
  }

  public int hashCode()
  {
    return toOnes.hashCode();
  }

}