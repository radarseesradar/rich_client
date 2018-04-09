package smartClient.framework;

import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
final public class ManyToOne extends BidirectionalAssociation implements ToOne, Serializable
{
  public ManyToOne( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole,  _toRoleTypeName, _fromRole );
  }

  public int getAssociationType()
  {
    return AssociationTypes.MANY_TO_ONE;
  }

  public Object get()
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      Object result = null;
      OneToManyRepository aRepository = getOneToManyRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      result = aRepository.getToOne( toKey );
      if( result != null &&  ! ((Persistable)getFrom()).getAssociationsCoordinator().isPreservingStubs() )
      	{
        	result = ((StubInterface) result).yourself();
        	result = ((Persistable)getFrom()).getAssociationsCoordinator().getWorkingSetElementAt( result );
        }
      return result;
    }
  }

  public void set( Object toOne )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      OneToManyRepository aRepository = getOneToManyRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey inverseKey = new RoleKey( toOne, getFromRole() );
      aRepository.associate( inverseKey, toKey );
      recordSet( toOne );
    }
  }

}