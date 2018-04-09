package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

public abstract class AssociationMode
{
  public boolean isBidirectionalOnlyMode()
  {
    return false;
  }

  public boolean isUnidirectionalAllowedMode()
  {
    return false;
  }

  public abstract void save( Visitable delegate ) throws Exception;

  public abstract void recordComand( UnidirectionalAssociation selfDelegator, Command aCommand );
}