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

public abstract class BidirectionalAssociation extends UnidirectionalAssociation
{
  private String fromRole;

  public BidirectionalAssociation( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super( _from, _toRole, _toRoleTypeName );
    setFromRole( _fromRole );
  }

  protected OneToManyRegistry getOneToManyRegistry()
  {
    return ((Visitable)getFrom()).getAssociationsCoordinator().getOneToManyRegistry();
  }

  protected ManyToManyRegistry getManyToManyRegistry()
  {
    return ((Visitable)getFrom()).getAssociationsCoordinator().getManyToManyRegistry();
  }

  protected OneToOneRegistry getOneToOneRegistry()
  {
    return ((Visitable)getFrom()).getAssociationsCoordinator().getOneToOneRegistry();
  }

  public BidirectionalAssociation()
  {
  }

  protected String getFromRole()
  {
    return fromRole;
  }

  private void setFromRole( String _fromRole )
  {
    fromRole = _fromRole;
  }

}