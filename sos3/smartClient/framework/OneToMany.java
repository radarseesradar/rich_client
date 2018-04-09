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
public class OneToMany extends BidirectionalAssociation implements ToMany, Serializable
{
  public OneToMany( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole,  _toRoleTypeName, _fromRole );
  }

  protected BidirectionalToManyRepository getRepository()
  {
    return (BidirectionalToManyRepository) getOneToManyRepository();
  }

  public int getAssociationType()
  {
    return AssociationTypes.ONE_TO_MANY;
  }

  public void add( Object toObject )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      BidirectionalToManyRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey inverseKey = new RoleKey( toObject, getFromRole() );
      aRepository.associate( toKey, inverseKey );
      recordAdd( toObject );
    }
  }

  public void remove( Object toObject )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      BidirectionalToManyRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      RoleKey fromKey = new RoleKey( toObject, getFromRole() );
      aRepository.disassociate( toKey, fromKey );
      recordRemove( toObject );
    }
  }

  protected Collection getAll( RoleKey toKey )
  {
    BidirectionalToManyRepository aRepository = getRepository();
    return (Collection) aRepository.getToMany( toKey );
  }

  public Collection getAll()
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      BidirectionalToManyRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      return Collections.unmodifiableCollection(unstubAll( toKey ));
    }
  }

}