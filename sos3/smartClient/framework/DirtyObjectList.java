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

public class DirtyObjectList implements CommandInterface
{
  /**
   * @associates Persistable 
   */
  private Set dirtyObjectList;

  public DirtyObjectList()
  {
    dirtyObjectList = new HashSet();
  }

  public void add( Persistable aPersistableObject )
  {
    dirtyObjectList.add( aPersistableObject );
  }

  public void execute()
  {
    Iterator anIterator;

    for( anIterator = dirtyObjectList.iterator(); anIterator.hasNext(); )
    {
      ((Persistable)anIterator.next()).accept( new VisitorUpdater() );
    }
  }

  public void postStorageCleanup()
  {
    Persistable nextPersistable = null;
    if( SmartClientFramework.getSingleton().isServer() )
      return;
    for( Iterator anIterator = dirtyObjectList.iterator(); anIterator.hasNext(); )
    {
      nextPersistable = (Persistable)anIterator.next();
      postStorageCleanup( nextPersistable );
    }
  }

  private void postStorageCleanup( Persistable aPersistable )
  {
      Persistable workingSetElement = null;
      if( SmartClientFramework.getSingleton().isServer() )
        return;
      workingSetElement = (Persistable)SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator().getWorkingSetElementAt( aPersistable );
      if( workingSetElement == null )
        {
          throw new InternalError( "<DirtyObjectList.postStorageCleanup> workingSetElement should never be null" );
        }
      if( aPersistable.isModified() )
      {
        workingSetElement.incrementUpdateCounter();
      }
      workingSetElement.setNewlyCreated( false );
      workingSetElement.setModified( false );
  }

}