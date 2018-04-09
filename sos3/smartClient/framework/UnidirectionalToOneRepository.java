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
public class UnidirectionalToOneRepository extends RepositoryMerger implements Serializable
{
  /**
   * @associates Object 
   */
  private Map toOnes;

  public UnidirectionalToOneRepository( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
    toOnes = (Map) new HashMap();
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    return super.isConsistent( anAssociationsCoordinator, toOnes );
  }

  public void printString( int depth )
  {
    String indent = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    System.out.println( indent + "<<<Printing UnidirectionalToOneRepositoryr>>>");
    Map.Entry toOneEntry = null;
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      System.out.println( indent2 + toOneEntry.getKey() + " ===>> " + toOneEntry.getValue() );
    }
  }

  public void merge( UnidirectionalToOneRepository that )
  {
    merge( that.toOnes, toOnes );
  }

  public void put( RoleKey from, Object to )
  {
    toOnes.put(  from,  to );
    getAssociationsCoordinator().addToWorkingSet( (Persistable) to );
    getAssociationsCoordinator().addToWorkingSet( (StubInterface) from.getManipulator() );
  }

  public Object get( RoleKey from )
  {
    return toOnes.get(  from );
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  toOnes.equals( ((UnidirectionalToOneRepository) that).toOnes );
  }

  public int hashCode()
  {
    return toOnes.hashCode();
  }
}