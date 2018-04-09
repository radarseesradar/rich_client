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
public class ManyToManyRepository implements Serializable, BidirectionalToManyRepository
{
  private ToManyRepository toManys;

  public ManyToManyRepository( AssociationsCoordinator _associationsCoordinator )
  {
    toManys = new ToManyRepository( _associationsCoordinator );
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    return toManys.isConsistent( anAssociationsCoordinator );
  }

  public void printString( int depth )
  {
    String indent = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    System.out.println( indent + "<<<Printing ManyToManyRepositoryr>>>");
    toManys.printString( depth + 1 );
  }


  public void merge( ManyToManyRepository that )
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
    return  toManys.equals( ((ManyToManyRepository) that).toManys );
  }

  public int hashCode()
  {
    return toManys.hashCode();
  }

}