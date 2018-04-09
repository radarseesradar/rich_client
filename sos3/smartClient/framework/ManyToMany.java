/*
 * ManyToMany
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 */
package smartClient.framework;


import java.io.*;

/**
 * @author Steve McDaniel
 * @version 1.0
 **/

final public class ManyToMany extends OneToMany
{
  public ManyToMany( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole,  _toRoleTypeName, _fromRole );
  }

  public int getAssociationType()
  {
    return AssociationTypes.MANY_TO_MANY;
  }

  protected BidirectionalToManyRepository getRepository()
  {
    return (BidirectionalToManyRepository) getManyToManyRepository();
  }

  public void add( Object toObject )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      super.add( toObject );
    }
  }

  public void remove( Object toObject )
  {
    synchronized( ((Persistable) getFrom()).getAssociationsCoordinator() )
    {
      super.remove( toObject );
    }
  }

}