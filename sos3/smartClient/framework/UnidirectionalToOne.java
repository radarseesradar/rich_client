/*
 * UnidirectionalToOne
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 */
package smartClient.framework;


import java.util.*;
import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 **/
final public class UnidirectionalToOne extends UnidirectionalAssociation implements ToOne, Serializable
{
  public UnidirectionalToOne( Object _from, String _toRole, String _toRoleTypeName )
  {
    super( _from, _toRole, _toRoleTypeName );
  }

  protected UnidirectionalToOneRepository getRepository()
  {
    return (UnidirectionalToOneRepository) getUnidirectionalToOneRepository();
  }

  public Object get()
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      Object result = null;
      UnidirectionalToOneRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      result = aRepository.get( toKey );
      if( result != null &&  ! ((Persistable)this.getFrom()).getAssociationsCoordinator().isPreservingStubs() )
      	{
        	result = ((StubInterface) result).yourself();
        	result = ((Persistable)getFrom()).getAssociationsCoordinator().getWorkingSetElementAt( result );
        }
      return result;
    }
  }

  public int getAssociationType()
  {
    return AssociationTypes.TO_ONE;
  }

  public void set( Object toOne )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      Persistable previousToOneIfAny = (Persistable) get();
      if( previousToOneIfAny != null && SmartClientFramework.getSingleton().isClient() )
        previousToOneIfAny.setModified( true );
      UnidirectionalToOneRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      aRepository.put( toKey, toOne );
      recordSet( toOne );
    }
  }
}