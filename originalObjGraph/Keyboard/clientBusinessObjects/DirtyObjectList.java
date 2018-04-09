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

public class DirtyObjectList implements CommandInterface, Serializable
{
  private Set dirtyObjectList;

  public DirtyObjectList()
  {
    dirtyObjectList = new HashSet();
  }

  public void add( Visitable aVisitableObject )
  {
    dirtyObjectList.add( aVisitableObject );
  }

  public void execute()
  {
    Iterator anIterator;

    for( anIterator = dirtyObjectList.iterator(); anIterator.hasNext(); )
    {
      ((Visitable)anIterator.next()).accept( new VisitorUpdater() );
    }
  }

  public void purge()
  {
    if( MiddleWareFramework.getSingleton().isServer() )
      return;
    MiddleWareFramework.getSingleton().getGlobalCRUDRecorder().purge( dirtyObjectList );
  }

  public void postStorageCleanup()
  {
    Visitable nextVisitable = null;
    if( MiddleWareFramework.getSingleton().isServer() )
      return;
    for( Iterator anIterator = dirtyObjectList.iterator(); anIterator.hasNext(); )
    {
      nextVisitable = (Visitable)anIterator.next();
      postStorageCleanup( nextVisitable );
    }
  }

  private void postStorageCleanup( Visitable aVisitable )
  {
      Visitable workingSetElement = null;
      if( MiddleWareFramework.getSingleton().isServer() )
        return;
      workingSetElement = (Visitable)MiddleWareFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( aVisitable );
      if( workingSetElement == null )
        {
          throw new InternalError( "<DirtyObjectList.postStorageCleanup> workingSetElement should never be null" );
        }
      if( aVisitable.isModified() )
      {
        workingSetElement.incrementUpdateCounter();
      }
      workingSetElement.setNewlyCreated( false );
      workingSetElement.setModified( false );
  }

}