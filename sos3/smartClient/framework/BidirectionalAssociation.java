package smartClient.framework;


import java.io.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public abstract class BidirectionalAssociation extends UnidirectionalAssociation
{
  private String fromRole;

  public BidirectionalAssociation( Object _from, String _toRole,  String _toRoleTypeName, String _fromRole )
  {
    super( _from, _toRole, _toRoleTypeName );
    setFromRole( _fromRole );
  }

  public void recordCommand( Command aCommand )
  {
    BidirectionalAssociation aBiAssoc = (BidirectionalAssociation) this;
    RoleKey directRoleKey = new RoleKey( aBiAssoc.getFrom(), aBiAssoc.getToRole() );
    Clamp directClamp = directRoleKey.toClamp();
    RoleKey inverseRoleKey = null;
    Clamp inverseClamp = null;
    if( ClampProvider.getSingleton().getStorageClamps().containsKey( directClamp ) )
    {
      directClamp = (Clamp) ClampProvider.getSingleton().getStorageClamps().get( directClamp );
      if( directClamp.getInverseClamp() == null )
      {
        inverseRoleKey = new RoleKey( aCommand.getArg().toTransient(), aBiAssoc.getFromRole() );
        inverseClamp = inverseRoleKey.toClamp();
        if( ClampProvider.getSingleton().getStorageClamps().containsKey( inverseClamp ) )
        {
          inverseClamp = (Clamp) ClampProvider.getSingleton().getStorageClamps().get( inverseClamp );
          directClamp.setInverseClamp( inverseClamp );
          inverseClamp.setInverseClamp( directClamp );
        }
      }
      directClamp.getChangeList().add( (CommandInterface) aCommand );
    }
    else
    {
      getChangeList().add( aCommand );
    }
  }

  protected OneToManyRepository getOneToManyRepository()
  {
    return ((Persistable)getFrom()).getAssociationsCoordinator().getOneToManyRepository();
  }

  protected ManyToManyRepository getManyToManyRepository()
  {
    return ((Persistable)getFrom()).getAssociationsCoordinator().getManyToManyRepository();
  }

  protected OneToOneRepository getOneToOneRepository()
  {
    return ((Persistable)getFrom()).getAssociationsCoordinator().getOneToOneRepository();
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