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

public class UnidirectionalToOneRegistry extends RegistryMerger implements Serializable
{
  private Map toOnes;

  public UnidirectionalToOneRegistry( AssociationsCoordinator _associationsCoordinator )
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
    String indent = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    System.out.println( indent + "<<<Printing UnidirectionalToOneRegistryr>>>");
    Map.Entry toOneEntry = null;
    for( Iterator toOnesIterator = toOnes.entrySet().iterator(); toOnesIterator.hasNext(); )
    {
      toOneEntry = (Map.Entry) toOnesIterator.next();
      System.out.println( indent2 + toOneEntry.getKey() + " ===>> " + toOneEntry.getValue() );
    }
  }

  public void merge( UnidirectionalToOneRegistry that )
  {
    merge( that.toOnes, toOnes );
  }

  public void put( RoleKey from, Object to )
  {
    toOnes.put(  from,  to );
    getAssociationsCoordinator().addToWorkingSet( (Visitable) to );
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
    return  toOnes.equals( ((UnidirectionalToOneRegistry) that).toOnes );
  }

  public int hashCode()
  {
    return toOnes.hashCode();
  }
}