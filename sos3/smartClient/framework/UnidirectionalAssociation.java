package smartClient.framework;


import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
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

  public void recordCommand( Command aCommand )
  {
  	getChangeList().add( aCommand );
  }

  protected void recordRemove( Object toObject )
  {
    RemoveCommand aRemoveCommand;
    String persistentRoleType;
    String fullRoleTypeName;
    if( SmartClientFramework.getSingleton().isServer() )
      return;
    try
    {
      fullRoleTypeName =  getToRoleTypeName();
      persistentRoleType = SmartClientFramework.getSingleton().derivePersistentType( Class.forName( fullRoleTypeName ) ).getName();
      aRemoveCommand = new RemoveCommand( ((Persistable)getFrom()).toStub(), getToRole(), ((Persistable)toObject).toStub(), persistentRoleType, getAssociationType() );
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
    if( SmartClientFramework.getSingleton().isServer() )
      return;
    try
    {
      fullRoleTypeName =  getToRoleTypeName();
      persistentRoleType = SmartClientFramework.getSingleton().derivePersistentType( Class.forName( fullRoleTypeName ) ).getName();
      aSetCommand = new SetCommand( ((Persistable)getFrom()).toStub(), getToRole(), ((Persistable)toObject).toStub(), persistentRoleType, getAssociationType() );
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

  protected UnidirectionalToManyRepository getUnidirectionalToManyRepository()
  {
    return ((Persistable)getFrom()).getAssociationsCoordinator().getUnidirectionalToManyRepository();
  }

  protected UnidirectionalToOneRepository getUnidirectionalToOneRepository()
  {
    return ((Persistable)getFrom()).getAssociationsCoordinator().getUnidirectionalToOneRepository();
  }

  protected void recordAdd( Object toObject )
  {
    AddCommand anAddCommand;
    String persistentRoleType;
    String fullRoleTypeName;
    if( SmartClientFramework.getSingleton().isServer() )
      return;
    try
    {
      fullRoleTypeName = getToRoleTypeName();
      persistentRoleType = SmartClientFramework.getSingleton().derivePersistentType( Class.forName( fullRoleTypeName ) ).getName();
      anAddCommand = new AddCommand( ((Persistable)getFrom()).toStub(), getToRole(), ((Persistable)toObject).toStub(), persistentRoleType, getAssociationType() );
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
		Collection result = new LinkedList();
		StubInterface nextElement = null;
        StubInterface nextStub = null;
        SwizzleProxy anInvocationHandler = null;
		Collection many = getAll( toKey );
		if( ((Persistable)this.getFrom()).getAssociationsCoordinator().isPreservingStubs() )
			return many;
        List stubs = new LinkedList();
        List stubProxies = new LinkedList();
        AssociationsCoordinator myASC = ((Persistable)this.getFrom()).getAssociationsCoordinator();

		for( Iterator anIterator = many.iterator(); anIterator.hasNext(); )
		{
			nextElement = (StubInterface) anIterator.next();
			if( nextElement == null )
			{
				continue;
			}
			else if( nextElement.isStub() )
			{
            	stubProxies.add( nextElement );
				anInvocationHandler = (SwizzleProxy)Proxy.getInvocationHandler( nextElement );
                nextStub = (StubInterface) anInvocationHandler.getTrueUnderlyingObject();
                stubs.add( nextStub );
            }
		}

		if( ! stubs.isEmpty() )
        {
        	try
            {
	    		Persistable retrievedObjGraph = SmartClientFramework.getSingleton().getSession().retrieveAllFromStubs( stubs );
				AssociationsCoordinator fromAC = ((Persistable) retrievedObjGraph).getAssociationsCoordinator();
				AssociationsCoordinator to = SmartClientFramework.getSingleton().getGlobalAssociationsCoordinator();
				to.merge( fromAC );
				((Persistable)retrievedObjGraph).setAssociationsCoordinator( to );
				for( Iterator anIterator = stubProxies.iterator(); anIterator.hasNext(); )
				{
					nextElement = (StubInterface) anIterator.next();
					anInvocationHandler = (SwizzleProxy)Proxy.getInvocationHandler( nextElement );
                    if( myASC.workingSetContains( nextElement ) )
                    	anInvocationHandler.setUnderlyingObject( myASC.getWorkingSetElementAt( nextElement ));
		        }
            }
            catch( java.rmi.RemoteException e )
            {
            	e.printStackTrace();
                throw new InternalError( "Problem retrieving object graph from list of stubs." );
            }
        }

		for( Iterator anIterator = many.iterator(); anIterator.hasNext(); )
		{
			nextElement = (StubInterface) anIterator.next();
			if( nextElement == null || nextElement.isStub() )
			{
				continue;
			}
			if( myASC.workingSetContains( nextElement ) )
            	result.add( myASC.getWorkingSetElementAt( nextElement ) );
		}

		return result;
	}

}