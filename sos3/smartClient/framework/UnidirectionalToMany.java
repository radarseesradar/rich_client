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
final public class UnidirectionalToMany extends UnidirectionalAssociation implements ToMany, Serializable
{
  public UnidirectionalToMany( Object _from, String _toRole, String _toRoleTypeName )
  {
    super( _from, _toRole, _toRoleTypeName );
  }

  protected UnidirectionalToManyRepository getRepository()
  {
    return (UnidirectionalToManyRepository) getUnidirectionalToManyRepository();
  }

  public int getAssociationType()
  {
    return AssociationTypes.TO_MANY;
  }

  public void add( Object toObject )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      UnidirectionalToManyRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      aRepository.put( toKey, toObject );
      recordAdd( toObject );
    }
  }

  public void remove( Object toObject )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      UnidirectionalToManyRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      aRepository.remove( toKey, toObject );
      recordRemove( toObject );
    }
  }

  protected Collection getAll( RoleKey toKey )
  {
    UnidirectionalToManyRepository aRepository = getRepository();
    return (Collection) aRepository.get( toKey );
  }

  public Collection getAll()
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      UnidirectionalToManyRepository aRepository = getRepository();
      RoleKey toKey = new RoleKey( getFrom(), getToRole() );
      return Collections.unmodifiableCollection(unstubAll( toKey ));
    }
  }

}