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
import java.lang.reflect.*;

public abstract class UnidirectionalAssociation implements Serializable
{

  static interface AssociationTypes
  {
    int ONE_TO_ONE = 0;
    int MANY_TO_MANY = 1;
    int ONE_TO_MANY = 2;
    int MANY_TO_ONE = 3;
    int TO_ONE = 4;
    int TO_MANY = 5;
  }

  private Object from;
  private String toRole;
  private String toRoleTypeName;
  private AssociationChangeList changeList;

  public UnidirectionalAssociation( Object _from, String _toRole, String _toRoleTypeName )
  {
    setFrom( _from );
    setToRole( _toRole );
    setToRoleTypeName( _toRoleTypeName );
    changeList = new AssociationChangeList();
  }

  public abstract int getAssociationType();


  public UnidirectionalAssociation()
  {
  }

  public AssociationChangeList getChangeList()
  {
    return changeList;
  }

  private void recordCommand( CommandInterface aCommand )
  {
      MiddleWareFramework.getSingleton().getAssociationMode().recordComand( this, (Command) aCommand );
  }

  protected CRUDRecorder getCRUDRecorder()
  {
    return MiddleWareFramework.getSingleton().getGlobalCRUDRecorder();
  }

  protected void recordRemove( Object toObject )
  {
    RemoveCommand aRemoveCommand;
    String persistentRoleType;
    String fullRoleTypeName;
    if( MiddleWareFramework.getSingleton().isServer() )
      return;
    try
    {
      fullRoleTypeName = MiddleWareFramework.getSingleton().getTransientPackageName() + "." + getToRoleTypeName();
      persistentRoleType = MiddleWareFramework.getSingleton().derivePersistentType( Class.forName( fullRoleTypeName ) ).getName();
      aRemoveCommand = new RemoveCommand( ((Visitable)getFrom()).toStub(), getToRole(), ((Visitable)toObject).toStub(), persistentRoleType, getAssociationType() );
      recordCommand( aRemoveCommand );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  protected void recordSet( Object toObject )
  {
    SetCommand aSetCommand;
    String persistentRoleType;
    String fullRoleTypeName;
    if( MiddleWareFramework.getSingleton().isServer() )
      return;
    try
    {
      fullRoleTypeName = MiddleWareFramework.getSingleton().getTransientPackageName() + "." + getToRoleTypeName();
      persistentRoleType = MiddleWareFramework.getSingleton().derivePersistentType( Class.forName( fullRoleTypeName ) ).getName();
      aSetCommand = new SetCommand( ((Visitable)getFrom()).toStub(), getToRole(), ((Visitable)toObject).toStub(), persistentRoleType, getAssociationType() );
      recordCommand( aSetCommand );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  protected Object getFrom()
  {
    return from;
  }

  protected String getToRole()
  {
    return toRole;
  }

  protected String getToRoleTypeName()
  {
    return toRoleTypeName;
  }

  private void setFrom( Object _from )
  {
    from = _from;
  }

  private void setToRole( String _toRole )
  {
    toRole = _toRole;
  }

  private void setToRoleTypeName( String _toRoleTypeName )
  {
    toRoleTypeName = _toRoleTypeName;
  }

  protected UnidirectionalToManyRegistry getUnidirectionalToManyRegistry()
  {
    return ((Visitable)getFrom()).getAssociationsCoordinator().getUnidirectionalToManyRegistry();
  }

  protected UnidirectionalToOneRegistry getUnidirectionalToOneRegistry()
  {
    return ((Visitable)getFrom()).getAssociationsCoordinator().getUnidirectionalToOneRegistry();
  }

  protected void recordAdd( Object toObject )
  {
    AddCommand anAddCommand;
    String persistentRoleType;
    String fullRoleTypeName;
    if( MiddleWareFramework.getSingleton().isServer() )
      return;
    try
    {
      fullRoleTypeName = MiddleWareFramework.getSingleton().getTransientPackageName() + "." + getToRoleTypeName();
      persistentRoleType = MiddleWareFramework.getSingleton().derivePersistentType( Class.forName( fullRoleTypeName ) ).getName();
      anAddCommand = new AddCommand( ((Visitable)getFrom()).toStub(), getToRole(), ((Visitable)toObject).toStub(), persistentRoleType, getAssociationType() );
      recordCommand( anAddCommand );
    }
    catch( ClassNotFoundException e )
    {
      // This would indicate a programming bug.
      throw new InternalError( e.toString() );
    }
  }

  protected Collection getAll( RoleKey toKey )
  {
    return (Collection) Collections.EMPTY_LIST;
  }

  protected Collection unstubAll( RoleKey toKey )
  {
    Collection result = unstubAll( toKey, false );
    Collection underlyingResult = new ArrayList();
    Iterator anIterator = result.iterator();
    Object element = null;
    while ( anIterator.hasNext() )
    {
        StubInterface sInf = (StubInterface)anIterator.next();
        if (sInf != null)
        {
            element = sInf.yourself();
            underlyingResult.add( element );
        }
    }
    return  underlyingResult;
   }

  protected Collection unstubAll( RoleKey toKey, boolean isOKToUseCache )
  {
    Collection result = new ArrayList();
    StubInterface nextElement = null;
    Collection many = getAll( toKey );
    if( ((Visitable)this.getFrom()).getAssociationsCoordinator().isPreservingStubs() )
      return many;
    for( Iterator anIterator = many.iterator(); anIterator.hasNext(); )
    {
      nextElement = (StubInterface) anIterator.next();
      if( nextElement == null )
      {
        continue;
      }
      else if( nextElement.isStub() )
      {
        if( isOKToUseCache )
        {
          if(((SwizzleProxy)Proxy.getInvocationHandler( nextElement )).checkLastRetrievalCache())
          {
            result.add(((Visitable)this.getFrom()).getAssociationsCoordinator().getWorkingSetElementAt( nextElement ) );
          }
          else
          {
            nextElement.yourself();
            return unstubAll( toKey, true );
          }
        }
        else
        {
          nextElement.yourself();
          return unstubAll( toKey, true );
        }
      }
      else
      {
        result.add(((Visitable)this.getFrom()).getAssociationsCoordinator().getWorkingSetElementIfAnyAt( nextElement ) );
      }
    }
    return result;
  }

}