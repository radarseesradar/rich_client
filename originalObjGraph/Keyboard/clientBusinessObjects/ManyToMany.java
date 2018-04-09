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

public class ManyToMany extends OneToMany
{
  public ManyToMany( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super(  _from,  _toRole,  _toRoleTypeName, _fromRole );
  }

  public int getAssociationType()
  {
    return AssociationTypes.MANY_TO_MANY;
  }

  protected BidirectionalToManyRegistry getRegistry()
  {
    return (BidirectionalToManyRegistry) getManyToManyRegistry();
  }

  public void add( Object toObject )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      super.add( toObject );
    }
  }

  public void remove( Object toObject )
  {
    synchronized( ((Visitable) getFrom()).getAssociationsCoordinator() )
    {
      super.remove( toObject );
    }
  }

}