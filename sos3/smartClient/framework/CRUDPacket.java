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

public class CRUDPacket implements CommandInterface
{
  private DirtyObjectList newlyCreatedObjects;
  private AssociationChangeList associationChanges;
  private DirtyObjectList modifiedObjects;

  public CRUDPacket()
  {
    newlyCreatedObjects = new DirtyObjectList();
    associationChanges = new AssociationChangeList();
    modifiedObjects = new DirtyObjectList();
  }

  public void normalize()
  {
    promoteConflictingOperandsToModified();
    sort();
  }

  private void sort()
  {
    associationChanges.sort();
  }

  private void promoteConflictingOperandsToModified()
  {
    Persistable nextOperand = null;
    for( Iterator anIterator = associationChanges.getAllConflictingOperands().iterator(); anIterator.hasNext(); )
    {
      nextOperand = (Persistable)((Stub)anIterator.next()).toTransient();
      nextOperand.setModified( true );
      nextOperand = (Persistable) nextOperand.clone();
      nextOperand.accept( new VisitorPruner() );
      addModified( nextOperand );
    }
  }

  public void addNewlyCreated( Persistable aPersistable )
  {
    aPersistable.setAssociationsCoordinator( null );
    newlyCreatedObjects.add( aPersistable );
  }

  public void addModified( Persistable aPersistable )
  {
	if( ((Persistable)SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( aPersistable )).isNewlyCreated()  )
    {
      addNewlyCreated(  aPersistable );
      return;
    }
    aPersistable.setAssociationsCoordinator( null );
    modifiedObjects.add( aPersistable );
  }

  public void addAssociationChanges( AssociationChangeList anotherList )
  {
    associationChanges.addAll( anotherList );
  }

  public void postStorageCleanup()
  {
    newlyCreatedObjects.postStorageCleanup();
    modifiedObjects.postStorageCleanup();
  }

  public void execute()
  {
    ((CommandInterface)newlyCreatedObjects).execute();
    ((CommandInterface)associationChanges).execute();
    ((CommandInterface)modifiedObjects).execute();
  }
}