package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class AssociationsCoordinator implements Serializable
{

  private OneToManyRegistry oneToManyRegistry;
  private OneToOneRegistry oneToOneRegistry;
  private ManyToManyRegistry manyToManyRegistry;
  private UnidirectionalToManyRegistry unidirectionalToManyRegistry;
  private UnidirectionalToOneRegistry unidirectionalToOneRegistry;
  private Map workingSet;
  private boolean preservingStubs;
  public String name;
  private AssociationsCoordinator lastRetrieval;
  private boolean overwriting;

  public AssociationsCoordinator()
  {
    initialize();
    name = this.toString();
  }

  private boolean isOverwriting()
  {
    return overwriting;
  }

  public AssociationsCoordinator getLastRetrieval(  )
  {
    return lastRetrieval;
  }

  public void setLastRetrieval( AssociationsCoordinator _lastRetrieval )
  {
    lastRetrieval = _lastRetrieval;
  }

  public Set getWorkingSet()
  {
    return workingSet.keySet();
  }

  public void setPreservingStubs( boolean trueOrFalse )
  {
    preservingStubs = trueOrFalse;
  }

  public boolean isPreservingStubs()
  {
    return preservingStubs;
  }

  public boolean getPreservingStubs()
  {
    return preservingStubs;
  }

  protected StubInterface getWorkingSetElementIfAnyAt( Object anObject )
  {
    if( ((StubInterface) anObject).isStub() )
      return (StubInterface) anObject;
    if( workingSetContains( anObject ) )
      return (StubInterface) getWorkingSetElementAt( anObject );
    return (StubInterface) anObject;
  }

  public Object getWorkingSetElementAt( Object anObject )
  {
    return  workingSet.get( anObject );
  }

  public boolean workingSetContains( Object anObject )
  {
    return workingSet.containsKey( anObject );
  }

  public void addToWorkingSet( StubInterface newAssociatable )
  {
    VisitorPrimitivesCopier aPrimitivesCopier = null;
    Visitable originalVisitable = null;
    if( newAssociatable.isStub() )
      return;
    Visitable newVisitable = (Visitable) newAssociatable;
    if( ! workingSet.containsKey( newVisitable ) )
    {
      workingSet.put( newVisitable, newVisitable );
    }
    else if( isOverwriting() || ! MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().conatainsModifiedObject( newVisitable ) )
    {
      originalVisitable = (Visitable)workingSet.get( newVisitable );
      if( originalVisitable == newVisitable )
        return;
      boolean savedMarkModifyFlag = MiddleWareFramework.getSingleton().getDisableMarkModify();
      MiddleWareFramework.getSingleton().setDisableMarkModify( true );
      aPrimitivesCopier = new VisitorPrimitivesCopier( originalVisitable );
      newVisitable.accept( aPrimitivesCopier );
      MiddleWareFramework.getSingleton().setDisableMarkModify( savedMarkModifyFlag );
    }
  }

  public void initialize()
  {
    oneToManyRegistry = new OneToManyRegistry( this );
    oneToOneRegistry = new OneToOneRegistry( this );
    manyToManyRegistry = new ManyToManyRegistry( this );
    unidirectionalToManyRegistry = new UnidirectionalToManyRegistry( this );
    unidirectionalToOneRegistry = new UnidirectionalToOneRegistry( this );
    workingSet = new HashMap();
    setPreservingStubs( false );
  }

  public synchronized void merge( AssociationsCoordinator that )
  {
    merge( that, false );
  }

  public void merge( AssociationsCoordinator that, boolean overwrite )
  {
    boolean savedOverwriting = overwriting;
    overwriting = overwrite;
    boolean savedIncludingRemovalsFlag = MiddleWareFramework.getSingleton().getIncludingRemovals();

    if( !overwrite )
      MiddleWareFramework.getSingleton().setIncludingRemovals( true );

    getManyToManyRegistry().merge( that.getManyToManyRegistry() );
    getOneToManyRegistry().merge( that.getOneToManyRegistry() );
    getOneToOneRegistry().merge( that.getOneToOneRegistry() );
    getUnidirectionalToManyRegistry().merge( that.getUnidirectionalToManyRegistry() );
    getUnidirectionalToOneRegistry().merge( that.getUnidirectionalToOneRegistry() );
    fixupWorkingSet();
    setLastRetrieval( that );

    if( !overwrite )
      MiddleWareFramework.getSingleton().setIncludingRemovals( savedIncludingRemovalsFlag );

    overwriting = savedOverwriting;
  }

  public void fixupWorkingSet( )
  {
    Iterator anIterator = getWorkingSet().iterator();
    Visitable nextElement = null;
    while( anIterator.hasNext() )
    {
      nextElement = (Visitable) anIterator.next();
      nextElement.setAssociationsCoordinator( this );
    }
  }


  public UnidirectionalToManyRegistry getUnidirectionalToManyRegistry()
  {
    return unidirectionalToManyRegistry;
  }

  public UnidirectionalToOneRegistry getUnidirectionalToOneRegistry()
  {
    return unidirectionalToOneRegistry;
  }

  public OneToManyRegistry getOneToManyRegistry()
  {
    return oneToManyRegistry;
  }

  public OneToOneRegistry getOneToOneRegistry()
  {
    return oneToOneRegistry;
  }

  public ManyToManyRegistry getManyToManyRegistry()
  {
    return manyToManyRegistry;
  }

  public void clear()
  {
    initialize();
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  getManyToManyRegistry().equals( ((AssociationsCoordinator) that).getManyToManyRegistry() )
            || getOneToManyRegistry().equals( ((AssociationsCoordinator) that).getOneToManyRegistry() )
            || getOneToOneRegistry().equals( ((AssociationsCoordinator) that).getOneToOneRegistry() )
            || getUnidirectionalToManyRegistry().equals( ((AssociationsCoordinator) that).getUnidirectionalToManyRegistry() )
            || getUnidirectionalToOneRegistry().equals( ((AssociationsCoordinator) that).getUnidirectionalToOneRegistry() );
  }

  public int hashCode()
  {
    return  getManyToManyRegistry().hashCode()
            ^ getOneToManyRegistry().hashCode()
            ^ getOneToOneRegistry().hashCode()
            ^ getUnidirectionalToManyRegistry().hashCode()
            ^ getUnidirectionalToOneRegistry().hashCode();
  }

  public void printString()
  {
    boolean savedPreservingStubs = isPreservingStubs();
    setPreservingStubs( true );
    System.out.println( "<<<Printing AssociationsCoordinator>>>");
    System.out.println( getPrintStringIndent( 1 ) + toString() );
    getManyToManyRegistry().printString( 1 );
    getOneToManyRegistry().printString( 1 );
    getOneToOneRegistry().printString( 1 );
    getUnidirectionalToManyRegistry().printString( 1 );
    getUnidirectionalToOneRegistry().printString( 1 );
    setPreservingStubs( savedPreservingStubs );
  }

  public boolean isConsistent()
  {
    boolean savedPreservingStubs = isPreservingStubs();
    setPreservingStubs( true );
    boolean result = false;

    result = getManyToManyRegistry().isConsistent( this )
             && getOneToManyRegistry().isConsistent( this )
             && getOneToOneRegistry().isConsistent( this )
             && getUnidirectionalToManyRegistry().isConsistent( this )
             && getUnidirectionalToOneRegistry().isConsistent( this );

    setPreservingStubs( savedPreservingStubs );
    return result;
  }

  public String getPrintStringIndent( int depth )
  {
    String result = "";
    for( int i = 0; i < depth; i++ )
    {
      result += "    ";
    }
    return result;
  }

  public String toString()
  {
    return getClass().getName() + '@' + Integer.toHexString(super.hashCode());
  }

}