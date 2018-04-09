package smartClient.framework;


import java.io.*;
import java.util.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class AssociationsCoordinator implements Serializable
{

  private OneToManyRepository oneToManyRepository;
  private OneToOneRepository oneToOneRepository;
  private ManyToManyRepository manyToManyRepository;
  private UnidirectionalToManyRepository unidirectionalToManyRepository;
  private UnidirectionalToOneRepository unidirectionalToOneRepository;

  /**
   * @associates Persistable 
   */
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
    Persistable originalPersistable = null;
    if( newAssociatable.isStub() )
      return;
    Persistable newPersistable = (Persistable) newAssociatable;
    if( ! workingSet.containsKey( newPersistable ) )
    {
      workingSet.put( newPersistable, newPersistable );
    }
	else if( isOverwriting() || ! ((Persistable)getWorkingSetElementAt( newPersistable )).isModified() )
    {
      originalPersistable = (Persistable)workingSet.get( newPersistable );
      if( originalPersistable == newPersistable )
        return;
      if( newPersistable.isModified() )
      	originalPersistable.setModified( true );
      boolean savedMarkModifyFlag = SmartClientFramework.getSingleton().getDisableMarkModify();
      SmartClientFramework.getSingleton().setDisableMarkModify( true );
      aPrimitivesCopier = new VisitorPrimitivesCopier( originalPersistable );
      newPersistable.accept( aPrimitivesCopier );
      SmartClientFramework.getSingleton().setDisableMarkModify( savedMarkModifyFlag );
    }
  }

  public void initialize()
  {
    oneToManyRepository = new OneToManyRepository( this );
    oneToOneRepository = new OneToOneRepository( this );
    manyToManyRepository = new ManyToManyRepository( this );
    unidirectionalToManyRepository = new UnidirectionalToManyRepository( this );
    unidirectionalToOneRepository = new UnidirectionalToOneRepository( this );
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
    boolean savedIncludingRemovalsFlag = SmartClientFramework.getSingleton().getIncludingRemovals();

    if( !overwrite )
      SmartClientFramework.getSingleton().setIncludingRemovals( true );

    getManyToManyRepository().merge( that.getManyToManyRepository() );
    getOneToManyRepository().merge( that.getOneToManyRepository() );
    getOneToOneRepository().merge( that.getOneToOneRepository() );
    getUnidirectionalToManyRepository().merge( that.getUnidirectionalToManyRepository() );
    getUnidirectionalToOneRepository().merge( that.getUnidirectionalToOneRepository() );
    fixupWorkingSet();
    setLastRetrieval( that );

    if( !overwrite )
      SmartClientFramework.getSingleton().setIncludingRemovals( savedIncludingRemovalsFlag );

    overwriting = savedOverwriting;
  }

  public void fixupWorkingSet( )
  {
    Iterator anIterator = getWorkingSet().iterator();
    Persistable nextElement = null;
    while( anIterator.hasNext() )
    {
      nextElement = (Persistable) anIterator.next();
      nextElement.setAssociationsCoordinator( this );
    }
  }


  public UnidirectionalToManyRepository getUnidirectionalToManyRepository()
  {
    return unidirectionalToManyRepository;
  }

  public UnidirectionalToOneRepository getUnidirectionalToOneRepository()
  {
    return unidirectionalToOneRepository;
  }

  public OneToManyRepository getOneToManyRepository()
  {
    return oneToManyRepository;
  }

  public OneToOneRepository getOneToOneRepository()
  {
    return oneToOneRepository;
  }

  public ManyToManyRepository getManyToManyRepository()
  {
    return manyToManyRepository;
  }

  public void clear()
  {
    initialize();
  }

  public boolean equals( Object that )
  {
    if( getClass() != that.getClass() )
      return false;
    return  getManyToManyRepository().equals( ((AssociationsCoordinator) that).getManyToManyRepository() )
            || getOneToManyRepository().equals( ((AssociationsCoordinator) that).getOneToManyRepository() )
            || getOneToOneRepository().equals( ((AssociationsCoordinator) that).getOneToOneRepository() )
            || getUnidirectionalToManyRepository().equals( ((AssociationsCoordinator) that).getUnidirectionalToManyRepository() )
            || getUnidirectionalToOneRepository().equals( ((AssociationsCoordinator) that).getUnidirectionalToOneRepository() );
  }

  public int hashCode()
  {
    return  getManyToManyRepository().hashCode()
            ^ getOneToManyRepository().hashCode()
            ^ getOneToOneRepository().hashCode()
            ^ getUnidirectionalToManyRepository().hashCode()
            ^ getUnidirectionalToOneRepository().hashCode();
  }

  public void printString()
  {
    boolean savedPreservingStubs = isPreservingStubs();
    setPreservingStubs( true );
    System.out.println( "<<<Printing AssociationsCoordinator>>>");
    System.out.println( getPrintStringIndent( 1 ) + toString() );
    getManyToManyRepository().printString( 1 );
    getOneToManyRepository().printString( 1 );
    getOneToOneRepository().printString( 1 );
    getUnidirectionalToManyRepository().printString( 1 );
    getUnidirectionalToOneRepository().printString( 1 );
    setPreservingStubs( savedPreservingStubs );
  }

  public boolean isConsistent()
  {
    boolean savedPreservingStubs = isPreservingStubs();
    setPreservingStubs( true );
    boolean result = false;

    result = getManyToManyRepository().isConsistent( this )
             && getOneToManyRepository().isConsistent( this )
             && getOneToOneRepository().isConsistent( this )
             && getUnidirectionalToManyRepository().isConsistent( this )
             && getUnidirectionalToOneRepository().isConsistent( this );

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