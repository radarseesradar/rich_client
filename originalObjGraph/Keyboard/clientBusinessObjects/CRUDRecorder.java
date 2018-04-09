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

public class CRUDRecorder
{
  private Set newlyCreatedObjects;
  private AssociationChangeList associationChanges;
  private Set modifiedObjects;
  private boolean recordingEnabled = true;

  public CRUDRecorder()
  {
    initialize();
  }

  public void setRecordingEnabled( boolean trueOrFalse )
  {
    recordingEnabled = trueOrFalse;
  }

  private boolean recordingIsEnabled( )
  {
    return recordingEnabled;
  }

  public boolean getRecordingEnabled()
  {
    return recordingEnabled;
  }

  public void purge( Collection aCollection )
  {
    newlyCreatedObjects.removeAll( aCollection );
    modifiedObjects.removeAll( aCollection );
  }

  public void addAssociationChange( CommandInterface aCommand )
  {
    associationChanges.add( aCommand );
  }

  public void addNewlyCreatedObject( Visitable aVisitable )
  {
    newlyCreatedObjects.add( aVisitable );
  }

  public void addModifiedObject( Visitable aVisitable )
  {
    if( ! recordingIsEnabled() )
      return;
    if( newlyCreatedObjects.contains( aVisitable ) )
      return;
    modifiedObjects.add( aVisitable );
  }

  public boolean conatainsModifiedObject( Visitable aVisitable )
  {
    return modifiedObjects.contains( aVisitable );
  }

  public boolean conatainsNewlyCreatedObject( Visitable aVisitable )
  {
    return newlyCreatedObjects.contains( aVisitable );
  }

  private void initialize()
  {
    newlyCreatedObjects = new HashSet();
    associationChanges = new AssociationChangeList();
    modifiedObjects = new HashSet();
  }

  public CRUDPacket createCRUDPacket()
  {
    CRUDPacket aPacket = new CRUDPacket();
    for( Iterator anIterator = newlyCreatedObjects.iterator(); anIterator.hasNext(); )
    {
      aPacket.addNewlyCreated( ((Visitable) ((Visitable) anIterator.next()).clone()).accept( new VisitorPruner() ) );
    }
    aPacket.addAssociationChanges( associationChanges );
    for( Iterator anIterator = modifiedObjects.iterator(); anIterator.hasNext(); )
    {
      aPacket.addModified( ((Visitable) ((Visitable) anIterator.next()).clone()).accept( new VisitorPruner() ) );
    }
    aPacket.normalize();
    return aPacket;
  }

  public void postStorageCleanup()
  {
    postStorageCleanup( newlyCreatedObjects );
    postStorageCleanup( modifiedObjects );
  }

  private void postStorageCleanup( Collection aCollection )
  {
    for(Iterator anIterator = aCollection.iterator(); anIterator.hasNext(); )
    {
      postStorageCleanup( (Visitable) anIterator.next() );
    }
  }

  private void postStorageCleanup( Visitable aVisitable )
  {
      if( MiddleWareFramework.getSingleton().isServer() )
        return;
      if( aVisitable.isModified() )
      {
        aVisitable.incrementUpdateCounter();
      }
      aVisitable.setNewlyCreated( false );
      aVisitable.setModified( false );
  }


  public void clear()
  {
    initialize();
  }
}