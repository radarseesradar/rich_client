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

public class ManyToManyRegistry implements Serializable, BidirectionalToManyRegistry
{
  private ToManyRegistry toManys;

  public ManyToManyRegistry( AssociationsCoordinator _associationsCoordinator )
  {
    toManys = new ToManyRegistry( _associationsCoordinator );
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    return toManys.isConsistent( anAssociationsCoordinator );
  }

  public void printString( int depth )
  {
    String indent = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    System.out.println( indent + "<<<Printing ManyToManyRegistryr>>>");
    toManys.printString( depth + 1 );
  }


  public void merge( ManyToManyRegistry that )
  {
    toManys.merge( that.toManys );
  }

  public void associate( RoleKey manyKeyA, RoleKey manyKeyB )
  {
    toManys.put( manyKeyA, manyKeyB.getManipulator() );
    toManys.put( manyKeyB, manyKeyA.getManipulator() );
  }

  public void disassociate( RoleKey manyKeyA, RoleKey manyKeyB )
  {
    toManys.remove( manyKeyA, manyKeyB.getManipulator() );
    toManys.remove( manyKeyB, manyKeyA.getManipulator() );
  }

  public Collection getToMany( RoleKey manyKey )
  {
    return toManys.get( manyKey );
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  toManys.equals( ((ManyToManyRegistry) that).toManys );
  }

  public int hashCode()
  {
    return toManys.hashCode();
  }

}