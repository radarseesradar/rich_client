package smartClient.framework;


import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
final public class OneToOne extends BidirectionalAssociation implements ToOne, Serializable
{
  public OneToOne( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole, _toRoleTypeName, _fromRole );
  }

  public int getAssociationType()
  {
    return AssociationTypes.ONE_TO_ONE;
  }

  public Object get()
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      Object result = null;
      OneToOneRepository aRepository = getOneToOneRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      result = aRepository.getToOne( toKey );
      if( result != null &&  ! ((Persistable)this.getFrom()).getAssociationsCoordinator().isPreservingStubs() )
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
      Persistable previousToOneIfAny = (Persistable) get();
      if( previousToOneIfAny != null && SmartClientFramework.getSingleton().isClient() )
        previousToOneIfAny.setModified( true );
      OneToOneRepository aRepository = getOneToOneRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey inverseKey = new RoleKey( toOne, getFromRole() );
      aRepository.associate( toKey, inverseKey );
      recordSet( toOne );
    }
  }
}