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

public class CRUDPacket implements CommandInterface, Serializable
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
    Visitable nextOperand = null;
    boolean savedRecordingEnabledFlag = MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().getRecordingEnabled();
    MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().setRecordingEnabled( false );
    for( Iterator anIterator = associationChanges.getAllConflictingOperands().iterator(); anIterator.hasNext(); )
    {
      nextOperand = (Visitable)((Stub)anIterator.next()).toTransient().clone();
      nextOperand.accept( new VisitorPruner() );
      nextOperand.setModified( true );
      addModified( nextOperand );
    }
    MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().setRecordingEnabled( savedRecordingEnabledFlag );
  }

  public void addNewlyCreated( Visitable aVisitable )
  {
    aVisitable.setAssociationsCoordinator( null );
    newlyCreatedObjects.add( aVisitable );
  }

  public void addModified( Visitable aVisitable )
  {
    if( MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().conatainsNewlyCreatedObject( aVisitable ) )
    {
      addNewlyCreated(  aVisitable );
      return;
    }
    aVisitable.setAssociationsCoordinator( null );
    modifiedObjects.add( aVisitable );
  }

  public void addAssociationChanges( AssociationChangeList anotherList )
  {
    associationChanges.addAll( anotherList );
  }

  public void postStorageCleanup()
  {
    newlyCreatedObjects.postStorageCleanup();
    modifiedObjects.postStorageCleanup();
    newlyCreatedObjects.purge();
    modifiedObjects.purge();
  }

  public void execute()
  {
    ((CommandInterface)newlyCreatedObjects).execute();
    ((CommandInterface)associationChanges).execute();
    ((CommandInterface)modifiedObjects).execute();
  }
}