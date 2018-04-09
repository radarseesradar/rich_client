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

public class ToManyRegistry extends RegistryMerger implements Serializable
{
  private Map fromOneToMany;

  public ToManyRegistry( AssociationsCoordinator _associationsCoordinator )
  {
    super( _associationsCoordinator );
    fromOneToMany = (Map) new HashMap();
  }

  protected Map getFromOneToMany()
  {
    return fromOneToMany;
  }

  public void merge( ToManyRegistry that )
  {
    merge( that.getFromOneToMany(), getFromOneToMany() );
  }

  public boolean isConsistent( AssociationsCoordinator anAssociationsCoordinator )
  {
    Map.Entry roleKeyEntry = null;
    Map.Entry manyEntry = null;
    RoleKey aRoleKey = null;
    StubInterface from = null;
    StubInterface toKey = null;
    StubInterface toValue = null;
    String validAssoc = anAssociationsCoordinator.toString();
    for( Iterator roleKeyIterator = fromOneToMany.entrySet().iterator(); roleKeyIterator.hasNext(); )
    {
      roleKeyEntry = (Map.Entry) roleKeyIterator.next();
      aRoleKey = (RoleKey) roleKeyEntry.getKey();
      from = (StubInterface) aRoleKey.getManipulator();
      if( !from.isStub() && ! ((Visitable) from).getAssociationsCoordinator().toString().equals( validAssoc ) )
        return false;
      for( Iterator manyIterator = ((Map)roleKeyEntry.getValue()).entrySet().iterator(); manyIterator.hasNext(); )
      {
        manyEntry = (Map.Entry) manyIterator.next();
        toKey = (StubInterface) manyEntry.getKey();
        toValue = (StubInterface) manyEntry.getValue();
      if( !toKey.isStub() && ! ((Visitable) toKey).getAssociationsCoordinator().toString().equals( validAssoc ) )
        return false;
      if( !toValue.isStub() && ! ((Visitable) toValue).getAssociationsCoordinator().toString().equals( validAssoc ) )
        return false;
      }
    }
    return true;
  }

  public void printString( int depth )
  {
    String indent = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth );
    String indent2 = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth + 1 );
    String indent3 = MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getPrintStringIndent( depth  + 2 );
    System.out.println( indent + "<<<Printing ToManyRegistryr>>>");
    Map.Entry roleKeyEntry = null;
    Map.Entry manyEntry = null;
    RoleKey aRoleKey = null;
    for( Iterator roleKeyIterator = fromOneToMany.entrySet().iterator(); roleKeyIterator.hasNext(); )
    {
      roleKeyEntry = (Map.Entry) roleKeyIterator.next();
      aRoleKey = (RoleKey) roleKeyEntry.getKey();
      System.out.println( indent2 + aRoleKey.toString());
      for( Iterator manyIterator = ((Map)roleKeyEntry.getValue()).entrySet().iterator(); manyIterator.hasNext(); )
      {
        manyEntry = (Map.Entry) manyIterator.next();
        System.out.println( indent3 + manyEntry.getKey() + " ||| " + manyEntry.getValue() );
      }
    }
  }

  protected void merge( Map from, Map to )
  {
    Map.Entry fromEntry = null;
    Visitable toValue = null;
    RoleKey fromKey = null;
    Visitable fromValue = null;
    VisitorPrimitivesCopier aPrimitivesCopier = null;
    Map fromManyMap = null;
    Map toManyMap = null;

    // iterate over "from ones"
    for( Iterator fromIterator = from.entrySet().iterator(); fromIterator.hasNext(); )
    {
      fromEntry = (Map.Entry) fromIterator.next();
      fromKey = (RoleKey) fromEntry.getKey();

      addToWorkingSet( fromKey.getManipulator() );

      if( ! to.containsKey( fromKey ) )
      {
        to.put( fromKey, createEmptyMany() );
      }

      fromManyMap = (Map) fromEntry.getValue();
      toManyMap = (Map) to.get( fromKey );

      // Iterate over "from manys" for a particular "from one"
      for( Iterator fromManyIterator = fromManyMap.values().iterator(); fromManyIterator.hasNext(); )
      {

        fromValue = (Visitable) fromManyIterator.next();

        addToWorkingSet( fromValue );

        toValue = (Visitable) toManyMap.get( fromValue );

        if( toValue == null || ( toValue.isStub() && ! fromValue.isStub() ) )
        {
          toManyMap.put( getWorkingSetElementIfAnyAt(fromValue), getWorkingSetElementIfAnyAt(fromValue) );
        }

      }

    }
  }


  private Map createEmptyMany()
  {
    return (Map) new RemembersRemovalsMapImpl();
  }

  public void put( RoleKey from, Object to )
  {
    Map many =  createEmptyMany();
    if( fromOneToMany.containsKey( from ) )
    {
      many = (Map) fromOneToMany.get( from );
    }
    else
    {
      fromOneToMany.put( from, many );
    }
    many.put( to, to );
    getAssociationsCoordinator().addToWorkingSet( (Visitable) to );
  }

  private Map getMany( RoleKey from )
  {
    if( fromOneToMany.containsKey( from ) )
    {
      return  (Map) fromOneToMany.get( from );
    }
    return createEmptyMany();
  }

  public Collection get( RoleKey from )
  {
    return (Collection) getMany( from ).values();
  }

  public boolean remove( RoleKey from, Object to )
  {
    Map many = getMany( from );
    return many.remove( to ) != null;
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  fromOneToMany.equals( ((ToManyRegistry) that).getFromOneToMany() );
  }

  public int hashCode()
  {
    return fromOneToMany.hashCode();
  }
}